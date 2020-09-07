package com.optima.plugin.host.utils;

import com.optima.plugin.host.binder.HostBinderFetcher;
import com.qihoo360.replugin.IHostBinderFetcher;
import com.qihoo360.replugin.RePlugin;

/**
 * create by wma
 * on 2020/8/28 0028
 */
public class H_Binder {

    /**
     * 注册一个宿主的binder
     *
     * @param hostBinderFetcher
     */
    public static void registerHostBinder(HostBinderFetcher hostBinderFetcher) {
        RePlugin.registerHostBinder(hostBinderFetcher);
    }
}
