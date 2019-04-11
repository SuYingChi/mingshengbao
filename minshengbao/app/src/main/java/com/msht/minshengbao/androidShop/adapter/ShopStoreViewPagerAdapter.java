package com.msht.minshengbao.androidShop.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.adapter.ViewPagerAdapter;
import com.msht.minshengbao.androidShop.Fragment.StoreActivityFragment;
import com.msht.minshengbao.androidShop.Fragment.StoreGoodFragment;
import com.msht.minshengbao.androidShop.Fragment.StoreMainFragment;
import com.msht.minshengbao.androidShop.Fragment.StoreNewGoodFragment;
import com.msht.minshengbao.androidShop.activity.ShopStoreMainActivity;
import com.msht.minshengbao.androidShop.util.LogUtils;

public class ShopStoreViewPagerAdapter extends FragmentPagerAdapter {
    private  Bundle bundle;
    private  String[] titles= new String[]{"首页","宝贝","新品","活动"};
    public ShopStoreViewPagerAdapter(FragmentManager fm,String storeId) {
        super(fm);
         bundle =new Bundle();
        bundle.putString("id",storeId);
    }

    @Override
    public Fragment getItem(int i) {
        LogUtils.e("ShopStoreViewPagerAdapter getItem");
        switch (i){
            case 0:
                StoreMainFragment storeMainFragment = new StoreMainFragment();
                storeMainFragment.setArguments(bundle);
                return storeMainFragment;
            case 1:
                StoreGoodFragment storeGoodFragment = new StoreGoodFragment();
                storeGoodFragment.setArguments(bundle);
                return storeGoodFragment;
            case 2:
                StoreNewGoodFragment storeNewGoodFragment = new StoreNewGoodFragment();
                storeNewGoodFragment.setArguments(bundle);
                return storeNewGoodFragment;
            case 3:
                StoreActivityFragment storeActivityFragment = new StoreActivityFragment();
                storeActivityFragment.setArguments(bundle);
                return storeActivityFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
