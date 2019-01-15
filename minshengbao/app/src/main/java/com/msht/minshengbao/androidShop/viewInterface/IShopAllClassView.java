package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.ClassDetailLeftBean;
import com.msht.minshengbao.androidShop.shopBean.ClassFirstBean;

import java.util.List;

public interface IShopAllClassView extends IBaseView{
    void onGetAllClassSuccess(List<ClassFirstBean.DatasBean.ClassListBean> list);
}
