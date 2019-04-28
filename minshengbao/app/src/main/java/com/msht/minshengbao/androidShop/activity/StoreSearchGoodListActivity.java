package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.StoreGoodListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStoreSearchGoodListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StoreSearchGoodListActivity extends ShopBaseActivity implements IStoreSearchGoodListView, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.def)
    TextView tvDef;
    @BindView(R.id.sell)
    TextView tvSell;
    @BindView(R.id.ren)
    TextView tvRen;
    @BindView(R.id.price)
    TextView tvPrice;
    @BindView(R.id.tvSearchD)
    EditText et;
    @BindView(R.id.grid)
    ImageView iv;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private int curPage = 1;
    private String order1 = "2";
    private String order2 = "2";
    private String order3 = "2";
    private String order = "2";
    private String orderKey = "";
    private String stc_id;
    private String storeid;
    private Drawable upTriangle;
    private Drawable downTriangle;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Drawable grid2;
    private Drawable grid;
    private boolean isGrid = false;
    private DividerItemDecoration dividerItemDecoration2;
    private DividerItemDecoration dividerItemDecoration;
    private StoreGoodListAdapter adapter;
    private List<StoreGoodBean> goodlist=new ArrayList<>();
    private String keyword;

    @Override
    protected void setLayout() {
      setContentView(R.layout.key_word_list);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et.setHint("搜索店铺内商品");
        stc_id = getIntent().getStringExtra("stc_id");
        storeid = getIntent().getStringExtra("storeid");
        keyword = getIntent().getStringExtra("keyword");
        if(!TextUtils.isEmpty(keyword)){
            et.setText(keyword);
        }
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchKeyWord = et.getText().toString();
                    keyword = searchKeyWord;
                    MyApplication.getInstance().addSearchHis(searchKeyWord);
                    ShopPresenter.getStoreSearchList(StoreSearchGoodListActivity.this);
                    return true;
                }
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        linearLayoutManager =  new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcl.addItemDecoration(dividerItemDecoration);
        adapter = new StoreGoodListAdapter(this,R.layout.item_keyword_search,goodlist);
        rcl.setAdapter(adapter);
        upTriangle = getResources().getDrawable(R.drawable.shop_up_triangle);
        downTriangle = getResources().getDrawable(R.drawable.shop_down_triangle);
        grid2 = getResources().getDrawable(R.drawable.grid_2);
        grid = getResources().getDrawable(R.drawable.shop_grid);
        initTab("");
    }

    @Override
    public String getStoreId() {
        return storeid;
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
        return curPage+"";
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
            } else if (curPage > pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
                return;
            } else if (curPage == 1) {
                goodlist.clear();
            }
            ivNoData.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.INVISIBLE);
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            JSONArray goodArray = obj.optJSONObject("datas").optJSONArray("goods_list");
            for(int i=0;i<goodArray.length();i++){
                goodlist.add(JsonUtil.toBean(goodArray.optJSONObject(i).toString(),StoreGoodBean.class));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStcId() {
        return stc_id;
    }

    @Override
    public String getKeyWord() {
        return keyword;
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
        if(isGrid){
            rcl.setLayoutManager(linearLayoutManager);
            isGrid = false;
            iv.setImageDrawable(grid);
            rcl.addItemDecoration(dividerItemDecoration);

        }
        curPage = 1;
        ShopPresenter.getStoreSearchList(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curPage=1;
        ShopPresenter.getStoreSearchList(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        curPage++;
        ShopPresenter.getStoreSearchList(this);
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
                        gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false) {
                            @Override
                            public boolean canScrollHorizontally() {
                                return false;
                            }
                        };
                    }
                    rcl.setLayoutManager(gridLayoutManager);
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    iv.setImageDrawable(grid2);
                    isGrid = true;
                    if(dividerItemDecoration2==null) {
                        dividerItemDecoration2 = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
                    }
                    rcl.addItemDecoration(dividerItemDecoration2);
                    adapter.notifyDataSetChanged();
                }else {
                    if (linearLayoutManager == null) {
                        linearLayoutManager =  new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                    }
                    rcl.setLayoutManager(linearLayoutManager);
                    tvDef.setTextColor(getResources().getColor(R.color.black));
                    tvSell.setTextColor(getResources().getColor(R.color.black));
                    tvRen.setTextColor(getResources().getColor(R.color.black));
                    tvPrice.setTextColor(getResources().getColor(R.color.black));
                    iv.setImageDrawable(grid);
                    isGrid = false;
                    if(dividerItemDecoration2==null) {
                        dividerItemDecoration2 = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
                    }
                    rcl.addItemDecoration(dividerItemDecoration2);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
}
