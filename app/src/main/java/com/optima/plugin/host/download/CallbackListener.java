package com.optima.plugin.host.download;

import org.xutils.common.Callback;

import java.io.File;

/**
 * create by wma
 * on 2020/9/9 0009
 */
public interface CallbackListener {
    void onSuccess(File result);

    void onError(Throwable ex, boolean isOnCallback);

    void onCancelled(Callback.CancelledException cex);

    void onFinished();

    void onWaiting();

    void onStarted();

    void onLoading(long total, long current, boolean isDownloading);
}
