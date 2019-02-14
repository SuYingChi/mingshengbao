package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.MsgUserListAdapter;
import com.msht.minshengbao.androidShop.adapter.SiteListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.SiteBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.ISiteListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopSelectSiteActivity extends ShopBaseActivity implements ISiteListView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.site_area)
    TextView tvSiteArea;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private List<SiteBean.DatasBean.AddrListBean> dataList= new ArrayList<SiteBean.DatasBean.AddrListBean>();
    private SiteListAdapter adapter;

    @Override
    protected void setLayout() {
      setContentView(R.layout.shop_selecte_site);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SiteListAdapter(this, R.layout.item_shop_site,dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new HaveHeadRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SiteBean.DatasBean.AddrListBean siteBean = dataList.get(position);
                Intent intent = new Intent();
                intent.putExtra("site", siteBean);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rcl.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ShopPresenter.getSiteList(this);
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true);
        ImmersionBar.setTitleBar(this, mToolbar);
    }
    @Override
    public void onGetSiteListSuccess(String s) {
        dataList.addAll(JsonUtil.toBean(s,SiteBean.class).getDatas().getAddr_list());
        adapter.notifyDataSetChanged();
    }
}
