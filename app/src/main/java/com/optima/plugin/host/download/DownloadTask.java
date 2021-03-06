package com.optima.plugin.host.download;

import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.optima.plugin.host.R;
import com.optima.plugin.host.download.impl.TaskFinishListener;
import com.optima.plugin.host.download.ui.NotificationUtils;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * create by wma
 * on 2020/9/17 0017
 * 下载任务
 */
public class DownloadTask {
    private final String TAG = DownloadTask.class.getSimpleName();
    private Callback.Cancelable cancelable;



    /**
     * 任务完成的监听
     */
    private TaskFinishListener mFinishListener;

    /**
     * 下载文件的储存的路径
     */
    private final String SAVE_PAH;

    /**
     * 文件下载成功后保存的路径
     */
    private String mLocalPath;

    /**
     * 任务是否取消的标志
     */
    private boolean isCancel;

    /**
     * 储存在文件夹里面的文件名字
     */
    private String name;

    /**
     * 下载文件的网络全路径
     */
    private String downloadUrl;

    /**
     * 通知栏显示，在下载apk或者插件的时候才会显示，下载图标，或者小文件的时候不应该有。
     */
    private NotificationUtils mNotificationUtils;
    private NotificationCompat.Builder mDownloadBuilder;

    /**
     * 每个任务的通知栏id，需要根据id更新或者取消
     */
    private int mNotificationId;

    /**
     * 下载文件的类型
     * 图标 {@link com.optima.plugin.repluginlib.pluginUtils.P_FileUtil#ICON_FOLDER}
     * 插件 {@link com.optima.plugin.repluginlib.pluginUtils.P_FileUtil#PLUGIN_FOLDER}
     * 宿主 {@link com.optima.plugin.repluginlib.pluginUtils.P_FileUtil#HOST_FOLDER}
     */
    private String mCurType;

    public void addFinishListener(TaskFinishListener finishListener) {
        this.mFinishListener = finishListener;
    }

    /**
     * 创建一个下载icon的任务
     *
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadIcon(String name, String downloadUrl) {
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, P_FileUtil.ICON_FOLDER);
        return downloadTask;
    }

    /**
     * 创建一个下载插件的任务
     *
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadPlugin(String name, String downloadUrl) {
        Logger.d("", "downloadPlugin: name = " + name);
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, P_FileUtil.PLUGIN_FOLDER);
        return downloadTask;
    }

    /**
     * 创建一个下载宿主APK的任务
     *
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadHost(String name, String downloadUrl) {
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, P_FileUtil.HOST_FOLDER);
        return downloadTask;
    }

    /**
     * 创建一个下载文件任务
     *
     * @param name
     * @param downloadUrl
     * @return
     */
    public static DownloadTask downloadFile(String name, String downloadUrl, String folderName) {
        DownloadTask downloadTask = new DownloadTask(name, downloadUrl, folderName);
        return downloadTask;
    }

    private DownloadTask(String name, String downloadUrl, String fileType) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.mCurType = fileType;
        SAVE_PAH = P_Context.getContext().getExternalFilesDir(fileType) + File.separator + name;
        if (!P_FileUtil.ICON_FOLDER.equals(fileType)) {// 只要不是图标下载，都显示通知栏
            mNotificationUtils = new NotificationUtils();
            mDownloadBuilder = mNotificationUtils.createDownloadBuilder(100, 0);
            mDownloadBuilder.setGroup(DownloadService.TAG);
            mDownloadBuilder.setContentText("正在下载...");
            mDownloadBuilder.setContentTitle(this.name);
            mDownloadBuilder.setSmallIcon(R.mipmap.icon_small);
        }
    }


    /**
     * 执行线程，开启任务
     * note:该方法在子线程中执行
     */
    public void start() {
        if (mNotificationUtils != null) {
            mNotificationId = (int) (downloadUrl.length() / 1000 + Math.random() * 100);
            mNotificationUtils.showNotification(mNotificationId, mDownloadBuilder.build());
        }
        download();
    }

    private Callback.Cancelable download() {
        RequestParams params = new RequestParams(downloadUrl);
        params.setSaveFilePath(SAVE_PAH);
        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                mLocalPath = result.getAbsolutePath();
                if (mNotificationUtils != null) {
                    mNotificationUtils.cancelNotification(mNotificationId);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished: mLocalPath = " + mLocalPath);
                if (TextUtils.isEmpty(mLocalPath)) {
                    if (mFinishListener != null) {
                        mFinishListener.error(DownloadTask.this);
                    }
                } else {
                    if (mFinishListener != null) {
                        mFinishListener.finish(DownloadTask.this);
                    }
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
                if (mNotificationUtils != null) {
                    mNotificationUtils.updateDownloadNotification(mNotificationId, 100, (int) ((current * 100) / total), mDownloadBuilder);
                }
                if (mFinishListener != null) {
                    mFinishListener.taskProcess(current, total);
                }
            }
        });
        return cancelable;
    }

    public String getFilePath() {
        return mLocalPath;
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

    public void cancel() {
        isCancel = true;
        if (cancelable != null) {
            cancelable.cancel();
            cancelable = null;
        }
    }

    public Callback.Cancelable getCancelable() {
        return cancelable;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public String getCurType() {
        return mCurType;
    }
}
