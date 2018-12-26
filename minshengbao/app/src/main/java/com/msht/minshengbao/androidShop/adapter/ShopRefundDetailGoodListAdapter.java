package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.RefundMoneyActivity;
import com.msht.minshengbao.androidShop.shopBean.RefundMoneyDetail;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class ShopRefundDetailGoodListAdapter extends MyHaveHeadAndFootRecyclerAdapter<RefundMoneyDetail.DatasBean.GoodsListBean>{

    public ShopRefundDetailGoodListAdapter(Context context) {
        super(context, R.layout.item_orders_child_list);
    }

    @Override
    public void convert(RecyclerHolder holder, final RefundMoneyDetail.DatasBean.GoodsListBean good, final int position) {
        holder.setImage(R.id.iv,good.getGoods_image());
        holder.setText(R.id.name,good.getGoods_name());
        if(good.getGoods_spec()==null||good.getGoods_spec().toString().equals("null")){
            holder.setText(R.id.desc,"");
        }else {
            holder.setText(R.id.desc,good.getGoods_spec().toString());
        }
        holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context,good.getGoods_price(),R.style.big_money,R.style.big_money));
        holder.setText(R.id.num,"x "+good.getGoods_num());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
