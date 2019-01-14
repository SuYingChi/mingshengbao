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
    int screenWidth = DimenUtil.getScreenWidth();
    int totalPadding = DimenUtil.dip2px( 20);
    int width = (screenWidth - totalPadding) / 5;
    mLayoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    mLayoutParams.gravity= Gravity.CENTER_HORIZONTAL;
}


    @Override
    public void convert(RecyclerHolder holder, String s, final int position) {
        //动态设置外层的布局参数，来调整item间隔和大小
        holder.getConvertView().setLayoutParams(mLayoutParams);
        TextView tv = holder.getView(R.id.tv);
        tv.setPadding(0,context.getResources().getDimensionPixelSize(R.dimen.margin_Modules),0,context.getResources().getDimensionPixelSize(R.dimen.margin_Modules));
        tv.setText(s);
        tv.setBackgroundDrawable(null);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
