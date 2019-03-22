package com.msht.minshengbao.adapter.uiadapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.WaterIncomeFra;
import com.msht.minshengbao.functionActivity.fragment.WaterRedPacketFragment;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/3/10  
 */
public class RedPacketViewAdapter extends FragmentPagerAdapter {

    private String[] fragments;
    public RedPacketViewAdapter(FragmentManager supportFragmentManager, Context applicationContext, String[] fragment) {
        super(supportFragmentManager);
        this.fragments=fragment;
    }
    @Override
    public Fragment getItem(int i) {
        return WaterRedPacketFragment.getInstance(i);
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
