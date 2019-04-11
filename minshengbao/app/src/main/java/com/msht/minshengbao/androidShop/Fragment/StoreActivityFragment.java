package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.util.LogUtils;

public class StoreActivityFragment extends ShopBaseLazyFragment{
    private String storeId;

    @Override
    protected int setLayoutId() {
        return R.layout.shop_activity_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId=getArguments().getString("id");
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.e("StoreActivityFragment initView");
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtils.e("StoreActivityFragment initData");
    }
}
