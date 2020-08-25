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
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)){// 屏幕点亮
                Logger.d(TAG, "onReceive: 解锁");
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){// 熄屏幕
                Logger.d(TAG, "onReceive: 锁屏");
            }else if(Intent.ACTION_USER_PRESENT.equals(action)){// 屏幕点亮，且解锁
                Logger.d(TAG, "onReceive: ACTION_USER_PRESENT");
            }else{
                String alias = intent.getStringExtra(P_Constants.INTENT_ALIAS);
                String className = intent.getStringExtra(P_Constants.INTENT_CLASS_NAME);
                Intent goIntent = P_Context.createIntent(alias, className);
                goIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                P_Context.startPluginActivity(context,goIntent);
            }
        }
    }
}
