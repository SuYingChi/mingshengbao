package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoodsBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_1Bean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.MyBaseAdapter;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.util.ViewHolder;


/**
 *厨电精选
 *
 */
public class ShopHomeGoodsAdapter extends MyBaseAdapter<ShopHomeGoodsBean.GoodsBean.ItemBean> {

    public ShopHomeGoodsAdapter(Context context) {
        super(context, R.layout.item_shop_home_goods_1);
    }

    @Override
    public void convert(ViewHolder holder, ShopHomeGoodsBean.GoodsBean.ItemBean itemBean,int position) {
        holder.setText(R.id.tvGoodName, itemBean.getGoods_name());
        ImageView img = holder.getView(R.id.ivGoodPic);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_stub)
                .error(R.drawable.icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()
                .priority(Priority.HIGH);

        Glide.with(context)
                .load(itemBean.getGoods_image())
                .apply(options)
                .into(img);
        TextView tvGoodPrice = holder.getView(R.id.tvGoodPrice);
        tvGoodPrice.setText(StringUtil.getPriceSpannable12String(context, itemBean.getGoods_promotion_price(), R.style.small_money, R.style.big_money));
    }
}
