package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionView.fragmeht.ConsultFragment;
import com.msht.minshengbao.FunctionView.fragmeht.RecommendFragment;


/**
 * Created by hong on 2016/3/24.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private RecommendFragment mComlait;
    private ConsultFragment mConsult;
    private String fragments [] = {"咨询","建议"};
    public ViewPagerAdapter(FragmentManager fm, Context applicationContext) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                mConsult=new ConsultFragment();
                return mConsult;
            case 1:
                mComlait=new RecommendFragment();
                return mComlait;
            default:
                return null;
        }
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
