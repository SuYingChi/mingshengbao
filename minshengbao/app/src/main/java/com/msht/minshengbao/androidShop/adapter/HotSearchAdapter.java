package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopSearchActivty;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class HotSearchAdapter extends MyHaveHeadViewRecyclerAdapter<String> {
    private LinearLayout.LayoutParams mLayoutParams;
    public HotSearchAdapter(Context context) {
    super(context, R.layout.item_text);
}


    @Override
    public void convert(RecyclerHolder holder, String s, final int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setBackgroundDrawable(null);
        tv.setText(s);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
