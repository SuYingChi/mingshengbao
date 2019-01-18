package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class NoLoginCarActivity extends ShopBaseActivity {

    @BindView(R.id.login)
    TextView tvLogin;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.return_home)
    TextView tvReturnHome;
    @BindView(R.id.back)
    ImageView back;


    @Override
    protected void setLayout() {
        setContentView(R.layout.no_login_car_activity);
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true);
        ImmersionBar.setTitleBar(this, mToolbar);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoLoginCarActivity.this, LoginActivity.class));
            }
        });
        tvReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(NoLoginCarActivity.this, MainActivity.class);
                intent.putExtra("index",1);
                startActivity(intent);
                finish();
            }
        });
    }
}
