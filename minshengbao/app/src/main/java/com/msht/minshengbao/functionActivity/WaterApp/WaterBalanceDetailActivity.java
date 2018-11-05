package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.PublicViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBalanceDetailActivity extends AppCompatActivity {
    private String    mPageName="余额明细(水宝)";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_balance_detail);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        initView();
    }
    private void initView() {
        String[] fragment=new String[]{"消费明细","余额明细"};
        TextView tvNavigation=(TextView)findViewById(R.id.tv_navigation) ;
        tvNavigation.setText(mPageName);
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        ViewPager viewPager=(ViewPager)findViewById(R.id.id_viewPager_fees);
        viewPager.setAdapter(new PublicViewAdapter(getSupportFragmentManager(), getApplicationContext(),fragment));
        indicator.setViewPager(viewPager,0);
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
        //ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
         MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
}
