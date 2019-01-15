package com.msht.minshengbao.androidShop.viewInterface;

public interface IPostRefundView extends IBaseView{
    String getOrderId();

    String getReasonId();

    String getBuyerMessage();

    String getOrder_goods_id();

    String getRefund_amount();

    String getGoods_num();

    String getRefund_type();

    void onPostRefundSuccess(String s);
}
