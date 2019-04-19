package com.msht.minshengbao.androidShop.shopBean;

public class PromotionActivityGoodBean {
    public String getGoodId() {
        return goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public String getGoodPromotionPrice() {
        return goodPromotionPrice;
    }

    public String getGoodImage() {
        return goodImage;
    }

    private String goodId;
    private String goodName;
    private String goodPromotionPrice;
    private String goodImage;

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public void setGoodPromotionPrice(String goodPromotionPrice) {
        this.goodPromotionPrice = goodPromotionPrice;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }
}
