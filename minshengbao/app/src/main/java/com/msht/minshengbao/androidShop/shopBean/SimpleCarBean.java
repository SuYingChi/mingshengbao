package com.msht.minshengbao.androidShop.shopBean;

public class SimpleCarBean {
    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    private  String goods_id;
    private  String cart_id;

    public SimpleCarBean(String goods_id, String cart_id) {
        this.goods_id = goods_id;
        this.cart_id = cart_id;
    }
}
