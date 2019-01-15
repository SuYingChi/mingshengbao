package com.msht.minshengbao.androidShop.shopBean;

import java.util.ArrayList;
import java.util.List;

public class MyEvaluateShopOrderBean {
    public int getTextNum() {
        return textNum;
    }

    public void setTextNum(int textNum) {
        this.textNum = textNum;
    }

    private  int textNum;

    public String getEvaluateText() {
        return evaluateText;
    }

    public void setEvaluateText(String evaluateText) {
        this.evaluateText = evaluateText;
    }

    private String evaluateText;

    public boolean isNiming() {
        return isNiming;
    }

    public void setNiming(boolean niming) {
        isNiming = niming;
    }

    private  boolean isNiming;

    public String getImaUrl() {
        return imaUrl;
    }

    public void setImaUrl(String imaUrl) {
        this.imaUrl = imaUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public ArrayList<String> getImagePathList() {
        return imagePathList;
    }

    public void setImagePathList(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
    }


    private  String imaUrl;
    private  String name;
    private  String goodId;
    private  int starts;
    private  ArrayList<String> imagePathList;

    public ArrayList<String> getToUploadimagePathList() {
        return toUploadimagePathList;
    }

    public void setToUploadimagePathList(ArrayList<String> toUploadimagePathList) {
        this.toUploadimagePathList = toUploadimagePathList;
    }

    private  ArrayList<String> toUploadimagePathList;

    public MyEvaluateShopOrderBean(String imaUrl, String name, String goodId, int starts, ArrayList<String> imagePathList,ArrayList<String> toUploadimagePathList,boolean isNiming,String evaluateText,int textNum) {
        this.imaUrl = imaUrl;
        this.name = name;
        this.goodId = goodId;
        this.starts = starts;
        this.imagePathList = imagePathList;
        this.isNiming = isNiming;
        this.evaluateText = evaluateText;
        this.textNum = textNum;
        this.toUploadimagePathList = toUploadimagePathList;
    }

    @Override
    public String toString() {
        return "MyEvaluateShopOrderBean{" +
                "textNum=" + textNum +
                ", evaluateText='" + evaluateText + '\'' +
                ", isNiming=" + isNiming +
                ", imaUrl='" + imaUrl + '\'' +
                ", name='" + name + '\'' +
                ", goodId='" + goodId + '\'' +
                ", starts=" + starts +
                ", imagePathList=" + imagePathList +
                ", toUploadimagePathList=" + toUploadimagePathList +
                '}';
    }
}
