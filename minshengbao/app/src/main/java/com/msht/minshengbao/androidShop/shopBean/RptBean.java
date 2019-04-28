package com.msht.minshengbao.androidShop.shopBean;

public class RptBean {

    /**
     * rpacket_price : 10
     * rpacket_limit : 101.00
     * rpacket_t_id : 13
     * desc : 10元红包 有效期至 2019-04-30 消费满101.00可用
     * rpacket_start_time_text : 2019-04-17
     * rpacket_end_time_text : 2019-04-30
     */

    private String rpacket_price;
    private String rpacket_limit;
    private String rpacket_t_id;
    private String desc;
    private String rpacket_start_time_text;
    private String rpacket_end_time_text;

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    private Boolean isCheck;
    public String getRpacket_price() {
        return rpacket_price;
    }

    public void setRpacket_price(String rpacket_price) {
        this.rpacket_price = rpacket_price;
    }

    public String getRpacket_limit() {
        return rpacket_limit;
    }

    public void setRpacket_limit(String rpacket_limit) {
        this.rpacket_limit = rpacket_limit;
    }

    public String getRpacket_t_id() {
        return rpacket_t_id;
    }

    public void setRpacket_t_id(String rpacket_t_id) {
        this.rpacket_t_id = rpacket_t_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRpacket_start_time_text() {
        return rpacket_start_time_text;
    }

    public void setRpacket_start_time_text(String rpacket_start_time_text) {
        this.rpacket_start_time_text = rpacket_start_time_text;
    }

    public String getRpacket_end_time_text() {
        return rpacket_end_time_text;
    }

    public void setRpacket_end_time_text(String rpacket_end_time_text) {
        this.rpacket_end_time_text = rpacket_end_time_text;
    }
}
