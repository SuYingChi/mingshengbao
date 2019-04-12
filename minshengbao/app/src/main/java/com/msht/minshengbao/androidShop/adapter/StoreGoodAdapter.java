package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreGoodBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import java.util.List;

public class StoreGoodAdapter extends HaveHeadRecyclerAdapter<StoreGoodBean>{
    public StoreGoodAdapter(Context context, int layoutId, List<StoreGoodBean> datas) {
        super(context, layoutId, datas);
    }
    @Override
    public void convert(RecyclerHolder holder, StoreGoodBean storeGoodBean, int position) {
        ViewGroup.LayoutParams lp = holder.getConvertView().getLayoutParams();
        lp.width= DimenUtil.getScreenWidth()/3;
        holder.getConvertView().setLayoutParams(lp);
        holder.setImage(R.id.iv, storeGoodBean.getGoods_image_url());
        holder.setText(R.id.name, storeGoodBean.getGoods_name());
        holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, storeGoodBean.getGoods_price(),R.style.small_money,R.style.small_money));
    }
}
