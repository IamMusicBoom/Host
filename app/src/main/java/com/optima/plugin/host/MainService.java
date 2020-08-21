package com.optima.plugin.host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.utils.NotificationUtils;

/**
 * create by wma
 * on 2020/8/21 0021
 */
public class MainService extends Service {
    final String TAG = MainService.class.getSimpleName();

    @Override
    public void onCreate() {
        Logger.d(TAG, "onCreate: ");
        super.onCreate();
        startForeground(1001,new NotificationUtils().createDefaultBuilder().setGroupSummary(true).build());
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
        return null;
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
}
