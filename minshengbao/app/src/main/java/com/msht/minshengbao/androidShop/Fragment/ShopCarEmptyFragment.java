package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GuessLikeAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.GuessLikeBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGuessLikeGoodListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public  class ShopCarEmptyFragment extends ShopBaseLazyFragment implements IGuessLikeGoodListView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.return_home)
    TextView returnHome;
    private GuessLikeAdapter ad;
    private List<GuessLikeBean.DatasBean.GuessLikeListBean> datalist=new ArrayList<GuessLikeBean.DatasBean.GuessLikeListBean>();
    private boolean isViewCreated;


    @Override
    protected int setLayoutId() {
        return R.layout.car_empty;
    }

    @Override
    protected void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcl.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        ad = new GuessLikeAdapter(getContext());
        ad.setDatas(datalist);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int childposition) {
                String goodsId = datalist.get(childposition).getGoods_id();
                doShopItemViewClick("goods",goodsId);
            }
        });
        rcl.setAdapter(ad);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GoShopMainEvent());
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    protected void initData() {
        if(!getKey().equals("")) {
            ShopPresenter.guessLikeGoodList(this);
        }
    }
    @Override
    protected void onVisible() {
        super.onVisible();
        if(isViewCreated){
            if(!getKey().equals("")) {
                ShopPresenter.guessLikeGoodList(this);
            }
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.setTitleBar(getActivity(),mToolbar);
    }

    @Override
    public void onGetGuessLikeGoodSuccess(String s) {
        datalist.clear();
        datalist.addAll(JsonUtil.toBean(s,GuessLikeBean.class).getDatas().getGuess_like_list());
        ad.notifyDataSetChanged();
    }
}
