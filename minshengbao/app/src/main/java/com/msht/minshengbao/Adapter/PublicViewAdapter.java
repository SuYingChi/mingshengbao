package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionActivity.fragment.WaterIncomeFra;
/**
 * Demo class
 *
 * @author hong
 * @date 2018/1/3
 */
public class PublicViewAdapter extends FragmentPagerAdapter {
    private String fragments[] = {"消费明细","充值明细"};
    private WaterIncomeFra waterIncomeFra;
    public PublicViewAdapter(FragmentManager fm, Context applicationContext, String userId, String passwords) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return WaterIncomeFra.getinstance(position);
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
