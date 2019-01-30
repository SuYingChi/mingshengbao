package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopCarParentFragment extends ShopBaseLazyFragment implements ICarListView, ShopCarFragment.CarParentListener {
    private ShopCarFragment f1;
    private ShopCarEmptyFragment f0;
    private List<ShopBaseLazyFragment> list = new ArrayList<ShopBaseLazyFragment>();
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private boolean isViewCreated;
    private ShopCarNoLoginFragment f2;
    private int initTab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            initTab = bundle.getInt("index");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.car_parent;
    }

    @Override
    protected void initView() {
        super.initView();
        f0 = new ShopCarEmptyFragment();
        f1 = new ShopCarFragment();
        f2 = new ShopCarNoLoginFragment();
        list.add(f0);
        list.add(f1);
        list.add(f2);
        vp.setAdapter(new BaseLazyFragmentPagerAdapter(getChildFragmentManager(), list));
        vp.setOffscreenPageLimit(3);
        vp.setNoScroll(true);
        vp.setPageTransformer(true, null);
        vp.setCurrentItem(initTab);
    }

    //在add hide show结构中，重复show的话不会再次回调onvisible
    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initImmersionBar();
    }

    @Override
    protected void initData() {
        if (getKey().equals("")) {
            vp.setCurrentItem(2);
        } else {
            ShopPresenter.getCarList(this, false);
        }
    }
    @Override
    protected void onVisible() {
        super.onVisible();
        refreshCarFragment();
    }

    @Override
    public void onGetCarListSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.optJSONObject("datas");
            JSONArray jsonArray = datas.optJSONArray("cart_list");
            if (jsonArray.length() == 0) {
                vp.setCurrentItem(0);
            } else {
                vp.setCurrentItem(1);
                f1.refreshCarList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void changeCarEmpty() {
            vp.setCurrentItem(0);
    }

    public void refreshCarFragment() {
        if(isViewCreated) {
            if (getKey().equals("")) {
                vp.setCurrentItem(2);
            } else {
                ShopPresenter.getCarList(this, false);
            }
        }
    }
}
