package com.optima.plugin.host.download;



import com.optima.plugin.host.download.impl.ProcessListener;
import com.optima.plugin.host.download.impl.TaskFinishListener;
import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.host.download.module.MenuInfoModule;
import com.optima.plugin.host.download.module.PluginInfoModule;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;

import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * create by wma
 * on 2020/9/17 0017
 *
 * 下载队列，保证线程并发量
 */
public class DownloadQueue {
    final String TAG = DownloadQueue.class.getSimpleName();
    /**
     * 正在跑的线程数量
     */
    private int mRunningCount;

    /**
     * 最大线程数
     */
    private int mMaxThreadCount = 3;

    /**
     * 任务总数
     */
    private int mTotal = 0;

    /**
     * 当前已经下载的任务数
     */
    private int mCur = 0;

    /**
     * 下载任务集合
     */
    private LinkedHashMap<String, DownloadTask> mTasks = new LinkedHashMap<>();

    /**
     * 下载完毕后的封装file对象
     */
    private List<DownloadModule> mDownloadFile = new ArrayList<>();

    /**
     * 下载进度监听器
     */
    private ProcessListener processListener;

    public void addOnProcessListener(ProcessListener processListener) {
        this.processListener = processListener;
    }

    public DownloadQueue(int maxThreadCount) {
        this.mMaxThreadCount = maxThreadCount;
    }

    /**
     * 添加任务
     * @param task
     */
    public void addTask(DownloadTask task) {
        mTasks.put(task.getName(), task);
    }

    /**
     * 任务总数
     * @return
     */
    public int taskCount() {
        return mTasks.size();
    }

    /**
     * 执行下载任务
     */
    public void excuse() {
        mTotal = taskCount();
        if (mTotal <= 0) {
            Logger.e(TAG, "excuse: 执行失败，没有任务");
            return;
        }
        WorkThread  workThread = new WorkThread();
        workThread.start();
    }

    /**
     * 取消任务
     */
    public void cancel() {
        for (String key : mTasks.keySet()) {
            DownloadTask downloadTask = mTasks.get(key);
            if (downloadTask != null) {
                if(downloadTask.getCancelable()!=null){
                    Logger.d(TAG, "cancelTask: " + downloadTask.getName());
                }
                downloadTask.cancel();
            } else {
                Logger.d(TAG, "cancelTask: " + downloadTask);
            }

        }
        if (processListener != null) {
            processListener.cancel();
        }
    }

    /**
     * 工作线程
     */
    class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (DownloadQueue.this) {
                if (processListener != null) {
                    processListener.onStart();
                }
                for (String name : mTasks.keySet()) {
                    mRunningCount++;
                    if (mRunningCount > mMaxThreadCount) {
                        try {
                            DownloadQueue.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    DownloadTask task = mTasks.get(name);
                    if (task != null) {
                        task.addFinishListener(new TaskFinishListener() {
                            @Override
                            public void finish(DownloadTask task) {
                                synchronized (DownloadQueue.this) {
                                    mDownloadFile.add(DownloadModule.build(task));
                                    updateLocalModule(task);
                                    mCur++;
                                    if (processListener != null) {
                                        processListener.onTotalProcess(mCur, mTotal);
                                    }
                                    mRunningCount--;
                                    String name1 = task.getName();
                                    task = null;
                                    mTasks.put(name1, task);
                                    if (mRunningCount < mMaxThreadCount) {
                                        DownloadQueue.this.notify();
                                    }
                                    if (mRunningCount == 0) {// 说明任务执行完毕
                                        mTasks.clear();
                                        if (processListener != null) {
                                            processListener.onFinish(mDownloadFile);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void error(DownloadTask task) {
                                if (processListener != null) {
                                    processListener.onError(task);
                                }
                            }

                            @Override
                            public void taskProcess(long cur, long total) {
                                processListener.onSingleProcess(cur,total);
                            }
                        });
                        if(!task.isCancel()){// 为保证取消后不继续执行任务，需要先判断是否已经取消该任务
                            Logger.d(TAG, "开始下载: " + task.getName());
                            task.start();
                        }
                    } else {
                        Logger.e(TAG, "run: task = null");
                    }
                }
            }
        }
    }


    /**
     * 更新本地module，设置本地路径，设置id
     * @param task
     */
    private void updateLocalModule(DownloadTask task) {
        if (P_FileUtil.ICON_FOLDER.equals(task.getCurType())) {
            Logger.d(TAG, "开始设置图标本地路径: " + task.getFilePath());
            // --------------------------------------------------- 更新插件图标本地路径 --start
            ArrayList<PluginInfoModule> pluginConfigList = ConfigSP.getPluginConfigList();

            if(pluginConfigList !=null){
                for (int j = 0; j < pluginConfigList.size(); j++) {
                    PluginInfoModule pluginInfoModule = pluginConfigList.get(j);
                    if(task.getName().contains(pluginInfoModule.getName())){
                        pluginInfoModule.setIconLocalPath(task.getFilePath());
                        pluginInfoModule.setId(MD5.md5(task.getFilePath()));
                    }
                }
                ConfigSP.putPluginConfigList(pluginConfigList);
            }
            // --------------------------------------------------- 更新插件图标本地路径 --end

            // --------------------------------------------------- 更新menu图标本地路径 --start
            ArrayList<MenuInfoModule> menuConfigList = ConfigSP.getMenuConfigList();
            if(menuConfigList != null){
                for (int j = 0; j < menuConfigList.size(); j++) {
                    MenuInfoModule menuInfoModule = menuConfigList.get(j);
                    if (menuInfoModule.getIcon().equals(task.getDownloadUrl())) {
                        menuInfoModule.setLocalPath(task.getFilePath());
                        menuInfoModule.setId(MD5.md5(task.getFilePath()));
                    }
                }
                ConfigSP.putMenuConfigList(menuConfigList);
            }
            // --------------------------------------------------- 更新menu图标本地路径 --end
        }
    }

}
