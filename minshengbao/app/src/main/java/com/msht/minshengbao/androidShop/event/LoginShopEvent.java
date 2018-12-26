package com.msht.minshengbao.androidShop.event;

import com.msht.minshengbao.androidShop.shopBean.LoginShopBean;

public class LoginShopEvent {

    private LoginShopBean bean;

    public LoginShopEvent(LoginShopBean s) {
        bean = s;
    }

    public LoginShopBean getUserLoginBean() {
        return bean;
    }
}
