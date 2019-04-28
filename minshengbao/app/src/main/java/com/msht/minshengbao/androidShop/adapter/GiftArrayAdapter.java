package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.GiftBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class GiftArrayAdapter extends HaveHeadRecyclerAdapter<GiftBean>{

    public GiftArrayAdapter(Context context, List<GiftBean> datas) {
        super(context, R.layout.item_gift, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, GiftBean giftBean, int position) {
         holder.setText(R.id.num,giftBean.getGift_amount());
         holder.setText(R.id.name,giftBean.getGift_goodsname());
    }
}
