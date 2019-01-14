package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.RefunBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

class ShopRefundMoneysAdapter extends MyHaveHeadAndFootRecyclerAdapter<RefunBean.DatasBean.RefundListBean.GoodsListBean>{


    private  String storeName;


    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }



    public void setSeller_state(String seller_state) {
        this.seller_state = seller_state;
    }



    public void setTotal(int total) {
        this.total = total;
    }



    public void setAmount(String amount) {
        this.amount = amount;
    }

    private  String seller_state;
    private  int total;
    private  String amount;

    public ShopRefundMoneysAdapter(Context context, String storeName, String seller_state, int total, String amount) {
        super(context,R.layout.item_shop_refund_money_child);
        this.storeName= storeName;
        this.seller_state = seller_state;
        this.total= total;
        this.amount =amount;
    }

    @Override
    public void convert(RecyclerHolder holder, RefunBean.DatasBean.RefundListBean.GoodsListBean goodsListBean, int position) {
        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            tvStore.setText(storeName);
            holder.setText(R.id.order_state, seller_state);
        } else if (holder.getItemViewType() == Integer.MAX_VALUE) {
            TextView tvTotalNum = holder.getView(R.id.total_num);
            tvTotalNum.setText("共" + total + "件商品，合计");
            TextView tvPrice = holder.getView(R.id.price);
            tvPrice.setText(StringUtil.getPriceSpannable12String(context, amount + "", R.style.big_money, R.style.big_money));
        } else if (holder.getItemViewType() == 100) {
            holder.setImage(R.id.iv, goodsListBean.getGoods_img_360());
            holder.setText(R.id.name, goodsListBean.getGoods_name());
            if (goodsListBean.getGoods_spec() == null || goodsListBean.getGoods_spec().toString().equals("null")) {
                holder.getView(R.id.desc).setVisibility(View.GONE);
            } else {
                holder.getView(R.id.desc).setVisibility(View.VISIBLE);
                holder.setText(R.id.desc,goodsListBean.getGoods_spec());
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Integer.MIN_VALUE;
        } else if (position == datas.size() + 1) {
            return Integer.MAX_VALUE;
        } else if (position > 0 && position <= datas.size()) {
            return 100;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 2;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == Integer.MIN_VALUE) {
            convert(holder, null, 0);
        } else if (getItemViewType(position) == Integer.MAX_VALUE) {
            convert(holder, null, datas.size() + 1);
        } else if (getItemViewType(position) == 100) {
            convert(holder, datas.get(position-1), position-1);
        }
    }
}
