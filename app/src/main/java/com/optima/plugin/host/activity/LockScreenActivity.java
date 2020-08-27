package com.optima.plugin.host.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.base.BaseFragment;
import com.qihoo360.replugin.RePlugin;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class LockScreenActivity extends BaseActivity {
    final String TAG = LockScreenActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ClassLoader classLoader = RePlugin.fetchClassLoader(P_Constants.ALIAS_PLUGIN_1);
            BaseFragment fragment = classLoader.loadClass("com.optima.plugin.plugin1.TestFragment").asSubclass(BaseFragment.class).newInstance();
            transaction.add(R.id.container,fragment,"lockScreen");
            transaction.commit();
            Logger.d(TAG, "onCreate: fragment = " + fragment);
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            Logger.d(TAG, "onCreate: e = " + e.toString());
            e.printStackTrace();
        }
    }
}
