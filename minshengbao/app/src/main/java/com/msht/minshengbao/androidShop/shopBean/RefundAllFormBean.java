package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class RefundAllFormBean {

    /**
     * code : 200
     * datas : {"order":{"order_id":"1318","order_type":"1","order_amount":"9789.00","order_sn":"1000000000130001","store_name":"福气廊","store_id":"8","allow_refund_amount":"9789.00","book_amount":"0.00"},"goods_list":[{"goods_id":"100092","goods_name":"万家乐热水器JSQ32-16QH3(白）（Y)","goods_price":"9799.00","goods_num":"1","goods_spec":null,"goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580289960559623_360.jpg","goods_type":""}],"gift_list":[{"goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"0.00","goods_num":"1","goods_spec":null,"goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_360.jpg","goods_type":"赠品"}],"reason_list":[{"reason_id":"99","reason_info":"7天无理由退货"},{"reason_id":"98","reason_info":"不想要了"},{"reason_id":"97","reason_info":"订单信息有误，需要重拍"},{"reason_id":"96","reason_info":"效果不好不喜欢"},{"reason_id":"95","reason_info":"其他"}]}
     */

    private DatasBean datas;

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * order : {"order_id":"1318","order_type":"1","order_amount":"9789.00","order_sn":"1000000000130001","store_name":"福气廊","store_id":"8","allow_refund_amount":"9789.00","book_amount":"0.00"}
         * goods_list : [{"goods_id":"100092","goods_name":"万家乐热水器JSQ32-16QH3(白）（Y)","goods_price":"9799.00","goods_num":"1","goods_spec":null,"goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580289960559623_360.jpg","goods_type":""}]
         * gift_list : [{"goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"0.00","goods_num":"1","goods_spec":null,"goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_360.jpg","goods_type":"赠品"}]
         * reason_list : [{"reason_id":"99","reason_info":"7天无理由退货"},{"reason_id":"98","reason_info":"不想要了"},{"reason_id":"97","reason_info":"订单信息有误，需要重拍"},{"reason_id":"96","reason_info":"效果不好不喜欢"},{"reason_id":"95","reason_info":"其他"}]
         */

        private OrderBean order;
        private List<GoodsListBean> goods_list;
        private List<GiftListBean> gift_list;
        private List<ReasonListBean> reason_list;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public List<GiftListBean> getGift_list() {
            return gift_list;
        }

        public void setGift_list(List<GiftListBean> gift_list) {
            this.gift_list = gift_list;
        }

        public List<ReasonListBean> getReason_list() {
            return reason_list;
        }

        public void setReason_list(List<ReasonListBean> reason_list) {
            this.reason_list = reason_list;
        }

        public static class OrderBean {
            /**
             * order_id : 1318
             * order_type : 1
             * order_amount : 9789.00
             * order_sn : 1000000000130001
             * store_name : 福气廊
             * store_id : 8
             * allow_refund_amount : 9789.00
             * book_amount : 0.00
             */

            private String order_id;
            private String order_type;
            private String order_amount;
            private String order_sn;
            private String store_name;
            private String store_id;
            private String allow_refund_amount;
            private String book_amount;

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

            public String getAllow_refund_amount() {
                return allow_refund_amount;
            }

            public void setAllow_refund_amount(String allow_refund_amount) {
                this.allow_refund_amount = allow_refund_amount;
            }

            public String getBook_amount() {
                return book_amount;
            }

            public void setBook_amount(String book_amount) {
                this.book_amount = book_amount;
            }
        }

        public static class GoodsListBean {
            /**
             * goods_id : 100092
             * goods_name : 万家乐热水器JSQ32-16QH3(白）（Y)
             * goods_price : 9799.00
             * goods_num : 1
             * goods_spec : null
             * goods_img_360 : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580289960559623_360.jpg
             * goods_type :
             */

            private String goods_id;
            private String goods_name;
            private String goods_price;
            private String goods_num;
            private Object goods_spec;
            private String goods_img_360;
            private String goods_type;

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

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public Object getGoods_spec() {
                return goods_spec;
            }

            public void setGoods_spec(Object goods_spec) {
                this.goods_spec = goods_spec;
            }

            public String getGoods_img_360() {
                return goods_img_360;
            }

            public void setGoods_img_360(String goods_img_360) {
                this.goods_img_360 = goods_img_360;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }
        }

        public static class GiftListBean {
            /**
             * goods_id : 100157
             * goods_name : 万和灶具 C3-12T-L16Z（Y)
             * goods_price : 0.00
             * goods_num : 1
             * goods_spec : null
             * goods_img_360 : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_360.jpg
             * goods_type : 赠品
             */

            private String goods_id;
            private String goods_name;
            private String goods_price;
            private String goods_num;
            private Object goods_spec;
            private String goods_img_360;
            private String goods_type;

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

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public Object getGoods_spec() {
                return goods_spec;
            }

            public void setGoods_spec(Object goods_spec) {
                this.goods_spec = goods_spec;
            }

            public String getGoods_img_360() {
                return goods_img_360;
            }

            public void setGoods_img_360(String goods_img_360) {
                this.goods_img_360 = goods_img_360;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }
        }

        public static class ReasonListBean {
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
