package com.optima.plugin.host.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.optima.plugin.repluginlib.base.BaseActivity;

/**
 * create by wma
 * on 2020/8/18 0018
 */
public class ServiceTestActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
    }

    MyServiceConnection connection = new MyServiceConnection();

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(P_Constants.INTENT_KEY, "WMA-OK");
        intent.setComponent(new ComponentName(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.service.Plugin1ServiceTest"));
        if (v.getId() == R.id.btn_start_service) {// 开启服务
            startService(intent, true);
        } else if (v.getId() == R.id.btn_stop_service) {// 停止服务
            stopService(intent, true);
        } else if (v.getId() == R.id.btn_bind_service) {// 绑定服务
            bindService(intent, connection, true);
        } else if (v.getId() == R.id.btn_unbind_service) {// 解绑服务
            unbindService(connection, true);
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(TAG, "onServiceDisconnected: ");
        }
    }
}
