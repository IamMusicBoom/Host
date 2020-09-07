package com.optima.plugin.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.optima.plugin.host.binder.HostBinderFetcher;
import com.optima.plugin.host.utils.H_Binder;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginConfig;

import org.xutils.x;

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
        x.Ext.init(this);
        Intent intent = new Intent(this,MainService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else{
            startService(intent);
        }
//        RePlugin.registerHookingClass("com.optima.plugin.host.view.download.WaveView", RePlugin.createComponentName(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.host.view.download.WaveView"), null);

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
