package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.AreaBean;

import java.util.List;

public interface IAddAddressView extends IBaseView{

    void onAddAddressSuccess(String s);

    String getTrue_name();

    String getCity_id();

    String getArea_id();

    String getArea_info();

    String getAddress();

    String getTel_phone();

    String getMob_phone();

    String isDefault();


}
