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
import com.msht.minshengbao.androidShop.activity.RefundMoneyActivity;
import com.msht.minshengbao.androidShop.adapter.ShopRefundMoneyListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.RefunBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IRefundMoneyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefundMoneyFragment extends ShopBaseLazyFragment implements OnRefreshListener, OnLoadMoreListener, ShopRefundMoneyListAdapter.ShopRefundMoneyListener, IRefundMoneyView {


    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv_no_data)
    ImageView ivNoOrder;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private ShopRefundMoneyListAdapter adapter;
    private List<RefunBean.DatasBean.RefundListBean> ordersList = new ArrayList<RefunBean.DatasBean.RefundListBean>();
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
        adapter = new ShopRefundMoneyListAdapter(getContext());
        adapter.setDatas(ordersList);
        adapter.setlistener(this);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        if (!getKey().equals("")) {
            ShopPresenter.getRefundMoneyList(this);
        }
    }


    @Override
    public void onGoDetail(int position) {
        String refundId = ordersList.get(position).getRefund_id();
        Intent intent = new Intent(getActivity(), RefundMoneyActivity.class);
        intent.putExtra("data", refundId);
        startActivity(intent);
    }

    @Override
    public String getCurpage() {
        return pageNum + "";
    }

    @Override
    public void onGetRefundMoneyListSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        RefunBean bean = JsonUtil.toBean(s, RefunBean.class);
        int pageTotal = bean.getPage_total();
        if (pageTotal == 0) {
            ordersList.clear();
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
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
            ordersList.addAll(bean.getDatas().getRefund_list());
            adapter.notifyDataSetChanged();
        }else {
            if (pageNum == 1) {
                ordersList.clear();
                ordersList.addAll(bean.getDatas().getRefund_list());
                if(pageNum<lastPageNum) {
                    pageNum++;
                    ShopPresenter.getRefundMoneyList(this);
                }else {
                    adapter.notifyDataSetChanged();
                    isRestart = false;
                }
            } else if (pageNum < lastPageNum && bean.getDatas().getRefund_list().size() != 0) {
                ordersList.addAll(bean.getDatas().getRefund_list());
                pageNum++;
                ShopPresenter.getRefundMoneyList(this);
            } else if (pageNum == lastPageNum &&bean.getDatas().getRefund_list().size() != 0) {
                ordersList.addAll(bean.getDatas().getRefund_list());
                adapter.notifyDataSetChanged();
                isRestart = false;
            }
        }
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
                ShopPresenter.getRefundMoneyList(this);
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getRefundMoneyList(this);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        if (!getKey().equals("")) {
            ShopPresenter.getRefundMoneyList(this);
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        ;
    }

    public void refresh(boolean isRestart) {
        this.isRestart = isRestart;
        lastPageNum = pageNum;
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getRefundMoneyList(this);
        }
    }
}
