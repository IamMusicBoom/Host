package com.optima.plugin.host.download.module;


import com.optima.plugin.repluginlib.base.BaseModule;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * create by wma
 * on 2020/9/23 0023
 */
@Table(name = "PluginInfo")
public class PluginInfoModule extends BaseModule {

    /**
     *
     */
    @Column(name = "id", isId = true, autoGen = true)
    private String id;


    /**
     *
     */
    @Column(name = "id2")
    private String id2;

    /**
     * 插件别名定义
     */
    @Column(name = "name")
    private String name;

    /**
     * 插件版本号
     */
    @Column(name = "versionCode")
    private int versionCode;

    /**
     * 插件版本名
     */
    @Column(name = "versionName")
    private String versionName;

    /**
     * 插件的APP图标
     */
    @Column(name = "iconUrl")
    private String iconUrl;

    /**
     * 插件下载地址
     */
    @Column(name = "downloadUrl")
    private String downloadUrl;

    /**
     * 服务地址类型
     */
    @Column(name = "serverType")
    private String serverType;

    /**
     * 服务器地址
     */
    @Column(name = "serverUrl")
    private String serverUrl;

    /**
     * 服务器ip
     */
    @Column(name = "serverIp")
    private String serverIp;

    /**
     * 服务器port
     */
    @Column(name = "serverPort")
    private String serverPort;

    /**
     * 服务器名称
     */
    @Column(name = "serverName")
    private String serverName;

    /**
     * 闹钟唤醒
     */
    @Column(name = "alarmEnable")
    private boolean alarmEnable;

    /**
     * 闹钟唤醒周期
     */
    @Column(name = "alarmCycle")
    private int alarmCycle;

    /**
     * 闹钟唤醒触发广播类
     */
    @Column(name = "alarmClass")
    private String alarmClass;

    /**
     * 是否需要宿主直接启动插件
     */
    @Column(name = "launcherAuto")
    private boolean launcherAuto;

    /**
     * 启动的组件类型
     */
    @Column(name = "launcherType")
    private String launcherType;

    /**
     * 启动的组件class
     */
    @Column(name = "launcherClass")
    private String launcherClass;

    /**
     * 本地路径 宿主登录成功后，会对插件信息和menu信息进行下载，下载后的路径将储存在iconLocalPath中
     * 插件apk路径不需要保存，下载完成后会进行安装删除的操作。
     */
    @Column(name = "iconLocalPath")
    private String iconLocalPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isAlarmEnable() {
        return alarmEnable;
    }

    public void setAlarmEnable(boolean alarmEnable) {
        this.alarmEnable = alarmEnable;
    }

    public int getAlarmCycle() {
        return alarmCycle;
    }

    public void setAlarmCycle(int alarmCycle) {
        this.alarmCycle = alarmCycle;
    }

    public String getAlarmClass() {
        return alarmClass;
    }

    public void setAlarmClass(String alarmClass) {
        this.alarmClass = alarmClass;
    }

    public boolean isLauncherAuto() {
        return launcherAuto;
    }

    public void setLauncherAuto(boolean launcherAuto) {
        this.launcherAuto = launcherAuto;
    }

    public String getLauncherType() {
        return launcherType;
    }

    public void setLauncherType(String launcherType) {
        this.launcherType = launcherType;
    }

    public String getLauncherClass() {
        return launcherClass;
    }

    public void setLauncherClass(String launcherClass) {
        this.launcherClass = launcherClass;
    }

    public String getIconLocalPath() {
        return iconLocalPath;
    }

    public void setIconLocalPath(String iconLocalPath) {
        this.iconLocalPath = iconLocalPath;
    }
}
