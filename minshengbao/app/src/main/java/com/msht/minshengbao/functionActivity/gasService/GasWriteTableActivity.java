package com.msht.minshengbao.functionActivity.gasService;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ViewPageWriteTable;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/26  
 */
public class GasWriteTableActivity extends BaseActivity {
    private ViewPagerIndicator indicator;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_write_table);
        context=this;
        PushAgent.getInstance(context).onAppStart();
        setCommonHeader("自助抄表");
        initView();
        initEvent();
    }
    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager =(ViewPager)findViewById(R.id.id_viewPager_record);
    }
    private void initEvent() {
        mViewPager.setAdapter(new ViewPageWriteTable(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(mViewPager,0);
    }
}
