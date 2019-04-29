package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreNewGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import java.util.List;

class ShopNewGoodChildAdapter extends HaveHeadRecyclerAdapter<StoreNewGoodBean.GoodBean>{


    public ShopNewGoodChildAdapter(Context context,  List<StoreNewGoodBean.GoodBean> datas) {
        super(context, R.layout.item_store_rec_good,datas);
    }

    @Override
    public void convert(RecyclerHolder holder, StoreNewGoodBean.GoodBean goodBean, final int position) {
            holder.setImage(R.id.iv,goodBean.getGoods_image_url());
            holder.setText(R.id.name,goodBean.getGoods_name());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context,goodBean.getGoods_marketprice(),R.style.big_money,R.style.big_money));
           holder.getConvertView().setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onItemClickListener.onItemClick(position);
               }
           });
    }
}
