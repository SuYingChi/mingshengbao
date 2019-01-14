package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;

public class CarGoodItemBean implements Serializable{

    private String goods_price;
    private String carId;
    private String goodId;
    private String quantity;

    public CarGoodItemBean(String carId, String goodId, String quantity, String goods_price) {
        this.carId = carId;
        this.goodId = goodId;
        this.quantity = quantity;
        this.goods_price = goods_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarGoodItemBean that = (CarGoodItemBean) o;

        if (!carId.equals(that.carId)) return false;
        return goodId.equals(that.goodId);
    }

    @Override
    public int hashCode() {
        int result = carId != null ? carId.hashCode() : 0;
        result = 31 * result + (goodId != null ? goodId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CarGoodItemBean{" +
                "carId='" + carId + '\'' +
                ", goodId='" + goodId + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public String getGoodPrice() {
        return goods_price;
    }
}
