package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodEvaluationChildAdapter;
import com.msht.minshengbao.androidShop.adapter.MoreGoodAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailLeftBean;
import com.msht.minshengbao.androidShop.shopBean.ClassFirstBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopAllClassView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

public class ShopMoreGoodActivity extends ShopBaseActivity implements IShopAllClassView {
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.tvSearchD)
    EditText et;
    private int carnum;

    @Override
    protected void setLayout() {
        setContentView(R.layout.more_good);
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
        //不允许获取焦点 不可编辑,却可以点击
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMoreGoodActivity.this, ShopSearchActivty.class);
                intent.putExtra("main", 1);
                startActivity(intent);
            }
        });
        ShopPresenter.getAllClass(this);
        /*final List<ClassFirstBean.DatasBean.ClassListBean> homeClassList = (List<ClassFirstBean.DatasBean.ClassListBean>) getIntent().getSerializableExtra("list");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcl.setLayoutManager(gridLayoutManager);
        MoreGoodAdapter ad = new MoreGoodAdapter(this);
        ad.setDatas(homeClassList);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int childposition) {
                Intent intent = new Intent(ShopMoreGoodActivity.this, ShopClassDetailActivity.class);
              *//*  String data = homeClassList.get(childposition).getData();
                if (data.contains("id=")) {
                    int index = data.indexOf("id=");
                    data = data.substring(index + 3).trim();
                    intent.putExtra("data", data);
                    intent.putExtra("position", childposition);
                    intent.putExtra("title", homeClassList.get(childposition).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) homeClassList);
                    intent.putExtras(bundle);
                    intent.putExtra("carnum", carnum);
                    startActivity(intent);
                }*//*
                String data = homeClassList.get(childposition).getGc_id();
                intent.putExtra("data", data);
                intent.putExtra("title", homeClassList.get(childposition).getGc_name());
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) homeClassList);
                intent.putExtras(bundle);
                intent.putExtra("carnum", carnum);
                startActivity(intent);
            }
        });
        rcl.setAdapter(ad);*/
    }

    @Override
    public void onGetAllClassSuccess(final List<ClassFirstBean.DatasBean.ClassListBean> homeClassList) {
      //  final List<ClassFirstBean.DatasBean.ClassListBean> homeClassList = (List<ClassFirstBean.DatasBean.ClassListBean>) getIntent().getSerializableExtra("list");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcl.setLayoutManager(gridLayoutManager);
        MoreGoodAdapter ad = new MoreGoodAdapter(this);
        ad.setDatas(homeClassList);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int childposition) {
                Intent intent = new Intent(ShopMoreGoodActivity.this, ShopClassDetailActivity.class);
              /*  String data = homeClassList.get(childposition).getData();
                if (data.contains("id=")) {
                    int index = data.indexOf("id=");
                    data = data.substring(index + 3).trim();
                    intent.putExtra("data", data);
                    intent.putExtra("position", childposition);
                    intent.putExtra("title", homeClassList.get(childposition).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) homeClassList);
                    intent.putExtras(bundle);
                    intent.putExtra("carnum", carnum);
                    startActivity(intent);
                }*/
                String data = homeClassList.get(childposition).getGc_id();
                intent.putExtra("data", data);
                intent.putExtra("title", homeClassList.get(childposition).getGc_name());
            /*    Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) homeClassList);
                intent.putExtras(bundle);*/
            /*    intent.putExtra("carnum", carnum);*/
                startActivity(intent);
            }
        });
        rcl.setAdapter(ad);
    }
}
