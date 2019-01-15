package com.msht.minshengbao.androidShop.shopBean;

public class InvContentItemBean {
    private String content;
    private boolean selected;

    public InvContentItemBean(String content, boolean b) {
        this.content = content;
        selected = b;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getContent() {
        return content;
    }

    public boolean getSelected() {
        return selected;
    }
}
