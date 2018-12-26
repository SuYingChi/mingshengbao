package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.GuessLikeBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoodsBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.util.ViewHolder;

public class GuessLikeAdapter extends MyHaveHeadViewRecyclerAdapter<GuessLikeBean.DatasBean.GuessLikeListBean> {


    public GuessLikeAdapter(Context context) {
        super(context, R.layout.item_shop_home_goods_1);
    }

    @Override
    public void convert(RecyclerHolder holder, GuessLikeBean.DatasBean.GuessLikeListBean itemBean, final int position) {
        holder.setText(R.id.tvGoodName, itemBean.getGoods_name())
                .setImage(R.id.ivGoodPic, itemBean.getGoods_image());
        TextView tvGoodPrice = holder.getView(R.id.tvGoodPrice);
        tvGoodPrice.setText(StringUtil.getPriceSpannable12String(context, itemBean.getGoods_promotion_price(), R.style.small_money, R.style.big_money));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
