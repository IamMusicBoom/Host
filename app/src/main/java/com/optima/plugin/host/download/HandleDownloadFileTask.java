package com.optima.plugin.host.download;

import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.host.download.module.MenuInfoModule;
import com.optima.plugin.host.download.module.PluginInfoModule;
import com.optima.plugin.host.utils.AppUtil;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public class HandleDownloadFileTask extends Thread {
    final String TAG = HandleDownloadFileTask.class.getSimpleName();
    List<DownloadModule> downloadModules;
    HandleFinishListener listener;

    public HandleDownloadFileTask(List<DownloadModule> downloadModules, HandleFinishListener listener) {
        this.downloadModules = downloadModules;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        Logger.d(TAG, "run: downloadModules.size() = " + downloadModules.size());
        if (downloadModules == null || downloadModules.size() <= 0) {
            if (listener != null) {
                listener.handleError("未发现下载的文件");
            }
            return;
        }
        for (int i = 0; i < downloadModules.size(); i++) {
            DownloadModule downloadModule = downloadModules.get(i);
            if (P_FileUtil.PLUGIN_FOLDER.equals(downloadModule.getFileType())) {
                Logger.d(TAG, "run: 插件下载成功，准备解压...：" + downloadModule.getFileName());
                String filePath = downloadModule.getFilePath();
                PluginInfo pluginInfo = RePlugin.install(filePath);
                if (listener != null && pluginInfo == null) {
                    listener.handleError("插件加载失败");
                    return;
                } else {
                    RePlugin.fetchComponentList(pluginInfo.getName());
                    Logger.d(TAG, "插件加载成功: " + pluginInfo.getName());
                }
            }
            if (P_FileUtil.HOST_FOLDER.equals(downloadModule.getFileType())) {
                Logger.d(TAG, "run: 宿主apk下载成功，准备安装...");
                String filePath = downloadModule.getFilePath();
                File apk = new File(filePath);
                if (apk.exists()) {
                    AppUtil.installApp(P_Context.getContext(), apk.getPath());
                } else {
                    Logger.e(TAG, "宿主安装失败文件不存在: filePath = " + filePath);
                }
            }
            if (P_FileUtil.ICON_FOLDER.equals(downloadModule.getFileType())) {
                Logger.d(TAG, "开始设置图标本地路径: " + downloadModule.getFilePath());
                // --------------------------------------------------- 更新插件图标本地路径 --start
                ArrayList<PluginInfoModule> pluginConfigList = ConfigSP.getPluginConfigList();
                if(pluginConfigList !=null){
                    for (int j = 0; j < pluginConfigList.size(); j++) {
                        PluginInfoModule pluginInfoModule = pluginConfigList.get(j);
                        if(downloadModule.getFileName().contains(pluginInfoModule.getName())){
                            pluginInfoModule.setIconLocalPath(downloadModule.getFilePath());
                        }
                    }
                    ConfigSP.putPluginConfigList(pluginConfigList);
                }
                // --------------------------------------------------- 更新插件图标本地路径 --end

                // --------------------------------------------------- 更新menu图标本地路径 --start
                ArrayList<MenuInfoModule> menuConfigList = ConfigSP.getMenuConfigList();
                if(menuConfigList != null){
                    for (int j = 0; j < menuConfigList.size(); j++) {
                        MenuInfoModule menuInfoModule = menuConfigList.get(j);
                        if (menuInfoModule.getIcon().equals(downloadModule.getDownloadUrl())) {
                            menuInfoModule.setLocalPath(downloadModule.getFilePath());
                        }
                    }
                    ConfigSP.putMenuConfigList(menuConfigList);
                }
                // --------------------------------------------------- 更新menu图标本地路径 --end
            }
        }
        if (listener != null) {
            listener.handleFinish();
        }
    }


    public interface HandleFinishListener {
        void handleError(String error);

        void handleFinish();
    }
}
