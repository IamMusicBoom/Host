package com.optima.plugin.host.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.base.BaseActivity;


/**
 * create by wma
 * on 2020/8/25 0025
 * 中转Activity，由于PendingIntent 和 通过 URI 的方式跳转的Activity无法直接跳转至插件Activity,所以需要通过该Activity进行一次中转
 */
public class TransitActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String pluginName = intent.getStringExtra(P_Constants.INTENT_TARGET_PLUGIN_NAME);
        String className = intent.getStringExtra(P_Constants.INTENT_TARGET_CLASS_NAME);
        intent.setComponent(new ComponentName(pluginName, className));
        int type = intent.getIntExtra(P_Constants.INTENT_TARGET_TYPE, P_Constants.TARGET_TYPE_START_ACTIVITY);
        Logger.d(TAG, "onCreate: type = " + type + " className = " + className);
        switch (type) {
            case P_Constants.TARGET_TYPE_START_ACTIVITY:
                startActivity(intent, true);
                break;
            case P_Constants.TARGET_TYPE_START_SERVICE:
                startService(intent, true);
                break;
            case P_Constants.TARGET_TYPE_SEND_BROADCAST:
                sendBroadcast(intent);
                break;
        }
        finish();

    }

    /**
     * 创建打开Activity的 PendingIntent
     *
     * @param context
     * @param pluginName 插件名字
     * @param className  需要打开的Activity的类全名
     * @return
     */
    public static Intent createStartActivityIntent(Context context, String pluginName, String className) {
        Logger.d("", "createStartActivityPendingIntent: className = " + className);
        Intent intent = new Intent(context, TransitActivity.class);
        intent.putExtra(P_Constants.INTENT_TARGET_TYPE, P_Constants.TARGET_TYPE_START_ACTIVITY);
        intent.putExtra(P_Constants.INTENT_TARGET_PLUGIN_NAME, pluginName);
        intent.putExtra(P_Constants.INTENT_TARGET_CLASS_NAME, className);
        return intent;
    }


    /**
     * 创建开启Service的 PendingIntent
     *
     * @param context
     * @param pluginName 插件名字
     * @param className  需要打开的Activity的类全名
     * @return
     */
    public static Intent createStartServiceIntent(Context context, String pluginName, String className) {
        Logger.d("", "createStartServicePendingIntent: className = " + className);
        Intent intent = new Intent(context, TransitActivity.class);
        intent.putExtra(P_Constants.INTENT_TARGET_TYPE, P_Constants.TARGET_TYPE_START_SERVICE);
        intent.putExtra(P_Constants.INTENT_TARGET_PLUGIN_NAME, pluginName);
        intent.putExtra(P_Constants.INTENT_TARGET_CLASS_NAME, className);
        return intent;
    }


    /**
     * 创建发送广播的 PendingIntent
     *
     * @param context
     * @param pluginName 插件名字
     * @param className  需要打开的Activity的类全名
     * @return
     */
    public static Intent createSendBroadcastIntent(Context context, String pluginName, String className) {
        Intent intent = new Intent(context, TransitActivity.class);
        intent.putExtra(P_Constants.INTENT_TARGET_TYPE, P_Constants.TARGET_TYPE_SEND_BROADCAST);
        intent.putExtra(P_Constants.INTENT_TARGET_PLUGIN_NAME, pluginName);
        intent.putExtra(P_Constants.INTENT_TARGET_CLASS_NAME, className);
        return intent;
    }

    /**
     * 创建 PendingIntent
     *
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent createPendingIntent(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


}
