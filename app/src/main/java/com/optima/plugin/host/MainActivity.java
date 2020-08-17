package com.optima.plugin.host;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_FileUtil;
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
        if(pluginRunning){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileNames.add(FILE_NAME_PLUGIN_1);
                fileNames.add(FILE_NAME_PLUGIN_2);
                P_FileUtil.simulateInstallExternalPlugin(fileNames);
            }
        }).start();
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
        }
    }
}
