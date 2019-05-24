package com.msht.minshengbao.functionActivity.myActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.msht.minshengbao.adapter.CouponFragmentAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class DiscountCouponActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
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
    @Override
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
