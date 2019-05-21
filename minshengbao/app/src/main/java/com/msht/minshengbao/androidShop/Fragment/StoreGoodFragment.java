package com.msht.minshengbao.androidShop.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.ClassDetailRightAdapter;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.KeywordListAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.StoreGoodListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AddCarBean;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopAddCarView;
import com.msht.minshengbao.androidShop.viewInterface.IStoreGoodView;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
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
import butterknife.OnClick;

public class StoreGoodFragment extends ShopBaseLazyFragment implements IStoreGoodView, OnRefreshListener, OnLoadMoreListener, ClassDetailRightAdapter.AddCarListener, IShopAddCarView {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.def)
    TextView tvDef;
    @BindView(R.id.sell)
    TextView tvSell;
    @BindView(R.id.ren)
    TextView tvRen;
    @BindView(R.id.price)
    TextView tvPrice;
    @BindView(R.id.grid)
    ImageView iv;
    private Drawable upTriangle;
    private Drawable downTriangle;
    private String storeId;
    private int curpage=1;
    private String order="2";
    private String order1 = "2";
    private String order2 = "2";
    private String order3 = "2";
    private String orderKey="";
    private LinearLayoutManager linearLayoutManager;

   /* private List<StoreGoodBean> goodlist=new ArrayList<StoreGoodBean>();*/
   private  List<ClassDetailRightBean.DatasBean.GoodsListBean> goodlist = new ArrayList<ClassDetailRightBean.DatasBean.GoodsListBean>();
   /* private StoreGoodListAdapter adapter;*/
   private ClassDetailRightAdapter adapter;
    private Drawable grid2;
    private Drawable grid;
    private GridLayoutManager gridLayoutManager;
    private boolean isGrid=false;
    private DividerItemDecoration dividerItemDecoration2;
    private DividerItemDecoration dividerItemDecoration;
    private ClassDetailRightAdapter gridAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.store_good_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId=getArguments().getString("id");
    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        linearLayoutManager =  new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
       /* dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcl.addItemDecoration(dividerItemDecoration);
        adapter = new StoreGoodListAdapter(getContext(),R.layout.item_keyword_search,goodlist);
        adapter.setOnItemClickListener(new HaveHeadRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                doShopItemViewClick("goods",goodlist.get(position).getGoods_id());
            }
        });
        rcl.setAdapter(adapter);*/
        adapter = new ClassDetailRightAdapter(getContext(),R.layout.item_class_detail_right);
        adapter.setOnAddCarListener(this);
        adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ClassDetailRightBean.DatasBean.GoodsListBean item = goodlist.get(position);
                String goodsId = item.getGoods_id();
                doShopItemViewClick("goods", goodsId);
            }
        });
        adapter.setDatas(goodlist);
        rcl.setAdapter(adapter);
        upTriangle = getResources().getDrawable(R.drawable.shop_up_triangle);
        downTriangle = getResources().getDrawable(R.drawable.shop_down_triangle);
        grid2 = getResources().getDrawable(R.drawable.grid_2);
        grid = getResources().getDrawable(R.drawable.shop_grid);
        //initTab("");
    }

    @Override
    protected void initData() {
        super.initData();
        initTab("");
       // ShopPresenter.getStoreGood(this);
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getTab() {
        return orderKey;
    }

    @Override
    public String getRankType() {
        return order;
    }

    @Override
    public String getCurpage() {
        return curpage+"";
    }

    @Override
    public void onGetStoreGoodSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        try {
            JSONObject obj = new JSONObject(s);
            int pageTotal = obj.optInt("page_total");
        if (pageTotal == 0) {
            goodlist.clear();
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.setNoMoreData(true);
            adapter.notifyDataSetChanged();
            ivNoData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
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
            goodlist.add(JsonUtil.toBean(goodArray.optJSONObject(i).toString(),ClassDetailRightBean.DatasBean.GoodsListBean.class));
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
    private void initTab(String selectTab) {

        switch (selectTab) {
            case "":
                if (!TextUtils.equals(orderKey, "")) {
                    tvDef.setTextColor(getResources().getColor(R.color.msb_color));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    orderKey = "";
                }
                order = "2";
                break;
            case "1":
                if (!TextUtils.equals(orderKey, "1")) {
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.msb_color));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    orderKey = "1";
                }
                if (!TextUtils.equals(order1, "1")) {
                    order1 = "1";
                    upTriangle.setBounds(0, 0, upTriangle.getMinimumWidth(), upTriangle.getMinimumHeight());
                    tvSell.setCompoundDrawables(null, null, upTriangle, null);
                }else {
                    order1 = "2";
                    downTriangle.setBounds(0, 0, downTriangle.getMinimumWidth(), downTriangle.getMinimumHeight());
                    tvSell.setCompoundDrawables(null, null, downTriangle, null);
                }
                order = order1;
                break;
            case "2":
                if (!TextUtils.equals(orderKey, "2")) {
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.msb_color));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    orderKey = "2";
                }
                if (!TextUtils.equals(order2, "1")) {
                    order2 = "1";
                    upTriangle.setBounds(0, 0, upTriangle.getMinimumWidth(), upTriangle.getMinimumHeight());
                    tvRen.setCompoundDrawables(null, null, upTriangle, null);
                } else {
                    order2 = "2";
                    downTriangle.setBounds(0, 0, downTriangle.getMinimumWidth(), downTriangle.getMinimumHeight());
                    tvRen.setCompoundDrawables(null, null, downTriangle, null);
                }
                order = order2;
                break;
            case "3":
                if (!TextUtils.equals(orderKey, "3")) {
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.msb_color));
                    orderKey = "3";
                }
                if (!TextUtils.equals(order3, "1")) {
                    order3 = "1";
                    upTriangle.setBounds(0, 0, upTriangle.getMinimumWidth(), upTriangle.getMinimumHeight());
                    tvPrice.setCompoundDrawables(null, null, upTriangle, null);
                } else {
                    order3 = "2";
                    downTriangle.setBounds(0, 0, downTriangle.getMinimumWidth(), downTriangle.getMinimumHeight());
                    tvPrice.setCompoundDrawables(null, null, downTriangle, null);
                }
                order = order3;
                break;
            default:
                break;
        }
        curpage = 1;
        ShopPresenter.getStoreGood(this);
    }

    @OnClick({R.id.def, R.id.sell, R.id.ren, R.id.price,R.id.grid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.def:
                initTab("");
                break;
            case R.id.sell:
                initTab("1");
                break;
            case R.id.ren:
                initTab("2");
                break;
            case R.id.price:
                initTab("3");
                break;
            case R.id.grid:
                if (!isGrid) {
                    if (gridLayoutManager == null) {
                        gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false) {
                            @Override
                            public boolean canScrollHorizontally() {
                                return false;
                            }
                        };
                        gridLayoutManager.setAutoMeasureEnabled(true);
                    }
                    if(gridAdapter == null){
                        gridAdapter = new ClassDetailRightAdapter(getContext(),R.layout.item_grid_keyword_list);
                        gridAdapter.setOnAddCarListener(this);
                        gridAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ClassDetailRightBean.DatasBean.GoodsListBean item = goodlist.get(position);
                                String goodsId = item.getGoods_id();
                                doShopItemViewClick("goods", goodsId);
                            }
                        });
                        gridAdapter.setDatas(goodlist);
                    }
                    rcl.setLayoutManager(gridLayoutManager);
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    iv.setImageDrawable(grid2);
                    isGrid = true;
                    rcl.setAdapter(gridAdapter);
                }else {
                    rcl.setLayoutManager(linearLayoutManager);
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    iv.setImageDrawable(grid);
                    isGrid = false;
                    rcl.setAdapter(adapter);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curpage=1;
        ShopPresenter.getStoreGood(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        curpage++;
        ShopPresenter.getStoreGood(this);
    }

    @Override
    public void addCar(ClassDetailRightBean.DatasBean.GoodsListBean good) {
        if (TextUtils.isEmpty(getKey())) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            if(1==good.getCart()) {
                ShopPresenter.addCar(this,good.getGoods_id());
            }else {
                String goodsId = good.getGoods_id();
                doShopItemViewClick("goods", goodsId);
            }
        }
    }

    @Override
    public void onAddCarSuccess(String s) {
        AddCarBean bean = JsonUtil.toBean(s, AddCarBean.class);
        if (TextUtils.equals(bean.getDatas(), "1")) {
            PopUtil.showAutoDissHookDialog(getContext(), "添加购物车成功", 100);
        }
    }
}
