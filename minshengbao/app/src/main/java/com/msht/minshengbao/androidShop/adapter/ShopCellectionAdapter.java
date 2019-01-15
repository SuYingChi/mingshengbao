package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopCellectionBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class ShopCellectionAdapter extends MyHaveHeadViewRecyclerAdapter<ShopCellectionBean.DatasBean.FavoritesListBean>{


    private final DeleteItemListner listner;

    public ShopCellectionAdapter(Context contex, DeleteItemListner listner) {
        super(contex, R.layout.item_shop_collection);
        this.listner=listner;
    }

    @Override
    public void convert(RecyclerHolder holder, ShopCellectionBean.DatasBean.FavoritesListBean goodsListBean, final int position) {
            holder.setImage(R.id.iv,goodsListBean.getGoods_image_url());
            holder.setText(R.id.name,goodsListBean.getGoods_name());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context,goodsListBean.getGoods_price(),R.style.big_money,R.style.big_money));
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(position);
                }
            });
            holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.deleteItem(position);
                }
            });
    }
    public interface DeleteItemListner{
        void deleteItem(int position);

        void onItemClick(int position);
    }
}
