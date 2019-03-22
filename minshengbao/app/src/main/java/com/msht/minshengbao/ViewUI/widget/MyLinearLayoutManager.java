package com.msht.minshengbao.ViewUI.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class MyLinearLayoutManager extends LinearLayoutManager {
    private int mLength=3;
    public MyLinearLayoutManager(Context context) {
        super(context);
    }
    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int count = state.getItemCount();

        if (count > 0) {
            if(count>mLength){
                count =mLength;
            }
            int realHeight = 0;
            int realWidth = 0;
            for(int i = 0;i < count; i++){
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    measureChild(view, widthSpec, heightSpec);
                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                    int measuredHeight = view.getMeasuredHeight();
                    realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                    realHeight += measuredHeight;
                }
                setMeasuredDimension(realWidth, realHeight);
            }
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }
}
