package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.WarnActivity;
import com.msht.minshengbao.androidShop.shopBean.WarnBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class WarnListAdapter  extends  MyHaveHeadViewRecyclerAdapter<WarnBean.DataBean> {


    public WarnListAdapter(Context context) {
        super(context, R.layout.item_warn);
    }

    @Override
    public void convert(RecyclerHolder holder, WarnBean.DataBean dataBean, final int position) {
        holder.setText(R.id.title,dataBean.getTitle());
        holder.setText(R.id.time,dataBean.getTime());
        holder.setText(R.id.desc,dataBean.getContent());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
