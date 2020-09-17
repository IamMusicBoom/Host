package com.optima.plugin.host.view.download;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.utils.ResourceUtils;

/**
 * create by wma
 * on 2020/9/3 0003
 */
public class WaveView extends View {
    String TAG = WaveView.class.getSimpleName();
    Paint mPaint;
    Path mPath;
    Context mContext;
    int mWidth, mHeight, mCenterX, mCenterY, mWaveHeight = 100;
    ValueAnimator mWaveAnim;
    PointF pointA, controlA, pointB, controlB, pointC;
    long mProcess;

    long mMax = -1;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                mWidth = Math.max(ResourceUtils.getScreenWidth(mContext), MeasureSpec.getSize(widthMeasureSpec));
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                mHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                int min = Math.min(ResourceUtils.getScreenWidth(mContext), ResourceUtils.getScreenHeight(mContext));
                mHeight = Math.max(min, MeasureSpec.getSize(heightMeasureSpec));
                break;
        }
        mWidth = mHeight = Math.min(mWidth, mHeight);
        setMeasuredDimension(mWidth, mHeight);
        mProcess = 0;
        mCenterX = mWidth / 2;
        mCenterY = mWidth / 2;
        pointA = new PointF(0, mProcess);
        controlA = new PointF(mCenterX / 2, mProcess - mWaveHeight);
        pointB = new PointF(mCenterX, mProcess);
        controlB = new PointF(mCenterX + mCenterX / 2, mProcess + mWaveHeight);
        pointC = new PointF(mWidth, mProcess);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPath != null) {
            initPaintForLine(mPaint);
            mPath.reset();
            mPath.moveTo(pointA.x, pointA.y);
            mPath.quadTo(controlA.x, controlA.y, pointB.x, pointB.y);
            mPath.moveTo(pointB.x, pointB.y);
            mPath.quadTo(controlB.x, controlB.y, pointC.x, pointC.y);
            mPath.lineTo(mWidth, mHeight);
            mPath.lineTo(0, mHeight);
            mPath.lineTo(0, mProcess);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }


    }


    private void initPaintForLine(Paint paint) {
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#66000000"));
        paint.setStyle(Paint.Style.FILL);
    }


    public void startAnim() {
        if (mPath == null) {
            mPath = new Path();
        }
        mWaveAnim = ValueAnimator.ofInt(mWaveHeight, -mWaveHeight);
        mWaveAnim.setRepeatCount(300);
        mWaveAnim.setRepeatMode(ValueAnimator.REVERSE);
        mWaveAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                controlA.y = mProcess - animatedValue;
                controlB.y = mProcess + animatedValue;
                invalidate();

            }
        });
        mWaveAnim.setDuration(1000);
        mWaveAnim.start();
    }

    public void stopAnim() {
        if (mWaveAnim != null && mWaveAnim.isRunning()) {
            mWaveAnim.removeAllUpdateListeners();
            mWaveAnim.cancel();
            mWaveAnim = null;
            mPath = null;
            invalidate();
        }
    }

    public void setMax(long max) {
        mMax = max;
        startAnim();
    }

    public void updateProcess(long process) {
        mProcess = (mHeight * process / mMax);
        pointA.y = mProcess;
        controlA.y = mCenterY - mWaveHeight - mProcess;
        pointB.y = mProcess;
        controlB.y = mCenterY + mWaveHeight - mProcess;
        pointC.y = mProcess;
        invalidate();
    }



    public long getMax(){
        return mMax;
    }

    public void reset() {
        mMax = -1;
        mProcess = 0;
    }
}
