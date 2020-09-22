package com.optima.plugin.host;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginConfig;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/8/13 0013
 */
public class MainApplication extends RePluginApplication {
    String TAG = MainApplication.class.getSimpleName();


    public static List<Activity> mActivities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        // Add signature to "White List"
//        RePlugin.addCertSignature("FB4124D44BFF4AF077B5A358EAEB89AA");// 添加校验签名文件
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        P_Context.setContext(this);
        x.Ext.init(this);
        Intent intent = new Intent(this,MainService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else{
            startService(intent);
        }
    }


    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityStopped(Activity activity) {
            Logger.d(TAG, "onActivityStopped: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Logger.d(TAG, "onActivityStarted: " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            Logger.d(TAG, "onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Logger.d(TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Logger.d(TAG, "onActivityPaused: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Logger.d(TAG, "onActivityDestroyed: " + activity.getClass().getSimpleName());
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Logger.d(TAG, "onActivityCreated: " + activity.getClass().getSimpleName());
            if (!mActivities.contains(activity)) {
                mActivities.add(activity);
            }
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig rePluginConfig = new RePluginConfig();
        rePluginConfig.setUseHostClassIfNotFound(true);
//        rePluginConfig.setVerifySign(true);//添加签名校验
        return rePluginConfig;
    }
}
