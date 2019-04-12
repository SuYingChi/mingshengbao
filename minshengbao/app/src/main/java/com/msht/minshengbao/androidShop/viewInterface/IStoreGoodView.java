package com.msht.minshengbao.androidShop.viewInterface;

public interface IStoreGoodView extends IBaseView{
    String getStoreId();

    String getTab();

    String getRankType();

    String getCurpage();

    void onGetStoreGoodSuccess(String s);
}
