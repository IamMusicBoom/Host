package com.optima.plugin.host.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.optima.plugin.host.R;
import com.optima.plugin.host.thread.DownloadDialog;
import com.optima.plugin.host.thread.ProgressView;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;


/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity {

    DownloadDialog downloadDialog;
    ProgressView mProcessView;
    int process = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mProcessView = findViewById(R.id.process_view);
//        downloadDialog = DownloadDialog.createDownloadPlugin();
//        downloadDialog = DownloadDialog.createDownloadHostDialog();
//        downloadDialog.show(getSupportFragmentManager(), DownloadDialog.TAG);
//        downloadDialog.startDownload(DownloadActivity.this);

//        mProcessView.setProcess(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    mProcessView.setMax(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (process < 100){
                    process++;
                    mProcessView.setProcess(process);
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
