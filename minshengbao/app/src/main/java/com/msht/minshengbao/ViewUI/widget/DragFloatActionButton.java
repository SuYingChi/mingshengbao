package com.msht.minshengbao.ViewUI.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * @author hong
 */
public class DragFloatActionButton extends FloatingActionButton {
    private static final String TAG = "DragFloatActionButton";

    private int parentHeight;
    private int parentWidth;
    private int startX, startY;
    private int lastX, lastY;
    private boolean isMove;


    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int rawX = (int)ev.getRawX();
        int rawY = (int)ev.getRawY();
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                startX = lastX = rawX;
                startY = lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float x = getX() + (rawX - lastX);
                float y = getY() + (rawY - lastY);
//检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                int dx = (int) ev.getRawX() - startX;
                int dy = (int) ev.getRawY() - startY;
                if (Math.abs(dx) < 3 && Math.abs(dy) < 3) {
                    performClick();//重点，确保DragFloatActionButton2.setOnclickListener生效
                    break;
                }
                if (rawX >= parentWidth / 2) {
//靠右吸附
                    animate().setInterpolator(new DecelerateInterpolator())
                            .setDuration(500)
                            .xBy(parentWidth - getWidth() - getX())
                            .start();
                } else {
//靠左吸附
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                    oa.setInterpolator(new DecelerateInterpolator());
                    oa.setDuration(500);
                    oa.start();
                }
                break;
                default:
                    break;
        }
        return !isMove;
    }
}
