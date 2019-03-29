package com.msht.minshengbao.functionActivity.waterApp;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.adapter.uiadapter.RedPacketViewAdapter;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/3/10  
 */
public class RedPacketCardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_card);
        context=this;
        setCommonHeader("红包卡券");
        String[] fragment=new String[]{"待使用","已使用","已失效"};
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        indicator.setColorHigh(ContextCompat.getColor(context,R.color.blue_center));
        indicator.setPaintColorHigh(ContextCompat.getColor(context,R.color.blue_center));
        indicator.setColorDivide(ContextCompat.getColor(context,R.color.white));
        ViewPager viewPager=(ViewPager)findViewById(R.id.id_viewPager_fees);
        viewPager.setAdapter(new RedPacketViewAdapter(getSupportFragmentManager(), getApplicationContext(),fragment));
        indicator.setViewPager(viewPager,0);
    }
}
