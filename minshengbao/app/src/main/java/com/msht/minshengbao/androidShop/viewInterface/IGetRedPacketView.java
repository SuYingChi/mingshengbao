package com.msht.minshengbao.androidShop.viewInterface;

public interface IGetRedPacketView extends IBaseView{
    String getPwdCode();

    void onGetRedPacket(String s);

    String getCodekey();

    String getCaptcha();
}
