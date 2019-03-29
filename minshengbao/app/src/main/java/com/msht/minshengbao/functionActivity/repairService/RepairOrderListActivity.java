package com.msht.minshengbao.functionActivity.repairService;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.adapter.OrderListViewpagerAdapter;
import com.msht.minshengbao.adapter.RepairOrderListAdapter;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RepairOrderListActivity extends BaseActivity {
    /*private XRecyclerView mRecyclerView;
    private View layoutNoData;*/
    private int status=0;
    private int pageIndex=0;
    private String  userId,password;
    private int refreshType=0;
    private RepairOrderListAdapter mAdapter;
    private CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order_list);
        context=this;
        setCommonHeader("维修订单");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
            status=data.getIntExtra("status",0);
        }else {
            status=0;
        }
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        ViewPager mViewpager = (ViewPager)findViewById(R.id.viewpager);
        ViewPagerIndicator mIndicator = (ViewPagerIndicator)findViewById(R.id.indicator);
        mViewpager.setAdapter(new OrderListViewpagerAdapter(getSupportFragmentManager()));
        mIndicator.setViewPager(mViewpager,status);
    }
}
