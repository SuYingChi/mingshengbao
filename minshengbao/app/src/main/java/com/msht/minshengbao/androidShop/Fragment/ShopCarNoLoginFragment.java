package com.msht.minshengbao.androidShop.Fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class ShopCarNoLoginFragment extends ShopBaseLazyFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login)
    TextView tvLogin;
    @BindView(R.id.return_home)
    TextView tvReturnHome;
    @Override
    protected int setLayoutId() {
        return R.layout.no_login_car;
    }
    @Override
    protected void initView() {
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(getContext()),0,0);
      tvLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
          }
      });
        tvReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GoShopMainEvent());
            }
        });
    }

}
