package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GuessLikeAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.GuessLikeBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGuessLikeGoodListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author mshtyfb
 */
public class NoCarActivity extends ShopBaseActivity implements IGuessLikeGoodListView {
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.return_home)
    TextView returnHome;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private GuessLikeAdapter ad;
    private List<GuessLikeBean.DatasBean.GuessLikeListBean> datalist = new ArrayList<GuessLikeBean.DatasBean.GuessLikeListBean>();


    @Override
    protected void setLayout() {
        setContentView(R.layout.car_empty_activity);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcl.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        ad = new GuessLikeAdapter(this);
        ad.setDatas(datalist);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int childposition) {
                HashMap<String, String> map = new HashMap<String, String>();
                String goodsId = datalist.get(childposition).getGoods_id();
                map.put("type", "goods");
                map.put("data", goodsId);
                doNotAdClick(map);
                finish();
            }
        });
        rcl.setAdapter(ad);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoCarActivity.this, ShopHomeActivity.class));
                finish();
            }
        });
        ShopPresenter.guessLikeGoodList(this);
    }

    @Override
    public void onGetGuessLikeGoodSuccess(String s) {
        datalist.clear();
        datalist.addAll(JsonUtil.toBean(s,GuessLikeBean.class).getDatas().getGuess_like_list());
        ad.notifyDataSetChanged();
    }
}
