package com.optima.plugin.host.download;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.download.module.MenuInfoModule;
import com.optima.plugin.host.download.module.PluginInfoModule;
import com.optima.plugin.repluginlib.pluginUtils.P_SPManager;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/9/25 0025
 */
public class ConfigSP {

    // -------------------------------------------  插件配置清单  start
    public static final String KEY_PLUGIN_CONFIG_LIST = "key_plugin_config_list";

    public static ArrayList<PluginInfoModule> getPluginConfigList() {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        String string = sp.getString(KEY_PLUGIN_CONFIG_LIST, "");
        ArrayList<PluginInfoModule> plugins = new Gson().fromJson(string, new TypeToken<List<PluginInfoModule>>() {
        }.getType());
        return plugins;
    }

    public static void putPluginConfigList(List<PluginInfoModule> plugins) {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        String str = new Gson().toJson(plugins);
        sp.edit().putString(KEY_PLUGIN_CONFIG_LIST, str).apply();
    }
    // -------------------------------------------  插件配置清单  end


    // -------------------------------------------  menu清单  start
    public static final String KEY_MENU_CONFIG_LIST = "key_menu_config_list";

    public static ArrayList<MenuInfoModule> getMenuConfigList() {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        String string = sp.getString(KEY_MENU_CONFIG_LIST, "");
        ArrayList<MenuInfoModule> menus = new Gson().fromJson(string, new TypeToken<List<MenuInfoModule>>() {
        }.getType());
        return menus;
    }

    public static void putMenuConfigList(List<MenuInfoModule> menus) {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        String str = new Gson().toJson(menus);
        sp.edit().putString(KEY_MENU_CONFIG_LIST, str).apply();
    }
    // -------------------------------------------  menu清单  end


    // -------------------------------------------  需要下载的  start
    public static final String KEY_NEED_DOWNLOAD_LIST = "key_need_download_list";

    public static String getNeedDownloadList() {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        String string = sp.getString(KEY_NEED_DOWNLOAD_LIST, "");
        return string;
    }

    public static void putNeedDownloadList(String str) {
        SharedPreferences sp = P_SPManager.getConfigPreferences();
        sp.edit().putString(KEY_NEED_DOWNLOAD_LIST, str).apply();
    }
    // -------------------------------------------  需要下载的 end

}
