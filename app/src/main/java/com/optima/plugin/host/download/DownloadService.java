package com.optima.plugin.host.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.R;
import com.optima.plugin.host.download.impl.ProcessListener;
import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.host.download.ui.NotificationUtils;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils = new NotificationUtils();
            NotificationCompat.Builder importanceBuilder = notificationUtils.createImportanceBuilder();
            importanceBuilder.setGroup(TAG);
            importanceBuilder.setSmallIcon(R.mipmap.icon_small);
            importanceBuilder.setGroupSummary(true);
            importanceBuilder.setContentTitle("正在下载");
            importanceBuilder.setContentText("请稍后，正在下载");
            startForeground(1005, importanceBuilder.build());
        }
        downloadQueue = new DownloadQueue(5);
        String needDownloadList = ConfigSP.getNeedDownloadList();
        List<DownloadModule> icons = new Gson().fromJson(needDownloadList, new TypeToken<List<DownloadModule>>() {
        }.getType());
        for (int i = 0; i < icons.size(); i++) {
            DownloadModule icon = icons.get(i);
            String type = icon.getFileType();
            if (P_FileUtil.PLUGIN_FOLDER.equals(type)) {
                downloadQueue.addTask(DownloadTask.downloadPlugin(icon.getFileName(), icon.getFilePath()));
            } else if (P_FileUtil.HOST_FOLDER.equals(type)) {
                downloadQueue.addTask(DownloadTask.downloadHost(icon.getFileName(), icon.getFilePath()));
            } else {
                downloadQueue.addTask(DownloadTask.downloadIcon(icon.getFileName(), icon.getFilePath()));
            }
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
        notificationUtils.cancelNotification(1005);
        super.onDestroy();
    }

    /**
     * 取消任务
     */
    public void cancelTask() {
        downloadQueue.cancel();
    }

    public void startTask() {
        downloadQueue.excuse();
    }


    public class DownloadBinder extends Binder {
        public DownloadService getService(ProcessListener processListener) {
            downloadQueue.addOnProcessListener(processListener);
            return DownloadService.this;
        }
    }
}
