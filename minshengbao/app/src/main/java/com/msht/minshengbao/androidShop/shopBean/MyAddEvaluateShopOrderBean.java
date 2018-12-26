package com.msht.minshengbao.androidShop.shopBean;

import java.util.ArrayList;

public class MyAddEvaluateShopOrderBean {
    private String imaUrl;
    private String name;
    private String comment;
    private String geval_content;
    private int textNum;
    private ArrayList<String> imagePathList;
    private ArrayList<String> toUploadimagePathList;
    private String gevalId;

    public MyAddEvaluateShopOrderBean(String imaUrl, String name, String goodId, ArrayList<String> imagePathList, ArrayList<String> toUploadimagePathList, String comment, int textNum,String geval_content) {
        this.imaUrl=imaUrl;
        this.name=name;
        this.imagePathList=imagePathList;
        this.toUploadimagePathList=toUploadimagePathList;
        this.comment=comment;
        this.textNum = textNum;
        this.geval_content = geval_content;
        gevalId = goodId;
    }

    public String getImaUrl() {
        return imaUrl;
    }

    public CharSequence getName() {
        return name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }



    public String getGeval_content() {
        return geval_content;
    }

    public int getTextNum() {
        return textNum;
    }


    public void setImagePathList(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
    }
    public ArrayList<String> getImagePathList() {
        return imagePathList;
    }

    public void setTextNum(int textNum) {
        this.textNum = textNum;
    }


    public ArrayList<String> getToUploadimagePathList() {
        return toUploadimagePathList;
    }

    public String getGeval_id() {
        return gevalId;
    }
}
