package com.optima.plugin.host.download;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.optima.plugin.repluginlib.utils.NotificationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

/**
 * create by wma
 * on 2020/9/1 0001
 * 下载任务
 */
public class DownloadTask {
    private final String TAG = DownloadTask.class.getSimpleName();
    private final String PLUGIN_FILE_LOCAL_PATH;
    public static final String FOLDER_NAME = "icon";
    private Context mContext;
    private TaskFinishListener finishListener;
    /**
     * 通知栏
     */
    private NotificationUtils notificationUtils;
    private NotificationCompat.Builder notificationBuilder;

    /**
     * 文件名字
     */
    private String fileName;

    /**
     * 文件下载地址
     */
    private String downloadUrl;

    /**
     * 任务是否在运行
     */
    private boolean isRunning;

    /**
     * 任务执行map
     */
    private HashMap<String, Callback.Cancelable> cancelableHashMap = new HashMap<>();
    /**
     * /**
     * 下载回调
     */
    private CallbackListener callback;
    /**
     * 下载进度条ID
     */
    private int notificationId = 250;

    public DownloadTask(String fileName, String downloadUrl, CallbackListener callback) {
        mContext = P_Context.getContext();
        this.callback = callback;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        PLUGIN_FILE_LOCAL_PATH = mContext.getExternalFilesDir(FOLDER_NAME).getAbsolutePath();
        notificationUtils = new NotificationUtils();
        notificationBuilder = notificationUtils.createDownloadBuilder(100, 0);
    }


    public void excuse() {
        this.notificationId = (int) (downloadUrl.length() / 1000 + Math.random() * 100 + 1);
        Logger.d(TAG, "excuse: notificationId = " + notificationId);
        notificationUtils.showNotification(notificationId, notificationBuilder.build());
        new TaskThread().start();

    }


    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }


    public void cancelDownLoadTasks() {
        for (String pluginName : cancelableHashMap.keySet()) {
            cancelDownloadTask(pluginName);
        }
        cancelableHashMap.clear();
    }

    public void cancelDownloadTask(String pluginName) {
        if (cancelableHashMap.containsKey(pluginName)) {
            Callback.Cancelable cancelable = cancelableHashMap.get(pluginName);
            if (cancelable != null) {
                cancelable.cancel();
                cancelable = null;
                cancelableHashMap.put(pluginName, cancelable);
            }
        }
    }


    class TaskThread extends Thread{
        @Override
        public void run() {
            super.run();
            Callback.Cancelable cancelable = download();
            cancelableHashMap.put(fileName, cancelable);
        }
    }

    private Callback.Cancelable download() {
        RequestParams params = new RequestParams(downloadUrl);
        params.setSaveFilePath(PLUGIN_FILE_LOCAL_PATH + File.separator + fileName);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(TAG, "onError: ex = " + ex);
                if (callback != null) {
                    callback.onError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.d(TAG, "onCancelled: ");
                if (callback != null) {
                    callback.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished: ");
                isRunning = false;
                notificationUtils.cancelNotification(notificationId);
                if (callback != null) {
                    callback.onFinished();
                }
                if(finishListener != null){
                    finishListener.finish(DownloadTask.this);
                }
            }

            @Override
            public void onWaiting() {
                Logger.d(TAG, "onWaiting: ");
                if (callback != null) {
                    callback.onWaiting();
                }
            }

            @Override
            public void onStarted() {
                Logger.d(TAG, "onStarted: ");
                isRunning = true;
                if (callback != null) {
                    callback.onStarted();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Logger.d(TAG, "onLoading: current = " + current + " total = " + total);
                Logger.d(TAG, "onLoading: process = " + current + "/" + total);
                notificationUtils.updateDownloadNotification(notificationId, 100, (int) ((current * 100) / total), notificationBuilder);
                if (callback != null) {
                    callback.onLoading(total, current, isDownloading);
                }
            }
        });
        return cancelable;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void addOnFinishListener(TaskFinishListener finishListener) {
        this.finishListener = finishListener;
    }
}
