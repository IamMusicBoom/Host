package com.optima.plugin.host;

import android.content.Context;

import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginConfig;

/**
 * create by wma
 * on 2020/8/13 0013
 */
public class MainApplication extends RePluginApplication {
    String TAG = MainApplication.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        P_Context.setContext(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig rePluginConfig = new RePluginConfig();
        rePluginConfig.setUseHostClassIfNotFound(true);
        return rePluginConfig;
    }
}
