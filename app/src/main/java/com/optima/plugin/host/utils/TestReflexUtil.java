package com.optima.plugin.host.utils;

import com.optima.plugin.repluginlib.Logger;

/**
 * create by wma
 * on 2020/8/20 0020
 */
public class TestReflexUtil {
    String TAG = TestReflexUtil.class.getSimpleName() + "-Host";

    public void noPnoR() {
        Logger.d(TAG, "noPnoR: ");
    }

    public String noPhR() {
        Logger.d(TAG, "noPhR: ");
        return "noPhR";
    }

    public void hPnoR(String s) {
        Logger.d(TAG, "hPnoR: s = " + s);
    }

    public String hPhR(String s) {
        Logger.d(TAG, "hPhR: s = " + s);
        String s2 = s + " 做个拼接吧";
        return s2;
    }

    public void mPs(String name, int age) {
        Logger.d(TAG, "mPs: name = " + name + " age = " + age);
    }

}
