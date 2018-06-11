package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.ViewPagerAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class ConsultRecommend extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_recommend);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        initView();
        initEvent();
    }
    private void initView() {
        String mPageName = "咨询建议";
        ((TextView)findViewById(R.id.tv_navigation)).setText(mPageName);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }
    private void initEvent() {
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
      //  ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
