package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopVouchActivity;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class VoucherAdapter extends HaveHeadRecyclerAdapter<VoucherBean>{

    private VoucherInterface listener;

    public VoucherAdapter(Context context, List<VoucherBean> voucherBeans, VoucherInterface listener) {
        super(context, R.layout.item_dicount_coupon,voucherBeans);
        this.listener =listener;
    }

    @Override
    public void convert(RecyclerHolder holder, final VoucherBean voucherBean, final int position) {
        String voucher_state = voucherBean.getVoucher_t_state();
        TextView tvuse = holder.getView(R.id.use);
        LinearLayout layout_back =  holder.getView(R.id.id_layout_back);
        TextView id_amount =  holder.getView(R.id.id_amount);
        TextView id_title_name =  holder.getView(R.id.id_title_name);
        TextView use_desc = holder.getView(R.id.use_desc);
        ImageView updown = holder.getView(R.id.updown);
        TextView show_use_desc = holder.getView(R.id.show_use_desc);
        TextView id_scope = holder.getView(R.id.id_scope);
        TextView below_amount= holder.getView(R.id.below_amount);
        TextView id_end_date = holder.getView(R.id.id_end_date);
        TextView id_effective_text = holder.getView(R.id.id_effective_text);
        holder.getView(R.id.remain_time).setVisibility(View.INVISIBLE);
        switch (voucher_state){
            case "1":
                id_amount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                id_title_name.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.black));
                layout_back .setBackgroundResource(R.drawable.left_kaqun);
                tvuse.setClickable(true);
                tvuse.setText("点击领取");
                tvuse.setTextColor(Color.WHITE);
                tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvuse.setBackgroundDrawable(MyApplication.getInstance().getResources().getDrawable(R.drawable.btn_yellow));
                tvuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onGetVoucher(voucherBean.getVoucher_t_id());
                    }
                });
                break;
            case "2":
                id_amount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                id_title_name.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                layout_back.setBackgroundResource(R.drawable.left_used_kaqun);
                tvuse.setText("已失效");
                tvuse.setClickable(false);
                tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                tvuse.setBackgroundResource(R.drawable.btn_cancle);
                break;
            default:
                id_amount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                id_title_name.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                layout_back.setBackgroundResource(R.drawable.left_used_kaqun);
                tvuse.setText("已失效");
                tvuse.setClickable(false);
                tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                tvuse.setBackgroundResource(R.drawable.btn_cancle);
                break;
        }
        if(voucherBean.isShowDesc()){
            use_desc.setVisibility(View.VISIBLE);
            updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_up_triangle));
        }else {
          updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_down_triangle));
          use_desc.setVisibility(View.GONE);
        }
        show_use_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClikshowDesc(position);
            }
        });
        updown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClikshowDesc(position);
            }
        });
        below_amount.setText("买满"+voucherBean.getVoucher_t_limit()+"可用");
        id_scope.setText(voucherBean.getVoucher_t_desc());
        id_title_name.setText(voucherBean.getVoucher_t_title());
        id_end_date.setText(voucherBean.getVoucher_t_end_date_text());
        id_amount.setText("¥" + voucherBean.getVoucher_t_price());
        id_effective_text.setText(voucherBean.getVoucher_t_start_date_text()+" ~");
        use_desc.setText(voucherBean.getVoucher_t_desc());
    }

    public interface VoucherInterface {
        void onGetVoucher(String voucher_t_id);

        void onClikshowDesc(int position);
    }
}
