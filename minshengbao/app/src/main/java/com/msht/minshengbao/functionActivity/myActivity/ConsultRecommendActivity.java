package com.msht.minshengbao.functionActivity.myActivity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ViewPagerAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/3/2  
 */
public class ConsultRecommendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_recommend);
        context=this;
        setCommonHeader("咨询建议");
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }

}
