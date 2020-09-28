package com.optima.plugin.host.download.ui;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.utils.ResourceUtils;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public class ProgressView extends View {

    private Paint mPaint;
    int mWidth, mHeight;
    private Context mContext;
    private int padding;
    private int mStrokeWidth = 20;
    private float mMax = 0;
    private float mCurProcess = 0;

    private float startX = 0;
    private float endX = 0;

    private float mMaxProcess;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(startX, mHeight / 2, mMaxProcess + startX, mHeight / 2, mPaint);
        mPaint.setColor(Color.CYAN);
        canvas.drawLine(startX, mHeight / 2, mCurProcess, mHeight / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        padding = ResourceUtils.dp2px(mContext, 40);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = Math.min(MeasureSpec.getSize(heightMeasureSpec) + padding, mStrokeWidth + padding / 2);
        setMeasuredDimension(mWidth, mHeight);
        startX = 0 + padding / 4;
        endX = mWidth - padding / 4;
        mMaxProcess = endX - startX;
        mMax = mMaxProcess;
    }

    public void setMax(long max) {
        this.mMax = max;
    }

    public void setProcess(long process) {
        float dd = process / mMax * mMaxProcess  + startX;
        this.mCurProcess = dd;
        postInvalidate();
    }
}
