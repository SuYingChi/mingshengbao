package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.RefunBean;
import com.msht.minshengbao.androidShop.shopBean.RefundMoneyBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class ShopRefundMoneyListAdapter extends MyHaveHeadAndFootRecyclerAdapter<RefunBean.DatasBean.RefundListBean>{

    private ShopRefundMoneyListener listener;

    public ShopRefundMoneyListAdapter(Context context) {
        super(context, R.layout.item_car_list);
    }

    @Override
    public void convert(RecyclerHolder holder, RefunBean.DatasBean.RefundListBean refundListBean, final int position) {
       final RecyclerView rcl = holder.getView(R.id.rcl);
        //内存view 抢占了焦点 需要外层的rcl 重写ontouch事件  捕获点击事件 返回 false 继续将事件往子view传递,但此時已經跳转页面 子view收不到点击事件
       rcl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        listener.onGoDetail(position);
                        rcl.performClick();
                        break;
                }
                return false;
            }
        });
        List<RefunBean.DatasBean.RefundListBean.GoodsListBean> childList = refundListBean.getGoods_list();
        String storeName = refundListBean.getStore_name();
        String seller_state = refundListBean.getAdmin_state().equals("无")?refundListBean.getSeller_state():refundListBean.getAdmin_state();
        int total = childList.size();
        String amount = refundListBean.getRefund_amount();
       if(rcl.getAdapter()==null){
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
           //自适应自身高度
           linearLayoutManager.setAutoMeasureEnabled(true);
           rcl.setLayoutManager(linearLayoutManager);
           ShopRefundMoneysAdapter childAdapter = new ShopRefundMoneysAdapter(context,storeName,seller_state,total,amount);
           childAdapter.setHead_layoutId(R.layout.item_shop_order_child_list_head);
           childAdapter.setFoot_layoutId(R.layout.item_shop_refund_child_list_foot);
           childAdapter.setDatas(childList);
           rcl.setAdapter(childAdapter);
       }else {
           ShopRefundMoneysAdapter childAdapter = (ShopRefundMoneysAdapter) rcl.getAdapter();
           childAdapter.setAmount(amount);
           childAdapter.setSeller_state(seller_state);
           childAdapter.setTotal(total);
           childAdapter.setStoreName(storeName);
           childAdapter.setDatas(childList);
           childAdapter.notifyDataSetChanged();
       }
    }


    public void setlistener(ShopRefundMoneyListener listener) {
        this.listener = listener;
    }

    public interface ShopRefundMoneyListener{
        void onGoDetail(int position);
    }
}
