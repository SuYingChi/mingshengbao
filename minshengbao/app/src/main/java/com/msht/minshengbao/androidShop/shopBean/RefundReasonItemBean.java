package com.msht.minshengbao.androidShop.shopBean;

public class RefundReasonItemBean {
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean b) {
        this.isSelected = b;
    }

    private  String reason;
    private  String id;
    private  boolean isSelected;

    public RefundReasonItemBean(String reason, String id, boolean isSelected) {
        this.reason = reason;
        this.id = id;
        this.isSelected = isSelected;
    }
}
