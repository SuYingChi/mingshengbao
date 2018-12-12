package com.msht.minshengbao.functionActivity.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseHomeFragment;
import com.msht.minshengbao.adapter.OrderListViewpagerAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class OrderFragment extends BaseHomeFragment {
    private final String mPageName = "订单";
    public OrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        ViewPager mViewpager = (ViewPager) view.findViewById(R.id.viewpager);
        ViewPagerIndicator mIndicator = (ViewPagerIndicator) view.findViewById(R.id.indicator);
        mViewpager.setAdapter(new OrderListViewpagerAdapter(getChildFragmentManager()));
        mIndicator.setViewPager(mViewpager,0);
        initView(view);
        return view;
    }
    @Override
    public View initFindView() {
        if(mRootView==null){
            mRootView= LayoutInflater.from(mContext).inflate(R.layout.fragment_order,null,false);
        }
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            mRootView.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        ViewPager mViewpager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        ViewPagerIndicator mIndicator = (ViewPagerIndicator) mRootView.findViewById(R.id.indicator);
        mViewpager.setAdapter(new OrderListViewpagerAdapter(getChildFragmentManager()));
        mIndicator.setViewPager(mViewpager,0);
        initView(mRootView);
        return null;
    }

    private void initView(View view) {
        ((TextView)view.findViewById(R.id.tv_navigation)).setText("订单");
        view.findViewById(R.id.id_goback).setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    };
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
