package com.msht.minshengbao.androidShop.viewInterface;

public interface IGetWuliuView extends IBaseView{
    void onSelectedItem(int position);

    String getReturnId();

    void onGetReturnGoodInitSuccess(String s);

    String getExpress_id();

    String invoice_no();

    void onPostReturnGoodSuccess(String s);
}
