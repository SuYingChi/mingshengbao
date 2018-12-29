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
import com.msht.minshengbao.androidShop.adapter.KeywordListAdapterHaveHeadView;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopkeywordBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.viewInterface.IKeyWordListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopKeywordListActivity extends ShopBaseActivity implements IKeyWordListView, OnRefreshLoadMoreListener, OnRefreshListener, IWarnMessageDetailView {
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
    private List<ShopkeywordBean.DatasBean.GoodsListBean> datalist = new ArrayList<ShopkeywordBean.DatasBean.GoodsListBean>();
    private KeywordListAdapterHaveHeadView adapter;
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
    private DividerItemDecoration dividerItemDecoration2;
    private DividerItemDecoration dividerItemDecoration;
    private String msgid;

    @Override
    protected void setLayout() {
        setContentView(R.layout.key_word_list);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
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
        keyword = getIntent().getStringExtra("keyword");
        msgid = getIntent().getIntExtra("msgid",0)+"";
        et.setHint(keyword);
        linearLayoutManager =  new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcl.addItemDecoration(dividerItemDecoration);
        adapter = new KeywordListAdapterHaveHeadView(this);
        adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ShopkeywordBean.DatasBean.GoodsListBean item = datalist.get(position);
                HashMap<String,String> map= new HashMap<String,String>();
                String goodsId = item.getGoods_id();
                map.put("type","goods");
                map.put("data",goodsId);
                map.put("price",item.getGoods_price());
                doNotAdClick(map);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getKeywordList(this);
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
        if(isGrid){
            rcl.setLayoutManager(linearLayoutManager);
            isGrid = false;
            iv.setImageDrawable(grid);
            rcl.addItemDecoration(dividerItemDecoration);

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
                    keyword = data.getStringExtra("keyword");
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
    public void onSuccess(List<ShopkeywordBean.DatasBean.GoodsListBean> list, int pageTotal) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (pageTotal == 0) {
            datalist.clear();
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            adapter.notifyDataSetChanged();
            ivNoData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }/*else if (curPage == 1) {
            datalist.clear();
            datalist.addAll(list);
            adapter.setDatas(datalist);
            adapter.notifyDataSetChanged();
        } else if (curPage >= pageTotal) {
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.setNoMoreData(true);
        }*/
        else if (curPage > pageTotal) {
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
            refreshLayout.setNoMoreData(true);
            return;
        } else if (curPage == 1) {
            datalist.clear();
        } /*else {
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            if (list.size() != 0) {
                datalist.addAll(list);
                adapter.setDatas(datalist);
                adapter.notifyDataSetChanged();
            }
        }*/
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
    public void onGetDetailSuccess(String s) {

    }
}
