package com.optima.plugin.host.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import com.optima.plugin.host.R;
import com.optima.plugin.host.view.GestureLockView;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class LockScreenActivity extends BaseActivity implements View.OnClickListener {
    final String TAG = LockScreenActivity.class.getSimpleName();
    GestureLockView gestureLockView;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        gestureLockView = findViewById(R.id.gesture_view);
        button = findViewById(R.id.btn_set_password);
        button.setOnClickListener(this);
        gestureLockView.setOnVerifyListener(new GestureLockView.OnVerifyListener() {
            @Override
            public void pass() {
                Logger.d(TAG, "pass: ");
            }

            @Override
            public void fail() {
                Logger.d(TAG, "fail: ");
            }
        });
    }

    @Override
    public void onClick(View v) {
        gestureLockView.setPassWord();
    }
}
