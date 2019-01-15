package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.NativePayMethodsBean;
import com.msht.minshengbao.androidShop.shopBean.NativePayMethodsBean2;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class ShopPayMethodsAdapter extends MyHaveHeadAndFootRecyclerAdapter<NativePayMethodsBean2> {

    private final ShopPayAdapterListener listener;

    public ShopPayMethodsAdapter(Context context, ShopPayAdapterListener listener) {
        super(context, R.layout.item_shop_pay_way);
        this.listener = listener;
    }

    @Override
    public void convert(RecyclerHolder holder, NativePayMethodsBean2 dataBean, final int position) {
        if (holder.getItemViewType() == Integer.MAX_VALUE) {
            TextView tv = holder.getView(R.id.pay);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.pay();
                }
            });
        } else {
            ImageView iv = holder.getView(R.id.id_pay_img);
            TextView tvName = holder.getView(R.id.id_pay_name);
            final CheckBox cb = holder.getView(R.id.cb);
            cb.setChecked(dataBean.isCheck());
            int channel = dataBean.getChannel();
            switch (channel) {
                //支付宝
                case 1:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.alipay_h));
                    tvName.setText("支付宝支付");
                    break;
                //微信
                case 5:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.wechat_pay_h));
                    tvName.setText("微信支付");
                    break;
                //银联
                case 3:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.yinlian_pay_h));
                    tvName.setText("银联支付");
                    break;
                //一网通银行卡支付
                case 7:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.yiwangtong_m));
                    tvName.setText("一网通银行卡支付");
                    break;
                default:
                    break;
            }
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //选中后不可更改
                        cb.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        cb.setClickable(true);
                    }
                    listener.onCheckChange(isChecked, position);
                }
            });
        }
    }

    public interface ShopPayAdapterListener {

        void pay();

        void onCheckChange(boolean isChecked, int position);
    }
}
