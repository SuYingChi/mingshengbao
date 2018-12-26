package com.msht.minshengbao.androidShop.viewInterface;

public interface IPostRefundAllView extends IBaseView{
    void onPostRefundSuccess(String s);

    String getOrderId();

    String getBuyerMessage();

    String getReasonId();
}
