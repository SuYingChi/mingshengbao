package com.msht.minshengbao.Bean;
/**
 * 描述：广告信息</br>
 * @author Eden Cheng</br>
 * @version 2015年4月23日 上午11:32:53
 */
public class ADInfo {
	String id = "";
	String image = "";
	String url = "";
	String type = "";
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
	
}
