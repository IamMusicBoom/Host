package com.optima.plugin.host.download.module;


import com.optima.plugin.repluginlib.base.BaseModule;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * create by wma
 * on 2020/9/25 0025
 */
@Table(name = "PluginInfo")
public class MenuInfoModule extends BaseModule {

    /**
     *
     */
    @Column(name = "id", isId = true, autoGen = true)
    private String id;

    /**
     * 菜单的图标下载地址
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 菜单的名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单对应的class
     */
    @Column(name = "menuClass")
    private String menuClass;

    /**
     * 归类名称
     */
    @Column(name = "typeName")
    private String typeName;

    /**
     * 归类cdoe
     */
    @Column(name = "typeCode")
    private String typeCode;

    /**
     * 菜单的描述
     */
    private String description;

    /**
     * 插件的别名
     */
    @Column(name = "pluginName")
    private String pluginName;

    /**
     * 本地路径 宿主登录成功后，会对插件信息和menu信息进行下载，下载后的路径将储存在localPath中
     */
    @Column(name = "localPath")
    private String localPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
