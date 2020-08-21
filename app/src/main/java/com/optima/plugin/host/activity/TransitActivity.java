package com.optima.plugin.host.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.optima.plugin.repluginlib.base.BaseActivity;

/**
 * create by wma
 * on 2020/8/21 0021
 */
public class TransitActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String alias = intent.getStringExtra(P_Constants.INTENT_ALIAS);
        String className = intent.getStringExtra(P_Constants.INTENT_CLASS_NAME);
        P_Context.startPluginActivity(TransitActivity.this,P_Context.createIntent(alias,className));
        finish();
    }
}
