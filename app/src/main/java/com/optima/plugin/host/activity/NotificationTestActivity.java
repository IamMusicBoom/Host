package com.optima.plugin.host.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.optima.plugin.host.MainActivity;
import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.PluginUtils.P_Constants;
import com.optima.plugin.repluginlib.PluginUtils.P_Context;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.utils.NotificationUtils;

/**
 * create by wma
 * on 2020/8/19 0019
 */
public class NotificationTestActivity extends BaseActivity implements View.OnClickListener {
    NotificationUtils utils;
    NotificationCompat.Builder downloadBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        utils = new NotificationUtils();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_show_default_notification) {// 展示默认通知栏
            Intent intent = TransitActivity.createStartActivityIntent(NotificationTestActivity.this, P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.Plugin1MainActivity");
            PendingIntent pendingIntent = TransitActivity.createPendingIntent(NotificationTestActivity.this, intent);
            NotificationCompat.Builder builder = utils.createDefaultBuilder();
            builder.setContentText("通知").setContentTitle("默认通知").setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_large)).setContentIntent(pendingIntent);
            utils.showNotification(666, builder.build());
        } else if (v.getId() == R.id.btn_show_importance_notification) {// 展示重要通知栏
            Intent intent = TransitActivity.createStartServiceIntent(NotificationTestActivity.this, P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.service.Plugin1ServiceTest");
            PendingIntent pendingIntent = TransitActivity.createPendingIntent(NotificationTestActivity.this, intent);
            NotificationCompat.Builder builder = utils.createImportanceBuilder();
            builder.setContentText("通知").setContentTitle("重要通知").setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_large)).setContentIntent(pendingIntent);
            utils.showNotification(777, builder.build());
        } else if (v.getId() == R.id.btn_show_customize_notification) {
//            PendingIntent pendingIntent = TransitActivity.createSendBroadcastPendingIntent(NotificationTestActivity.this, P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.broadcast.BroadcastTest");
            NotificationCompat.Builder builder = utils.createDefaultBuilder();
            RemoteViews smallView = new RemoteViews(getPackageName(), R.layout.notification_small);
            RemoteViews largeView = new RemoteViews(getPackageName(), R.layout.notification_large);
            builder.setCustomContentView(smallView);
            builder.setCustomBigContentView(largeView);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent sure = PendingIntent.getActivity(this, 0, intent, 0);
            largeView.setOnClickPendingIntent(R.id.btn_sure, sure);
            utils.showNotification(888, builder.build());
        } else if (v.getId() == R.id.btn_show_download_notification) {// 下载进度条通知
            downloadBuilder = utils.createDownloadBuilder(max, process);
            downloadBuilder.setContentTitle("正在下载");
            downloadBuilder.setContentText((process * 100 / max) + "%");
            utils.showNotification(999, downloadBuilder.build());
            goOpenDownloadThread();
        } else if (v.getId() == R.id.btn_go_plugin_notification_activity) {// 去插件的通栏测试Activity
            Intent intent = P_Context.createIntent(P_Constants.ALIAS_PLUGIN_1, "com.optima.plugin.plugin1.activity.NotificationTestActivity");
            startActivity(intent, true);
        }
    }

    /**
     * 开启下载进度条
     */
    int process = 0;
    int max = 100;

    private void goOpenDownloadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (process < 100) {
                    process++;
                    utils.updateDownloadNotification(999, max, process, downloadBuilder);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}
