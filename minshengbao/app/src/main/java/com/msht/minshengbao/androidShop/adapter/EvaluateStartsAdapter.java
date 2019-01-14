package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class EvaluateStartsAdapter extends MyHaveHeadAndFootRecyclerAdapter{

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    private  int starts=5;

    public EvaluateStartsAdapter(Context context, int starts) {
        super(context, R.layout.item_star);
        this.starts = starts;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        convert(holder,null,position);
    }

    @Override
    public void convert(RecyclerHolder holder, Object o, final int position) {
        if(position<starts){
            holder.setImageDrawable(R.id.iv,context.getResources().getDrawable(R.drawable.shop_star));
        }else {
            holder.setImageDrawable(R.id.iv,context.getResources().getDrawable(R.drawable.shop_star_empty));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
