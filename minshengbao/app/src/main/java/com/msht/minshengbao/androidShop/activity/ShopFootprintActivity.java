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
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.ShopFootprintAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopFootPrintBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IClearShopFootprintView;
import com.msht.minshengbao.androidShop.viewInterface.IGetShopFootprintView;

import butterknife.BindView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import java.util.ArrayList;
import java.util.List;

public class ShopFootprintActivity extends ShopBaseActivity implements IGetShopFootprintView, OnRefreshListener, OnLoadMoreListener, ShopFootprintAdapter.FootprintListener, IClearShopFootprintView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private List<ShopFootPrintBean.DatasBean.GoodsbrowseListBean> datalist = new ArrayList<ShopFootPrintBean.DatasBean.GoodsbrowseListBean>();
    private ShopFootprintAdapter adapter;
    private int pageNum = 1;

    @Override
    protected void setLayout() {
        setContentView(R.layout.foot_print);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopPresenter.clearFootprintList(ShopFootprintActivity.this);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
       // rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ShopFootprintAdapter(this);
        adapter.setDatas(datalist);
        adapter.setListener(this);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        ShopPresenter.getFootprintList(this);
    }



    @Override
    public void onGetClearFootprintSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        ShopFootPrintBean bean = JsonUtil.toBean(s, ShopFootPrintBean.class);
        int pageTotal = bean.getPage_total();
        if (pageTotal == 0) {
            datalist.clear();
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            adapter.notifyDataSetChanged();
            ivNoData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            return;
        } else if (pageNum > pageTotal) {
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.setNoMoreData(true);
            return;
        } else if (pageNum == 1) {
            datalist.clear();
        }
        ivNoData.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setNoMoreData(false);
        datalist.addAll(bean.getDatas().getGoodsbrowse_list());
        adapter.notifyDataSetChanged();
    }

    @Override
    public String getCurpage() {
        return pageNum+"";
    }

    @Override
    public void onClearFootprintSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this,"清空浏览记录",100);
        datalist.clear();
        adapter.notifyDataSetChanged();
        ivNoData.setVisibility(View.VISIBLE);
        ivNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getFootprintList(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        ShopPresenter.getFootprintList(this);
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onCLick(int item, int childPosition) {
        Intent intent = new Intent(this, ShopGoodDetailActivity.class);
        intent.putExtra("goodsid", datalist.get(item).getGoods_list().get(childPosition).getGoods_id());
        startActivity(intent);
    }


}
