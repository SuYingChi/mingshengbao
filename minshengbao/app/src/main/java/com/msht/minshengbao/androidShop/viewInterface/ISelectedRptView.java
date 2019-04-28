package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.OrderVoucherBean;

import java.util.List;

public interface ISelectedRptView {
    void onRptItemCheckedChange(int position, String rpt_id, Boolean isCheck);

    void noSelectedRpt(boolean isChecked);
}
