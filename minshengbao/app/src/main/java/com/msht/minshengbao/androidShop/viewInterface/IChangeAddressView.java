package com.msht.minshengbao.androidShop.viewInterface;

public interface IChangeAddressView extends IBaseView{

    String getCityId();

    String getAreaId();

    String getFreight_hash();

    void onChangeAddressSuccess(String s);
}
