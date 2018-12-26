package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyExtendOrderGoodsBean;
import com.msht.minshengbao.androidShop.shopBean.OrderslistBean;
import com.msht.minshengbao.androidShop.shopBean.ZengpingBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class ShopOrdersListAdapter extends MyHaveHeadAndFootRecyclerAdapter<OrderslistBean> {

    private OrdersListListener listener;
    private final int CANCELED_ORDER = 0;
    private final int PAYED_ORDER = 20;
    private final int WAIT_TO_PAY_ORDER = 10;
    private final int SENDED_ORDER = 30;
    private final int FINISHED_ORDER = 40;
    private final int AUTO_CANCLE_ORDER = 24;
    private final int AUTO_RECEIVE_ORDER = 7;

    public ShopOrdersListAdapter(Context context) {
        super(context, R.layout.item_car_list);
    }

    @Override
    public void convert(RecyclerHolder holder, final OrderslistBean orderslistBean, final int position) {
        RecyclerView rcl = holder.getView(R.id.rcl);
        List<TextView> btnList = orderslistBean.getBtnList();
        List<MyExtendOrderGoodsBean> childList = orderslistBean.getMyExtendOrderGoods();
        int total = 0;
        for (int i = 0; i < childList.size(); i++) {
            if (childList.get(i) != null) {
                total += Integer.valueOf(childList.get(i).getGoods_num());
            }
        }
        if (rcl.getAdapter() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            //自适应自身高度
            linearLayoutManager.setAutoMeasureEnabled(true);
            rcl.setLayoutManager(linearLayoutManager);
            ShopOrdersChildAdapter childAdapter = new ShopOrdersChildAdapter(context, orderslistBean.getStore_name(), orderslistBean.getPay_amount(), orderslistBean.getAdd_time(), orderslistBean.getPay_sn(), orderslistBean.getShipping_fee(), orderslistBean.getOrder_desc(), btnList, total,orderslistBean.getZengpings());
            childAdapter.setHead_layoutId(R.layout.item_shop_order_child_list_head);
            childAdapter.setFoot_layoutId(R.layout.item_shop_orders_child_list_foot);
            childAdapter.setDatas(childList);
            childAdapter.setListener(new ShopOrdersChildAdapter.OrderListener() {
                @Override
                public void onGoDetail() {
                    listener.onGoDetail(orderslistBean.getOrderId());
                }
            });
            rcl.setAdapter(childAdapter);
        } else if (rcl.getAdapter() instanceof ShopOrdersChildAdapter) {
            ShopOrdersChildAdapter childAdapter = (ShopOrdersChildAdapter) rcl.getAdapter();
            childAdapter.setTotalNum(total);
            childAdapter.setStoreName(orderslistBean.getStore_name());
            childAdapter.setState_desc(orderslistBean.getOrder_desc());
            childAdapter.setShipping_fee(orderslistBean.getShipping_fee());
            childAdapter.setPaySn(orderslistBean.getPay_sn());
            childAdapter.setPayAmount(orderslistBean.getPay_amount());
            childAdapter.setAddTime(orderslistBean.getAdd_time());
            childAdapter.setBtns(btnList);
            childAdapter.setZengpings(orderslistBean.getZengpings());
            childAdapter.setDatas(childList);
            childAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (datas.get(position).getOrder_state()) {
            case "0":
                return CANCELED_ORDER;
            case "10":
                return WAIT_TO_PAY_ORDER;
            case "20":
                return PAYED_ORDER;
            case "30":
                return SENDED_ORDER;
            case "40":
                return FINISHED_ORDER;
            case "24":
                return AUTO_CANCLE_ORDER;
            case "7":
                return AUTO_RECEIVE_ORDER;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        convert(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setlistener(OrdersListListener listener) {
        this.listener = listener;
    }

    public interface OrdersListListener {


        void onGoDetail(String goodId);
    }
}
