package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class GoodFmVoucherAdpter extends HaveHeadRecyclerAdapter<VoucherBean> {


    public GoodFmVoucherAdpter(Context context, int layoutId, List<VoucherBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, VoucherBean voucherBean, final int position) {
        holder.setText(R.id.use_limit,"满"+voucherBean.getVoucher_t_limit()+"可用");
        holder.setText(R.id.amount,"¥" + voucherBean.getVoucher_t_price());
        holder.setText(R.id.time_limit,"至"+voucherBean.getVoucher_t_end_date()+"前使用");
        holder.getView(R.id.get_voucher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
