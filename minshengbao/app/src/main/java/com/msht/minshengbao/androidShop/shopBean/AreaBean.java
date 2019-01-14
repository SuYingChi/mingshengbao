package com.msht.minshengbao.androidShop.shopBean;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;

import java.util.List;

public class AreaBean  {


    /**
     * area_id : 21
     * area_name : 海南
     */

    private String area_id;
    private String area_name;

    public AreaBean(String area_id, String area_name, Boolean isSelected) {
        this.area_id = area_id;
        this.area_name = area_name;
        this.isSelected = isSelected;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    private Boolean isSelected;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaBean areaBean = (AreaBean) o;

        if (area_id != null ? !area_id.equals(areaBean.area_id) : areaBean.area_id != null)
            return false;
        return area_name != null ? area_name.equals(areaBean.area_name) : areaBean.area_name == null;
    }

    @Override
    public int hashCode() {
        int result = area_id != null ? area_id.hashCode() : 0;
        result = 31 * result + (area_name != null ? area_name.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "AreaBean{" +
                "area_id='" + area_id + '\'' +
                ", area_name='" + area_name + '\'' +
                '}';
    }
}
