package com.msht.minshengbao.androidShop.viewInterface;

public interface IBuyStep1View extends IBaseView{
    String getCarId();

    String ifCarted();

    String ifPickupSelf();

    String getAddressid();

    void onBuyStep1Success(String s);
}
