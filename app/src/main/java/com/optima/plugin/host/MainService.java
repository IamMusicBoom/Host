package com.optima.plugin.host;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.optima.plugin.host.binder.HostBinderFetcher;
import com.optima.plugin.host.broadcast.LockScreenReceiver;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Manager;
import com.optima.plugin.repluginlib.utils.NotificationUtils;
import com.qihoo360.replugin.IHostBinderFetcher;
import com.qihoo360.replugin.RePlugin;

/**
 * create by wma
 * on 2020/8/21 0021
 */
public class MainService extends Service {
    LockScreenReceiver screenReceiver;
    final String TAG = MainService.class.getSimpleName();

    @Override
    public void onCreate() {
        Logger.d(TAG, "onCreate: packageName = " + P_Constants.HOST_PACKAGE_NAME);
        super.onCreate();
        startForeground(1001, new NotificationUtils().createDefaultBuilder().setGroupSummary(true).build());
        screenReceiver = new LockScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_USER_UNLOCKED);
        registerReceiver(screenReceiver, filter);
        Logger.d(TAG, "onCreate: pid = " + android.os.Process.myPid() + " ProcessName = " + P_Manager.getProcessName(android.os.Process.myPid()));
        HostBinderFetcher hostBinderFetcher = new HostBinderFetcher();
        RePlugin.registerHostBinder(hostBinderFetcher);
        RePlugin.registerGlobalBinder(P_Constants.LOCK_SCREEN_BINDER, new ILockScreenImpl.Stub() {
            @Override
            public void setUseLockScreen(String pluginName, int priority) throws RemoteException {
                Logger.d(TAG, "setUseLockScreen: pluginName = " + pluginName + " priority = " + priority);
            }
        });
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
        unregisterReceiver(screenReceiver);
    }
}
