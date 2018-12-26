package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.RecommendActivity;
import com.msht.minshengbao.androidShop.shopBean.RecommendBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class RecommendListAdapter extends MyHaveHeadViewRecyclerAdapter<RecommendBean>{

    public RecommendListAdapter(Context context) {
        super(context, R.layout.item_class_detail_left);
    }

    @Override
    public void convert(RecyclerHolder holder, RecommendBean recommendBean, final int position) {
        holder.setText(R.id.tv,recommendBean.getRecommend_phone());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
