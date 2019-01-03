package com.msht.minshengbao.androidShop.Fragment;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodEvaluationAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.ImageListPagerDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.EvaluationBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IEvaluationView;
import com.msht.minshengbao.androidShop.viewInterface.ImageListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GoodEvaluationFragment extends ShopBaseLazyFragment implements IEvaluationView, OnRefreshListener, OnLoadMoreListener, GoodEvaluationAdapter.GoodEveluateListener, ImageListView {
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.all)
    RadioButton all;
    @BindView(R.id.good)
    RadioButton good;
    @BindView(R.id.mid)
    RadioButton mid;
    @BindView(R.id.poor)
    RadioButton poor;
    @BindView(R.id.add)
    RadioButton add;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutEmpty)
    ImageView layoutEmpty;
    private String goodsid;
    private TypedArray actionbarSizeTypedArray;
    private int topMargin;
    private String type;
    private int page;
    private GoodEvaluationAdapter goodEvaluationAdapter;
    private List<EvaluationBean.DatasBean.GoodsEvalListBean> list = new ArrayList<EvaluationBean.DatasBean.GoodsEvalListBean>();
    private List<String> imageList;
    private int selectedPosition;
    @BindView(R.id.tv_no_data)
     TextView tvNoData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            goodsid = arg.getString("goodsid");
        }
        actionbarSizeTypedArray = getContext().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        topMargin = (int) (actionbarSizeTypedArray.getDimension(0, 0) * 2 + ImmersionBar.getStatusBarHeight(getActivity()) + getResources().getDimension(R.dimen.margin_Modules));

    }

    @Override
    protected int setLayoutId() {
        return R.layout.good_evalution;
    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup.LayoutParams params = radioGroup.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        marginParams.setMargins(0, topMargin, 0, 0);
        rcl.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        goodEvaluationAdapter = new GoodEvaluationAdapter(getContext(), this);
        goodEvaluationAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        goodEvaluationAdapter.setDatas(list);
        rcl.setAdapter(goodEvaluationAdapter);
        type = "";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        type = "";
                        all.setTextColor(getResources().getColor(R.color.white));
                        good.setTextColor(getResources().getColor(R.color.black));
                        mid.setTextColor(getResources().getColor(R.color.black));
                        poor.setTextColor(getResources().getColor(R.color.black));
                        add.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case R.id.good:
                        type = "1";
                        good.setTextColor(getResources().getColor(R.color.white));
                        all.setTextColor(getResources().getColor(R.color.black));
                        mid.setTextColor(getResources().getColor(R.color.black));
                        poor.setTextColor(getResources().getColor(R.color.black));
                        add.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case R.id.mid:
                        type = "2";
                        mid.setTextColor(getResources().getColor(R.color.white));
                        good.setTextColor(getResources().getColor(R.color.black));
                        all.setTextColor(getResources().getColor(R.color.black));
                        poor.setTextColor(getResources().getColor(R.color.black));
                        add.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case R.id.poor:
                        type = "3";
                        poor.setTextColor(getResources().getColor(R.color.white));
                        mid.setTextColor(getResources().getColor(R.color.black));
                        good.setTextColor(getResources().getColor(R.color.black));
                        all.setTextColor(getResources().getColor(R.color.black));
                        add.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case R.id.add:
                        type = "5";
                        add.setTextColor(getResources().getColor(R.color.white));
                        poor.setTextColor(getResources().getColor(R.color.black));
                        mid.setTextColor(getResources().getColor(R.color.black));
                        good.setTextColor(getResources().getColor(R.color.black));
                        all.setTextColor(getResources().getColor(R.color.black));
                        break;
                    default:
                        break;
                }
                LogUtils.e("type=" + type);
                page = 1;
                ShopPresenter.getEvaluation(GoodEvaluationFragment.this);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        ShopPresenter.getEvaluation(GoodEvaluationFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionbarSizeTypedArray.recycle();
    }

    @Override
    public void onSuccess(String s) {
        EvaluationBean bean = JsonUtil.toBean(s, EvaluationBean.class);
        int pageTotal;
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (bean != null) {
            pageTotal = bean.getPage_total();
            List<EvaluationBean.DatasBean.GoodsEvalListBean> goodsEvalListBeanList = bean.getDatas().getGoods_eval_list();
            if (pageTotal == 0) {
                list.clear();
                refreshLayout.setEnableAutoLoadMore(true);
                refreshLayout.setNoMoreData(false);
                goodEvaluationAdapter.notifyDataSetChanged();
                layoutEmpty.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.VISIBLE);
                return;
            }else if (page > pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
                return;
            } else if (page == 1) {
                list.clear();
            }
            layoutEmpty.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.INVISIBLE);
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            list.addAll(goodsEvalListBeanList);
            if (page == pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
            } else {
                refreshLayout.setEnableAutoLoadMore(true);
                refreshLayout.setNoMoreData(false);
            }
            goodEvaluationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public String getGoodsId() {
        return goodsid;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getPage() {
        return "10";
    }

    @Override
    public String getCurrenPage() {
        return page + "";
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        ShopPresenter.getEvaluation(GoodEvaluationFragment.this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        ShopPresenter.getEvaluation(GoodEvaluationFragment.this);
    }

    @Override
    public void onClickImage(boolean isEvaluate ,int position, int childposition) {
        if(isEvaluate){
            imageList = list.get(position).getGeval_image_again_1024();
        }else {
            imageList = list.get(position).getGeval_image_1024();
        }
        selectedPosition = childposition;
        ImageListPagerDialog imageListDialog = new ImageListPagerDialog(getContext(), this, selectedPosition);
        imageListDialog.show();
    }

    @Override
    public List<String> getImageList() {
        return imageList;
    }

    @Override
    public int getSelectedPosition() {
        return selectedPosition;
    }
}
