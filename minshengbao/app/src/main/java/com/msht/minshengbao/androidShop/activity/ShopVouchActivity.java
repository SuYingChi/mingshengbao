package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.SiteListAdapter;
import com.msht.minshengbao.androidShop.adapter.VoucherAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.SiteBean;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetVoucherCenterView;
import com.msht.minshengbao.androidShop.viewInterface.IGetVoucherView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopVouchActivity extends ShopBaseActivity implements VoucherAdapter.VoucherInterface, IGetVoucherCenterView, IGetVoucherView, OnRefreshListener {
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_no_data)
    ImageView iv_no_data;
    private List<VoucherBean> dataList=new ArrayList<VoucherBean>();
    private VoucherAdapter adapter;


    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_voucher_center);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this), 0, 0);
        adapter = new VoucherAdapter(this, dataList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcl.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        ShopPresenter.getVoucherCenter(this);
    }

    @Override
    public void onGetVoucher(String voucher_t_id) {
        ShopPresenter.getVoucher(this, voucher_t_id);
    }

    @Override
    public void onGetVoucherSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "成功领取代金券", 0);
    }

    @Override
    public void onClikshowDesc(int position) {
        if(dataList.get(position).isShowDesc()){
            dataList.get(position).setShowDesc(false);
        }else {
            dataList.get(position).setShowDesc(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetVoucherCenter(String s) {
        refreshLayout.finishRefresh();
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray voucher_list = obj.optJSONObject("datas").optJSONArray("voucher_list");
            dataList.clear();
            for(int i=0;i<voucher_list.length();i++){
                VoucherBean bean = JsonUtil.toBean(voucher_list.optJSONObject(i).toString(), VoucherBean.class);
                if (bean != null) {
                    bean.setShowDesc(false);
                    dataList.add(bean);
                }
            }
            adapter.notifyDataSetChanged();
            if(dataList.size()==0){
                iv_no_data.setVisibility(View.VISIBLE);
            }else {
                iv_no_data.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ShopPresenter.getVoucherCenter(this);
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
    }
}
