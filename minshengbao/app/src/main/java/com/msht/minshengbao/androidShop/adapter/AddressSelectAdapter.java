package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.AreaBean;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;


public class AddressSelectAdapter extends CircleScrollerRecyclerAdapter<AreaBean> {


    public AddressSelectAdapter(Context context) {
        super(context, R.layout.item_class_detail_left);
    }

    @Override
    public void convert(RecyclerHolder holder, final AreaBean item, final int position) {
        TextView name = holder.getView(R.id.tv);
        name.setText(item.getArea_name());
        if(item.getSelected()){
            holder.getConvertView().setBackgroundColor(context.getResources().getColor(R.color.white));
            name.setTextSize(DimenUtil.px2dip(context.getResources().getDimension(R.dimen.text_width22)));
            name.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            holder.getConvertView().setBackgroundColor(context.getResources().getColor(R.color.nc_bg));
            name.setTextSize(DimenUtil.px2dip(context.getResources().getDimension(R.dimen.text_width16)));
            name.setTextColor(context.getResources().getColor(R.color.app_text));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

}
