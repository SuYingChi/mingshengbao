package com.msht.minshengbao.androidShop.shopBean;

import android.widget.TextView;

import java.util.List;

public class OrderslistBean {
    private List<MyExtendOrderGoodsBean> myExtendOrderGoods;

    public List<ZengpingBean> getZengpings() {
        return zengpings;
    }

    private  List<ZengpingBean> zengpings;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private  String orderId;

    public List<TextView> getBtnList() {
        return btnList;
    }

    public void setBtnList(List<TextView> btnList) {
        this.btnList = btnList;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    private  List<TextView> btnList;
    private String order_desc;

    public String getOrder_state() {
        return order_state;
    }

    private final String order_state;
    private String shipping_fee;



    public String getStore_name() {
        return store_name;
    }

    private final String store_name;


    public void setMyExtendOrderGoods(List<MyExtendOrderGoodsBean> ordersItem) {
        this.myExtendOrderGoods = ordersItem;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }


    private  String pay_amount;
    private  String add_time;
    private  String pay_sn;

    public void setZengpings(List<ZengpingBean> zengpings) {
        this.zengpings = zengpings;
    }

    public OrderslistBean(List<MyExtendOrderGoodsBean> ordersItem, String pay_amount, String add_time, String pay_sn, String store_name, String shipping_fee, String order_state, String order_desc, List<TextView> btnList, String order_id, List<ZengpingBean> zengpings) {
        this.myExtendOrderGoods = ordersItem;
        this.pay_amount = pay_amount;
        this.add_time = add_time;
        this.pay_sn =pay_sn;
        this.store_name = store_name;
        this.shipping_fee = shipping_fee;
        this.order_state = order_state;
        this.order_desc = order_desc;
        this.btnList = btnList;
        this.orderId = order_id;
        this.zengpings = zengpings;
    }


    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getOrder_desc() {
        return order_desc;
    }


    public List<MyExtendOrderGoodsBean> getMyExtendOrderGoods() {
        return myExtendOrderGoods;
    }
}
