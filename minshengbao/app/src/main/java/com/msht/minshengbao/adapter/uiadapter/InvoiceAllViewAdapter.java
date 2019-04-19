package com.msht.minshengbao.adapter.uiadapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.CounterFrag;
import com.msht.minshengbao.functionActivity.fragment.GasInvoiceFragment;
import com.msht.minshengbao.functionActivity.fragment.GasNeedKnowFrag;
import com.msht.minshengbao.functionActivity.fragment.RepairInvoiceFragment;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/4/2  
 */
public class InvoiceAllViewAdapter extends FragmentPagerAdapter {
    private String[] fragments;
    public InvoiceAllViewAdapter(FragmentManager supportFragmentManager, Context applicationContext, String[] fragment) {
        super(supportFragmentManager);
        this.fragments=fragment;
    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return  new GasInvoiceFragment();
            case 1:
                return new RepairInvoiceFragment();
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
