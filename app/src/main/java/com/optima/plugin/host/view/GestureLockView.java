package com.optima.plugin.host.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.PluginUtils.P_SPManager;
import com.optima.plugin.repluginlib.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by wma
 * on 2020/8/28 0028
 */
public class GestureLockView extends View {
    final String TAG = GestureLockView.class.getSimpleName();
    private Paint mPaint, mAnimPaint;
    private Context mContext;
    private int mLineColor;// 连接线的颜色
    private int mPointColor;// 点颜色
    private int mCircleLineColor;// 外圈线的颜色
    private int mCircleColor;// 圆圈颜色

    private int mWidth, mHeight;// 宽高

    private int mNOPerLine = 3;// 每行个数

    private int interval;// 间隔

    private int mRadius;// 每个圈的半径

    private Map<Point, Rect> mRPMap = new HashMap<>();

    private List<Point> mSelectPoint = new ArrayList<>();

    private boolean isSetPassword;


    public GestureLockView(Context context) {
        this(context, null);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.GestureLockView);
        mLineColor = ta.getColor(R.styleable.GestureLockView_lineColor, ContextCompat.getColor(mContext, R.color.colorAccent));
        mPointColor = ta.getColor(R.styleable.GestureLockView_pointColor, ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        mCircleLineColor = ta.getColor(R.styleable.GestureLockView_circleLineColor, ContextCompat.getColor(mContext, R.color.colorPrimary));
        mCircleColor = ta.getColor(R.styleable.GestureLockView_circleColor, ContextCompat.getColor(mContext, R.color.colorPrimary));
        ta.recycle();
        init();

    }

    /**
     * 初始化操作
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAnimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaintForPointer(mAnimPaint);
        interval = ResourceUtils.dip2px(mContext, 20);
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
        Logger.d(TAG, "onMeasure: mWidth = " + mWidth + " mHeight = " + mHeight);
        mRadius = (mWidth - mNOPerLine * interval - interval) / (2 * mNOPerLine);
        Logger.d(TAG, "onMeasure: mRadius = " + mRadius);
        for (int i = 1; i <= mNOPerLine; i++) {
            int y = i * interval + i * 2 * mRadius - mRadius;
            for (int j = 1; j <= mNOPerLine; j++) {
                int x = j * interval + j * 2 * mRadius - mRadius;
                Point point = new Point(x, y);
                Rect rect = new Rect(x - mRadius / 3, y - mRadius / 3, x + mRadius / 3, y + mRadius / 3);
                mRPMap.put(point, rect);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : mRPMap.keySet()) {
            initPaintForPointer(mPaint);
            canvas.drawCircle(point.x, point.y, mRadius / 10, mPaint);
            initPaintForCircle(mPaint);
            canvas.drawCircle(point.x, point.y, mRadius, mPaint);
        }

        intPaintForLine(mPaint);
        if (mSelectPoint.size() > 0) {
            for (int i = 0; i < mSelectPoint.size(); i++) {
                Point curPoint = mSelectPoint.get(i);
                Point lastPoint = null;
                if (i - 1 >= 0) {
                    lastPoint = mSelectPoint.get(i - 1);
                }
                if (lastPoint != null) {
                    canvas.drawLine(lastPoint.x, lastPoint.y, curPoint.x, curPoint.y, mPaint);
                }
            }
        }
        if (mStartX != -1 && mStartY != -1 && mStopX != -1 && mStopY != -1) {
            canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
        }
    }


    private void intPaintForLine(Paint paint) {
        paint.setColor(mLineColor);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
    }


    private void initPaintForCircle(Paint paint) {
        paint.setColor(mCircleLineColor);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 为画点初始化画笔
     */
    private void initPaintForPointer(Paint paint) {
        paint.setColor(mPointColor);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
    }


    private int mStartX = -1, mStartY = -1, mStopX = -1, mStopY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (Point point : mRPMap.keySet()) {
                    Rect rect = mRPMap.get(point);
                    if (rect.contains(x, y)) {
                        if (!mSelectPoint.contains(point)) {
                            mStartX = point.x;
                            mStartY = point.y;
                            mSelectPoint.add(point);
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mStopX = (int) event.getX();
                mStopY = (int) event.getY();
                for (Point point : mRPMap.keySet()) {
                    Rect rect = mRPMap.get(point);
                    if (rect.contains(mStopX, mStopY)) {
                        if (!mSelectPoint.contains(point)) {
                            mStartX = point.x;
                            mStartY = point.y;
                            mSelectPoint.add(point);
                            break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isSetPassword) {
                    isSetPassword = false;
                    P_SPManager.putString("GestureLockView", "password", new Gson().toJson(mSelectPoint), true);
                    Logger.d(TAG, "onTouchEvent: 密码设置成功");
                } else {
                    String password = P_SPManager.getString("GestureLockView", "password", "", true);
                    String s = new Gson().toJson(mSelectPoint);
                    if (onVerifyListener != null) {
                        if (password.equals(s)) {
                            onVerifyListener.pass();
                        } else {
                            onVerifyListener.fail();
                        }
                    }
                }
                reset();

                break;
        }
        invalidate();
        return true;
    }

    private void reset() {
        mSelectPoint.clear();
        mStartX = -1;
        mStartY = -1;
        mStopX = -1;
        mStopY = -1;
    }

    public void setPassWord() {
        Logger.d(TAG, "setPassWord: 开始设置密码");
        isSetPassword = true;
    }

    public void setOnVerifyListener(OnVerifyListener onVerifyListener) {
        this.onVerifyListener = onVerifyListener;
    }

    OnVerifyListener onVerifyListener;

    public interface OnVerifyListener {
        void pass();

        void fail();
    }
}
