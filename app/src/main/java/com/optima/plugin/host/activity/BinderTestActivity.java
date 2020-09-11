package com.optima.plugin.host.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import androidx.annotation.Nullable;

import com.optima.plugin.host.IHostImpl;
import com.optima.plugin.host.R;
import com.optima.plugin.host.binder.HostBinder;
import com.optima.plugin.host.binder.HostBinderFetcher;
import com.optima.plugin.host.module.User;
import com.optima.plugin.host.utils.H_Binder;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Binder;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.optima.plugin.repluginlib.base.BaseActivity;

import java.util.List;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class BinderTestActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_test);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.go_plugin_binder_activity){
            Intent intent = P_Context.createIntent(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.activity.BinderTestActivity");
            startActivity(intent,true);
        }else if(v.getId() == R.id.register_host_binder){
            H_Binder.registerHostBinder(new HostBinderFetcher());
        }else if(v.getId() == R.id.register_global_binder){
            P_Binder.registerGlobalBinder("globalBinder",new HostBinder());
        }else if(v.getId() == R.id.get_plugin_binder){
            IBinder plugin1Binder = com.optima.plugin.repluginlib.pluginUtils.P_Binder.getPluginBinder(P_Constants.ALIAS_PLUGIN_1, "plugin1Binder");
            IHostImpl iHost = IHostImpl.Stub.asInterface(plugin1Binder);
            try {
                iHost.say("已经获取到HostBinder");
                iHost.addUser(new User("赵云", 32));
                iHost.setUser(new User("赵云", 23), 0);
                List<User> users = iHost.getUsers();
                Logger.d(TAG, "onClick: users = " + users.size());
                User user = iHost.getUser(0);
                Logger.d(TAG, "onClick: name = " + user.getName() + " age = " + user.getAge());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
