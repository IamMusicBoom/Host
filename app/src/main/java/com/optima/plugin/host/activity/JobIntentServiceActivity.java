package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.host.service.WorkService;

public class JobIntentServiceActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_intent_service);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(JobIntentServiceActivity.this, WorkService.class);
        if(v.getId() == R.id.btn_start_service){
            startService(intent);
        }else if(v.getId() == R.id.btn_stop_service){
            stopService(intent);
        }else if(v.getId() == R.id.btn_bind_service){
            bindService(intent,conn,BIND_AUTO_CREATE);
        }else if(v.getId() == R.id.btn_unbind_service){
            unbindService(conn);
        }else if(v.getId() == R.id.btn_enqueue_service){
            WorkService.enqueueWork(JobIntentServiceActivity.this,intent);
        }
    }

    MyServiceConnection conn = new MyServiceConnection();
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
