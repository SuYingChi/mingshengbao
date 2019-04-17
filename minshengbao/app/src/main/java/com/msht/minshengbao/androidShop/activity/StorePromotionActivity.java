package com.msht.minshengbao.androidShop.activity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.XScrollView;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStorePromotiondDetailView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

public class StorePromotionActivity extends ShopBaseActivity implements IStorePromotiondDetailView, OnRefreshListener {
    private String type;
    private String id;
    private String storeId;
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.sv)
   XScrollView xScrollView;
   @BindView(R.id.iv)
    ImageView iv;
   @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Override
    protected void setLayout() {
     setContentView(R.layout.store_promotion_activity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        storeId = getIntent().getStringExtra("storeId");
        ViewGroup.LayoutParams bannerParams = iv.getLayoutParams();
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) (actionbarSizeTypedArray.getDimension(0, 0));
        final int bannerHeight = bannerParams.height -toolbarHeight;
        Log.e("scrollChanged", "bannerParams.height=" + DimenUtil.px2dip(bannerParams.height) + "titleBarParams.height=" + toolbarHeight + "ImmersionBar.getStatusBarHeight(getActivity()=" + ImmersionBar.getStatusBarHeight(this));
        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                , ContextCompat.getColor(this, R.color.colorPrimary), 0));
        xScrollView.setXScrollViewListener(new XScrollView.XScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                Log.e("scrollChanged", "shop  y=  " + y + "oldy=  " + oldy);
                if (y < 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), 1));
                } else if (y <= bannerHeight) {
                    float alpha = (float) y / bannerHeight;
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), alpha));
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), 1));
                }
            }

            @Override
            public void onScrollOverTop() {
                //   mToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollNormal() {
                mToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollOverBottom() {

            }
        });
        refreshLayout.setOnRefreshListener(this);
        ShopPresenter.getStorePromotionDetail(this);
    }

    @Override
    public String getPromotionType() {
        return type;
    }

    @Override
    public String getPromotionId() {
        return id;
    }

    @Override
    public void onGetStorePromotionDetailSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getStorePromotionDetail(this);
    }
}
