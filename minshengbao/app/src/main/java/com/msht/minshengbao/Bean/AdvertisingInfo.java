package com.msht.minshengbao.Bean;
/**
 * 描述：广告信息</br>
 * @author Eden Cheng</br>
 * @version 2015年4月23日 上午11:32:53
 */
public class AdvertisingInfo {
	private String id = "";
	private String type = "";
	private String image = "";
	private String url = "";
	private String share;
	private String title;
	private String desc;
	private String backUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
