package com.msht.minshengbao.androidShop.viewInterface;

import android.content.Context;

public interface IModifyCarGoodNumView extends IBaseView{

    String getCarId();

    String getCarItemNum();

    void onModifyGoodNumSuccess(String s);

    Context getViewContext();
}
