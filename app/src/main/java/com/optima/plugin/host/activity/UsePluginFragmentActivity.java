package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.qihoo360.replugin.RePlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 目前宿主无法直接使用插件Fragment，360插件化里面使用的是support的Fragment无法转换成androidx中Fragment
 */
public class UsePluginFragmentActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = UsePluginFragmentActivity.class.getSimpleName();
    Dialog dialog;//使用插件的Classloader获取指定Fragment实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = RePlugin.fetchViewByLayoutName(P_Constants.ALIAS_PLUGIN_1, "fragment_test", null);
        setContentView(view);
        RePlugin.registerHookingClass("com.optima.plugin.dialog.TestDialog", RePlugin.createComponentName(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.dialog.TestDialog"), null);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_get_plugin_fragment){// 获取插件fragment对象
            //代码使用插件Fragment
            ClassLoader d1ClassLoader = RePlugin.fetchClassLoader(P_Constants.ALIAS_PLUGIN_1);//获取插件的ClassLoader
            try {
                Class<?> aClass = d1ClassLoader.loadClass("com.optima.plugin.dialog.TestDialog");
                Constructor<?> constructor = aClass.getConstructor(Context.class);
                Object o = constructor.newInstance(this);
                dialog = (Dialog) o;
                Logger.e(TAG, "onClick: " + dialog);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
                Logger.e(TAG, "onClick: " + e);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            Logger.d(TAG, "onClick: ");
        }else if(v.getId() == R.id.btn_use_plugin_fragment){// 使用插件fragment对象
            dialog.show();
        }
    }
}
