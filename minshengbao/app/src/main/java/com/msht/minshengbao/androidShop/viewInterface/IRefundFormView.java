package com.msht.minshengbao.androidShop.viewInterface;

public interface IRefundFormView extends IBaseView{
    void onGetRefundFormSuccess(String s);

    String getOrderid();

    String getRecGoodsid();
}
