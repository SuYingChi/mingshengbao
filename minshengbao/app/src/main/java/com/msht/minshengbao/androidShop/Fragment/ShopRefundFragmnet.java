package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;
import com.msht.minshengbao.androidShop.event.RefundType;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopRefundFragmnet extends ShopBaseLazyFragment  {
    private int refundType;
    @BindView(R.id.refund_money)
    Button btn0;
    @BindView(R.id.refund_good)
    Button btn1;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView back;
    private RefundMoneyFragment f0;
    private RefundGoodFragment f1;
    private List<ShopBaseLazyFragment> list=new ArrayList<ShopBaseLazyFragment>();
    private int tabPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            tabPosition = bundle.getInt("tab", 0);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.shop_refund_fragment;
    }
    @Override
    protected void initView() {
        super.initView();
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(getContext()),0,0);
        f0 = new RefundMoneyFragment();
        f1 = new RefundGoodFragment();
        list.add(f0);
        list.add(f1);
        initTopTab(tabPosition);
        vp.setAdapter(new BaseLazyFragmentPagerAdapter(getChildFragmentManager(), list));
        vp.setCurrentItem(tabPosition);
        vp.setOffscreenPageLimit(2);
        vp.setNoScroll(true);
        vp.setPageTransformer(true, null);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }
    @OnClick({R.id.refund_money, R.id.refund_good})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.refund_money:
                if (refundType != 0) {
                    initTopTab(0);
                    vp.setCurrentItem(0);
                }
                break;
            case R.id.refund_good:
                if (refundType != 1) {
                    initTopTab(1);
                    vp.setCurrentItem(1);
                }
                break;

            default:
                break;
        }
    }
    private void initTopTab(int item) {
        refundType = item;
        EventBus.getDefault().postSticky(new RefundType(refundType));
        if (item == 0) {
            btn0.setBackgroundResource(R.drawable.btn_left_corner_bg);
            btn1.setBackgroundResource(R.drawable.btn_right_corner_unselect_bg);

            btn0.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            btn1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        }
        if (item == 1) {
            btn0.setBackgroundResource(R.drawable.btn_left_corner_unselect_bg);
            btn1.setBackgroundResource(R.drawable.btn_right_corner_bg);

            btn0.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            btn1.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }

    }

    public void refreshCurrentTab(int indexChilde,boolean isRestart) {

        if (refundType != indexChilde) {
            initTopTab(indexChilde);
            vp.setCurrentItem(indexChilde);
        }else {
            if(indexChilde==0){
                f0.refresh(isRestart);
            }else {
                f1.refresh(isRestart);
            }
        }
    }
}
