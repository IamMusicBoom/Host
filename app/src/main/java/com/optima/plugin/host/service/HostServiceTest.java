package com.optima.plugin.host.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.qihoo360.replugin.helper.LogDebug;

/**
 * create by wma
 * on 2020/8/18 0018
 */
public class HostServiceTest extends Service {
    String TAG = HostServiceTest.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate: ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String stringExtra = intent.getStringExtra(P_Constants.INTENT_KEY);
            Logger.d(TAG, "onStartCommand: stringExtra = " + stringExtra);
        }
        Logger.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(intent != null){
            String stringExtra = intent.getStringExtra(P_Constants.INTENT_KEY);
            Logger.d(TAG, "onBind: stringExtra = " + stringExtra);
        }
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
    }

    class MyBinder extends Binder {

    }
}
