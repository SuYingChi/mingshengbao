package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;



public class ShopClassGridAdapter extends MyHaveHeadViewRecyclerAdapter<ShopHomeClassBean.ClassBean.ItemBean> {

    private LinearLayout.LayoutParams mLayoutParams;
    public static final String TAG = "ShopClassGridAdapter";

    public ShopClassGridAdapter(Context context) {
        super(context, R.layout.recyclerview_home_hotnav_item);
        int screenWidth = DimenUtil.getScreenWidth();
        int totalPadding = DimenUtil.dip2px( 50) * 2;
        int width = (screenWidth - totalPadding) / 4;
        mLayoutParams = new LinearLayout.LayoutParams(width, width);
    }

    @Override
    public void convert(RecyclerHolder holder, final ShopHomeClassBean.ClassBean.ItemBean itemNav, final int position) {
        ImageView ivPic = holder.getView(R.id.ivPic);
        ivPic.setLayoutParams(mLayoutParams);
        TextView tv = holder.getView(R.id.tv);
        tv.setText(itemNav.getTitle());
        GlideUtil.loadRemoteImg(context,ivPic,itemNav.getImage());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
