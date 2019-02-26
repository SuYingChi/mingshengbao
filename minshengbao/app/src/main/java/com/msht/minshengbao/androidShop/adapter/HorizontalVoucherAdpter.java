package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class HorizontalVoucherAdpter extends HaveHeadRecyclerAdapter<VoucherBean> {


    public HorizontalVoucherAdpter(Context context, int layoutId, List<VoucherBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, VoucherBean voucherBean, int position) {
        holder.setText(R.id.tv,"满"+voucherBean.getVoucher_t_limit()+"减"+voucherBean.getVoucher_t_price()+"券");
    }
}
