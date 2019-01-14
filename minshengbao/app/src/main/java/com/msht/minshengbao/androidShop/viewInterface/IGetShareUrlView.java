package com.msht.minshengbao.androidShop.viewInterface;

public interface IGetShareUrlView extends IBaseView{
    String getGoodId();

    void onGetShareUrlSuccess(String s,String type);

}
