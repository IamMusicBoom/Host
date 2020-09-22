package com.optima.plugin.host.thread;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.module.Icon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * create by wma
 * on 2020/9/21 0021
 */
public class DownloadService extends Service {
    final String TAG = DownloadService.class.getSimpleName();
    public DownloadQueue downloadQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate: ");
        downloadQueue = new DownloadQueue(5);
        AssetManager assets = getResources().getAssets();
        try {
            InputStream open = assets.open("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(open));
            Gson gson = new Gson();
            List<Icon> icons = gson.fromJson(reader, new TypeToken<List<Icon>>() {
            }.getType());
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                downloadQueue.addTask(DownloadTask.downloadIcon(icon.getName(), icon.getPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG, "onBind: ");
        startTask();
        return new DownloadBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    /**
     * 取消任务
     */
    public void cancelTask() {
        downloadQueue.cancel();
    }

    public void startTask(){
        downloadQueue.excuse();
    }


    public class DownloadBinder extends Binder {
        public DownloadService getService(ProcessListener processListener) {
            downloadQueue.addOnProcessListener(processListener);
            return DownloadService.this;
        }
    }
}
