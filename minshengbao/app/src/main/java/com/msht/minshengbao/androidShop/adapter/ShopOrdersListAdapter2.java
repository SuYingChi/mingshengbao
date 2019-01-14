package com.msht.minshengbao.androidShop.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.msht.minshengbao.androidShop.shopBean.OrderslistBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShopOrdersListAdapter2 extends RecyclerView.Adapter {
    List<OrderslistBean> datas  = new ArrayList<OrderslistBean>();
    private final int CANCELED_ORDER = 0;
    private final int PAYED_ORDER = 20;
    private final int WAIT_TO_PAY_ORDER = 10;
    private final int SENDED_ORDER = 30;
    private final int FINISHED_ORDER = 40;
    private final int AUTO_CANCLE_ORDER = 24;
    private final int AUTO_RECEIVE_ORDER = 7;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
                    return super.getItemViewType(position);
            }
    }

}
