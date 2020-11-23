package com.optima.plugin.host.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.optima.plugin.host.R;
import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.host.download.ui.DownloadActivity;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.databases.ConfigDB;
import com.optima.plugin.repluginlib.databases.ConfigListModule;
import com.optima.plugin.repluginlib.module.MenuInfoModule;
import com.optima.plugin.repluginlib.module.PluginInfoModule;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        RequestParams entity = new RequestParams();
        entity.setUri("http://192.168.0.242:8084/v100/device/get/config/info");
        entity.addBodyParameter("userId", "2f759e2bf8054e40a43a44b8c129b444");
        entity.addBodyParameter("deviceType", "phone");
        x.http().get(entity, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                ConfigListModule configListModule = null;
                try {
                    configListModule = new Gson().fromJson(result.getJSONObject("data").toString(), ConfigListModule.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<PluginInfoModule> plugins = configListModule.getPlugins();
                List<MenuInfoModule> menus = configListModule.getMenus();
                Logger.d(TAG, "清单获取成功: plugins 个数 = " + (plugins == null ? "null" : plugins.size()) + " menus 个数 " + (menus == null ? "null" : menus.size()));
                List<PluginInfoModule> localPlugins = ConfigDB.getPluginConfigList();
                if (localPlugins == null || localPlugins.size() <= 0) {// 本地配置清单为空，说明第一次下载
                    Logger.d(TAG, "本地配置清单为空，下载所有插件和menu");
                    List<DownloadModule> downloadList = new ArrayList<>();
                    if (plugins != null) {
                        for (int i = 0; i < plugins.size(); i++) {
                            PluginInfoModule pluginInfoModule = plugins.get(i);
                            pluginInfoModule.setId(MD5.md5(pluginInfoModule.getDownloadUrl() + pluginInfoModule.getName()));
//                            ArrayList<DownloadModule> build = DownloadModule.build(pluginInfoModule);
//                            downloadList.addAll(build);
                        }
                    } else {
                        Logger.d(TAG, "plugins:  = null");
                    }
                    if (menus != null) {
                        for (int i = 0; i < menus.size(); i++) {
                            MenuInfoModule menuInfoModule = menus.get(i);
//                            DownloadModule build = DownloadModule.build(menuInfoModule);
//                            menuInfoModule.setId(MD5.md5(menuInfoModule.getIcon() + menuInfoModule.getName()));
//                            downloadList.add(build);
                        }
                    } else {
                        Logger.d(TAG, "menus:  = null");
                    }
//                    ConfigDB.putNeedDownloadList(downloadList);// 储存需要下载的列表
//                    DownloadActivity.downloadPlugin(P_Context.getContext());
                    ConfigDB.putPluginConfigList(plugins == null ? new ArrayList<PluginInfoModule>() : plugins);// 储存到本地
                    ConfigDB.putMenuConfigList(menus == null ? new ArrayList<MenuInfoModule>() : menus);// 储存到本地
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
