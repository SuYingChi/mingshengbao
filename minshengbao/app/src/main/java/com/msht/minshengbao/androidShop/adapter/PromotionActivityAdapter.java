package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.event.RefreshFinish;
import com.msht.minshengbao.androidShop.shopBean.PromotionActivityGoodBean;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PromotionActivityAdapter extends HaveHeadRecyclerAdapter<PromotionActivityGoodBean>{
    public PromotionActivityAdapter(Context context, int layoutId, List<PromotionActivityGoodBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, PromotionActivityGoodBean storeGoodBean, int position) {
        holder.setImage(R.id.iv, storeGoodBean.getGoodImage());
        holder.setText(R.id.name, storeGoodBean.getGoodName());
        holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, storeGoodBean.getGoodPromotionPrice(),R.style.small_money,R.style.small_money));
    }
}
