package com.optima.plugin.host.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;

/**
 * create by wma
 * on 2020/8/17 0017
 */
public class BroadcastTest extends BroadcastReceiver {
    String TAG = BroadcastTest.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            String alias = intent.getStringExtra(P_Constants.INTENT_ALIAS);
            String className = intent.getStringExtra(P_Constants.INTENT_CLASS_NAME);
            Intent goIntent = P_Context.createIntent(alias, className);
            goIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            P_Context.startPluginActivity(context,goIntent);
        }
    }
}
