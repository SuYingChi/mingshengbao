package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionActivity.fragment.LpgOrderListFragment;
import com.msht.minshengbao.FunctionActivity.fragment.WaterIncomeFra;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/11  
 */
public class LplOrderViewAdapter extends FragmentPagerAdapter {
    private String[] fragments;
    public LplOrderViewAdapter(FragmentManager fm, Context applicationContext, String[] fragment) {
        super(fm);
        this.fragments=fragment;
    }
    @Override
    public Fragment getItem(int position) {
        return LpgOrderListFragment.getInstance(position);
    }
    @Override
    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}
