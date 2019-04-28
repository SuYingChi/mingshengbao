package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodFmVoucherAdpter;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.StoreGoodAdapter;
import com.msht.minshengbao.androidShop.adapter.StoreRecGoodAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.ImageCycleView;
import com.msht.minshengbao.androidShop.customerview.RecyclerItemDecoration;
import com.msht.minshengbao.androidShop.event.RefreshFinish;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.Imagebean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeAdvBean;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetVoucherView;
import com.msht.minshengbao.androidShop.viewInterface.IStoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StoreMainFragment extends ShopBaseLazyFragment implements IStoreView, OnRefreshListener, IGetVoucherView {

    private String storeId;
    @BindView(R.id.ll_voucher)
    LinearLayout llvoucher;
    @BindView(R.id.rcl_voucher)
    RecyclerView rcl;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.rcl_collect)
    RecyclerView rclCollect;
    @BindView(R.id.ll_sole)
    LinearLayout llSole;
    @BindView(R.id.rcl_sole)
    RecyclerView rclSole;
    @BindView(R.id.ll_rec)
    LinearLayout llrec;
    @BindView(R.id.rcl_rec)
    RecyclerView rclRec;
    @BindView(R.id.cycleView)
    ImageCycleView imageCycleView;
    List<VoucherBean> voucherList = new ArrayList<VoucherBean>();
    private GoodFmVoucherAdpter adapter;
    private List<StoreGoodBean> collectList = new ArrayList<StoreGoodBean>();
    private StoreGoodAdapter collectAdapter;
    private List<StoreGoodBean> soleList = new ArrayList<StoreGoodBean>();
    private StoreGoodAdapter soleAdapter;
    private List<StoreGoodBean> recList = new ArrayList<StoreGoodBean>();
    private StoreRecGoodAdapter recAdapter;
    private List<Imagebean> imageCycleList=new ArrayList<>();
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener;

    @Override
    protected int setLayoutId() {
        return R.layout.store_main_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString("id");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        refreshLayout.setOnRefreshListener(this);
        adapter = new GoodFmVoucherAdpter(getContext(), R.layout.item_store_voucher, voucherList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener(new HaveHeadRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String voucherId = voucherList.get(position).getVoucher_t_id();
                ShopPresenter.getVoucher(StoreMainFragment.this,voucherId);
            }
        });
        rcl.setAdapter(adapter);
        rcl.setNestedScrollingEnabled(false);
        collectAdapter = new StoreGoodAdapter(getContext(), R.layout.item_store_collect_good, collectList);
        LinearLayoutManager collectedllm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rclCollect.setLayoutManager(collectedllm);
        rclCollect.setAdapter(collectAdapter);
        rclCollect.setNestedScrollingEnabled(false);
        soleAdapter = new StoreGoodAdapter(getContext(), R.layout.item_store_collect_good, soleList);
        LinearLayoutManager solelm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rclSole.setLayoutManager(solelm);
        rclSole.setAdapter(soleAdapter);
        rclSole.setNestedScrollingEnabled(false);
        recAdapter = new StoreRecGoodAdapter(getContext(), R.layout.item_store_rec_good, recList);
        GridLayoutManager recglm = new GridLayoutManager(getContext(), 2);
        recglm.setAutoMeasureEnabled(true);
        rclRec.setLayoutManager(recglm);
        rclRec.setAdapter(recAdapter);
        rclRec.setNestedScrollingEnabled(false);
        mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                GlideUtil.loadByWidthFitHeight(getContext(), imageView, imageURL);
            }

            @Override
            public void onImageClick(int position, View imageView) {
                if (imageCycleList != null&&position<imageCycleList.size()) {
                    Imagebean itemData = imageCycleList.get(position);
                    doShopItemViewClickByUrl(itemData.getLink());
                }
            }
        };
        imageCycleView.setFocusable(true);
        imageCycleView.setFocusableInTouchMode(true);
        imageCycleView.requestFocus();
        imageCycleView.requestFocusFromTouch();
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
            if (voucherArray.length() > 1) {
                llvoucher.setVisibility(View.VISIBLE);
                voucherList.clear();
                for (int i = 0; i < voucherArray.length(); i++) {
                    voucherList.add(JsonUtil.toBean(voucherArray.optJSONObject(i).toString(), VoucherBean.class));
                }
                adapter.notifyDataSetChanged();
            } else {
                llvoucher.setVisibility(View.GONE);
            }
            JSONArray mb_sliders = datas.optJSONObject("store_info").optJSONArray("mb_sliders");
            imageCycleList.clear();
            for(int i=0;i<mb_sliders.length();i++){
               Imagebean imagebean = new Imagebean();
               imagebean.setImgUrl(mb_sliders.optJSONObject(i).optString("imgUrl"));
               imagebean.setLink(mb_sliders.optJSONObject(i).optString("link"));
                imageCycleList.add(imagebean);
            }
            if (imageCycleList.size() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                for(Imagebean b :imageCycleList){
                    list.add(b.getImgUrl());
                }
                imageCycleView.setImageResources(list, mAdCycleViewListener, true, true);
            }else {
                imageCycleView.setVisibility(View.GONE);
            }
            JSONArray store_collect_rank = datas.optJSONArray("store_collect_rank");
            if (store_collect_rank.length() > 1) {
                collectList.clear();
                for (int i = 0; i < store_collect_rank.length(); i++) {
                    collectList.add(JsonUtil.toBean(store_collect_rank.optJSONObject(i).toString(), StoreGoodBean.class));
                }
                collectAdapter.notifyDataSetChanged();
            } else {
                llCollect.setVisibility(View.GONE);
            }
            JSONArray store_sole_rank = datas.optJSONArray("store_sole_rank");
            if (store_sole_rank.length() > 1) {
                soleList.clear();
                for (int i = 0; i < store_sole_rank.length(); i++) {
                    soleList.add(JsonUtil.toBean(store_sole_rank.optJSONObject(i).toString(), StoreGoodBean.class));
                }
                soleAdapter.notifyDataSetChanged();
            } else {
                llSole.setVisibility(View.GONE);
            }
            JSONArray rec_goods_list = datas.optJSONArray("rec_goods_list");
            if (rec_goods_list.length() > 1) {
                llrec.setVisibility(View.VISIBLE);
                recList.clear();
                for (int i = 0; i < rec_goods_list.length(); i++) {
                    recList.add(JsonUtil.toBean(rec_goods_list.optJSONObject(i).toString(), StoreGoodBean.class));
                }
                recAdapter.notifyDataSetChanged();
            } else {
                llrec.setVisibility(View.GONE);
            }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshFinish refreshFinish) {
        if(refreshFinish.index==3){
            llrec.setVisibility(View.VISIBLE);
        }else {
            llCollect.setVisibility(View.VISIBLE);
            llSole.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getStoreInfo(this);
    }
    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetVoucher(String voucherid) {

    }

    @Override
    public void onGetVoucherSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "成功领取代金券", 0);
    }
}
