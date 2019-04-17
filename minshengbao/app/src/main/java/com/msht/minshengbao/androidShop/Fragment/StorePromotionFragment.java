package com.msht.minshengbao.androidShop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.StorePromotionActivity;
import com.msht.minshengbao.androidShop.adapter.StorePromotionAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.PromotionBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStorePromotionView;
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

public class StorePromotionFragment extends ShopBaseLazyFragment implements IStorePromotionView, OnRefreshListener, StorePromotionAdapter.SpaInterface {
    private String storeId;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv_no_data)
    ImageView imageView;
    @BindView(R.id.tv_no_data)
    TextView textView;
    private List<PromotionBean> promotionList = new ArrayList<PromotionBean>();
    private StorePromotionAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.store_promotion_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString("id");
    }

    @Override
    protected void initView() {
        refreshLayout.setOnRefreshListener(this);
        adapter = new StorePromotionAdapter(getContext(), promotionList);
        adapter.setSpaInterface(this);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        lm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(lm);
        rcl.setNestedScrollingEnabled(false);
        rcl.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
        ShopPresenter.getStorePromotion(this);
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getStorePromotion(this);
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    //1 mansong满送 2 xianshi 限时3 groupbuy团购 4 spike闪购 5 pintuan拼团
    @Override
    public void onGetStoreActivitySuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
        try {
            promotionList.clear();
            JSONObject obj = new JSONObject(s);
            JSONObject promotion = obj.optJSONObject("datas").optJSONObject("promotion");
            if (promotion != null) {
                JSONArray pList = promotion.optJSONArray("list");
                if(pList!=null&&pList.length()>0) {
                    textView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < pList.length(); i++) {
                        JSONObject jsonobj = pList.optJSONObject(i);
                        PromotionBean promotionBean = JsonUtil.toBean(jsonobj.toString(), PromotionBean.class);
                        switch (promotionBean.getPromotion_type()) {
                            case 1:
                                JSONObject mansong = promotion.optJSONObject("mansong");
                                promotionBean.setPromotion_title(mansong.optString("mansong_name"));
                                break;
                            case 2:
                                JSONObject xianshi = promotion.optJSONObject("xianshi");
                                promotionBean.setPromotion_title(xianshi.optString("xianshi_name"));
                                break;
                            case 3:
                                JSONObject groupbuy = promotion.optJSONObject("groupbuy");
                                promotionBean.setPromotion_title(groupbuy.optString("groupbuy_name"));
                                break;
                            case 4:
                                JSONObject spike = promotion.optJSONObject("spike");
                                promotionBean.setPromotion_title(spike.optString("spike_name"));
                                break;
                            case 5:
                                JSONObject pintuan = promotion.optJSONObject("pintuan");
                                promotionBean.setPromotion_title(pintuan.optString("pintuan_name"));
                                break;
                            default:
                                break;
                        }
                        promotionList.add(promotionBean);
                    }
                }else {
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            } else {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
            adapter.notifyChange();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStoreId() {
        return storeId;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cancelAllTimers();
    }

    @Override
    public void onClick(int promotion_type, String promotion_id) {
        Intent intent = new Intent( getActivity(), StorePromotionActivity.class);
        intent.putExtra("type",promotion_type+"");
        intent.putExtra("id",promotion_id);
        intent.putExtra("storeId",storeId);
        getActivity().startActivity(intent);
    }
}
