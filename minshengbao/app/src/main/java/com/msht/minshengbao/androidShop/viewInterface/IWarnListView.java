package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.WarnBean;

public interface IWarnListView extends IBaseView{
    String getPage();

    void onGetWarnListSuccess(WarnBean bean);
}
