package com.optima.plugin.host.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.R;
import com.optima.plugin.host.adapter.DownloadImgAdapter;
import com.optima.plugin.host.download.CallbackListener;
import com.optima.plugin.host.download.DownloadTask;
import com.optima.plugin.host.download.TaskQueue;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.module.Icon;

import org.xutils.common.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity implements View.OnClickListener, CallbackListener {
    //    final String netUrl = "http://seopic.699pic.com/photo/50073/8276.jpg_wh1200.jpg";
//    final String netUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598953251041&di=a710c8d0ef81ac28393751ebd4181499&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fe%2F59ae3a99a20fb.jpg%3Fdown";

    RecyclerView recyclerView;
    final String netUrl1 = "http://xiazai.suanmiao-zuida.com/2008/BaBai.HD1280%E9%AB%98%E6%B8%85%E5%9B%BD%E8%AF%AD%E4%B8%AD%E5%AD%97%E7%89%88.mp4";
    final String pluginName1 = "babai.mp4";

    final String pluginName2 = "nezha.mp4";
    final String netUrl2 = "http://caizi.meizuida.com/1910/NZZMT%E9%99%8D%E4%B8%96.HD1280%E9%AB%98%E6%B8%85%E5%9B%BD%E8%AF%AD%E4%B8%AD%E5%AD%97%E7%89%88.mp4";
    TaskQueue taskQueue;

    List<Icon> icons = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        recyclerView = findViewById(R.id.recycler_view);
        taskQueue = new TaskQueue(5);
        AssetManager assets = getResources().getAssets();
        try {
            InputStream open = assets.open("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(open));
            Gson gson = new Gson();
            List<Icon> icons = gson.fromJson(reader, new TypeToken<List<Icon>>() {
            }.getType());
            Logger.d(TAG, "onCreate: icons.size() = " + icons.size());
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                taskQueue.addTask(new DownloadTask(icon.getName(), icon.getPath(), this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_download) {// 开始下载
            taskQueue.excuse();
        } else if (v.getId() == R.id.btn_pause_download) {

        } else if (v.getId() == R.id.btn_show_image) {
            new QueryLocalIcon(false).start();
//            recyclerView.setAdapter(mAdapter);
        } else if (v.getId() == R.id.btn_delete_image) {
            new QueryLocalIcon(true).start();
        }
    }


    @Override
    public void onSuccess(File result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
    }

    class QueryLocalIcon extends Thread {
        boolean isDelete;

        public QueryLocalIcon(boolean isDelete) {
            this.isDelete = isDelete;
        }

        @Override
        public void run() {
            super.run();
            File externalFilesDir = DownloadActivity.this.getExternalFilesDir(DownloadTask.FOLDER_NAME);
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
