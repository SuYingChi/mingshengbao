package com.msht.minshengbao.functionActivity.repairService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.adapter.MyWorkOrderAdapter;
import com.msht.minshengbao.adapter.OrderListViewpagerAdapter;
import com.msht.minshengbao.adapter.RepairOrderListAdapter;
import com.msht.minshengbao.functionActivity.fragment.OrderListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

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
