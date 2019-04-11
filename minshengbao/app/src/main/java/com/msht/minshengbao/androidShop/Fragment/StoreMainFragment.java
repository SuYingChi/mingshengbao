package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodFmVoucherAdpter;
import com.msht.minshengbao.androidShop.adapter.HorizontalVoucherAdpter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IStoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StoreMainFragment extends ShopBaseLazyFragment implements IStoreView, OnRefreshListener {

    private String storeId;
    @BindView(R.id.ll_voucher)
    LinearLayout llvoucher;
    @BindView(R.id.rcl_voucher)
    RecyclerView rcl;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    List<VoucherBean> voucherList=new ArrayList<VoucherBean>();
    private GoodFmVoucherAdpter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.store_main_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString("id");
    }

    @Override
    protected void initView() {
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        ShopPresenter.getStoreInfo(this);
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void onGetStoreInfoSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray voucherArray = datas.optJSONArray("store_voucher_list");
            if(voucherArray.length()>1){
                llvoucher.setVisibility(View.VISIBLE);
                voucherList.clear();
                for (int i = 0; i < voucherArray.length(); i++) {
                    voucherList.add(JsonUtil.toBean(voucherArray.optJSONObject(i).toString(), VoucherBean.class));
                }
                adapter = new GoodFmVoucherAdpter(getContext(), R.layout.item_store_voucher, voucherList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                rcl.setLayoutManager(linearLayoutManager);
                rcl.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                llvoucher.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VerticalOffset messageEvent) {
        if(messageEvent.verticalOffset==0){
            refreshLayout.setEnableRefresh(true);
        }else{
            refreshLayout.setEnableRefresh(false);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getStoreInfo(this);
    }
}
