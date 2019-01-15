package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_1Bean;
import com.msht.minshengbao.androidShop.util.MyBaseAdapter;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.util.ViewHolder;


/**
 *厨电精选
 *
 */
public class ShopHomeGoods_1Adapter extends MyBaseAdapter<ShopHomeGoods_1Bean.Goods1Bean.ItemBean> {

    public ShopHomeGoods_1Adapter(Context context) {
        super(context, R.layout.item_shop_home_goods_1);
    }

    @Override
    public void convert(ViewHolder holder, ShopHomeGoods_1Bean.Goods1Bean.ItemBean ItemBean,int position) {
        holder.setText(R.id.tvGoodName, ItemBean.getGoods_name())
                .setImage(R.id.ivGoodPic, ItemBean.getGoods_image());
        TextView tvGoodPrice = holder.getView(R.id.tvGoodPrice);
        tvGoodPrice.setText(StringUtil.getPriceSpannable12String(context, ItemBean.getGoods_promotion_price(), R.style.small_money, R.style.big_money));
    }
}
