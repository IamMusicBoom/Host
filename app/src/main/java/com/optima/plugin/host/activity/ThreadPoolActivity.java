package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optima.plugin.host.R;
import com.optima.plugin.host.thread.ThreadQueue;
import com.optima.plugin.host.thread.ThreadTask;

public class ThreadPoolActivity extends AppCompatActivity implements View.OnClickListener {
    ThreadQueue queue;
    TextView taskCountTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool);
        queue = new ThreadQueue(5);
        taskCountTv = findViewById(R.id.tv_task_count);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_task) {
            ThreadTask task = new ThreadTask("我是第：" + (queue.taskCount() + 1) + " 项任务");
            queue.addTask(task);
            taskCountTv.setText("任务数量：" + queue.taskCount());
        } else if (v.getId() == R.id.btn_start_task_queue) {
            queue.excuse();
        }
    }
}
