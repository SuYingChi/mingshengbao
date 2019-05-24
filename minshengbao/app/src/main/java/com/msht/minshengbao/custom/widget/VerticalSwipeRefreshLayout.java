package com.msht.minshengbao.custom.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by hong on 2017/11/9.
 */

public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {
    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;
    private float mPrexY;
    public VerticalSwipeRefreshLayout(Context context) {
        super(context);

    }
    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                mPrexY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                final float eventY = event.getY();
                float xDiff = Math.abs(eventX - mPrevX);
                float yDiff = Math.abs(eventY -mPrexY);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                /*if (xDiff > mTouchSlop + 60) {
                    return false;
                }*/
                //// 如果X轴位移大于Y轴位移，不处理。
                if (yDiff<xDiff){
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }
}
