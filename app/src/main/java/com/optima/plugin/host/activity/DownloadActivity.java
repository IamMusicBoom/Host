package com.optima.plugin.host.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.plugin.host.R;
import com.optima.plugin.host.adapter.DownloadImgAdapter;
import com.optima.plugin.host.thread.DownloadDialog;
import com.optima.plugin.host.thread.DownloadService;
import com.optima.plugin.host.thread.DownloadTask;
import com.optima.plugin.host.thread.ProcessListener;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.module.Icon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
//        downloadDialog = DownloadDialog.createDownloadPlugin();
        downloadDialog = DownloadDialog.createDownloadHostDialog();
        downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
//        downloadDialog.startDownload(DownloadActivity.this);

    }



}
