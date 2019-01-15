package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class RefundFormBean implements Serializable{

    /**
     * code : 200
     * datas : {"order":{"order_id":"922","order_type":"1","order_amount":"10224.00","order_sn":"1000000000090601","store_name":"福气廊","store_id":"8"},"goods":{"store_id":"8","order_goods_id":"1002","goods_id":"100138","goods_name":"万和燃热水器JSQ25-13GT36（Y)","goods_type":"","goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581764694122777_360.png","goods_price":"3758.00","goods_spec":null,"goods_num":"1","goods_pay_price":"3750.50"},"reason_list":[{"reason_id":"99","reason_info":"7天无理由退货"},{"reason_id":"98","reason_info":"不想要了"},{"reason_id":"97","reason_info":"订单信息有误，需要重拍"},{"reason_id":"96","reason_info":"效果不好不喜欢"},{"reason_id":"95","reason_info":"其他"}]}
     */

    private DatasBean datas;

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable{
        /**
         * order : {"order_id":"922","order_type":"1","order_amount":"10224.00","order_sn":"1000000000090601","store_name":"福气廊","store_id":"8"}
         * goods : {"store_id":"8","order_goods_id":"1002","goods_id":"100138","goods_name":"万和燃热水器JSQ25-13GT36（Y)","goods_type":"","goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581764694122777_360.png","goods_price":"3758.00","goods_spec":null,"goods_num":"1","goods_pay_price":"3750.50"}
         * reason_list : [{"reason_id":"99","reason_info":"7天无理由退货"},{"reason_id":"98","reason_info":"不想要了"},{"reason_id":"97","reason_info":"订单信息有误，需要重拍"},{"reason_id":"96","reason_info":"效果不好不喜欢"},{"reason_id":"95","reason_info":"其他"}]
         */

        private OrderBean order;
        private GoodsBean goods;
        private List<ReasonListBean> reason_list;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public List<ReasonListBean> getReason_list() {
            return reason_list;
        }

        public void setReason_list(List<ReasonListBean> reason_list) {
            this.reason_list = reason_list;
        }

        public static class OrderBean implements Serializable{
            /**
             * order_id : 922
             * order_type : 1
             * order_amount : 10224.00
             * order_sn : 1000000000090601
             * store_name : 福气廊
             * store_id : 8
             */

            private String order_id;
            private String order_type;
            private String order_amount;
            private String order_sn;
            private String store_name;
            private String store_id;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getOrder_type() {
                return order_type;
            }

            public void setOrder_type(String order_type) {
                this.order_type = order_type;
            }

            public String getOrder_amount() {
                return order_amount;
            }

            public void setOrder_amount(String order_amount) {
                this.order_amount = order_amount;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }
        }

        public static class GoodsBean implements Serializable{
            /**
             * store_id : 8
             * order_goods_id : 1002
             * goods_id : 100138
             * goods_name : 万和燃热水器JSQ25-13GT36（Y)
             * goods_type :
             * goods_img_360 : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581764694122777_360.png
             * goods_price : 3758.00
             * goods_spec : null
             * goods_num : 1
             * goods_pay_price : 3750.50
             */

            private String store_id;
            private String order_goods_id;
            private String goods_id;
            private String goods_name;
            private String goods_type;
            private String goods_img_360;
            private String goods_price;
            private Object goods_spec;
            private String goods_num;
            private String goods_pay_price;

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getOrder_goods_id() {
                return order_goods_id;
            }

            public void setOrder_goods_id(String order_goods_id) {
                this.order_goods_id = order_goods_id;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }

            public String getGoods_img_360() {
                return goods_img_360;
            }

            public void setGoods_img_360(String goods_img_360) {
                this.goods_img_360 = goods_img_360;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public Object getGoods_spec() {
                return goods_spec;
            }

            public void setGoods_spec(Object goods_spec) {
                this.goods_spec = goods_spec;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public String getGoods_pay_price() {
                return goods_pay_price;
            }

            public void setGoods_pay_price(String goods_pay_price) {
                this.goods_pay_price = goods_pay_price;
            }
        }

        public static class ReasonListBean implements Serializable{
            /**
             * reason_id : 99
             * reason_info : 7天无理由退货
             */

            private String reason_id;
            private String reason_info;

            public String getReason_id() {
                return reason_id;
            }

            public void setReason_id(String reason_id) {
                this.reason_id = reason_id;
            }

            public String getReason_info() {
                return reason_info;
            }

            public void setReason_info(String reason_info) {
                this.reason_info = reason_info;
            }
        }
    }
}
