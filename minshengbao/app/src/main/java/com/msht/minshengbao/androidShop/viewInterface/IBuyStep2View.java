package com.msht.minshengbao.androidShop.viewInterface;

public interface IBuyStep2View extends IBaseView{

    String getDelivery();

    String getPayName();

    String getInvoiceIds();

    String getVoucher();

    String getPd_pay();

    String getPassword();

    String getFcode();

    String getRcb_pay();

    String getRpt();

    String getPay_message();

    void onBuyStep2Success(String s);

    String getDlypId();

    String getPingTuanId();

    String getBuyerId();

    String getIsPinTuan();
}
