package com.msht.minshengbao.androidShop.viewInterface;

public interface IBuyStep4CreatChargeView extends IBaseView{
    String getPay_sn();

    String getPayment_code();

/*    String getyfPassword();*/

    String getRcb_pay();

    String getPd_pay();

    void onCreatChargedSuccess(String s);

    String getPayAmount();

    String getChannel();
}
