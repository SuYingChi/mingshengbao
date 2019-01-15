package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopFootPrintBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

class ShopFootprintChildAdapter extends MyHaveHeadViewRecyclerAdapter<ShopFootPrintBean.DatasBean.GoodsbrowseListBean.GoodsListBean>{

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private  String date;

    public ShopFootprintChildAdapter(Context context, String date) {
        super(context, R.layout.item_footprint);
        this.date = date;
    }

    @Override
    public void convert(RecyclerHolder holder, ShopFootPrintBean.DatasBean.GoodsbrowseListBean.GoodsListBean goodsListBean, final int position) {
        if(holder.getItemViewType()==Integer.MIN_VALUE){
            holder.setText(R.id.tv,date);
        }else {
            holder.setImage(R.id.iv,goodsListBean.getGoods_image_url());
            holder.setText(R.id.name,goodsListBean.getGoods_name());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context,goodsListBean.getGoods_marketprice(),R.style.big_money,R.style.big_money));
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if(getItemViewType(position) == Integer.MIN_VALUE){
            convert(holder, null, position);
        } else {
            convert(holder, datas.get(position-1),position-1);
        }
    }
}
