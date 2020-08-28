package com.optima.plugin.host;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.activity.BinderTestActivity;
import com.optima.plugin.host.activity.LockScreenActivity;
import com.optima.plugin.host.activity.NotificationTestActivity;
import com.optima.plugin.host.activity.PluginManagerTestActivity;
import com.optima.plugin.host.activity.ProviderTestActivity;
import com.optima.plugin.host.activity.ReflexTestActivity;
import com.optima.plugin.host.activity.ServiceTestActivity;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_FileUtil;
import com.optima.plugin.repluginlib.PluginUtils.P_Manager;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.qihoo360.replugin.RePlugin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    List<String> fileNames = new ArrayList<>();
    final String FILE_NAME_PLUGIN_1 = "plugin1.apk";
    final String FILE_NAME_PLUGIN_2 = "plugin2.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean pluginRunning = RePlugin.isPluginRunning(P_Constants.ALIAS_PLUGIN_1);
        if (pluginRunning) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileNames.add(FILE_NAME_PLUGIN_1);
//                fileNames.add(FILE_NAME_PLUGIN_2);
                P_FileUtil.simulateInstallExternalPlugin(fileNames);
            }
        }).start();
        Logger.d(TAG, "onCreate: pid = " + android.os.Process.myPid() + " taskId = " + getTaskId());
        Logger.d(TAG, "onCreate: pid = " + android.os.Process.myPid() + " ProcessName = " + P_Manager.getProcessName(android.os.Process.myPid() ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_plugin_activity) {// 跳转到插件Activity
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, P_Constants.PACKAGE_NAME_PLUGIN_1 + ".Plugin1MainActivity"));
            startActivity(intent, true);
        } else if (v.getId() == R.id.btn_go_plugin_activity_for_result) {// 跳转插件Activity并且有返回值
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, P_Constants.PACKAGE_NAME_PLUGIN_1 + ".Plugin1MainActivity"));
            startActivityForResult(intent, P_Constants.REQUEST_CODE, true);
        } else if (v.getId() == R.id.btn_send_broad_cast_receiver) {// 给插件发送广播
            RePlugin.fetchComponentList(P_Constants.ALIAS_PLUGIN_1);
            RePlugin.fetchComponentList(P_Constants.ALIAS_PLUGIN_2);
            Intent intent = new Intent(P_Constants.ACTION_BROADCAST_RECEIVER);
            intent.putExtra(P_Constants.INTENT_ALIAS,P_Constants.ALIAS_PLUGIN_1);
            intent.putExtra(P_Constants.INTENT_CLASS_NAME,P_Constants.PACKAGE_NAME_PLUGIN_1+".activity.BroadcastActivity");
            intent.putExtra(P_Constants.INTENT_KEY, "WMA-OK");
            sendBroadcast(intent);
        } else if (v.getId() == R.id.btn_go_service_test_activity) {
            Intent intent = new Intent(MainActivity.this, ServiceTestActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_go_provider_test_activity){
            Intent intent = new Intent(MainActivity.this,ProviderTestActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_go_Notification_test_activity){
            Intent intent = new Intent(MainActivity.this, NotificationTestActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_go_reflex_test_activity){
            Intent intent = new Intent(MainActivity.this, ReflexTestActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.btn_go_plugin_manager_test_activity){// 插件管理测试界面
            Intent intent = new Intent(MainActivity.this, PluginManagerTestActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.btn_go_test_binder_activity){// binder测试
            Intent intent = new Intent(MainActivity.this, BinderTestActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_go_lock_screen_activity){
            Intent intent = new Intent(MainActivity.this, LockScreenActivity.class);
            startActivity(intent);
        }
    }
}
