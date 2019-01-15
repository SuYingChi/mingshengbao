package com.msht.minshengbao.androidShop.shopBean;

public class MyExtendOrderGoodsBean {
    public MyExtendOrderGoodsBean(String goods_image_url, String goods_name, String goods_spec, String goods_price, String goods_num) {
        this.goods_image_url = goods_image_url;
        this.goods_name = goods_name;
        this.goods_spec = goods_spec;
        this.goods_price = goods_price;
        this.goods_num = goods_num;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setGoods_spec(String goods_spec) {
        this.goods_spec = goods_spec;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    private String goods_image_url;
    private String goods_name;
    private String goods_spec;
    private String goods_price;
    private String goods_num;

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getGoods_spec() {
        return goods_spec;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public String getGoods_num() {
        return goods_num;
    }
}
