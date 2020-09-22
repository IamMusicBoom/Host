package com.optima.plugin.host.thread;

import com.optima.plugin.repluginlib.Logger;
import com.qihoo360.replugin.helper.LogDebug;

import java.util.LinkedHashMap;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class DownloadQueue {
    final String TAG = DownloadQueue.class.getSimpleName();
    private int runningCount;
    private int mMaxThreadCount = 3;
    private int total = 0;
    private int cur = 0;
    private LinkedHashMap<String, DownloadTask> tasks = new LinkedHashMap<>();
    private ProcessListener processListener;

    public void addOnProcessListener(ProcessListener processListener) {
        this.processListener = processListener;
    }

    public DownloadQueue(int maxThreadCount) {
        this.mMaxThreadCount = maxThreadCount;
    }

    public void addTask(DownloadTask task) {
        tasks.put(task.getName(), task);
    }

    public int taskCount() {
        return tasks.size();
    }

    public void excuse() {
        total = taskCount();
        if (total <= 0) {
            Logger.e(TAG, "excuse: 执行失败，没有任务");
            return;
        }
        WorkThread  workThread = new WorkThread();
        workThread.start();
    }

    public void cancel() {
        for (String key : tasks.keySet()) {
            DownloadTask downloadTask = tasks.get(key);
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

    class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (DownloadQueue.this) {
                if (processListener != null) {
                    processListener.onStart();
                }
                for (String name : tasks.keySet()) {
                    runningCount++;
                    if (runningCount > mMaxThreadCount) {
                        try {
                            DownloadQueue.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    DownloadTask task = tasks.get(name);
                    if (task != null) {
                        task.addFinishListener(new TaskFinishListener() {
                            @Override
                            public void finish(DownloadTask task) {
                                synchronized (DownloadQueue.this) {
                                    cur++;
                                    if (processListener != null) {
                                        processListener.onProcess(cur, total);
                                    }
                                    runningCount--;
                                    String name1 = task.getName();
                                    task = null;
                                    tasks.put(name1, task);
                                    if (runningCount < mMaxThreadCount) {
                                        DownloadQueue.this.notify();
                                    }
                                    if (runningCount == 0) {
                                        tasks.clear();
                                        if (processListener != null) {
                                            processListener.onFinish();
                                        }
                                    }
                                }
                            }
                        });
                        if(!task.isCancel()){
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

}
