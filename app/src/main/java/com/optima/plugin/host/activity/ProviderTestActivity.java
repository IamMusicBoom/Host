package com.optima.plugin.host.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.View;

import androidx.annotation.Nullable;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.optima.plugin.repluginlib.pluginUtils.P_Manager;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.component.ComponentList;

/**
 * create by wma
 * on 2020/8/18 0018
 */
public class ProviderTestActivity extends BaseActivity implements View.OnClickListener {
    Uri uri;
    ContentResolver resolver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_test);
        uri = Uri.parse(P_Constants.PLUGIN1_PROVIDER_URI);
        resolver = getContentResolver();
        ComponentList componentList = RePlugin.fetchComponentList(P_Constants.ALIAS_PLUGIN_1);
        Logger.d(TAG, "onCreate: componentList" + componentList);
        Logger.d(TAG, "onCreate: pid = " + Process.myPid() + " taskId = " + getTaskId());
        Logger.d(TAG, "onCreate: pid = " + android.os.Process.myPid() + " ProcessName = " + P_Manager.getProcessName(android.os.Process.myPid() ));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {// 增
            P_Context.insert(P_Constants.ALIAS_PLUGIN_1, uri, new ContentValues());
//            resolver.insert(uri,new ContentValues());
        } else if (v.getId() == R.id.btn_delete) {// 删
            P_Context.delete(P_Constants.ALIAS_PLUGIN_1, uri, null, null);
//            resolver.delete(uri,null,null);
        } else if (v.getId() == R.id.btn_update) {// 改
            P_Context.update(P_Constants.ALIAS_PLUGIN_1, uri, new ContentValues(), null, null);
//            resolver.update(uri, new ContentValues(), null, null);
        } else if (v.getId() == R.id.btn_query) {// 查
            P_Context.query(P_Constants.ALIAS_PLUGIN_1,uri,null,null,null,null);
//            resolver.query(uri, null, null, null, null);
        }
    }
}
