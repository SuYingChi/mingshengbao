package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class OrdersGoodChildListAdapter extends MyHaveHeadAndFootRecyclerAdapter<ComfirmShopGoodBean.GoodsBean> {

    private final OrdersChildListlistener ordersChildListlistener;

    public OrdersGoodChildListAdapter(Context context,OrdersChildListlistener ordersChildListlistener) {
        super(context, R.layout.item_orders_child_car_list);
        this.ordersChildListlistener = ordersChildListlistener;
    }

    @Override
    public void convert(RecyclerHolder holder, ComfirmShopGoodBean.GoodsBean goodsBean, final int position) {
        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            tvStore.setText(goodsBean.getStore_name());
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

        }
    }

    public interface OrdersChildListlistener {
         void onMessaged(String message);
    }

}
