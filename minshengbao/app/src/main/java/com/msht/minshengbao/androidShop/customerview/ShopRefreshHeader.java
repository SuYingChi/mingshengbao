package com.msht.minshengbao.androidShop.customerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class ShopRefreshHeader extends LinearLayout implements com.scwang.smartrefresh.layout.api.RefreshHeader {

    private View mHeadView;
    private ImageView mHeaderImageRe;
    private Animation rotate;

    public ShopRefreshHeader(Context context) {
        super(context);
        mHeadView = LayoutInflater.from(context).inflate(R.layout.item_refresh_header, this, true);
        mHeaderImageRe = (ImageView) mHeadView.findViewById(R.id.iv_header_re);
        rotate = AnimationUtils.loadAnimation(context, R.anim.anim_referesh_rotate);
    }


    @NonNull
    @Override
    public View getView() {
        return mHeadView;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mHeaderImageRe.startAnimation(rotate);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 500;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (newState == RefreshState.PullDownToRefresh) {
            mHeaderImageRe.clearAnimation();
        }
    }
}
