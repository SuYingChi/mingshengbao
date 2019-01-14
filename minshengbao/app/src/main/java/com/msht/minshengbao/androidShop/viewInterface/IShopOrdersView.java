package com.msht.minshengbao.androidShop.viewInterface;

public interface IShopOrdersView extends IBaseView {

    String getUrl();

    String getState_type();

    void onGetShopOrdersList(String s);
}
