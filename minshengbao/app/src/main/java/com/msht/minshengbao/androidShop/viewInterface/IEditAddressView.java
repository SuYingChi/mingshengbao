package com.msht.minshengbao.androidShop.viewInterface;

public interface IEditAddressView extends IBaseView{
    void onEditAddressSuccess();

    String getAddress_id();

    String getTrue_name();

    String getCity_id();

    String getArea_id();

    String getArea_info();

    String getAddress();

    String getTel_phone();

    String getMob_phone();

    String isDefault();
}
