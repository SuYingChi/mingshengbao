package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.LogoutEventBusEvent;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopParentFragment extends ShopBaseLazyFragment {


    @BindView(R.id.iv_tab0)
    ImageView tab0;
    @BindView(R.id.iv_tab1)
    ImageView tab1;
    @BindView(R.id.iv_tab2)
    ImageView tab2;
    @BindView(R.id.tv_tab0)
    TextView tvTab0;
    @BindView(R.id.tv_tab1)
    TextView tvTab1;
    @BindView(R.id.tv_tab2)
    TextView tvTab2;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private ShopMainFragment f0;
    private ShopCarParentFragment f1;
    private List<ShopBaseLazyFragment> list = new ArrayList<ShopBaseLazyFragment>();
    private int currtentFm;
    private ShopOrdersFragement f2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shop_home;
    }


    @Override
    protected void initView() {
        super.initView();
        f0 = new ShopMainFragment();
        f1 = new ShopCarParentFragment();
        f2 = new ShopOrdersFragement();
        list.add(f0);
        list.add(f1);
        list.add(f2);
        vp.setAdapter(new BaseLazyFragmentPagerAdapter(getChildFragmentManager(), list));
        vp.setCurrentItem(0);
        initBottomTab(0);
        vp.setOffscreenPageLimit(3);
        vp.setNoScroll(true);
        vp.setPageTransformer(true, null);
    }

    private void initBottomTab(int item) {
        if (item == 0) {
            currtentFm = 0;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_p);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_n);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_n);

            tvTab0.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text_dark));
            tvTab1.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));
            tvTab2.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));

        }else if (item == 1) {
            currtentFm = 1;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_n);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_p);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_n);

            tvTab0.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));
            tvTab1.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text_dark));
            tvTab2.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));
        }else if(item == 2){
            currtentFm = 2;
            tab0.setBackgroundResource(R.drawable.main_index_my_home_n);
            tab1.setBackgroundResource(R.drawable.main_index_my_cart_n);
            tab2.setBackgroundResource(R.drawable.main_index_my_mine_p);

            tvTab0.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));
            tvTab1.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text));
            tvTab2.setTextColor(ContextCompat.getColor(getContext(), R.color.nc_text_dark));
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         return mRootView;
    }

    @OnClick({R.id.ll_tab0, R.id.ll_tab1,R.id.ll_tab2})
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
                    initBottomTab(2);
                    vp.setCurrentItem(2);
                }
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LogoutEventBusEvent messageEvent) {
       vp.setCurrentItem(0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
