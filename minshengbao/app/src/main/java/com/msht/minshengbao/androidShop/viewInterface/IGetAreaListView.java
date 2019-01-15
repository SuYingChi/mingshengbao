package com.msht.minshengbao.androidShop.viewInterface;

public interface IGetAreaListView extends IBaseView{

    void onGetAreaListSuccess(String s);

    void onGetProviceListSuccess(String s);

    void onGetCityListSuccess(String s);

    void onSelectedAreaItem(int position);

    void onSelectedCityItem(int position);

    void onSelectedProviceItem(int position);


}
