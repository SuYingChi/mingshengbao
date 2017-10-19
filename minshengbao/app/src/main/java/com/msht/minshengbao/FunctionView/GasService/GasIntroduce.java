package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.ViewPagerGasIntroduce;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;


public class GasIntroduce extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private TextView  tv_naviga;
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
        tv_naviga=(TextView)findViewById(R.id.tv_navigation) ;
        tv_naviga.setText(R.string.gas_introduce);
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
