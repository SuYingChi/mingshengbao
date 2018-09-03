package com.msht.minshengbao.functionActivity.GasService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.ViewAdapter;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;


/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/20  
 */
public class GasPayFeeActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private TextView  tvPrice;
    private String    userId;
    private String    password;
    private Context   mContext;
    private String navigate="气价说明";
    public static final String FINISH_ACTIVITY = "ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_payfee);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
        initEvent();
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(this);
        IntentFilter filter=new IntentFilter();
        filter.addAction(FINISH_ACTIVITY);
        broadcastManager. registerReceiver(broadcastReceiver, filter);
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String info=intent.getStringExtra("broadcast");
            if (info.equals(VariableUtil.VALUE_FOUR)){
                finish();
            }
        }
    };
    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager =(ViewPager)findViewById(R.id.id_viewPager_fees);
        findViewById(R.id.id_goback).setOnClickListener(this);
        tvPrice =(TextView)findViewById(R.id.id_tv_rightText);
        TextView tvNavigation=(TextView)findViewById(R.id.tv_navigation) ;
        tvNavigation.setText(R.string.gas_payfee);
        tvPrice.setVisibility(View.VISIBLE);
        tvPrice.setText(navigate);
    }
    private void initEvent() {
        tvPrice.setOnClickListener(this);
        mViewPager.setAdapter(new ViewAdapter(getSupportFragmentManager(), getApplicationContext(),userId,password));
        indicator.setViewPager(mViewPager,0);
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
        Intent price=new Intent(mContext,HtmlPageActivity.class);
        price.putExtra("navigate",navigate);
        price.putExtra("url",url);
        startActivity(price);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        //ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
