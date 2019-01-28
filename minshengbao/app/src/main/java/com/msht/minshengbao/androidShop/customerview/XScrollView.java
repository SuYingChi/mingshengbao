package com.msht.minshengbao.androidShop.customerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.msht.minshengbao.androidShop.util.LogUtils;

public class XScrollView extends NestedScrollView {
    private OnScrollBottomListener _listener;
    private int _calCount;
    private XScrollViewListener xScrollViewListener;
    private int lastX;
    private int lastY;
    private int lastDownY;

    //滚动到底部时，clampedY变为true，其余情况为false，通过回调将状态传出去即可。
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        LogUtils.e("onOverScrolled", "onOverScrolled: scrollY=" + scrollY + "clampedY=" + clampedY);
        if (scrollY != 0 && null != mOnScrollToBottomListener) {
            mOnScrollToBottomListener.onScrollBottomListener(clampedY);
        }
    }

    public interface OnScrollToBottomListener {
        public void onScrollBottomListener(boolean isBottom);
    }


    private OnScrollToBottomListener mOnScrollToBottomListener;

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
        mOnScrollToBottomListener = listener;
    }

    public interface OnScrollBottomListener {
        void srollToBottom();
    }

    public void registerOnBottomListener(OnScrollBottomListener l) {
        _listener = l;
    }

    public void unRegisterOnBottomListener() {
        _listener = null;
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = this.getChildAt(0);
        if (this.getHeight() + this.getScrollY() == view.getHeight()) {
            _calCount++;
            if (_calCount == 1) {
                if (_listener != null) {
                    _listener.srollToBottom();
                }
            }
        } else {
            _calCount = 0;
        }
        if (null != xScrollViewListener) {
            xScrollViewListener.onScrollChanged(l, t, oldl, oldt);
        }

    }

    public interface XScrollViewListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);

        void onScrollOverTop();

        void onScrollNormal();

        void onScrollOverBottom();
    }

    public void setXScrollViewListener(XScrollViewListener xlistener) {
        this.xScrollViewListener = xlistener;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View view = this.getChildAt(0);
        // 获取当前触摸的绝对坐标
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        boolean isNormalScroll = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 上一次离开时的坐标
                lastX = rawX;
                lastY = rawY;
                lastDownY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                // 两次的偏移量
                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;
                int offsetY2 = rawY - lastDownY;

                if (getScrollY()<=3&&view.getTop() <= 3 && offsetY > 0  && xScrollViewListener != null) {
                    LogUtils.e("onTouchEvent onScrollOverTop", "ACTION_MOVE: getScrollY()=" + getScrollY() + "offsetY=" + offsetY+"offsetY2= "+offsetY2+"view.getTop()="+view.getTop());
                    xScrollViewListener.onScrollOverTop();
                }else if(getHeight() + getScrollY() == view.getHeight() && offsetY < 0  && xScrollViewListener != null){
                    LogUtils.e("onTouchEvent onScrollOverBottom", "ACTION_MOVE: getScrollY()=" + getScrollY() + "offsetY=" + offsetY+"offsetY2= "+offsetY2);
                    xScrollViewListener.onScrollOverBottom();
                } else if(xScrollViewListener != null){
                    LogUtils.e("onTouchEvent onScrollNormal", "ACTION_MOVE: getScrollY()=" + getScrollY() + "offsetY=" + offsetY+"offsetY2= "+offsetY2);
                    xScrollViewListener.onScrollNormal();
                }
                // 不断修改上次移动完成后坐标
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    //禁止scrollView内布局变化后自动滚动
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;

    }
}
