package com.msht.minshengbao.FunctionActivity.LPGActivity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.msht.minshengbao.Adapter.LplOrderViewAdapter;
import com.msht.minshengbao.Adapter.PublicViewAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.umeng.analytics.MobclickAgent;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/11 
 */
public class LpgMyOrderListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_my_order_list);
        context=this;
        setCommonHeader("我的订单");
        initFindViewId();
    }
    private void initFindViewId() {
        String[] fragment=new String[]{"全部","待发货","已发货","待付款","已完成"};
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        ViewPager viewPager=(ViewPager)findViewById(R.id.id_viewPager_order);
        viewPager.setAdapter(new LplOrderViewAdapter(getSupportFragmentManager(), getApplicationContext(),fragment));
        indicator.setViewPager(viewPager,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
        //ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
         MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);
    }
}
