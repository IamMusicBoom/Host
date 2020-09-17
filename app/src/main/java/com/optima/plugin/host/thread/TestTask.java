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
public class TestTask extends Thread {
    private int count;
    private TestCallback callback;
    private int total =30;

    String downloadUrl;
    String fileName;

    public TestTask(String downloadUrl, String fileName,TestCallback callback) {
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.callback = callback;
    }

    public TestTask(TestCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();

        try {
            download();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void download() throws InterruptedException {
        RequestParams params = new RequestParams(downloadUrl);
        params.setSaveFilePath(P_Context.getContext().getExternalFilesDir("icon").getAbsolutePath() + File.separator + fileName);
        x.http().get(params, new Callback.ProgressCallback<File>() {
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
                callback.finish();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                callback.start();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                callback.loading(current, total);
            }
        });
    }
}
