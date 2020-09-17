package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.optima.plugin.host.R;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool);
    }
}
