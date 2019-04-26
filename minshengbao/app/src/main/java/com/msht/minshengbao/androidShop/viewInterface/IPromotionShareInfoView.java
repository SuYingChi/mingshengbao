package com.msht.minshengbao.androidShop.viewInterface;

public interface IPromotionShareInfoView extends IBaseView{

    void onGetPromotionShareInfo(String s);

    String getStoreId();

    String getPromotionId();

    String getPromotionType();
}
