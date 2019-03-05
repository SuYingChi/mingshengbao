package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.OrderVoucherBean;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.viewInterface.ISelectedVoucherView;

import java.util.List;

public class OrderVoucherListAdpter extends HaveHeadAndFootRecyclerAdapter<OrderVoucherBean> {


    private ISelectedVoucherView iSelectedVoucherView;
    private boolean isUseVoucher = true;

    public OrderVoucherListAdpter(Context context, int layoutId, List<OrderVoucherBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, OrderVoucherBean voucherBean, final int position) {
        if (holder.getItemViewType() == Integer.MAX_VALUE) {
            final CheckBox checkBox = (CheckBox) holder.getView(R.id.select);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //选中后不可更改
                        checkBox.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        checkBox.setClickable(true);
                    }
                    iSelectedVoucherView.noSelectedVoucher(datas, isChecked);
                }
            });
            checkBox.setChecked(!isUseVoucher);
        } else {
            holder.setText(R.id.user_limit, "使用说明:订单满" + voucherBean.getVlimitAmount() + "元可用");
            holder.setText(R.id.amount, "¥" + voucherBean.getVoucherPrice());
            holder.setText(R.id.time_limit, "有效期" + voucherBean.getStartLimit() + "—" + voucherBean.getLimitTime());
            holder.setText(R.id.title, voucherBean.getVtitle());
            final CheckBox checkBox = (CheckBox) holder.getView(R.id.select);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //选中后不可更改
                        checkBox.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        checkBox.setClickable(true);
                    }
                    //用户check的状态
                    iSelectedVoucherView.onItemCheckedChange(datas, position, isChecked);
                }
            });
            //列表数据刷新的是绑定的数据，因为与之前用户check的状态一致，不会再走进去oncheckchange
            checkBox.setChecked(voucherBean.isSelected());
        }
    }

    public void setOrderVoucherListener(ISelectedVoucherView iSelectedVoucherView) {
        this.iSelectedVoucherView = iSelectedVoucherView;
    }

    public void isUseVoucher(boolean isUseVoucher) {
        this.isUseVoucher = isUseVoucher;
    }
}
