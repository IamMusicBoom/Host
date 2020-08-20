package com.optima.plugin.host.activity;


import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.optima.plugin.repluginlib.PluginUtils.P_Reflex;
import com.optima.plugin.repluginlib.base.BaseActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflexTestActivity extends BaseActivity implements View.OnClickListener {
    P_Reflex p_reflex;
    Object obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_test);
        p_reflex = new P_Reflex(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.utils.TestReflexUtil");
        Constructor<?> constructor = null;
        try {
            constructor = p_reflex.getConstructor(String.class, int.class);
            obj = p_reflex.createObject(constructor, "张飞", 50);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Logger.e(TAG, "onClick: e = " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {

            if (v.getId() == R.id.excuse_no_parameter_no_result) {// 调用无参数，无返回值的方法
                Method noPnoR = p_reflex.getMethod("noPnoR", null);
                p_reflex.excuse(obj, noPnoR, null);
            } else if (v.getId() == R.id.excuse_no_parameter_has_result) {// 调用无参数，有返回值的方法
                Method noPhR = p_reflex.getMethod("noPhR", null);
                Object excuse = p_reflex.excuse(obj, noPhR, null);
                Logger.d(TAG, "onClick: " + ((excuse instanceof String) ? (String) excuse : "返回值有问题"));
            } else if (v.getId() == R.id.excuse_parameter_no_result) {// 调用有参数，无返回值的方法
                Method hPnoR = p_reflex.getMethod("hPnoR", String.class);
                p_reflex.excuse(obj, hPnoR, "我传的参数");
            } else if (v.getId() == R.id.excuse_parameter_result) {// 调用有参数，有返回值的方法
                Method hPhR = p_reflex.getMethod("hPhR", String.class);
                Object excuse = p_reflex.excuse(obj, hPhR, "我传的参数");
                Logger.d(TAG, "onClick: " + ((excuse instanceof String) ? (String) excuse : "返回值有问题"));
            } else if (v.getId() == R.id.excuse_parameters) {// 调用多参数方法
                Method mPs = p_reflex.getMethod("mPs", String.class, int.class);
                p_reflex.excuse(obj, mPs, "赵云", 20);
            } else if (v.getId() == R.id.get_field_value) {// 获取属性值，并且修改属性值
                String nameValue = (String) p_reflex.getFieldValue(obj, "name");
                int ageValue = (int) p_reflex.getFieldValue(obj, "age");
                Logger.d(TAG, "onClick: " + "name = " + nameValue + " ageValue = " + ageValue);
                p_reflex.setFieldValue(obj, "name", "关二哥");
                p_reflex.setFieldValue(obj, "age", 52);
                String nameValue2 = (String) p_reflex.getFieldValue(obj, "name");
                int ageValue2 = (int) p_reflex.getFieldValue(obj, "age");
                Logger.d(TAG, "onClick: " + "name = " + nameValue2 + " ageValue = " + ageValue2);
            } else if (v.getId() == R.id.excuse_static_method) {// 执行静态方法
                Method method = p_reflex.getMethod("jump", String.class);
                p_reflex.excuseStatic(method, "我执行一个静态方法");
            } else if (v.getId() == R.id.get_static_field) {// 获取一个静态属性值，并且修改它
                Object tag = p_reflex.getStaticFieldValue("TAG");
                Logger.d(TAG, "onClick: tag = " + ((tag instanceof String) ? (String) tag : "返回值有问题"));
                p_reflex.setStaticFieldValue("TAG","我已经设置了");
                Object tag2 = p_reflex.getStaticFieldValue("TAG");
                Logger.d(TAG, "onClick: tag = " + ((tag2 instanceof String) ? (String) tag2 : "返回值有问题"));
            }
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            Logger.e(TAG, "onClick: e = " + e);
            e.printStackTrace();
        }
    }
}
