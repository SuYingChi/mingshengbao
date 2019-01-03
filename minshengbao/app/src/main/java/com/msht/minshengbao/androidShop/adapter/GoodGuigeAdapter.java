package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.GuiGeBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class GoodGuigeAdapter extends MyHaveHeadViewRecyclerAdapter<GuiGeBean>{



    public GoodGuigeAdapter(Context context) {
        super(context, R.layout.item_text);
    }

    @Override
    public void convert(RecyclerHolder holder, GuiGeBean bean, final int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(bean.getGuigeId());
        if(bean.isCheck()){
            tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_normal));
            tv.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_normal_unselect));
            tv.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
