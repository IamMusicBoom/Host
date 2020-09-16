package com.optima.plugin.host.service;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.optima.plugin.repluginlib.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * create by wma
 * on 2020/9/16 0016
 */
public class WorkService extends JobIntentService {
    final String TAG = WorkService.class.getSimpleName();
    final String downloadUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598953251041&di=a710c8d0ef81ac28393751ebd4181499&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fe%2F59ae3a99a20fb.jpg%3Fdown";

    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, WorkService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand: " + Thread.currentThread().getName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        Logger.d(TAG, "onBind: " + Thread.currentThread().getName());
        return super.onBind(intent);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.d(TAG, "onHandleWork: " + Thread.currentThread().getName());
        download();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG, "onUnbind: " + Thread.currentThread().getName());
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy: " + Thread.currentThread().getName());
        super.onDestroy();
    }


    private Callback.Cancelable download() {
        RequestParams params = new RequestParams(downloadUrl);
        params.setSaveFilePath(getApplication().getFilesDir() + File.separator + "test.jpg");
        Callback.Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(TAG, "onError: ex = " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.d(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished: ");
            }

            @Override
            public void onWaiting() {
                Logger.d(TAG, "onWaiting: ");
            }

            @Override
            public void onStarted() {
                Logger.d(TAG, "onStarted: ");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Logger.d(TAG, "onLoading: ");
            }
        });
        return cancelable;
    }

}

