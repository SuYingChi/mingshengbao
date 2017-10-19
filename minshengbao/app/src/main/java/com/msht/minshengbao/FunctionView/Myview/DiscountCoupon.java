package com.msht.minshengbao.FunctionView.Myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.CouponFragmentAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class DiscountCoupon extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_coupon);
        mContext=this;
        findViews();
        init();
    }
    private void findViews(){
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_navigation)).setText("代金券");
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }
    private void init(){
        mViewPager.setAdapter(new CouponFragmentAdapter(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
    }
    @Override
    public void onPageScrollStateChanged(int state) {}
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
