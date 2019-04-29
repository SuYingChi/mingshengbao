package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.StoreNewGoodAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.StoreNewGoodBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStoreGoodNewView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StoreNewGoodFragment extends ShopBaseLazyFragment implements IStoreGoodNewView,OnRefreshListener,OnLoadMoreListener {
    private String storeId;
    private int curpage=1;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private List<StoreNewGoodBean> goodlist=new ArrayList<StoreNewGoodBean>();
    private StoreNewGoodAdapter adapter;
    String goods_addtime_text="";

    @Override
    protected int setLayoutId() {
        return R.layout.store_new_good_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString("id");
    }

    @Override
    protected void initData() {
        super.initData();
        ShopPresenter.getStoreNewGood(this);
    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        adapter = new StoreNewGoodAdapter(getContext(),goodlist);
        adapter.setInterface(new StoreNewGoodAdapter.GoGoodDetail() {
            @Override
            public void goGoodDetail(String goodsid) {
                doShopItemViewClick("goods",goodsid);
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        lm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(lm);
        rcl.setNestedScrollingEnabled(false);
        rcl.setAdapter(adapter);
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getCurpage() {
        return curpage+"";
    }

    @Override
    public void onGetStoreNewGoodSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        try {
            JSONObject obj = new JSONObject(s);
            int pageTotal = obj.optInt("page_total");
            if (pageTotal == 0) {
                goodlist.clear();
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.setNoMoreData(true);
                ivNoData.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                return;
            } else if (curpage > pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
                return;
            } else if (curpage == 1) {
                goodlist.clear();
            }
            ivNoData.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.INVISIBLE);
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            JSONArray goodArray = obj.optJSONObject("datas").optJSONArray("goods_list");
            for(int i=0;i<goodArray.length();i++){
                if(DateUtils.secondFormatToDate(goodArray.optJSONObject(i).optString("goods_addtime")).equals(goods_addtime_text)){
                    StoreNewGoodBean storeNewBean = goodlist.get(goodlist.size()-1);
                    storeNewBean.getGoodList().add(JsonUtil.toBean(goodArray.optJSONObject(i).toString(),StoreNewGoodBean.GoodBean.class));
                }else {
                    goods_addtime_text =  DateUtils.secondFormatToDate(goodArray.optJSONObject(i).optString("goods_addtime"));
                    List<StoreNewGoodBean.GoodBean> childGoodList = new ArrayList<StoreNewGoodBean.GoodBean>();
                    childGoodList.add(JsonUtil.toBean(goodArray.optJSONObject(i).toString(),StoreNewGoodBean.GoodBean.class));
                    StoreNewGoodBean storeNewBean = new StoreNewGoodBean(goods_addtime_text, childGoodList);
                    goodlist.add(storeNewBean);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VerticalOffset messageEvent) {
        if (messageEvent.verticalOffset == 0) {
            refreshLayout.setEnableRefresh(true);
        } else {
            refreshLayout.setEnableRefresh(false);
        }
    }
    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curpage=1;
        goods_addtime_text="";
        ShopPresenter.getStoreNewGood(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        curpage++;
        ShopPresenter.getStoreNewGood(this);
    }
}
