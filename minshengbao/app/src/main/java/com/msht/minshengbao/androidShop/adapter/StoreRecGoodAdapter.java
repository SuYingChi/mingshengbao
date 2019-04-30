package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.event.RefreshFinish;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class StoreRecGoodAdapter extends HaveHeadRecyclerAdapter<StoreGoodBean>{
    public StoreRecGoodAdapter(Context context, int layoutId, List<StoreGoodBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, StoreGoodBean storeGoodBean, final int position) {
        holder.setImage(R.id.iv, storeGoodBean.getGoods_image_url());
        holder.setText(R.id.name, storeGoodBean.getGoods_name());
        holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, storeGoodBean.getGoods_price(),R.style.small_money,R.style.small_money));
        if(position==datas.size()){
            EventBus.getDefault().post(new RefreshFinish(3));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}