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
public class DownloadActivity extends BaseActivity implements View.OnClickListener, DownloadDialog.DialogClickListener {

    RecyclerView recyclerView;
    List<Icon> icons = new ArrayList<>();
    DownloadDialog downloadDialog;
    DownloadService.DownloadBinder binder;
    DownloadService downloadService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        recyclerView = findViewById(R.id.recycler_view);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_download) {// 开始下载
            downloadDialog = DownloadDialog.newInstance("请稍等", "资源下载中：", null);
            downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
            downloadDialog.setOnDialogClickListener(this);
        } else if (v.getId() == R.id.btn_show_image) {
            new QueryLocalIcon(false).start();
        } else if (v.getId() == R.id.btn_delete_image) {
            new QueryLocalIcon(true).start();
        } else if (v.getId() == R.id.btn_start_download_service) {

        } else if (v.getId() == R.id.btn_stop_download_service) {

        }
    }

    @Override
    public void onNegativeClick(View view) {
        Logger.d(TAG, "onNegativeClick: 取消按钮");
        downloadService.cancelTask();
        downloadDialog.dismiss();
    }

    @Override
    public void onPositiveClick(View view) {
        Logger.d(TAG, "onNegativeClick: 确定按钮");
        Intent intent = new Intent(DownloadActivity.this, DownloadService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    DownloadServiceConnection conn = new DownloadServiceConnection();

    class DownloadServiceConnection implements ServiceConnection, ProcessListener {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            binder = (DownloadService.DownloadBinder) service;

            downloadService = binder.getService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onStart() {
            Logger.d(TAG, "onStart: ");
        }

        @Override
        public void onFinish() {
            Logger.d(TAG, "onFinish: ");
            unbindService(conn);
        }

        @Override
        public void onProcess(int cur, int total) {
            StringBuffer sb = new StringBuffer();
            sb.append("资源下载中：");
            sb.append(cur);
            sb.append("/");
            sb.append(total);
            downloadDialog.setMessage(sb.toString());
        }

        @Override
        public void cancel() {
            Logger.d(TAG, "cancel: ");
        }
    }


    class QueryLocalIcon extends Thread {
        boolean isDelete;

        public QueryLocalIcon(boolean isDelete) {
            this.isDelete = isDelete;
        }

        @Override
        public void run() {
            super.run();
            File externalFilesDir = DownloadActivity.this.getExternalFilesDir(DownloadTask.ICON_FOLDER);
            if (externalFilesDir != null && externalFilesDir.isDirectory()) {
                File[] files = externalFilesDir.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (isDelete) {
                            file.delete();
                        } else {
                            Icon icon = new Icon();
                            Logger.d(TAG, "run: " + file.getName() + ":" + file.getAbsolutePath());
                            icon.setName(file.getName());
                            icon.setPath(file.getAbsolutePath());
                            icons.add(icon);
                        }
                    }
                    if (!isDelete) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DownloadImgAdapter mAdapter = new DownloadImgAdapter(icons, DownloadActivity.this);
                                recyclerView.setAdapter(mAdapter);
                            }
                        });
                    }
                } else {
                    Logger.e(TAG, "run: 没有找到icon文件");
                }
            } else {
                Logger.e(TAG, "run: 装icon的文件出错了");
            }
        }
    }
}
