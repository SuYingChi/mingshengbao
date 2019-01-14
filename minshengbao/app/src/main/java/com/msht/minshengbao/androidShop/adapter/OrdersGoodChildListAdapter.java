package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class OrdersGoodChildListAdapter extends MyHaveHeadAndFootRecyclerAdapter<ComfirmShopGoodBean.GoodsBean> {

    private final OrdersChildListlistener ordersChildListlistener;
    private String storeName;

    public OrdersGoodChildListAdapter(Context context,OrdersChildListlistener ordersChildListlistener) {
        super(context, R.layout.item_orders_child_car_list);
        this.ordersChildListlistener = ordersChildListlistener;
    }

    @Override
    public void convert(RecyclerHolder holder, final ComfirmShopGoodBean.GoodsBean goodsBean, final int position) {
        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            tvStore.setText(storeName);
        }else if(holder.getItemViewType() == Integer.MAX_VALUE){
               EditText et = holder.getView(R.id.et_address);
               et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ordersChildListlistener.onMessaged(s.toString());
                }
            });
        } else {
            holder.setImage(R.id.iv, goodsBean.getGoods_image_url());
            holder.setText(R.id.name, goodsBean.getGoods_name());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, goodsBean.getGoods_price(), R.style.big_money, R.style.big_money));
            holder.setText(R.id.num, "X" + goodsBean.getGoods_num());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersChildListlistener.onGoGoodDetail(goodsBean.getGoods_id());
                }
            });
        }
    }

    public void setStoreName(String store_name) {
        this.storeName = store_name;
    }

    public interface OrdersChildListlistener {
         void onMessaged(String message);

        void onGoGoodDetail(String goods_id);
    }

    @Override
    public int getItemCount() {
        return (datas == null ? 0 : datas.size()+2);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == Integer.MIN_VALUE) {
            convert(holder, null, position);
        } else if (getItemViewType(position) == Integer.MAX_VALUE) {
            convert(holder, null, position);
        } else {
            convert(holder, datas.get(position-1), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && head_layoutId != Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (position == datas.size() + 1 && foot_layoutId != Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }else {
            return 1;
        }
    }
}
