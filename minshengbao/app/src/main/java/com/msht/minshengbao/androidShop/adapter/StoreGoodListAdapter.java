package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class StoreGoodListAdapter extends HaveHeadRecyclerAdapter<StoreGoodBean> {

    public StoreGoodListAdapter(Context context, int layoutId, List<StoreGoodBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, StoreGoodBean storeGoodBean, final int position) {
        final ImageView iv = holder.getView(R.id.iv);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.width = (int) (DimenUtil.getScreenWidth() * 0.25);
        int vw = params.width - iv.getPaddingLeft() - iv.getPaddingRight();
        params.height = vw + iv.getPaddingTop() + iv.getPaddingBottom();
        iv.setLayoutParams(params);
        GlideUtil.loadRemoteImg(context, iv, storeGoodBean.getGoods_image_url());
        TextView tv = holder.getView(R.id.name);
        tv.setText(storeGoodBean.getGoods_name().trim());
        TextView tvPrice = holder.getView(R.id.price);
        tvPrice.setText(String.format("%s  %s", context.getResources().getString(R.string.monetary_unit), storeGoodBean.getGoods_price()));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
