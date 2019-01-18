package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.Fragment.ShopCarParentFragment;
import com.msht.minshengbao.androidShop.Fragment.ShopMainFragment;
import com.msht.minshengbao.androidShop.Fragment.ShopOrdersFragement;
import com.msht.minshengbao.androidShop.Fragment.ShopRefundFragmnet;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopHomeActivity extends ShopBaseActivity{

    @BindView(R.id.iv_tab0)
    ImageView tab0;
    @BindView(R.id.iv_tab1)
    ImageView tab1;
    @BindView(R.id.iv_tab2)
    ImageView tab2;
    @BindView(R.id.iv_tab3)
    ImageView tab3;
    @BindView(R.id.tv_tab0)
    TextView tvTab0;
    @BindView(R.id.tv_tab1)
    TextView tvTab1;
    @BindView(R.id.tv_tab2)
    TextView tvTab2;
    @BindView(R.id.tv_tab3)
    TextView tvTab3;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private ShopMainFragment f0;
    private ShopCarParentFragment f1;
    private List<ShopBaseLazyFragment> list = new ArrayList<ShopBaseLazyFragment>();
    private int currtentFm;
    private ShopOrdersFragement f2;
    private ShopRefundFragmnet f3;


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_shop_home);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f0 = new ShopMainFragment();
        f1 = new ShopCarParentFragment();
        f2 = new ShopOrdersFragement();
        f3 = new ShopRefundFragmnet();
        list.add(f0);
        list.add(f1);
        list.add(f2);
        list.add(f3);
        vp.setAdapter(new BaseLazyFragmentPagerAdapter(getSupportFragmentManager(), list));
        vp.setCurrentItem(0);
        initBottomTab(0);
        vp.setOffscreenPageLimit(4);
        vp.setNoScroll(true);
        vp.setPageTransformer(true, null);
        initNoNetworkLayout();
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GoShopMainEvent messageEvent) {
        initBottomTab(0);
        vp.setCurrentItem(0);
    }
    private void initBottomTab(int item) {
        if (item == 0) {
            currtentFm = 0;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_p);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_n);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_n);
            tab3.setBackgroundResource(R.drawable.shop_refund_n);
            tvTab0.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text_dark));
            tvTab1.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab2.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab3.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));

        }else if (item == 1) {
            currtentFm = 1;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_n);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_p);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_n);
            tab3.setBackgroundResource(R.drawable.shop_refund_n);
            tvTab0.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab1.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text_dark));
            tvTab2.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab3.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
        }else if(item == 2){
            currtentFm = 2;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_n);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_n);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_p);
            tab3.setBackgroundResource(R.drawable.shop_refund_n);
            tvTab0.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab1.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab2.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text_dark));
            tvTab3.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
        }else if(item == 3){
            currtentFm = 3;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_n);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_n);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_n);
            tab3.setBackgroundResource(R.drawable.shop_refund);
            tvTab0.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab1.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab2.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text));
            tvTab3.setTextColor(ContextCompat.getColor(ShopHomeActivity.this, R.color.nc_text_dark));
        }
    }
    @OnClick({R.id.ll_tab0, R.id.ll_tab1,R.id.ll_tab2,R.id.ll_tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tab0:
                if (currtentFm != 0) {
                    initBottomTab(0);
                    vp.setCurrentItem(0);
                }
                break;
            case R.id.ll_tab1:
                if (currtentFm != 1) {
                    initBottomTab(1);
                    vp.setCurrentItem(1);
                }
                break;
            case R.id.ll_tab2:
                if (currtentFm != 2) {
                    if(getKey().equals("")){
                       startActivity(new Intent(this, LoginActivity.class));
                    }else {
                        initBottomTab(2);
                        vp.setCurrentItem(2);
                    }
                }
                break;
            case R.id.ll_tab3:
                if (currtentFm != 3) {
                    if(getKey().equals("")){
                        startActivity(new Intent(this, LoginActivity.class));
                    }else {
                        initBottomTab(3);
                        vp.setCurrentItem(3);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        f1.refreshCarFragment();
    /*    f2.getOrderList();*/
    }
}
