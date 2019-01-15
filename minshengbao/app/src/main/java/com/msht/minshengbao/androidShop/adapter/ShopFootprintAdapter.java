package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopFootPrintBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class ShopFootprintAdapter extends MyHaveHeadViewRecyclerAdapter<ShopFootPrintBean.DatasBean.GoodsbrowseListBean>{


    private FootprintListener listener;

    public ShopFootprintAdapter(Context context) {
        super(context, R.layout.item_car_list);
    }

    @Override
    public void convert(RecyclerHolder holder, ShopFootPrintBean.DatasBean.GoodsbrowseListBean goodsbrowseListBean, final int position) {
        RecyclerView rcl = holder.getView(R.id.rcl);
        if (rcl.getAdapter() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            //自适应自身高度
            linearLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(linearLayoutManager);
            ShopFootprintChildAdapter childAdapter = new ShopFootprintChildAdapter(context, goodsbrowseListBean.getDay());
            childAdapter.setHead_layoutId(R.layout.item_head_text_rcl);
            childAdapter.setDatas(goodsbrowseListBean.getGoods_list());
            childAdapter.setDate(goodsbrowseListBean.getDay());
            childAdapter.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(int childPosition) {
                  listener.onCLick(position,childPosition);
              }
          });
            rcl.setAdapter(childAdapter);
        } else if (rcl.getAdapter() instanceof ShopFootprintChildAdapter) {
            ShopFootprintChildAdapter childAdapter = (ShopFootprintChildAdapter) rcl.getAdapter();
            childAdapter.setDatas(goodsbrowseListBean.getGoods_list());
            childAdapter.setDate(goodsbrowseListBean.getDay());
            childAdapter.notifyDataSetChanged();
        }
    }



    public void setListener(FootprintListener listener) {
        this.listener = listener;
    }


    public interface FootprintListener{
        void onCLick(int item,int childPosition);
    }
}
