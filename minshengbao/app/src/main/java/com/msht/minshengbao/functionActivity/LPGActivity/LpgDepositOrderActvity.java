package com.msht.minshengbao.FunctionActivity.LPGActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.msht.minshengbao.Adapter.LpgDepositOrderViewAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;

public class LpgDepositOrderActvity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_deposit_order_actvity);
        context=this;
        setCommonHeader("退瓶订单");
        initFindViewId();
    }

    private void initFindViewId() {
        String[] fragment=new String[]{"全部","待验瓶","待评价"};
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        ViewPager viewPager=(ViewPager)findViewById(R.id.id_viewPager_order);
        viewPager.setAdapter(new LpgDepositOrderViewAdapter(getSupportFragmentManager(), getApplicationContext(),fragment));
        indicator.setViewPager(viewPager,0);
    }
}
