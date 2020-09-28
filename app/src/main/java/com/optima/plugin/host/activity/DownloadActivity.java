package com.optima.plugin.host.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.optima.plugin.host.R;
import com.optima.plugin.host.download.ConfigSP;
import com.optima.plugin.host.download.MyDBManager;
import com.optima.plugin.host.download.module.ConfigListModule;
import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.host.download.module.MenuInfoModule;
import com.optima.plugin.host.download.module.PluginInfoModule;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;
import com.qihoo360.replugin.model.PluginInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Logger.d(TAG, "onCreate: " + getExternalFilesDir("icon").getAbsolutePath());
        download();
    }

    private void download() {

        RequestParams entity = new RequestParams("http://192.168.0.180:8084/v100/device/get/config/info");
        entity.addBodyParameter("userId","");
        entity.addBodyParameter("deviceType","phone");
        x.http().get(entity, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Logger.d(TAG, "onSuccess: result = " + result.get("data"));
                    JSONObject data = result.getJSONObject("data");
                    ConfigListModule configListModule = new Gson().fromJson(data.toString(), ConfigListModule.class);
                    List<PluginInfoModule> plugins = configListModule.getPlugins();
                    List<MenuInfoModule> menus = configListModule.getMenus();
                    List<DownloadModule> downloadList = new ArrayList<>();
                    DbManager db = new MyDBManager().getDb();
                    for (int i = 0; i < plugins.size(); i++) {
                        PluginInfoModule pluginInfoModule = plugins.get(i);
                        pluginInfoModule.setId(MD5.md5(pluginInfoModule.getDownloadUrl() + pluginInfoModule.getName()));
                        pluginInfoModule.setId2(MD5.md5("wma"+pluginInfoModule.getDownloadUrl() + pluginInfoModule.getName()));
                        ArrayList<DownloadModule> build = DownloadModule.build(pluginInfoModule);
                        downloadList.addAll(build);
                        Logger.d(TAG, "onSuccess: id = " + pluginInfoModule.getId());
                        db.replace(pluginInfoModule);
                    }
                    for (int i = 0; i < menus.size(); i++) {
                        MenuInfoModule menuInfoModule = menus.get(i);
                        DownloadModule build = DownloadModule.build(menuInfoModule);
                        downloadList.add(build);
                    }
                    String downloadListStr = new Gson().toJson(downloadList);
                    ConfigSP.putNeedDownloadList(downloadListStr);// 储存需要下载的列表
                    com.optima.plugin.host.download.ui.DownloadActivity.downloadPlugin(P_Context.getContext());
                    ConfigSP.putPluginConfigList(plugins);// 储存到本地
                    ConfigSP.putMenuConfigList(menus);// 储存到本地
//                    db.save(plugins);
//                    db.save(menus);
                    Logger.d(TAG, "储存成功: ");
                    List<PluginInfoModule> all = db.findAll(PluginInfoModule.class);
                    for (int i = 0; i < all.size(); i++) {
                        PluginInfoModule pluginInfo = all.get(i);
                        Logger.d(TAG, "onSuccess: " + pluginInfo.getName());
                    }
                    Logger.d(TAG, "onSuccess: plugins = " + plugins.size());
                    Logger.d(TAG, "onSuccess: menus = " + menus.size());
                } catch (JSONException | DbException e) {
                    e.printStackTrace();
                    Logger.d(TAG, "出错拉: " + e);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.d(TAG, "onError: ex = " + ex);
            }   

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished: ");
            }
        });
    }


}
