package com.msht.minshengbao.androidShop.viewInterface;

public interface IStoreSearchGoodListView extends IBaseView{
    String getStoreId();

    String getTab();

    String getRankType();

    String getCurpage();

    void onGetStoreGoodSuccess(String s);

    String getStcId();
}
