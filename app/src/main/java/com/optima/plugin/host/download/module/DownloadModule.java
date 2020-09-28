package com.optima.plugin.host.download.module;


import android.text.TextUtils;


import com.optima.plugin.host.download.DownloadTask;
import com.optima.plugin.repluginlib.pluginUtils.P_FileUtil;

import java.util.ArrayList;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public class DownloadModule {

    private String fileName;
    private String filePath;
    private String downloadUrl;
    private String fileType;

    private DownloadModule(String fileName, String filePath, String downloadUrl, String fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.downloadUrl = downloadUrl;
    }

    private DownloadModule(String fileName, String filePath, String fileType) {
        this(fileName, filePath, "", fileType);
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    /**
     * 下载完毕后创建的module
     *
     * @param task
     * @return
     */
    public static DownloadModule build(DownloadTask task) {
        if (task == null) {
            return null;
        }
        DownloadModule module = new DownloadModule(task.getName(), task.getFilePath(), task.getDownloadUrl(), task.getCurType());
        return module;
    }


    /**
     * 构建一个下载宿主的module
     *
     * @param hostModule
     * @return
     */
    public static DownloadModule build(HostModule hostModule) {
        if (hostModule == null) {
            return null;
        }
        DownloadModule module = new DownloadModule(hostModule.getName(), hostModule.getDownloadUrl(), P_FileUtil.HOST_FOLDER);
        return module;
    }

    /**
     * 构建一个下载插件信息的module,该module包括插件apk,插件图标
     *
     * @param pluginInfoModule
     * @return
     */
    public static ArrayList<DownloadModule> build(PluginInfoModule pluginInfoModule) {
        if (pluginInfoModule == null) {
            return null;
        }
        ArrayList<DownloadModule> downloadModules = new ArrayList<>();
        String downloadUrl = pluginInfoModule.getDownloadUrl();
        if (!TextUtils.isEmpty(downloadUrl)) {
            String[] split = downloadUrl.split("\\.");
            StringBuffer sb = new StringBuffer();
            sb.append(pluginInfoModule.getName());
            sb.append(".");
            sb.append(split[split.length - 1]);
            String apkName = sb.toString();
            DownloadModule module = new DownloadModule(apkName, pluginInfoModule.getDownloadUrl(), P_FileUtil.PLUGIN_FOLDER);
            downloadModules.add(module);
        }
        String iconUrl = pluginInfoModule.getIconUrl();
        if (!TextUtils.isEmpty(iconUrl)) {
            String[] split = iconUrl.split("\\.");
            StringBuffer sb = new StringBuffer();
            sb.append(pluginInfoModule.getName());
            sb.append(".");
            sb.append(split[split.length - 1]);
            String apkName = sb.toString();
            DownloadModule module = new DownloadModule(apkName, pluginInfoModule.getIconUrl(), P_FileUtil.ICON_FOLDER);
            downloadModules.add(module);
        }
        return downloadModules;
    }

    /**
     * 构建一个下载menu的module
     *
     * @param menuInfoModule
     * @return
     */
    public static DownloadModule build(MenuInfoModule menuInfoModule) {
        if (menuInfoModule == null) {
            return null;
        }
        if (!TextUtils.isEmpty(menuInfoModule.getIcon())) {
            String[] split = menuInfoModule.getIcon().split("\\.");
            StringBuffer sb = new StringBuffer();
            sb.append(menuInfoModule.getName());
            sb.append(".");
            sb.append(split[split.length - 1]);
            String iconName = sb.toString();
            DownloadModule module = new DownloadModule(iconName, menuInfoModule.getIcon(), P_FileUtil.ICON_FOLDER);
            return module;
        }
        return null;
    }
}
