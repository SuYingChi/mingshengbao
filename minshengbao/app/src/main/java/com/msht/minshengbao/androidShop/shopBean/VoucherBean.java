package com.msht.minshengbao.androidShop.shopBean;

public class VoucherBean {

    /**
     * voucher_t_id : 33
     * voucher_t_price : 5
     * voucher_t_limit : 10.00
     * voucher_t_end_date : 2019年06月30日
     */

    private String voucher_t_id;
    private String voucher_t_price;
    private String voucher_t_limit;
    private String voucher_t_end_date;

    public String getVoucher_t_start_date_text() {
        return voucher_t_start_date_text;
    }

    public void setVoucher_t_start_date_text(String voucher_t_start_date_text) {
        this.voucher_t_start_date_text = voucher_t_start_date_text;
    }

    public String getVoucher_t_end_date_text() {
        return voucher_t_end_date_text;
    }

    public void setVoucher_t_end_date_text(String voucher_t_end_date_text) {
        this.voucher_t_end_date_text = voucher_t_end_date_text;
    }

    private String voucher_t_start_date_text;
    private String voucher_t_end_date_text;

    public String getVoucher_t_state() {
        return voucher_t_state;
    }

    public void setVoucher_t_state(String voucher_t_state) {
        this.voucher_t_state = voucher_t_state;
    }

    private String voucher_t_state;

    public void setVoucher_t_title(String voucher_t_title) {
        this.voucher_t_title = voucher_t_title;
    }

    private String voucher_t_title;

    public void setVoucher_t_desc(String voucher_t_desc) {
        this.voucher_t_desc = voucher_t_desc;
    }

    private String voucher_t_desc;



    public void setShowDesc(boolean showDesc) {
        this.showDesc = showDesc;
    }

    private boolean showDesc;

    public String getVoucher_t_id() {
        return voucher_t_id;
    }

    public void setVoucher_t_id(String voucher_t_id) {
        this.voucher_t_id = voucher_t_id;
    }

    public String getVoucher_t_price() {
        return voucher_t_price;
    }

    public void setVoucher_t_price(String voucher_t_price) {
        this.voucher_t_price = voucher_t_price;
    }

    public String getVoucher_t_limit() {
        return voucher_t_limit;
    }

    public void setVoucher_t_limit(String voucher_t_limit) {
        this.voucher_t_limit = voucher_t_limit;
    }

    public String getVoucher_t_end_date() {
        return voucher_t_end_date;
    }

    public void setVoucher_t_end_date(String voucher_t_end_date) {
        this.voucher_t_end_date = voucher_t_end_date;
    }




    public boolean isShowDesc() {
        return showDesc;
    }

    public String getVoucher_t_title () {
        return voucher_t_title ;
    }

    public String getVoucher_t_desc() {
        return voucher_t_desc;
    }
}
