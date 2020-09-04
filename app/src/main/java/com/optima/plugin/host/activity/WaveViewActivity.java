package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.optima.plugin.host.R;
import com.optima.plugin.host.view.download.WaveView;

public class WaveViewActivity extends AppCompatActivity implements View.OnClickListener {
    WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);
        waveView = findViewById(R.id.wave_view);
    }

    int process = 0;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_anim) {
            waveView.setMax(100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (process <= 100){
                        waveView.updateProcess(process);
                        process++;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        if (v.getId() == R.id.stop_anim) {
            waveView.stopAnim();
        }
    }
}
