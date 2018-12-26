package com.msht.minshengbao.androidShop.shopBean;

public class ReturnGoodsListBean {
    public String getGoodId() {
        return goodId;
    }

    private String goodId;
    private String goods_image;
    private String goods_name;
    private Object goods_spec;
    private String goods_num;
    private String goods_price;

    public ReturnGoodsListBean(String goods_image, String goods_name, Object goods_spec, String goods_price, String goods_num, String goods_id) {
        this.goods_image = goods_image;
        this.goods_name = goods_name;
        this.goods_spec = goods_spec;
        this.goods_price = goods_price;
        this.goods_num = goods_num;
        this.goodId = goods_id;

    }


    public String getGoods_image() {
        return goods_image;
    }

    public String getGoods_name() {
        return goods_name;
    }


    public Object getGoods_spec() {
        return goods_spec;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public String getGoods_price() {
        return goods_price;
    }
}
