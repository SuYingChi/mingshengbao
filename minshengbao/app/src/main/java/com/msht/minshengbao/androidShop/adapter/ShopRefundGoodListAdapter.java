package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.RefunGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class ShopRefundGoodListAdapter extends MyHaveHeadAndFootRecyclerAdapter<RefunGoodBean.DatasBean.ReturnListBean>{

    private ShopRefundGoodListener listener;

    public ShopRefundGoodListAdapter(Context context) {
        super(context, R.layout.item_return_good);
    }

    @Override
    public void convert(RecyclerHolder holder, final RefunGoodBean.DatasBean.ReturnListBean returnListBean, final int position) {
       holder.setText(R.id.store,returnListBean.getStore_name());
       holder.setText(R.id.order_state,returnListBean.getAdmin_state());
       holder.setImage(R.id.iv,returnListBean.getGoods_img_360());
       holder.setText(R.id.name,returnListBean.getGoods_name());
       holder.setText(R.id.time,returnListBean.getAdd_time());
       holder.setText(R.id.tv_amount, StringUtil.getPriceSpannable12String(context,returnListBean.getRefund_amount(),R.style.big_money,R.style.big_money));
       holder.setText(R.id.num,returnListBean.getGoods_num());
       holder.getConvertView().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onGoDetail(returnListBean.getRefund_id());
           }
       });
    }


    public void setlistener(ShopRefundGoodListener listener) {
        this.listener = listener;
    }

    public interface ShopRefundGoodListener{
        void onGoDetail(String  refundId);
    }
}
