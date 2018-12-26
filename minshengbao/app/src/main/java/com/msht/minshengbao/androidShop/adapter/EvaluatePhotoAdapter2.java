package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

class EvaluatePhotoAdapter2 extends MyHaveHeadAndFootRecyclerAdapter<String>{

    private final int itemWidth;
    private final int itemImageWidth;

    public EvaluatePhotoAdapter2(Context context) {
        super(context, R.layout.item_image);
        itemWidth =  (int)(DimenUtil.getScreenWidth() / 4);
        itemImageWidth = (int)(DimenUtil.getScreenWidth() / 4 - context.getResources().getDimension(R.dimen.margin_Modules)*2);
    }

    @Override
    public void convert(RecyclerHolder holder, String s, final int position) {
        ImageView imageView = holder.getView(R.id.iv);
        holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemWidth));
        imageView.setLayoutParams(new LinearLayout.LayoutParams(itemImageWidth,itemImageWidth));
        if (position == datas.size()) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_addpic_unfocused));
            if (position == 5){
                holder.getConvertView().setVisibility(View.GONE);
            }
        } else {
            imageView.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(s, itemImageWidth, itemImageWidth));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if(datas.size()==0||position==datas.size()){
            convert(holder,"",position);
        }else {
            convert(holder,datas.get(position),position);
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        if (datas.size() == 5) {
            return 5;
        } else {
            return datas.size() + 1;
        }
    }

}
