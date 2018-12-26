package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopkeywordBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class KeywordListAdapterHaveHeadView extends MyHaveHeadViewRecyclerAdapter<ShopkeywordBean.DatasBean.GoodsListBean> {

    public KeywordListAdapterHaveHeadView(Context context) {
        super(context, R.layout.item_keyword_search);
    }

    @Override
    public void convert(RecyclerHolder holder, final ShopkeywordBean.DatasBean.GoodsListBean goodsListBean, final int position) {
        final ImageView iv = holder.getView(R.id.iv);
        iv.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.width = (int) (DimenUtil.getScreenWidth() * 0.25);
                int vw = params.width - iv.getPaddingLeft() - iv.getPaddingRight();
                params.height = vw + iv.getPaddingTop() + iv.getPaddingBottom();
                iv.setLayoutParams(params);
                GlideUtil.loadByImageView(context, iv, goodsListBean.getGoods_image_url());
            }
        });
        TextView tv = holder.getView(R.id.name);
        tv.setText(goodsListBean.getGoods_name());
        TextView tv2 = holder.getView(R.id.name2);
        tv2.setText(goodsListBean.getGoods_jingle());
        TextView tv3 = holder.getView(R.id.sell);
        tv3.setText(String.format("%s人已买", goodsListBean.getGoods_salenum()));
        TextView tvPrice = holder.getView(R.id.price);
        tvPrice.setText(String.format("%s  %s", context.getResources().getString(R.string.monetary_unit), goodsListBean.getGoods_price()));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}

