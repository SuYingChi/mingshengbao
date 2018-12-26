package com.msht.minshengbao.androidShop.viewInterface;

public interface IShopOrderDetailView extends IBaseView{
    String getOrderId();

    void onGetDetailSuccess(String s);
}
