package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.R;
import com.optima.plugin.host.download.DownloadTask;
import com.optima.plugin.host.thread.ThreadQueue;
import com.optima.plugin.host.thread.ThreadTask;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.module.Icon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ThreadPoolActivity extends AppCompatActivity implements View.OnClickListener {
    ThreadQueue queue;
    TextView taskCountTv;
    List<Icon> icons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool);
        queue = new ThreadQueue(5);
        taskCountTv = findViewById(R.id.tv_task_count);
        AssetManager assets = getResources().getAssets();
        try {
            InputStream open = assets.open("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(open));
            Gson gson = new Gson();
            icons = gson.fromJson(reader, new TypeToken<List<Icon>>() {
            }.getType());
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_task) {
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                ThreadTask task = new ThreadTask(icon.getName(),icon.getPath());
                queue.addTask(task);
            }
        } else if (v.getId() == R.id.btn_start_task_queue) {
            queue.excuse();
        }
    }
}
