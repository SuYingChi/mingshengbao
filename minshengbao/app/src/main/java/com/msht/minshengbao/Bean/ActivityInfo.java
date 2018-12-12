package com.msht.minshengbao.Bean;

/**
 *
 * @author hong
 * @date 2017/11/8
 */

public class ActivityInfo {
    private String image = "";
    private String url = "";
    private String title="";
    private String share;
    private String desc;
    private String backUrl;
    public String getImages() {
        return image;
    }
    public void setImages(String url) {
        this.image = url;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String content) {
        this.url = content;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String content) {
        this.title = content;
    }
    public String getShare() {
        return share;
    }
    public void setShare(String share) {
        this.share = share;
    }
    public String getDesc(){
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }
    public String getBackUrl() {
        return backUrl;
    }
}
