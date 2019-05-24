package com.msht.minshengbao.functionActivity.gasService;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ViewPagerGasIntroduce;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.ViewPagerIndicator;
import com.msht.minshengbao.base.BaseActivity;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/18 
 */
public class GasIntroduceActivity extends BaseActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_introduce);
        context=this;
        if (getIntent()!=null){
           mPageName=getIntent().getStringExtra("name");
           if (TextUtils.isEmpty(mPageName)){
               mPageName="燃气介绍";
           }
        }else {
           mPageName="燃气介绍";
        }
        setCommonHeader(mPageName);
        initView();
        initEvent();
    }
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        TextView tvNavigation =(TextView)findViewById(R.id.tv_navigation) ;
        tvNavigation.setText(R.string.gas_introduce);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }
    private void initEvent() {
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.setAdapter(new ViewPagerGasIntroduce(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
}
