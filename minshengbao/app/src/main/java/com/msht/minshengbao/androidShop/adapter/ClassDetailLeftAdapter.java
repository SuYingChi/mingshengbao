package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;


public class ClassDetailLeftAdapter extends MyHaveHeadViewRecyclerAdapter<MyClassListBean> {


    public ClassDetailLeftAdapter(Context context) {
        super(context, R.layout.item_class_detail_left);
    }

    @Override
    public void convert(RecyclerHolder holder, final MyClassListBean item, final int position) {
        TextView name = holder.getView(R.id.tv);
        name.setText(item.getGc_name());
        if(item.getIsSelected()){
            holder.getConvertView().setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.getConvertView().setBackgroundColor(context.getResources().getColor(R.color.nc_bg));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

}
