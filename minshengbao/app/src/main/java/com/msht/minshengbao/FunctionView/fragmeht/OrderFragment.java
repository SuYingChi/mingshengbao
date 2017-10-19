package com.msht.minshengbao.FunctionView.fragmeht;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.OrderListViewpagerAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private TextView  tv_naviga;
    private ViewPager viewpager;
    private ViewPagerIndicator indicator;
    private final String mPageName = "订单";
    public OrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        indicator = (ViewPagerIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new OrderListViewpagerAdapter(getChildFragmentManager()));
        indicator.setViewPager(viewpager,0);
        initView(view);
        return view;
    }
    private void initView(View view) {
        ((TextView)view.findViewById(R.id.tv_navigation)).setText("订单");
        view.findViewById(R.id.id_goback).setVisibility(View.GONE);
    }

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
