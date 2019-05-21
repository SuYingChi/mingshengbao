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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.Fragment.GoodFragment;
import com.msht.minshengbao.androidShop.adapter.ClassDetailRightAdapter;
import com.msht.minshengbao.androidShop.adapter.KeywordListAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AddCarBean;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.ShopkeywordBean;
import com.msht.minshengbao.androidShop.shopBean.SimpleCarBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IKeyWordListView;

import java.util.ArrayList;
import java.util.List;

import com.msht.minshengbao.androidShop.viewInterface.IShopAddCarView;
import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopKeywordListActivity extends ShopBaseActivity implements IKeyWordListView, OnRefreshLoadMoreListener, OnRefreshListener, IWarnMessageDetailView, ClassDetailRightAdapter.AddCarListener,IShopAddCarView {
    private static final int KEYWORD = 1000;
    private String keyword;
    private int curPage = 1;
    private String order = "2";
    private String orderKey = "";
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
    private List<ClassDetailRightBean.DatasBean.GoodsListBean> datalist = new ArrayList<ClassDetailRightBean.DatasBean.GoodsListBean>();
    private ClassDetailRightAdapter adapter;
    private String order1 = "2";
    private String order2 = "2";
    private String order3 = "2";
    private Drawable upTriangle;
    private Drawable downTriangle;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Drawable grid2;
    private Drawable grid;
    private boolean isGrid = false;
    private String msgid;
    private String gcId="";
    private  ClassDetailRightAdapter gridAdapter;


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
        keyword = getIntent().getStringExtra("keyword");
        if(keyword==null){
            keyword="";
        }
        gcId = getIntent().getStringExtra("gcid");
        msgid = getIntent().getIntExtra("msgid",0)+"";
        et.setHint(keyword);
        linearLayoutManager =  new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        adapter = new ClassDetailRightAdapter(this,R.layout.item_class_detail_right);
        adapter.setOnAddCarListener(this);
        adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ClassDetailRightBean.DatasBean.GoodsListBean item = datalist.get(position);
                String goodsId = item.getGoods_id();
                onShopItemViewClick("goods", goodsId);
            }
        });
        adapter.setDatas(datalist);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        upTriangle = getResources().getDrawable(R.drawable.shop_up_triangle);
        downTriangle = getResources().getDrawable(R.drawable.shop_down_triangle);
        grid2 = getResources().getDrawable(R.drawable.grid_2);
        grid = getResources().getDrawable(R.drawable.shop_grid);
        initTab("");
        //不允许获取焦点 不可编辑,却可以点击
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearch();
            }
        });
        if(!msgid.equals("0")) {
            ShopPresenter.getMessageDetail(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), msgid);
        }
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
        curPage = 1;
        ShopPresenter.getKeywordList(this);
    }

    private void goSearch() {
        Intent intent = new Intent(this, ShopSearchActivty.class);
        intent.putExtra("keyword", keyword);
        AppUtil.hideSoftInput(this);
        startActivityForResult(intent, KEYWORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEYWORD && resultCode == RESULT_OK) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getStringExtra("keyword"))&&!TextUtils.equals(keyword,data.getStringExtra("keyword"))) {
                    gcId="";
                    keyword = data.getStringExtra("keyword");
                    curPage = 1;
                    refreshLayout.setEnableAutoLoadMore(true);
                    refreshLayout.setNoMoreData(false);
                    ShopPresenter.getKeywordList(this);
                    et.setText(keyword);
                }
            }
        }
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public void onSuccess(List<ClassDetailRightBean.DatasBean.GoodsListBean> list, int pageTotal) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (pageTotal == 0) {
            datalist.clear();
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
            datalist.clear();
        }
        ivNoData.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setNoMoreData(false);
        datalist.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public String order() {
        return order;
    }


    @Override
    public String getCurrenPage() {
        return curPage + "";
    }

    @Override
    public String getPage() {
        return 10 + "";
    }

    @Override
    public String orderKey() {
        return orderKey;
    }

    @Override
    public String getGcId() {
        if(TextUtils.isEmpty(gcId)){
            gcId="";
        }
        return gcId;
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        curPage++;
        ShopPresenter.getKeywordList(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curPage = 1;
        ShopPresenter.getKeywordList(this);
    }

    @OnClick({R.id.def, R.id.sell, R.id.ren, R.id.price, R.id.back,R.id.grid})
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
            case R.id.back:
                finish();
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
                    if(gridAdapter == null){
                        gridAdapter = new ClassDetailRightAdapter(this,R.layout.item_grid_keyword_list);
                        gridAdapter.setOnAddCarListener(this);
                        gridAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ClassDetailRightBean.DatasBean.GoodsListBean item = datalist.get(position);
                                String goodsId = item.getGoods_id();
                                onShopItemViewClick("goods", goodsId);
                            }
                        });
                        gridAdapter.setDatas(datalist);
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
    public void onGetDetailSuccess(String s) {

    }

    @Override
    public void addCar(ClassDetailRightBean.DatasBean.GoodsListBean goods) {
        if (TextUtils.isEmpty(getKey())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if(1==goods.getCart()) {
                ShopPresenter.addCar(this,goods.getGoods_id());
            }else {
                String goodsId = goods.getGoods_id();
                onShopItemViewClick("goods", goodsId);
            }
        }
    }


    @Override
    public void onAddCarSuccess(String s) {
        AddCarBean bean = JsonUtil.toBean(s, AddCarBean.class);
        if (bean != null && TextUtils.equals(bean.getDatas(), "1")) {
            PopUtil.showAutoDissHookDialog(this, "添加购物车成功", 100);
        }
    }
}
