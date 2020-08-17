package com.optima.plugin.host.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;

/**
 * create by wma
 * on 2020/8/17 0017
 */
public class BroadcastTest extends BroadcastReceiver {
    String TAG = BroadcastTest.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            Logger.d(TAG, "onReceive: " + intent.getStringExtra(P_Constants.INTENT_KEY));
        }
    }
}
