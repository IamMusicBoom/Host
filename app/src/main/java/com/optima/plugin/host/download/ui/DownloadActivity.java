package com.optima.plugin.host.download.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.optima.plugin.host.download.LoginConstans;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;


/**
 * create by wma
 * on 2020/9/24 0024
 */
public class DownloadActivity extends BaseActivity {
    private static final String FROM = "FROM";
    DownloadDialog downloadDialog;
    private String mFrom;// 更新宿主有两个地方，一个是IndexActivity,一个是AboutActivity;
    String mAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        mAction = intent.getAction();
        Logger.d(TAG, "onCreate: action = " + mAction);
        switch (mAction) {
            case LoginConstans.ACTION_UPDATE_HOST:
                downloadDialog = DownloadDialog.createDownloadHostDialog();
                downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
                break;
            case LoginConstans.ACTION_DOWNLOAD_PLUGIN:
                downloadDialog = DownloadDialog.createDownloadPlugin();
                downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
                downloadDialog.startDownload(DownloadActivity.this);
                break;
        }
    }

    /**
     * 更新宿主
     *
     * @param context
     * @param from
     */
    public static void upDataHost(Context context, String from) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.setAction(LoginConstans.ACTION_UPDATE_HOST);
        intent.putExtra(FROM, from);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 下载插件
     *
     * @param context
     */
    public static void downloadPlugin(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.setAction(LoginConstans.ACTION_DOWNLOAD_PLUGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
