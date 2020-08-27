package com.optima.plugin.host.binder;

import android.os.IBinder;

import com.optima.plugin.host.MainService;
import com.optima.plugin.host.service.AIDLService;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.qihoo360.replugin.IHostBinderFetcher;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class HostBinderFetcher implements IHostBinderFetcher {
    String TAG = HostBinderFetcher.class.getSimpleName();
    @Override
    public IBinder query(String module) {
        Logger.d(TAG, "query: module = " + module);
        if(P_Constants.LOCK_SCREEN_BINDER.equals(module)){
            return new LockScreenBinder();
        }
        return null;
    }
}
