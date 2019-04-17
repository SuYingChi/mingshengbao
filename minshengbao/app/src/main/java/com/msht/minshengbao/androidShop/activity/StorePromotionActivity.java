package com.msht.minshengbao.androidShop.activity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.PromotionActivityAdapter;
import com.msht.minshengbao.androidShop.adapter.StoreRecGoodAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.XScrollView;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.PromotionActivityGoodBean;
import com.msht.minshengbao.androidShop.shopBean.PromotionBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStorePromotiondDetailView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StorePromotionActivity extends ShopBaseActivity implements IStorePromotiondDetailView, OnRefreshListener {
    private String type;
    private String id;
    private String storeId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sv)
    XScrollView xScrollView;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.minute)
    TextView minute;
    @BindView(R.id.hour)
    TextView hour;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.name)
    TextView name;
    String titleKey;
    @BindView(R.id.rule)
    TextView rule;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    List<PromotionActivityGoodBean> datalist = new ArrayList<PromotionActivityGoodBean>();
    private PromotionActivityAdapter adapter;
    private CountDownTimer timer;

    @Override
    protected void setLayout() {
        setContentView(R.layout.store_promotion_activity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        storeId = getIntent().getStringExtra("storeId");
        titleKey = getIntent().getStringExtra("key");
        ViewGroup.LayoutParams bannerParams = iv.getLayoutParams();
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) (actionbarSizeTypedArray.getDimension(0, 0));
        final int bannerHeight = bannerParams.height - toolbarHeight;
        Log.e("scrollChanged", "bannerParams.height=" + DimenUtil.px2dip(bannerParams.height) + "titleBarParams.height=" + toolbarHeight + "ImmersionBar.getStatusBarHeight(getActivity()=" + ImmersionBar.getStatusBarHeight(this));
        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                , ContextCompat.getColor(this, R.color.colorPrimary), 0));
        xScrollView.setXScrollViewListener(new XScrollView.XScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                Log.e("scrollChanged", "shop  y=  " + y + "oldy=  " + oldy);
                if (y < 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), 1));
                } else if (y <= bannerHeight) {
                    float alpha = (float) y / bannerHeight;
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), alpha));
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(StorePromotionActivity.this, R.color.colorPrimary), 1));
                }
            }

            @Override
            public void onScrollOverTop() {
                //   mToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollNormal() {
                mToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollOverBottom() {

            }
        });
        refreshLayout.setOnRefreshListener(this);
        adapter = new PromotionActivityAdapter(this, R.layout.item_store_rec_good, datalist);
        GridLayoutManager recglm = new GridLayoutManager(this, 2);
        recglm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(recglm);
        rcl.setAdapter(adapter);
        rcl.setNestedScrollingEnabled(false);
        ShopPresenter.getStorePromotionDetail(this);
    }

    @Override
    public String getPromotionType() {
        return type;
    }

    @Override
    public String getPromotionId() {
        return id;
    }

    @Override
    public void onGetStorePromotionDetailSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            GlideUtil.loadByImageView(this, iv, datas.optString("promotion_banner"));
            long leftTime = datas.optLong("promotion_end_time_left");
            name.setText(datas.optString(titleKey + "_name"));
            if (timer != null) {
                timer.cancel();
            }
            timer = new CountDownTimer(leftTime*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                    day.setText(list.get(0));
                    hour.setText(list.get(1));
                    minute.setText(list.get(2));
                    second.setText(list.get(3));
                }

                @Override
                public void onFinish() {

                }
            }.start();
            JSONArray promotion_goods_list = datas.optJSONArray("promotion_goods_list");
            datalist.clear();
            for (int i = 0; i < promotion_goods_list.length(); i++) {
                JSONObject objj = promotion_goods_list.optJSONObject(i);
                PromotionActivityGoodBean bean = new PromotionActivityGoodBean();
                bean.setGoodId(objj.optString("goods_id"));
                bean.setGoodName(objj.optString("goods_name"));
                bean.setGoodPromotionPrice(objj.optString(titleKey + "_price"));
                bean.setGoodImage(objj.optString("goods_image"));
                datalist.add(bean);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getStorePromotionDetail(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
