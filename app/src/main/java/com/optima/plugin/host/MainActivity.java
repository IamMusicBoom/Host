package com.optima.plugin.host;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.optima.plugin.host.activity.BinderTestActivity;
import com.optima.plugin.host.activity.DOCSelectActivity;
import com.optima.plugin.host.activity.DownloadActivity;
import com.optima.plugin.host.activity.HomeActivity;
import com.optima.plugin.host.activity.JobIntentServiceActivity;
import com.optima.plugin.host.activity.UsePluginFragmentActivity;
import com.optima.plugin.host.activity.UsePluginResourceActivity;
import com.optima.plugin.host.activity.UsePluginViewActivity;
import com.optima.plugin.host.activity.WaveViewActivity;
import com.optima.plugin.host.databases.DBTestActivity;
import com.optima.plugin.host.view.gesture.LockScreenActivity;
import com.optima.plugin.host.activity.NotificationTestActivity;
import com.optima.plugin.host.activity.PluginManagerTestActivity;
import com.optima.plugin.host.activity.ProviderTestActivity;
import com.optima.plugin.host.activity.ReflexTestActivity;
import com.optima.plugin.host.activity.ServiceTestActivity;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.TestActivity;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;
import com.optima.plugin.repluginlib.pluginUtils.P_Manager;
import com.optima.plugin.repluginlib.base.BasePermissionActivity;
import com.qihoo360.replugin.RePlugin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasePermissionActivity implements View.OnClickListener {
    List<String> fileNames = new ArrayList<>();
    final String FILE_NAME_PLUGIN_1 = "plugin1.apk";
    final String FILE_NAME_PLUGIN_2 = "plugin2.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNeedCheckPermission(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d(TAG, "onCreate: " + P_Manager.getPluginVersion(P_Constants.ALIAS_PLUGIN_1));
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
        int dasda = P_Manager.getPluginVersion("dasda");
        Logger.d(TAG, "onCreate: dasda = " + dasda);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_plugin_activity) {// 跳转到插件Activity
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, P_Constants.PACKAGE_NAME_PLUGIN_1 + ".Plugin1MainActivity"));
//            intent.setComponent(new ComponentName("com.optima.plugin.plugindaily","com.optima.plugin.daily.task.activity.IndexActivity"));
            startActivity(intent, true);
        } else if (v.getId() == R.id.btn_go_plugin_activity_for_result) {// 跳转插件Activity并且有返回值
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, P_Constants.PACKAGE_NAME_PLUGIN_1 + ".Plugin1MainActivity"));
            startActivityForResult(intent, P_Constants.REQUEST_CODE, true);
        } else if (v.getId() == R.id.btn_send_broad_cast_receiver) {// 给插件发送广播
            RePlugin.fetchComponentList(P_Constants.ALIAS_PLUGIN_1);
            RePlugin.fetchComponentList(P_Constants.ALIAS_PLUGIN_2);
            Intent intent = new Intent(P_Constants.ACTION_BROADCAST_RECEIVER);
            intent.putExtra(P_Constants.INTENT_ALIAS, P_Constants.ALIAS_PLUGIN_1);
            intent.putExtra(P_Constants.INTENT_CLASS_NAME, P_Constants.PACKAGE_NAME_PLUGIN_1 + ".activity.BroadcastActivity");
            intent.putExtra(P_Constants.INTENT_KEY, "WMA-OK");
            sendBroadcast(intent);
        } else if (v.getId() == R.id.btn_go_service_test_activity) {
            Intent intent = new Intent(MainActivity.this, ServiceTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_provider_test_activity) {
            Intent intent = new Intent(MainActivity.this, ProviderTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_Notification_test_activity) {
            Intent intent = new Intent(MainActivity.this, NotificationTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_reflex_test_activity) {
            Intent intent = new Intent(MainActivity.this, ReflexTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_plugin_manager_test_activity) {// 插件管理测试界面
            Intent intent = new Intent(MainActivity.this, PluginManagerTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_test_binder_activity) {// binder测试
            Intent intent = new Intent(MainActivity.this, BinderTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_lock_screen_activity) {
            Intent intent = new Intent(MainActivity.this, LockScreenActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_download) {
            Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_path) {
            Intent intent = new Intent(MainActivity.this, WaveViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_plugin_fragment) {
            Intent intent = new Intent(MainActivity.this, UsePluginFragmentActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_plugin_view) {
            Intent intent = new Intent(MainActivity.this, UsePluginViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_go_update_plugin) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fileNames.add(FILE_NAME_PLUGIN_1);
//                fileNames.add(FILE_NAME_PLUGIN_2);
                    P_FileUtil.simulateInstallExternalPlugin(fileNames);
                }
            }).start();

        } else if (v.getId() == R.id.btn_go_show_float_window) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            } else {
                Intent intent = new Intent();
                intent.putExtra(P_Constants.INTENT_KEY, "WMA-OK");
                intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.service.FloatingService"));
                startService(intent, true);
            }
        } else if (v.getId() == R.id.btn_go_test_home_page) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_go_test_job_intent_service) {
            Intent intent = new Intent(MainActivity.this, JobIntentServiceActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_go_test_thread_pool) {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_doc_select){
            Intent intent = new Intent(MainActivity.this, DOCSelectActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_plugin_resource){
            Intent intent = new Intent(MainActivity.this, UsePluginResourceActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_db_test){
            Intent intent = new Intent(MainActivity.this, DBTestActivity.class);
            startActivity(intent);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(P_Constants.INTENT_KEY, "WMA-OK");
                intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.service.FloatingService"));
                startService(intent, true);
            }
        }
    }
}
