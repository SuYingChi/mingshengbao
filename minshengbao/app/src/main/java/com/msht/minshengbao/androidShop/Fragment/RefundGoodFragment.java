package com.msht.minshengbao.androidShop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.activity.ReturnGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ReturnGoodSendGoodActivity;
import com.msht.minshengbao.androidShop.adapter.ShopRefundGoodListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.RefunGoodBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IRefundGoodView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefundGoodFragment extends ShopBaseLazyFragment implements OnRefreshListener, OnLoadMoreListener, ShopRefundGoodListAdapter.ShopRefundGoodListener, IRefundGoodView {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv_no_data)
    ImageView ivNoOrder;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private ShopRefundGoodListAdapter adapter;
    private List<RefunGoodBean.DatasBean.ReturnListBean> ordersList = new ArrayList<RefunGoodBean.DatasBean.ReturnListBean>();
    private int pageNum = 1;
    private boolean isViewCreated = false;
    private boolean isRestart = false;
    private int lastPageNum;

    @Override
    protected int setLayoutId() {
        return R.layout.refund_child;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
      //  rcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ShopRefundGoodListAdapter(getContext());
        adapter.setDatas(ordersList);
        adapter.setlistener(this);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        if (!getKey().equals("")) {
            ShopPresenter.getRefundGoodList(this);
        }
    }


    @Override
    public void onGoDetail(String refund) {

        Intent intent = new Intent(getActivity(), ReturnGoodDetailActivity.class);
        intent.putExtra("data", refund);
        startActivity(intent);
    }

    @Override
    public void onGoSendGood(String refund_id) {
        Intent intent = new Intent(getActivity(), ReturnGoodSendGoodActivity.class);
        intent.putExtra("data", refund_id);
        startActivity(intent);
    }

    @Override
    public String getCurpage() {
        return pageNum + "";
    }

    @Override
    public void onGetRetrunGoodListSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        RefunGoodBean bean = JsonUtil.toBean(s, RefunGoodBean.class);
        int pageTotal = bean.getPage_total();
        if (pageTotal == 0) {
            ordersList.clear();
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.setNoMoreData(true);
            adapter.notifyDataSetChanged();
            ivNoOrder.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        ivNoOrder.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        if (!isRestart) {
            if (pageNum > pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
                return;
            } else if (pageNum == 1) {
                ordersList.clear();
            }
            ivNoOrder.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.INVISIBLE);
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            ordersList.addAll(bean.getDatas().getReturn_list());
            adapter.notifyDataSetChanged();
        } else {
            if (pageNum == 1) {
                ordersList.clear();
                ordersList.addAll(bean.getDatas().getReturn_list());
                if(pageNum<lastPageNum) {
                    pageNum++;
                    ShopPresenter.getRefundGoodList(this);
                }else {
                    adapter.notifyDataSetChanged();
                    isRestart = false;
                }
            } else if (pageNum < lastPageNum && bean.getDatas().getReturn_list().size() != 0) {
                ordersList.addAll(bean.getDatas().getReturn_list());
                pageNum++;
                ShopPresenter.getRefundGoodList(this);
            } else if (pageNum == lastPageNum &&bean.getDatas().getReturn_list().size() != 0) {
                ordersList.addAll(bean.getDatas().getReturn_list());
                adapter.notifyDataSetChanged();
                isRestart = false;
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getRefundGoodList(this);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        if (!getKey().equals("")) {
            ShopPresenter.getRefundGoodList(this);
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (isViewCreated) {
            pageNum = 1;
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableAutoLoadMore(true);
            if (!getKey().equals("")) {
                ShopPresenter.getRefundGoodList(this);
            }
        }
    }

    public void refresh(boolean isRestart) {
        this.isRestart = isRestart;
        lastPageNum = pageNum;
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getRefundGoodList(this);
        }
    }
}
