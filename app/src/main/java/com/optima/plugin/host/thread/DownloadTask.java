package com.optima.plugin.host.thread;

import com.optima.plugin.repluginlib.pluginUtils.P_Context;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class DownloadTask {
    private final String TAG = DownloadTask.class.getSimpleName();

    public static final String ICON_FOLDER = "icon";// 下载图标
    public static final String PLUGIN_FOLDER = "plugin";// 下载插件


    TaskFinishListener mFinishListener;
    final String SAVE_PAH;

    public void addFinishListener(TaskFinishListener finishListener) {
        this.mFinishListener = finishListener;
    }

    /**
     * 创建一个下载icon的任务
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadIcon(String name, String downloadUrl) {
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, ICON_FOLDER);
        return downloadTask;
    }

    /**
     * 创建一个下载插件的任务
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadPlugin(String name, String downloadUrl){
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, PLUGIN_FOLDER);
        return downloadTask;
    }

    /**
     * 创建一个下载文件任务
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadFile(String name, String downloadUrl,String folderName){
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, folderName);
        return downloadTask;
    }

    private DownloadTask(String name, String downloadUrl, String fileType) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        SAVE_PAH = P_Context.getContext().getExternalFilesDir(fileType) + File.separator + name;
    }

    private String name;

    private String downloadUrl;


    /**
     * 执行线程
     */
    public void start() {
        download();
    }

    private Callback.Cancelable download() {
        RequestParams params = new RequestParams(downloadUrl);
        params.setSaveFilePath(SAVE_PAH);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (mFinishListener != null) {
                    mFinishListener.finish(DownloadTask.this);
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
        return cancelable;
    }

    public String getFilePath() {
        return SAVE_PAH;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
