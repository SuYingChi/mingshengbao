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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvContentItemBean)) return false;

        InvContentItemBean that = (InvContentItemBean) o;

        if (selected != that.selected) return false;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }
}
