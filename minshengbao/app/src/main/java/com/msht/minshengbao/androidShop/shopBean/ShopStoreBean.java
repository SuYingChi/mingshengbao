package com.msht.minshengbao.androidShop.shopBean;

import java.util.Objects;

public class ShopStoreBean {

    /**
     * store_id : 22
     * store_name : 民生宝自营店
     * goods : []
     */

    private String store_id;
    private String store_name;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public ShopStoreBean(String store_name,String store_id){
        this.store_name = store_name;
        this.store_id = store_id;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ShopStoreBean)) {
            return false;
        }
        ShopStoreBean shopStoreBean = (ShopStoreBean) o;
        return shopStoreBean.store_name.equals(store_name) &&
                shopStoreBean.store_id.equals(store_id);
    }
    //Idea from effective Java : Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + store_name.hashCode();
        result = 31 * result + store_id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ShopStoreBean{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                '}';
    }
}
