package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.LpgDepositFragment;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/16 
 */
public class LpgDepositOrderViewAdapter extends FragmentPagerAdapter {
    private String[] fragments;
    public LpgDepositOrderViewAdapter(FragmentManager fm, Context applicationContext, String[] fragment) {
        super(fm);
        this.fragments=fragment;
    }
    @Override
    public Fragment getItem(int position) {
        return LpgDepositFragment.getInstance(position);
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
