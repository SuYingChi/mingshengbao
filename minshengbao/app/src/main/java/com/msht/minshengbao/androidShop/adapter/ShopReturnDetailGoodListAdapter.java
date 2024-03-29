package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ReturnGoodDetailActivity;
import com.msht.minshengbao.androidShop.shopBean.ReturnGoodDetailBean;
import com.msht.minshengbao.androidShop.shopBean.ReturnGoodsListBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class ShopReturnDetailGoodListAdapter extends MyHaveHeadAndFootRecyclerAdapter<ReturnGoodsListBean>{

    public ShopReturnDetailGoodListAdapter(Context context) {
        super(context, R.layout.item_orders_child_list);
    }

    @Override
    public void convert(RecyclerHolder holder, final ReturnGoodsListBean good, final int position) {
        holder.setImage(R.id.iv,good.getGoods_image());
        holder.setText(R.id.name,good.getGoods_name());
        if (good.getGoods_spec() == null || good.getGoods_spec().toString().equals("null")) {
            holder.getView(R.id.desc).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.desc).setVisibility(View.VISIBLE);
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
