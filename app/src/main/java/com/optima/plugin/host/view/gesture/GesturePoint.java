package com.optima.plugin.host.view.gesture;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.view.animation.AccelerateInterpolator;

/**
 * create by wma
 * on 2020/8/31 0031
 */
public class GesturePoint extends Point {
    transient private int radius;
    private boolean isSelect;


    public GesturePoint(int x, int y) {
        super(x, y);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void startAnim(final GestureLockView view, int maxRadius) {
        ValueAnimator anim = ValueAnimator.ofInt(radius, maxRadius);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                radius = animatedValue;
                view.invalidate();
            }
        });
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(200);
        anim.start();
    }
}
