package com.msht.minshengbao.androidShop.viewInterface;

public interface GoodDetailActivityListener {

    void onScrollChange1();

    void onScrollChange2(int y, int bannerHeight);

    void onScrollChange3();

    void onScrollChange4();

    void onScrollChange5();

    void hasAddedCar(String totalAddedCarNum);

    void noAddedCar();

    void onStorageChange(int goods_storage);

    void onGetGoodDetailSuccess();

    void goEveluateFragment();

    void showBottomDialog();

    void isPingTuan(boolean isPingTuan);

    void isAllowNewPingTuan(boolean b);
}
