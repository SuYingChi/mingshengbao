package com.msht.minshengbao.FunctionView.Myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.CouponFragmentAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class DiscountCoupon extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_coupon);
        context=this;
        setCommonHeader("代金券");
        findViews();
        init();
    }
    private void findViews(){
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
        MobclickAgent.onResume(context);
       // ZhugeSDK.getInstance().init(getApplicationContext());

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }

}
