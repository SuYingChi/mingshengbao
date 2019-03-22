package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.WaterIncomeFra;
/**
 * Demo class
 *
 * @author hong
 * @date 2018/1/3
 */
public class PublicViewAdapter extends FragmentPagerAdapter {
    private String[] fragments;
    public PublicViewAdapter(FragmentManager supportFragmentManager, Context applicationContext, String[] fragment) {
        super(supportFragmentManager);
        this.fragments=fragment;
    }
    @Override
    public Fragment getItem(int position) {
        return WaterIncomeFra.getInstance(position);
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
