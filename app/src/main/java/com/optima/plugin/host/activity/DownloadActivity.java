package com.optima.plugin.host.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import com.optima.plugin.host.thread.DownloadDialog;
import com.optima.plugin.repluginlib.base.BaseActivity;


/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity{

    DownloadDialog downloadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_download);
        downloadDialog = DownloadDialog.createDownloadPlugin();
//        downloadDialog = DownloadDialog.createDownloadHostDialog();
        downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
        downloadDialog.startDownload(DownloadActivity.this);

    }



}
