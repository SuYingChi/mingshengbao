package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class GoodEvaluationChildAdapter extends MyHaveHeadViewRecyclerAdapter<String>{

    private final int itemWidth;
    private final int itemImageWidth;

    public GoodEvaluationChildAdapter(Context context) {
        super(context, R.layout.item_image);
        itemWidth =  (int)(DimenUtil.getScreenWidth() / 4);
        itemImageWidth = (int)(DimenUtil.getScreenWidth() / 4 - context.getResources().getDimension(R.dimen.margin_Modules)*2);
    }

    @Override
    public void convert(RecyclerHolder holder, String s, final int position) {
        ImageView imageView = holder.getView(R.id.iv);
        holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemWidth));
        imageView.setLayoutParams(new LinearLayout.LayoutParams(itemImageWidth,itemImageWidth));
        GlideUtil.loadRemoteImg(context,imageView,s);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
