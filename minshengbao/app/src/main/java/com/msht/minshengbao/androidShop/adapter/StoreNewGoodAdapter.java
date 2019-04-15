package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreNewGoodBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class StoreNewGoodAdapter extends HaveHeadRecyclerAdapter<StoreNewGoodBean>{


    public StoreNewGoodAdapter(Context context, List<StoreNewGoodBean> datas) {
        super(context, R.layout.item_store_new,datas);
    }

    @Override
    public void convert(RecyclerHolder holder, StoreNewGoodBean goodBean, final int position) {
        RecyclerView rcl = holder.getView(R.id.rcl);
        holder.setText(R.id.tv,goodBean.getDate());
        if (rcl.getAdapter() == null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            //自适应自身高度
            gridLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(gridLayoutManager);
            ShopNewGoodChildAdapter childAdapter = new ShopNewGoodChildAdapter(context,goodBean.getGoodList());
            rcl.setAdapter(childAdapter);
            rcl.setNestedScrollingEnabled(false);
        } else if (rcl.getAdapter() instanceof ShopNewGoodChildAdapter) {
            ShopNewGoodChildAdapter childAdapter = (ShopNewGoodChildAdapter) rcl.getAdapter();
            childAdapter.setDatas(goodBean.getGoodList());
            childAdapter.notifyDataSetChanged();
        }
    }
}
