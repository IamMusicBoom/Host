package com.optima.plugin.host.binder;

import android.os.RemoteException;

import com.optima.plugin.host.ILockScreenImpl;
import com.optima.plugin.host.activity.LockScreenActivity;
import com.optima.plugin.repluginlib.Logger;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class LockScreenBinder extends ILockScreenImpl.Stub {
    String TAG = LockScreenBinder.class.getSimpleName();
    private String pluginName;
    private int priority;

    @Override
    public void setUseLockScreen(String pluginName, int priority) throws RemoteException {
        Logger.d(TAG, "setUseLockScreen: pluginName = " + pluginName + " priority = " + priority);
        this.pluginName = pluginName;
        this.priority = priority;
    }


    public String getPluginName(){
        return pluginName;
    }

    public int getPriority(){
        return priority;
    }


}
