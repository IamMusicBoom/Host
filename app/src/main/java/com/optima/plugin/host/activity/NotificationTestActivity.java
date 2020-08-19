package com.optima.plugin.host.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.PluginUtils.NotificationUtils;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.optima.plugin.repluginlib.base.BaseActivity;

/**
 * create by wma
 * on 2020/8/19 0019
 */
public class NotificationTestActivity extends BaseActivity implements View.OnClickListener {
    NotificationUtils utils;
    String title = "这个是标题";
    String content = "这个是内容";
    Bitmap largeIcon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        utils = new NotificationUtils(NotificationUtils.CHANNEL_ID, NotificationUtils.NAME, NotificationManager.IMPORTANCE_DEFAULT);
        largeIcon = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_large);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_show_notification) {// 展示通知栏
            utils.show(title, content, largeIcon, R.mipmap.icon_small);
        } else if (v.getId() == R.id.btn_go_plugin_notification_activity) {// 去插件的通栏测试Activity

        }
    }

}
