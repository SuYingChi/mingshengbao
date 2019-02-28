package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.OrderVoucherBean;

import java.util.List;

public interface ISelectedVoucherView  {

    void onItemCheckedChange(List<OrderVoucherBean> datas, int position, Boolean isCheck);

    void noSelectedVoucher(List<OrderVoucherBean> datas, boolean isChecked);
}
