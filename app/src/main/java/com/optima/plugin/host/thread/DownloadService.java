package com.optima.plugin.host.thread;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.module.Icon;
import com.optima.plugin.repluginlib.utils.NotificationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * create by wma
 * on 2020/9/21 0021
 * 下载服务，接口请求服务，主要做宿主版本验证，宿主APK下载，插件配置清单接口请求，插件配置清单下载
 */
public class DownloadService extends Service {
    public static final String TAG = DownloadService.class.getSimpleName();
    public DownloadQueue downloadQueue;
    private NotificationUtils notificationUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate: ");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationUtils = new NotificationUtils();
            NotificationCompat.Builder importanceBuilder = notificationUtils.createImportanceBuilder();
            importanceBuilder.setGroup(TAG);
            importanceBuilder.setSmallIcon(R.mipmap.ic_download);
            importanceBuilder.setGroupSummary(true);
            importanceBuilder.setContentTitle("正在下载");
            importanceBuilder.setContentText("请稍后，正在下载");
            startForeground(1005,importanceBuilder.build());
        }
        downloadQueue = new DownloadQueue(5);
        AssetManager assets = getResources().getAssets();
        try {
            InputStream open = assets.open("json2.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(open));
            Gson gson = new Gson();
            List<Icon> icons = gson.fromJson(reader, new TypeToken<List<Icon>>() {
            }.getType());
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                String name = icon.getName();
                String[] split = name.split("\\.");
                if(split[1].equalsIgnoreCase("jpg")){
                    downloadQueue.addTask(DownloadTask.downloadIcon(icon.getName(), icon.getPath()));
                }else{
                    downloadQueue.addTask(DownloadTask.downloadPlugin(icon.getName(), icon.getPath()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand: ");
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
