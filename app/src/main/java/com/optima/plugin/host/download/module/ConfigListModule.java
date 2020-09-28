package com.optima.plugin.host.download.module;


import com.optima.plugin.repluginlib.base.BaseModule;

import java.util.List;

/**
 * create by wma
 * on 2020/9/25 0025
 */
public class ConfigListModule extends BaseModule {

    private List<PluginInfoModule> plugins;
    private List<MenuInfoModule> menus;

    public List<PluginInfoModule> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<PluginInfoModule> plugins) {
        this.plugins = plugins;
    }

    public List<MenuInfoModule> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuInfoModule> menus) {
        this.menus = menus;
    }

}
