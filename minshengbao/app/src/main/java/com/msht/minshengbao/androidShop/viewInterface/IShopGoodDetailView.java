package com.msht.minshengbao.androidShop.viewInterface;

import android.content.Context;

import com.msht.minshengbao.androidShop.shopBean.GuiGeBean;

import java.util.List;

public interface IShopGoodDetailView extends IBaseView {
    void onGetGoodDetailSuccess(String s);

    String getGoodsid();

    void addCar();

    void buyGood(boolean ispingTuan);

    int getSelectedGoodNum();

    void setSelectedGoodNum(int num);

    String getNameDialog();

    String getPrice();

    String getMarketPrice();

    String getRemainNum();

    String getImageUrl();

    void onAddCarSuccess(String s);

    Context getViewContext();


    String getGuigeName();

    List<GuiGeBean> getGuigeList();


    void onSelectGoodId(int childposition);

    long getleftTime();

    void showAddCarDialog();

    int getPingTuanMaxNum();
}
