package com.optima.plugin.host.download.ui;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;


/**
 * create by wma
 * on 2020/8/19 0019
 */
public class NotificationUtils {

    private NotificationManager mManager;
    private String mChannelImportanceId = "9998";
    private String mChannelDownloadId = "9997";
    private String mChannelImportanceName = "重要通知";
    private String mChannelDefaultId = "9999";
    private String mChannelDefaultName = "一般通知";
    private String mChannelGroupId = P_Constants.HOST_PACKAGE_NAME;
    private String mDownloadChannelGroupId = P_Constants.HOST_PACKAGE_NAME + ".download";
    private Context mContext;
    private String contentText;
    private String contentTitle;
    private int smallIcon = R.mipmap.icon_small;
    private Bitmap largeIcon;

    public NotificationUtils() {
        this.mContext = P_Context.getContext();
        this.mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    /**
     * 创建一个重要的频道
     *
     * @param channelId
     * @param channelName
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createImportanceChannel(String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        mManager.createNotificationChannel(channel);
        channel.setGroup(mChannelGroupId);// 给 channel 分配组
        channel.enableLights(true);// 是否可以亮呼吸灯
        channel.setLightColor(Color.RED);// 设置呼吸灯颜色
        channel.enableVibration(true);// 是否可以震动
        channel.setVibrationPattern(new long[]{1000, 500, 1000});// 震动1秒。停止0.5秒。再震动2秒
        channel.setBypassDnd(true);// 是否可以打断用户
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);// 设置是否锁屏后可见
        channel.setShowBadge(true);// Launcher图标上是否可显示消息
//        channel.setSound();// 设置提示声音
        return channel;
    }

    /**
     * 创建一个重要NotificationBuilder
     *
     * @return
     */
    public NotificationCompat.Builder createImportanceBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createImportanceChannel(mChannelImportanceId, mChannelImportanceName);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mChannelImportanceId);
        builder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setVibrate(new long[]{1000, 500, 2000})
                .setAutoCancel(false)
                .setGroup(mChannelGroupId)
                .setLights(Color.RED, 1000, 1000)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis());
        return builder;
    }


    /**
     * 创建一个默认频道
     *
     * @param channelId
     * @param channelName
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createDefaultChannel(String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        mManager.createNotificationChannel(channel);
        channel.setGroup(mChannelGroupId);// 给 channel 分配组
        channel.enableLights(true);// 是否可以亮呼吸灯
        channel.setLightColor(Color.GREEN);// 设置呼吸灯颜色
        channel.enableVibration(true);// 是否可以震动
        channel.setVibrationPattern(new long[]{1000, 500, 1000});// 震动1秒。停止0.5秒。再震动2秒
        channel.setBypassDnd(false);// 是否可以打断用户
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);// 设置是否锁屏后可见
        channel.setShowBadge(false);// Launcher图标上是否可显示消息
//        channel.setSound();// 设置提示声音
        return channel;
    }


    /**
     * 创建一个默认配置的NotificationBuilder
     *
     * @return
     */
    public NotificationCompat.Builder createDefaultBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDefaultChannel(mChannelDefaultId, mChannelDefaultName);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mChannelDefaultId);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setVibrate(new long[]{1000, 500, 2000})
                .setAutoCancel(true)
                .setGroup(mChannelGroupId)
                .setLights(Color.GREEN, 1000, 1000)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis());
        return builder;
    }

    public void showNotification(int notificationId, Notification notification) {
        mManager.notify(notificationId, notification);
    }


    public void cancelNotification(int id) {
        mManager.cancel(id);
    }


    /**
     * 创建下载通知的Builder
     *
     * @return
     */
    public NotificationCompat.Builder createDownloadBuilder(int max, int process) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createImportanceChannel(mChannelDownloadId, mChannelImportanceName);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mChannelImportanceId);
        builder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.icon_small)
                .setLargeIcon(largeIcon)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{1000, 500, 2000})
                .setAutoCancel(false)
                .setGroup(mDownloadChannelGroupId)
                .setProgress(max, process, false)
                .setLights(Color.RED, 1000, 1000)
//                .setShowWhen(true)
                .setWhen(System.currentTimeMillis());
        return builder;

    }

    /**
     * 更新下载notification
     *
     * @param notificationId
     * @param max
     * @param process
     * @param builder
     */
    public void updateDownloadNotification(int notificationId, int max, int process, NotificationCompat.Builder builder) {
        if (process != max) {
            builder.setProgress(max, process, false);
            builder.setContentText((process * 100 / max) + "%");
        } else {
            builder.setProgress(0, 0, false);
        }
        showNotification(notificationId, builder.build());
    }
}
