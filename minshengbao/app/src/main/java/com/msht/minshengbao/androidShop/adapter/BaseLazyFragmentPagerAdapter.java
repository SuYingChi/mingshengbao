package com.msht.minshengbao.androidShop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;

import java.util.List;

public class BaseLazyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<ShopBaseLazyFragment> list_fragment;

    public BaseLazyFragmentPagerAdapter(FragmentManager supportFragmentManager, List<ShopBaseLazyFragment> list_fragment) {
        super(supportFragmentManager);
        this.list_fragment = list_fragment;
    }


    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }
}
