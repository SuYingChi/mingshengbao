package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;

public class InvItemBean implements Serializable{
    private String inv_content;
    private boolean check;
    private String inv_id;
    private String inv_title ;

    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }

    private String inv_code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvItemBean that = (InvItemBean) o;

        if (check != that.check) return false;
        if (inv_content != null ? !inv_content.equals(that.inv_content) : that.inv_content != null)
            return false;
        if (inv_id != null ? !inv_id.equals(that.inv_id) : that.inv_id != null) return false;
        if (inv_title != null ? !inv_title.equals(that.inv_title) : that.inv_title != null)
            return false;
        return inv_code != null ? inv_code.equals(that.inv_code) : that.inv_code == null;
    }

    @Override
    public String toString() {
        return "InvItemBean{" +
                "inv_content='" + inv_content + '\'' +
                ", check=" + check +
                ", inv_id='" + inv_id + '\'' +
                ", CNinv_title='" + inv_title + '\'' +
                ", inv_code='" + inv_code + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = inv_content != null ? inv_content.hashCode() : 0;
        result = 31 * result + (check ? 1 : 0);
        result = 31 * result + (inv_id != null ? inv_id.hashCode() : 0);
        result = 31 * result + (inv_title != null ? inv_title.hashCode() : 0);
        result = 31 * result + (inv_code != null ? inv_code.hashCode() : 0);
        return result;
    }

    public InvItemBean(String inv_content, boolean check, String inv_id, String inv_title, String inv_code) {
        this.inv_content = inv_content;
        this.check = check;
        this.inv_id = inv_id;
        this.inv_title = inv_title;

        this.inv_code = inv_code;
    }

    public void setInv_content(String inv_content) {
        this.inv_content = inv_content;
    }

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public String getInv_title() {
        return inv_title;
    }

    public void setInv_title(String inv_title) {
        this.inv_title = inv_title;
    }
    public String getInv_content() {
        return inv_content;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
