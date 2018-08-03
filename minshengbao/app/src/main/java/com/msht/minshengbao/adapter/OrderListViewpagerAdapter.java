package com.msht.minshengbao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.OrderListFragment;


/**
 * Created by hei on 2016/12/26.
 * 我的订单界面的viewpager适配器
 */

public class OrderListViewpagerAdapter extends FragmentPagerAdapter {
    public String[] Titles={"全部","未完成","已完成","退款返修"};
    public OrderListViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return OrderListFragment.getInstanse(position);
    }

    @Override
    public int getCount() {
        return Titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
