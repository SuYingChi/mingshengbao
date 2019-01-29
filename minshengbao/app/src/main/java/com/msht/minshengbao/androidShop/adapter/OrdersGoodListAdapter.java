package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class OrdersGoodListAdapter extends MyHaveHeadViewRecyclerAdapter <ComfirmShopGoodBean>{

    private  OrdersListListener ordersListListener;

    public OrdersGoodListAdapter(Context context,  OrdersListListener ordersListListener) {
        super(context, R.layout.item_car_list);
        this.ordersListListener = ordersListListener;
    }
    @Override
    public void convert(RecyclerHolder holder, ComfirmShopGoodBean comfirmShopGoodBean, final int position) {
        RecyclerView rcl = holder.getView(R.id.rcl);
        if (rcl.getAdapter() == null) {
             OrdersGoodChildListAdapter childAdapter = new OrdersGoodChildListAdapter(context, new OrdersGoodChildListAdapter.OrdersChildListlistener() {
                @Override
                public void onMessaged(String message) {
                    ordersListListener.onMessaged(message,position);
                }

                 @Override
                 public void onGoGoodDetail(String goods_id) {
                     ordersListListener.onGoGoodDetail(goods_id);
                 }

                 @Override
                 public void etVisible(boolean etVisible) {
                     ordersListListener.etVisible(etVisible,position);
                 }

                 @Override
                 public void onInputUserId(String s) {
                     ordersListListener.onInputUserId(s,position);
                 }
             });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            //自适应自身高度
            linearLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(linearLayoutManager);
            //list 是地址引用，在这里对list添加删除元素，在activity那边也是添加删除元素
            List<ComfirmShopGoodBean.GoodsBean> childlist = comfirmShopGoodBean.getGoods();
            childAdapter.setHead_layoutId(R.layout.item_order_child_list_head);
            childAdapter.setFoot_layoutId(R.layout.items_orders_child_car_list_foot);
            childAdapter.setStoreName(comfirmShopGoodBean.getStore_name());
            childAdapter.setStoreDoorService(comfirmShopGoodBean.getStoreDoorService());
            childAdapter.setIsNeedEtVisible(comfirmShopGoodBean.getIsNeedEtVisible());
            childAdapter.setDatas(childlist);
            rcl.setAdapter(childAdapter);
        } else if (rcl.getAdapter() instanceof OrdersGoodChildListAdapter) {
            OrdersGoodChildListAdapter childAdapter = (OrdersGoodChildListAdapter) rcl.getAdapter();
            childAdapter.setStoreName(comfirmShopGoodBean.getStore_name());
            childAdapter.setStoreDoorService(comfirmShopGoodBean.getStoreDoorService());
            childAdapter.setIsNeedEtVisible(comfirmShopGoodBean.getIsNeedEtVisible());
            childAdapter.notifyDataSetChanged();
        }
    }
    public interface  OrdersListListener{
        void onMessaged(String message,int position);

        void onGoGoodDetail(String goods_id);

        void etVisible(boolean etVisible,int position);

        void onInputUserId(String s, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
