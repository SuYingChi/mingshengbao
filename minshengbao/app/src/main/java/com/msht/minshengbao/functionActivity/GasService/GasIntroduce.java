package com.msht.minshengbao.functionActivity.GasService;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ViewPagerGasIntroduce;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/18 
 */
public class GasIntroduce extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private TextView  tvNavigation;
    private Context   mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_introduce);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        initView();
        initEvent();
    }
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tvNavigation =(TextView)findViewById(R.id.tv_navigation) ;
        tvNavigation.setText(R.string.gas_introduce);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }
    private void initEvent() {
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.setAdapter(new ViewPagerGasIntroduce(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
       // ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
