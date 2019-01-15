package com.msht.minshengbao.androidShop.viewInterface;

public interface IAddCompanyNormalInvView extends IBaseView{
    String getCNRecId();

    String CNinv_title();

    String getCNSbh();

    String getCNBank();

    String getCNBanknum();

    String getCNComtel();

    String getCNComaddr();

    String getCNInv_content();

    void onAddCompanyNormalSuccess(String s);

    String getCompanyInvType();
}
