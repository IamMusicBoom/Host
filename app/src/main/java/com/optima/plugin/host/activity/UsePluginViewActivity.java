package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;

import com.optima.plugin.host.IViewAidlInterface;
import com.optima.plugin.host.binder.HostBinderFetcher;
import com.optima.plugin.host.utils.H_Binder;
import com.optima.plugin.repluginlib.pluginUtils.P_Binder;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Resource;
import com.qihoo360.replugin.RePlugin;


/**
 * 使用插件中的View,主要是插件与宿主建的通信，用注册binder的方式比较方便，可以使用自定义View,如果需要使用自定义View的对象，那么自定义控件应当放在宿主中
 */
public class UsePluginViewActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    IViewAidlInterface iView;


    private void setListener(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            childAt.setOnClickListener(this);
            childAt.setOnLongClickListener(this);
            if (childAt instanceof ViewGroup) {
                setListener(((ViewGroup) childAt));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RePlugin.registerHookingClass("com.optima.plugin.host.view.download.WaveView",RePlugin.createComponentName("main","com.optima.plugin.host.view.download.WaveView"),null);
        ViewGroup group = (ViewGroup) P_Resource.fetchLayoutByName(P_Constants.ALIAS_PLUGIN_1, "provide_layout", null);
        setContentView(group);
        IBinder viewBinder = P_Binder.getPluginBinder(P_Constants.ALIAS_PLUGIN_1, "view_holder");
        iView = IViewAidlInterface.Stub.asInterface(viewBinder);
        setListener(group);
        H_Binder.registerHostBinder(new HostBinderFetcher(group));
    }

    @Override
    public void onClick(View v) {
        try {
            iView.onClick(v.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        try {
            iView.onLongClick(v.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

}

