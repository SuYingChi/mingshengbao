package com.msht.minshengbao.androidShop.viewInterface;

public interface IEvaluationView extends IBaseView{

    void onSuccess(String s);

    String getGoodsId();

    String getType();

    String getPage();

    String getCurrenPage();
}
