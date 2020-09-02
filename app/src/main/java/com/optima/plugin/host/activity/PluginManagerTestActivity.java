package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.ArrayList;
import java.util.List;

public class PluginManagerTestActivity extends AppCompatActivity implements View.OnClickListener{
    final String FILE_NAME_PLUGIN_2 = "plugin2.apk";
    String TAG = PluginManagerTestActivity.class.getSimpleName();
    private final String PLUGIN_NAME = P_Constants.ALIAS_PLUGIN_2;
    PluginInfo mInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_manager_test);
        getInfoTest();
    }

    private void getInfoTest() {
        Logger.d(TAG, "getInfoTest: isPluginUsed = " + RePlugin.isPluginUsed(PLUGIN_NAME));
        Logger.d(TAG, "getInfoTest: isPluginRunning = " + RePlugin.isPluginRunning(PLUGIN_NAME));
        Logger.d(TAG, "getInfoTest: isPluginInstalled = " + RePlugin.isPluginInstalled(PLUGIN_NAME));
        Logger.d(TAG, "getInfoTest: isPluginDexExtracted = " + RePlugin.isPluginDexExtracted(PLUGIN_NAME));
        Logger.d(TAG, "getInfoTest: getPluginVersion = " + RePlugin.getPluginVersion(PLUGIN_NAME));
        PluginInfo pluginInfo = RePlugin.getPluginInfo(PLUGIN_NAME);
        Logger.d(TAG, "getInfoTest: pluginInfo = " + pluginInfo);
        if(pluginInfo != null){
            Logger.d(TAG, "getInfoTest: getPackageName = " + pluginInfo.getPackageName());
            Logger.d(TAG, "getInfoTest: getPath = " + pluginInfo.getPath());
            Logger.d(TAG, "getInfoTest: getName = " + pluginInfo.getName());
            Logger.d(TAG, "getInfoTest: getAlias = " + pluginInfo.getAlias());
            Logger.d(TAG, "getInfoTest: getApkDir = " + pluginInfo.getApkDir());
            Logger.d(TAG, "getInfoTest: getDexFile = " + pluginInfo.getDexFile());
            Logger.d(TAG, "getInfoTest: getDexParentDir = " + pluginInfo.getDexParentDir());
            Logger.d(TAG, "getInfoTest: getExtraDexDir = " + pluginInfo.getExtraDexDir());
            Logger.d(TAG, "getInfoTest: getExtraOdexDir = " + pluginInfo.getExtraOdexDir());
            Logger.d(TAG, "getInfoTest: getVersionValue = " + pluginInfo.getVersionValue());
            PluginInfo parentInfo = pluginInfo.getParentInfo();
            Logger.d(TAG, "getInfoTest: parentInfo = " + parentInfo);
            if(parentInfo != null){
                Logger.d(TAG, "getInfoTest: parentInfo.getPackageName() = " + parentInfo.getPackageName());
            }

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_install_plugin){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> fileNames = new ArrayList<>();
                    fileNames.add(FILE_NAME_PLUGIN_2);
                    P_FileUtil.simulateInstallExternalPlugin(fileNames);
                    getInfoTest();
                }
            }).start();
        }else if(v.getId() == R.id.btn_preload_plugin){
            RePlugin.preload(PLUGIN_NAME);
            getInfoTest();
        }else if(v.getId() == R.id.btn_fetch_list){
            RePlugin.fetchComponentList(PLUGIN_NAME);
            getInfoTest();
        }
    }
}
