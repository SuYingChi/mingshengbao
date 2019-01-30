package com.msht.minshengbao.functionActivity.GasService;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ViewPageWriteTable;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/26  
 */
public class GasWriteTableActivity extends AppCompatActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_write_table);
        mContext=this;
        StatusBarCompat.setStatusBar(this);
        PushAgent.getInstance(mContext).onAppStart();
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        ((TextView)findViewById(R.id.tv_navigation)).setText("自助抄表") ;
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager =(ViewPager)findViewById(R.id.id_viewPager_record);
    }
    private void initEvent() {
        mViewPager.setAdapter(new ViewPageWriteTable(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
