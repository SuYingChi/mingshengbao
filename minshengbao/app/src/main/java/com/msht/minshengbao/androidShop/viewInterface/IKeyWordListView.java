package com.msht.minshengbao.androidShop.viewInterface;

import com.msht.minshengbao.androidShop.shopBean.ShopkeywordBean;

import java.util.List;

public interface IKeyWordListView extends IBaseView {

    String getKeyword();

    void onSuccess(List<ShopkeywordBean.DatasBean.GoodsListBean> list, int page_total);

    String order();

    String getCurrenPage();

    String getPage();

    String orderKey();
}
