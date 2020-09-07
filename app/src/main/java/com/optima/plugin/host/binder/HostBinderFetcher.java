package com.optima.plugin.host.binder;

import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;

import com.optima.plugin.host.activity.UsePluginViewActivity;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Resource;
import com.qihoo360.replugin.IHostBinderFetcher;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class HostBinderFetcher implements IHostBinderFetcher {
    View view;

    public HostBinderFetcher(View view) {
        this.view = view;
    }

    public HostBinderFetcher() {
    }
    String TAG = HostBinderFetcher.class.getSimpleName();
    @Override
    public IBinder query(String module) {
        Logger.d(TAG, "query: module = " + module);
        if(P_Constants.LOCK_SCREEN_BINDER.equals(module)){
            return new LockScreenBinder();
        }
        if(P_Constants.HOST_BINDER.equals(module)){
            return new HostBinder();
        }
        if("host_view_binder".equals(module)){
            return new ViewBinder(view);
        }
        return null;
    }
}
