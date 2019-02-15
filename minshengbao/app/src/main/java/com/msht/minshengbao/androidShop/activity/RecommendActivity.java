package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.AddressListAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.RecommendListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.shopBean.RecommendBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendActivity extends ShopBaseActivity {
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @Override
    protected void setLayout() {
        setContentView(R.layout.recommend);
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
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final List<RecommendBean> list = (List<RecommendBean>) bundle.getSerializable("data");
        rcl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecommendListAdapter addressListAdapter = new RecommendListAdapter(this);
        addressListAdapter.setDatas(list);
        addressListAdapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent1 = new Intent();
                intent.putExtra("data",list.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rcl.setAdapter(addressListAdapter);
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
}
