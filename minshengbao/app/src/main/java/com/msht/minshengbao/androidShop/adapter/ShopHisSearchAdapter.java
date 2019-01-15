package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.util.MyBaseAdapter;
import com.msht.minshengbao.androidShop.util.ViewHolder;
import com.zhy.autolayout.utils.AutoUtils;


public class ShopHisSearchAdapter extends MyBaseAdapter<String> {


    private final DeleteItemListener deleteItemListener;

    public ShopHisSearchAdapter(Context context, DeleteItemListener deleteItemListener) {
        super(context, R.layout.item_search_history);
        this.deleteItemListener = deleteItemListener;

    }

    @Override
    public void convert(ViewHolder holder, final String itemBean, final int position) {
        holder.setText(R.id.tv, itemBean);
        ImageView iv =  holder.getView(R.id.delete);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemListener.deleteItem(position);
            }
        });
    }


    public interface DeleteItemListener {
         void deleteItem(int position);
    }
}
