package com.msht.minshengbao.FunctionActivity.GasService;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.IccardViewAdapter;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

public class GasIccard extends AppCompatActivity implements View.OnClickListener {
    private ViewPagerIndicator indicator;
    private ViewPager mviewPager;
    private TextView  tv_price,tv_naviga;
    private String    userId;
    private String    password;
    private String navigate="气价说明";
    private String mPageName="IC卡缴费";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_iccard);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
        initEvent();
    }
    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mviewPager=(ViewPager)findViewById(R.id.id_viewPager_fees);
        findViewById(R.id.id_goback).setOnClickListener(this);
        tv_price=(TextView)findViewById(R.id.id_tv_rightText);
        tv_naviga=(TextView)findViewById(R.id.tv_navigation) ;
        tv_naviga.setText(mPageName);
        tv_price.setVisibility(View.VISIBLE);
        tv_price.setText(navigate);
    }
    private void initEvent() {
        tv_price.setOnClickListener(this);
        mviewPager.setAdapter(new IccardViewAdapter(getSupportFragmentManager(), getApplicationContext(),userId,password));
        indicator.setViewPager(mviewPager,0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_tv_rightText:
                gasprice();
                break;
            default:
                break;
        }
    }
    private void gasprice() {
        String url= UrlUtil.Gasprice_Url;
        Intent price=new Intent(mContext,HtmlPage.class);
        price.putExtra("navigate",navigate);
        price.putExtra("url",url);
        startActivity(price);
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
