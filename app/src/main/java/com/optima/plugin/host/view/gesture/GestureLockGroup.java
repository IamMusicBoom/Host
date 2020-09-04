package com.optima.plugin.host.view.gesture;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.utils.ResourceUtils;

/**
 * create by wma
 * on 2020/8/31 0031
 */
public class GestureLockGroup extends LinearLayout implements GestureLockView.OnStatusChangeListener, View.OnClickListener, GestureLockView.OnVerifyListener {
    private final String TAG = GestureLockGroup.class.getSimpleName();
    private Context mContext;
    private GestureLockView mLockView;
    private TextView mHintTv;

    public GestureLockGroup(Context context) {
        this(context, null);
    }

    public GestureLockGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();


    }

    private void init() {
        setOrientation(VERTICAL);
        mLockView = new GestureLockView(mContext);
        mLockView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(mLockView);
        mHintTv = new TextView(mContext);
        mHintTv.setGravity(Gravity.RIGHT);
        LayoutParams tvLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tvLp.rightMargin = ResourceUtils.dip2px(mContext, 20);
        mHintTv.setLayoutParams(tvLp);
        mHintTv.setPadding(ResourceUtils.dip2px(mContext, 10), ResourceUtils.dip2px(mContext, 10), ResourceUtils.dip2px(mContext, 10), ResourceUtils.dip2px(mContext, 10));
        mHintTv.setTextColor(Color.CYAN);
        mHintTv.getPaint().setFakeBoldText(true);
        mHintTv.setTextSize(18);
        addView(mHintTv);
        mHintTv.setText(TextUtils.isEmpty(mLockView.getPassword()) ? "设置密码？" : "忘记密码？");
        mLockView.addOnStatusChangeListener(this);
        mHintTv.setOnClickListener(this);
        mLockView.setOnVerifyListener(this);
    }


    @Override
    public void currentStatus(int curStatus) {
        switch (curStatus) {
            case GestureLockView.INPUT_STATUS:
                mHintTv.setText(TextUtils.isEmpty(mLockView.getPassword()) ? "设置密码？" : "忘记密码？");
                break;
            case GestureLockView.SET_STATUS:
                mHintTv.setText("连线设置一个密码");
                break;
            case GestureLockView.SET_STATUS_AGAIN:
                mHintTv.setText("后退");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (mLockView.getCurStatus()) {
            case GestureLockView.INPUT_STATUS:
                if (TextUtils.isEmpty(mLockView.getPassword())) {
                    mLockView.setCurStatus(GestureLockView.SET_STATUS);
                } else {
                    if(onVerifyListener != null){
                        onVerifyListener.forget();
                    }
                }
                break;
            case GestureLockView.SET_STATUS:
            case GestureLockView.SET_STATUS_AGAIN:
                mLockView.setCurStatus(GestureLockView.INPUT_STATUS);
                break;
        }
    }

    @Override
    public void pass() {
        if(onVerifyListener != null){
            onVerifyListener.pass();
        }
    }

    @Override
    public void fail() {
        if(onVerifyListener != null){
            onVerifyListener.fail();
        }
    }


    public void setOnVerifyListener(OnVerifyListener onVerifyListener) {
        this.onVerifyListener = onVerifyListener;
    }

    OnVerifyListener onVerifyListener;

    public void clearPassword() {
        mLockView.clearPassword();
    }

    public interface OnVerifyListener {
        void pass();

        void fail();

        void forget();
    }
}
