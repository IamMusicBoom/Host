package com.optima.plugin.host.view.gesture;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class LockScreenActivity extends BaseActivity implements View.OnClickListener {
    final String TAG = LockScreenActivity.class.getSimpleName();
    GestureLockGroup gestureLockGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        gestureLockGroup = findViewById(R.id.gesture_view);
        gestureLockGroup.setOnVerifyListener(new GestureLockGroup.OnVerifyListener() {
            @Override
            public void pass() {
                Logger.d(TAG, "pass: ");
                LockScreenActivity.super.onBackPressed();
            }

            @Override
            public void fail() {
                Logger.d(TAG, "fail: ");
            }

            @Override
            public void forget() {
                Logger.d(TAG, "forget: ");
                gestureLockGroup.clearPassword();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            Logger.d(TAG, "onKeyDown: ");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }
}
