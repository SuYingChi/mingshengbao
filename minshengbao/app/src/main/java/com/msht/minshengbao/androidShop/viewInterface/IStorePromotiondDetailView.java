package com.msht.minshengbao.androidShop.viewInterface;

public interface IStorePromotiondDetailView extends IBaseView{
    String getPromotionType();

    String getPromotionId();

    void onGetStorePromotionDetailSuccess(String s);

    String getStoreId();
}
