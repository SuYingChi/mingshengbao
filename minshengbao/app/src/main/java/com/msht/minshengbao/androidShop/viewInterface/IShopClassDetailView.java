package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.MyClassListBean;

import java.util.ArrayList;
import java.util.List;

public interface IShopClassDetailView extends IBaseView{
    void onLeftSuccess(ArrayList<MyClassListBean> bean);

    String getGcId();

    String getRightGcId();

    String getRightCurrenPage();

    String getCurrenPage();

    void onRightRclSuccess(List<ClassDetailRightBean.DatasBean.GoodsListBean> list, int page_total);
}
