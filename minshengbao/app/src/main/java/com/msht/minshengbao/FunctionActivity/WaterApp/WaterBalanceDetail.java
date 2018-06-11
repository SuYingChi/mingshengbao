package com.msht.minshengbao.FunctionActivity.WaterApp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.PublicViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class WaterBalanceDetail extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mviewPager;
    private TextView tv_naviga;
    private String    userId;
    private String    password;
    private String mPageName="余额明细";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_balance_detail);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
    }
    private void initView() {
        tv_naviga=(TextView)findViewById(R.id.tv_navigation) ;
        tv_naviga.setText(mPageName);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mviewPager=(ViewPager)findViewById(R.id.id_viewPager_fees);
        mviewPager.setAdapter(new PublicViewAdapter(getSupportFragmentManager(), getApplicationContext(),userId,password));
        indicator.setViewPager(mviewPager,0);
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
        //MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
        //ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
}
