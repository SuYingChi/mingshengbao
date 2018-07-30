package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionActivity.fragment.CounterFrag;
import com.msht.minshengbao.FunctionActivity.fragment.GasNeedKnowFrag;

/**
 * Created by hong on 2016/12/19.
 */
public class ViewPagerGasIntroduce extends FragmentPagerAdapter {
    private GasNeedKnowFrag mGasNeedKnow;
    private CounterFrag mCounter;
    private String fragments [] = {"柜台业务","燃气须知"};
    public ViewPagerGasIntroduce(FragmentManager supportFragment, Context applicationContext) {
        super(supportFragment);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                mCounter=new CounterFrag();
                return  mCounter;
            case 1:
                mGasNeedKnow=new GasNeedKnowFrag();
                return mGasNeedKnow;
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
