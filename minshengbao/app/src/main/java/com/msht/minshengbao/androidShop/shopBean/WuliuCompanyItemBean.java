package com.msht.minshengbao.androidShop.shopBean;

public class WuliuCompanyItemBean {
    private  String expressId;
    private String content;

    public WuliuCompanyItemBean(String express_name, String express_id, boolean isSelected) {
        content = express_name;
        this.isSelected = isSelected;
        expressId = express_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;



    public String getContent() {
        return content;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }
}
