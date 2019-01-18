package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.msht.minshengbao.androidShop.adapter.ShopCellectionAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopCellectionBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopCollectionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import com.msht.minshengbao.androidShop.viewInterface.IShopDeleteCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

public class ShopCollectionActivity extends ShopBaseActivity implements ShopCellectionAdapter.DeleteItemListner, IShopCollectionView , OnRefreshListener, OnLoadMoreListener, IShopDeleteCollectionView {
    @BindView(R.id.back)
    ImageView back;
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
    private ShopCellectionAdapter adapter;
    private List<ShopCellectionBean.DatasBean.FavoritesListBean> datalist=new ArrayList<ShopCellectionBean.DatasBean.FavoritesListBean>();
    private int pageNum =1;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_collection_activity);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true);
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ShopCellectionAdapter(this,this);
        adapter.setDatas(datalist);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        ShopPresenter.getCollectList(this);
    }


    @Override
    public void deleteItem(int position) {
        ShopPresenter.deleteCollect(this,datalist.get(position).getFav_id(),position);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ShopGoodDetailActivity.class);
        intent.putExtra("goodsid", datalist.get(position).getGoods_id());
        startActivity(intent);
    }

    @Override
    public String getCurpage() {
        return pageNum +"";
    }

    @Override
    public void onGetCollectSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        ShopCellectionBean bean = JsonUtil.toBean(s, ShopCellectionBean.class);
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
        datalist.addAll(bean.getDatas().getFavorites_list());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getCollectList(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        ShopPresenter.getCollectList(this);
    }
    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }


    @Override
    public void onDeleteCollectSuccess(String s, final int position) {
        PopUtil.showAutoDissHookDialog(this, "删除收藏成功", 100, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                datalist.remove(position);
                adapter.notifyDataSetChanged();
                if(datalist.size()==0){
                    ivNoData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
