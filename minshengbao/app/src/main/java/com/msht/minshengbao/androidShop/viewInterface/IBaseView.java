package com.msht.minshengbao.androidShop.viewInterface;

public  interface IBaseView {
    void showLoading();

    void dismissLoading();

    void onError(String s);

    String getKey();

    String getUserId();

    String getLoginPassword();

   void onLogout();

    void onNetError();




}
