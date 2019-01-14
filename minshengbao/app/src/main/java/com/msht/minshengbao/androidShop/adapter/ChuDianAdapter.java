package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_1Bean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;


public class ChuDianAdapter extends MyHaveHeadViewRecyclerAdapter<ShopHomeGoods_1Bean.Goods1Bean.ItemBean> {


    public ChuDianAdapter(Context context) {
        super(context, R.layout.item_shop_home_goods_1_2);
    }

    @Override
    public void convert(RecyclerHolder holder, final ShopHomeGoods_1Bean.Goods1Bean.ItemBean item, final int position) {
        holder.setText(R.id.tvGoodName, item.getGoods_name())
                .setImage(R.id.ivGoodPic, item.getGoods_image());
        TextView tvGoodPrice = holder.getView(R.id.tvGoodPrice);
        tvGoodPrice.setText(StringUtil.getPriceSpannable12String(context, item.getGoods_promotion_price(), R.style.small_money, R.style.big_money));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

}
