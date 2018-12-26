package com.msht.minshengbao.androidShop.viewInterface;

public interface IAddInvView extends IBaseView{
    String getInvType();

    void onAddPersonalInvSuccess(String s);

    String getRecId();

    String getInv_content();

    String getSbh();

    String getBank();

    String getBanknum();

    String getComtel();

    String getComaddr();

    void onAddCompanyNormalSuccess(String s);
}
