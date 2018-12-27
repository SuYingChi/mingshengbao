package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.WarnListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.WarnBean;
import com.msht.minshengbao.androidShop.viewInterface.IWarnListView;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WarnActivity extends ShopBaseActivity implements OnRefreshLoadMoreListener, OnRefreshListener, IWarnListView {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.back)
    ImageView ivback;
    private List<WarnBean.DataBean> dataList=new ArrayList<WarnBean.DataBean>();
    private WarnListAdapter adapter;
    private int page=1;

    @Override
    protected void setLayout() {
        setContentView(R.layout.warn_list);
    }

    @Override
    protected void initImmersionBar() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new WarnListAdapter(this);
        adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(WarnActivity.this,WarnMessageDetail.class);
                intent.putExtra("data",dataList.get(position).getContent());
                startActivity(intent);
            }
        });
        adapter.setDatas(dataList);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page=1;
        ShopPresenter.getWarnMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        ShopPresenter.getWarnMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""));

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        ShopPresenter.getWarnMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""));

    }

    @Override
    public String getPage() {
        return page+"";
    }

    @Override
    public void onGetWarnListSuccess(WarnBean bean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
            if (page == 1) {
                dataList.clear();
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();
            }  else {
                if (bean.getData().size() != 0) {
                    dataList.addAll(bean.getData());
                    adapter.notifyDataSetChanged();
                }
            }
    }
}
