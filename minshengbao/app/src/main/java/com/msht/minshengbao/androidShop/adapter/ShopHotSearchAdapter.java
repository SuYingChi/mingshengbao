package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.GoodCommendBean;
import com.msht.minshengbao.androidShop.util.MyBaseAdapter;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.util.ViewHolder;



public class ShopHotSearchAdapter extends MyBaseAdapter<String> {

    public ShopHotSearchAdapter(Context context) {
        super(context, R.layout.item_class_detail_left);
    }

    @Override
    public void convert(ViewHolder holder, String itemBean,int position) {
        holder.setText(R.id.tv, itemBean);
    }
}
