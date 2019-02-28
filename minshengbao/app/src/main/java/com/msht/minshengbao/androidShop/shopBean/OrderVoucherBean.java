package com.msht.minshengbao.androidShop.shopBean;

public class OrderVoucherBean {
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private  String desc;

    public String getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getStartLimit() {
        return startLimit;
    }

    public void setStartLimit(String startLimit) {
        this.startLimit = startLimit;
    }

    private  String voucherPrice;
    private  String startLimit;

    public String getVtid() {
        return vtid;
    }

    public void setVtid(String vtid) {
        this.vtid = vtid;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public String getVlimitAmount() {
        return vlimitAmount;
    }

    public void setVlimitAmount(String vlimitAmount) {
        this.vlimitAmount = vlimitAmount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private  String vtid;
    private  String vtitle;
    private  String limitTime;
    private  String vlimitAmount;
    private  boolean isSelected;

    public OrderVoucherBean(String vtid, String vtitle, String limitTime, String vlimitAmount, boolean isSelected,String startLimit,String voucherPrice,String desc) {
        this.vtid=vtid;
        this.vtitle=vtitle;
        this.limitTime=limitTime;
        this.vlimitAmount=vlimitAmount;
        this.isSelected=isSelected;
        this.startLimit = startLimit;
        this.voucherPrice = voucherPrice;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderVoucherBean)) return false;

        OrderVoucherBean that = (OrderVoucherBean) o;

        if (isSelected != that.isSelected) return false;
        if (!voucherPrice.equals(that.voucherPrice)) return false;
        if (!startLimit.equals(that.startLimit)) return false;
        if (!vtid.equals(that.vtid)) return false;
        if (!vtitle.equals(that.vtitle)) return false;
        if (!limitTime.equals(that.limitTime)) return false;
        return vlimitAmount.equals(that.vlimitAmount);
    }

    @Override
    public int hashCode() {
        int result = voucherPrice.hashCode();
        result = 31 * result + startLimit.hashCode();
        result = 31 * result + vtid.hashCode();
        result = 31 * result + vtitle.hashCode();
        result = 31 * result + limitTime.hashCode();
        result = 31 * result + vlimitAmount.hashCode();
        result = 31 * result + (isSelected ? 1 : 0);
        return result;
    }
}
