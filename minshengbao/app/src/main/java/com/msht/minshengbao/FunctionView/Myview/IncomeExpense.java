package com.msht.minshengbao.FunctionView.Myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.InExpViewPageAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class IncomeExpense extends AppCompatActivity  {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        findViews();
        initView();
    }
    private void findViews() {
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_navigation)).setText("收支明细") ;
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }
    private void initView() {
        mViewPager.setAdapter(new InExpViewPageAdapter(getSupportFragmentManager(), getApplicationContext()));
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
