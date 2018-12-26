package com.msht.minshengbao.androidShop.shopBean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecommendBean implements Serializable{

    public RecommendBean(String recommend_phone, String defaultX) {
        this.recommend_phone = recommend_phone;
        this.defaultX = defaultX;
    }

    /**
     * recommend_phone :
     * default : 1
     */

    private String recommend_phone;
    @SerializedName("default")
    private String defaultX;

    public String getRecommend_phone() {
        return recommend_phone;
    }

    public void setRecommend_phone(String recommend_phone) {
        this.recommend_phone = recommend_phone;
    }

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }





    @Override
    public String toString() {
        return "RecommendBean{" +
                "recommend_phone='" + recommend_phone + '\'' +
                ", defaultX=" + defaultX +
                '}';
    }
}
