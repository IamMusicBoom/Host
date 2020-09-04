package com.optima.plugin.host.view.gesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
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
    private final String TAG = GestureLockView.class.getSimpleName();

    public static final int INPUT_STATUS = 0;// 输入密码状态
    public static final int SET_STATUS = 1;// 设置密码状态
    public static final int SET_STATUS_AGAIN = 2;// 再次设置密码状态


    private Paint mPaint, mAnimPaint;
    private Context mContext;
    private int mLineColor;// 连接线的颜色
    private int mPointColor;// 点颜色
    private int mCircleLineColor;// 外圈线的颜色
    private int mCircleColor;// 圆圈颜色

    private int mDefaultLineColor;// 连接线的颜色
    private int mDefaultPointColor;// 点颜色
    private int mDefaultCircleLineColor;// 外圈线的颜色
    private int mDefaultCircleColor;// 圆圈颜色

    private int mSetLineColor;// 连接线的颜色
    private int mSetPointColor;// 点颜色
    private int mSetCircleLineColor;// 外圈线的颜色
    private int mSetCircleColor;// 圆圈颜色

    private int mErrorLineColor;// 连接线的颜色
    private int mErrorPointColor;// 点颜色
    private int mErrorCircleLineColor;// 外圈线的颜色
    private int mErrorCircleColor;// 圆圈颜色

    private int mWidth, mHeight;// 宽高

    private int mNOPerLine = 3;// 每行个数

    private int interval;// 间隔

    private int mRadius;// 每个圈的半径

    private int mCurStatus = INPUT_STATUS;// 当前状态

    private String firstSetPassword, secondSetPassword;// 第一次和第二次设置密码


    private Map<GesturePoint, Rect> mRPMap = new HashMap<>();

    private List<GesturePoint> mSelectPoint = new ArrayList<>();

    private int mPointRadius;// 点半径


    private GestureSP mSP;


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
        mDefaultLineColor = ta.getColor(R.styleable.GestureLockView_lineColor, ContextCompat.getColor(mContext, R.color.lineColor));
        mDefaultPointColor = ta.getColor(R.styleable.GestureLockView_pointColor, ContextCompat.getColor(mContext, R.color.pointColor));
        mDefaultCircleLineColor = ta.getColor(R.styleable.GestureLockView_circleLineColor, ContextCompat.getColor(mContext, R.color.circleLineColor));
        mDefaultCircleColor = ta.getColor(R.styleable.GestureLockView_circleColor, ContextCompat.getColor(mContext, R.color.circleColor));
        mErrorLineColor = ta.getColor(R.styleable.GestureLockView_lineColor, ContextCompat.getColor(mContext, R.color.errorLineColor));
        mErrorPointColor = ta.getColor(R.styleable.GestureLockView_pointColor, ContextCompat.getColor(mContext, R.color.errorPointColor));
        mErrorCircleLineColor = ta.getColor(R.styleable.GestureLockView_circleLineColor, ContextCompat.getColor(mContext, R.color.errorCircleLineColor));
        mErrorCircleColor = ta.getColor(R.styleable.GestureLockView_circleColor, ContextCompat.getColor(mContext, R.color.errorCircleColor));
        mSetLineColor = ta.getColor(R.styleable.GestureLockView_lineColor, ContextCompat.getColor(mContext, R.color.setLineColor));
        mSetPointColor = ta.getColor(R.styleable.GestureLockView_pointColor, ContextCompat.getColor(mContext, R.color.setPointColor));
        mSetCircleLineColor = ta.getColor(R.styleable.GestureLockView_circleLineColor, ContextCompat.getColor(mContext, R.color.setCircleLineColor));
        mSetCircleColor = ta.getColor(R.styleable.GestureLockView_circleColor, ContextCompat.getColor(mContext, R.color.setCircleColor));
        initColorForDefault();
        ta.recycle();
        init();

    }

    /**
     * 初始化操作
     */
    private void init() {
        mSP = new GestureSP(mContext);
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
        Logger.d(TAG, "onMeasure: mWidth = " + mWidth + " mHeight = " + mHeight);
        setMeasuredDimension(mWidth, mHeight);
        mRadius = (mWidth - mNOPerLine * interval - interval) / (2 * mNOPerLine);
        mPointRadius = mRadius / 10;
        for (int i = 1; i <= mNOPerLine; i++) {
            int y = i * interval + i * 2 * mRadius - mRadius;
            for (int j = 1; j <= mNOPerLine; j++) {
                int x = j * interval + j * 2 * mRadius - mRadius;
                GesturePoint point = new GesturePoint(x, y);
                point.setRadius(mPointRadius);
                Rect rect = new Rect(x - mRadius / 2, y - mRadius / 2, x + mRadius / 2, y + mRadius / 2);// 手指触摸到的范围，进入这个范围表示连接上该点
                mRPMap.put(point, rect);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (GesturePoint point : mRPMap.keySet()) {
            initPaintForPointer(mPaint);
            canvas.drawCircle(point.x, point.y, point.getRadius(), mPaint);
            initPaintForCircle(mPaint);
            canvas.drawCircle(point.x, point.y, mRadius, mPaint);
        }

        intPaintForLine(mPaint);
        if (mSelectPoint.size() > 0) {
            for (int i = 0; i < mSelectPoint.size(); i++) {
                GesturePoint curPoint = mSelectPoint.get(i);
                GesturePoint lastPoint = null;
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
        paint.setStrokeWidth(5);
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
                for (GesturePoint point : mRPMap.keySet()) {
                    Rect rect = mRPMap.get(point);
                    if (rect.contains(x, y)) {
                        if (!mSelectPoint.contains(point)) {
                            mStartX = point.x;
                            mStartY = point.y;
                            mSelectPoint.add(point);
                            point.startAnim(this, mRadius / 2);
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mStopX = (int) event.getX();
                mStopY = (int) event.getY();
                for (GesturePoint point : mRPMap.keySet()) {
                    Rect rect = mRPMap.get(point);
                    if (rect.contains(mStopX, mStopY)) {
                        if (!mSelectPoint.contains(point)) {
                            mStartX = point.x;
                            mStartY = point.y;
                            mSelectPoint.add(point);
                            point.startAnim(this, mRadius / 2);
                            break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mCurStatus == SET_STATUS) {// 如果是设置密码
                    firstSetPassword = new Gson().toJson(mSelectPoint);
                    setCurStatus(SET_STATUS_AGAIN);
                    Toast.makeText(mContext, "请再次输入密码", Toast.LENGTH_SHORT).show();
                } else if (mCurStatus == SET_STATUS_AGAIN) {// 再次输入密码
                    secondSetPassword = new Gson().toJson(mSelectPoint);
                    if (!firstSetPassword.equals(secondSetPassword)) {
                        Toast.makeText(mContext, "两次输入不匹配", Toast.LENGTH_SHORT).show();
                    } else {
                        mSP.putPassword(new Gson().toJson(mSelectPoint));
                        Toast.makeText(mContext, "密码设置成功", Toast.LENGTH_SHORT).show();
                        setCurStatus(INPUT_STATUS);
                    }
                } else {// 输入密码
                    String password = mSP.getPassword();
                    String input = new Gson().toJson(mSelectPoint);
                    if (onVerifyListener != null) {
                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(mContext, "还未设置密码，请先设置密码", Toast.LENGTH_SHORT).show();
                        } else if (password.equals(input)) {
                            onVerifyListener.pass();
                        } else {
                            Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                            initColorForError();
                            invalidate();
                            onVerifyListener.fail();
                        }
                    }
                }
                if (onStatusChangeListener != null) {
                    onStatusChangeListener.currentStatus(mCurStatus);
                }
                reset();
                break;
        }
        invalidate();
        return true;
    }


    private void initColorForDefault() {
        mLineColor = mDefaultLineColor;
        mPointColor = mDefaultPointColor;
        mCircleLineColor = mDefaultCircleLineColor;
        mCircleColor = mDefaultCircleColor;
    }

    private void initColorForError() {
        mLineColor = mErrorLineColor;
        mPointColor = mErrorPointColor;
        mCircleLineColor = mErrorCircleLineColor;
        mCircleColor = mErrorCircleColor;
    }

    private void initColorForSet() {
        mLineColor = mSetLineColor;
        mPointColor = mSetPointColor;
        mCircleLineColor = mSetCircleLineColor;
        mCircleColor = mSetCircleColor;
    }

    private void reset() {
        for (int i = 0; i < mSelectPoint.size(); i++) {
            mSelectPoint.get(i).startAnim(this, mPointRadius);
        }
        mSelectPoint.clear();
        mStartX = -1;
        mStartY = -1;
        mStopX = -1;
        mStopY = -1;
    }


    public String getPassword() {
        return mSP.getPassword();
    }

    public void setOnVerifyListener(OnVerifyListener onVerifyListener) {
        this.onVerifyListener = onVerifyListener;
    }

    OnVerifyListener onVerifyListener;

    public int getCurStatus() {
        return mCurStatus;
    }

    public void setCurStatus(int status) {
        mCurStatus = status;
        switch (mCurStatus) {
            case GestureLockView.INPUT_STATUS:
                initColorForDefault();
                invalidate();
                break;
            case GestureLockView.SET_STATUS:
            case GestureLockView.SET_STATUS_AGAIN:
                initColorForSet();
                invalidate();
                break;
        }
        if (onStatusChangeListener != null) {
            onStatusChangeListener.currentStatus(mCurStatus);
        }
    }

    public void clearPassword() {
        mSP.putPassword("");
        setCurStatus(INPUT_STATUS);
    }

    public interface OnVerifyListener {
        void pass();

        void fail();
    }


    public void addOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.onStatusChangeListener = onStatusChangeListener;
    }

    public OnStatusChangeListener onStatusChangeListener;

    public interface OnStatusChangeListener {
        void currentStatus(int curStatus);
    }
}
