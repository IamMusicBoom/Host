package com.optima.plugin.host.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.optima.plugin.repluginlib.pluginUtils.P_Context;


/**
 * create by wma
 * on 2020/9/24 0024
 */
public class DownloadReceiver extends BroadcastReceiver {

    public static final String HANDLE_DOWNLOAD_HOST_FINISH = "HANDLE_DOWNLOAD_HOST_FINISH";
    public static final String HANDLE_DOWNLOAD_PLUGIN_FINISH = "HANDLE_DOWNLOAD_PLUGIN_FINISH";
    public static final String CANCEL_DOWNLOAD_HOST = "CANCEL_DOWNLOAD_HOST";
    public static final String CANCEL_DOWNLOAD_PLUGIN = "CANCEL_DOWNLOAD_PLUGIN";


    @Override
    public void onReceive(Context context, Intent intent) {
    }

    public static void sendMessage(String action){
        Intent intent = new Intent(action);
        P_Context.getContext().sendBroadcast(intent);

    }
}
