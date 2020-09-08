package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.qihoo360.replugin.RePlugin;

/**
 * 目前宿主无法直接使用插件Fragment，360插件化里面使用的是support的Fragment无法转换成androidx中Fragment
 */
public class UsePluginFragmentActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = UsePluginFragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_plugin_fragment);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get_plugin_fragment) {// 获取插件fragment对象
            //代码使用插件Fragment
            try {
                ClassLoader classLoader = RePlugin.fetchClassLoader(P_Constants.ALIAS_PLUGIN_1);
                Fragment fragment = classLoader.loadClass("com.optima.plugin.plugin1.TestFragment").asSubclass(Fragment.class).newInstance();//使用插件的Classloader获取指定Fragment实例
                getFragmentManager().beginTransaction().replace(R.id.container2, fragment,"TestFragment").commit();
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
                Logger.e(TAG, "onClick: " + e);
            }
        } else if (v.getId() == R.id.btn_use_plugin_fragment) {// 使用插件fragment对象

        }
    }
}
