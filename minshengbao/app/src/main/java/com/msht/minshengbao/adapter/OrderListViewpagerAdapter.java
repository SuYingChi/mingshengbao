package com.msht.minshengbao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.OrderListFragment;


/**
 *
 * @author hei
 * @date 2016/12/26
 * 我的订单界面的viewpager适配器
 */

public class OrderListViewpagerAdapter extends FragmentPagerAdapter {
    public String[] mTitles={"全部","未完成","已完成","待评价","退款返修"};
    public OrderListViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return OrderListFragment.getInstanse(position);
    }
    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
