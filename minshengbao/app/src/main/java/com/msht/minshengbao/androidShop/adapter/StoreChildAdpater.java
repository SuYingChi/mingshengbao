package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreClassBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class StoreChildAdpater extends HaveHeadRecyclerAdapter<StoreClassBean.ChildStoreClassBean>{


    public StoreChildAdpater(Context context, int layoutId, List<StoreClassBean.ChildStoreClassBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, StoreClassBean.ChildStoreClassBean childStoreClassBean, final int position) {
        holder.setText(R.id.tv,childStoreClassBean.getName());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

}
