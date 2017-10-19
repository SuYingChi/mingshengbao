package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionView.fragmeht.CouponFragment;

/**
 * Created by hong on 2017/4/1.
 */

public class CouponFragmentAdapter  extends FragmentPagerAdapter {
    private String fragments[] = {"未使用","已使用","已过期"};
    public CouponFragmentAdapter(FragmentManager fm, Context applicationContext) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return CouponFragment.getinstance(position);
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
