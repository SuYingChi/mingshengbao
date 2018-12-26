package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class EvaluateShopOrderBean {

    /**
     * code : 200
     * datas : {"store_info":{"store_id":"8","store_name":"福气廊","is_own_shop":"0"},"order_goods":[{"rec_id":"1429","order_id":"1291","goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"0.00","goods_num":"1","goods_image":"8_05581955944170345.jpg","goods_pay_price":"0.00","store_id":"8","buyer_id":"98906","goods_type":"5","promotions_id":"0","commis_rate":"0","gc_id":"0","goods_spec":null,"goods_contractid":"","goods_commonid":"0","add_time":"1542773019","is_dis":"0","dis_commis_rate":"0.000","dis_member_id":"0","dis_type":"0","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_240.jpg"},{"rec_id":"1428","order_id":"1291","goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"1500.00","goods_num":"1","goods_image":"8_05581955944170345.jpg","goods_pay_price":"1490.00","store_id":"8","buyer_id":"98906","goods_type":"10","promotions_id":"0","commis_rate":"25","gc_id":"1173","goods_spec":null,"goods_contractid":"","goods_commonid":"100150","add_time":"1542773019","is_dis":"1","dis_commis_rate":"7.200","dis_member_id":"0","dis_type":"1","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_240.jpg"}]}
     */

    private int code;
    private DatasBean datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * store_info : {"store_id":"8","store_name":"福气廊","is_own_shop":"0"}
         * order_goods : [{"rec_id":"1429","order_id":"1291","goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"0.00","goods_num":"1","goods_image":"8_05581955944170345.jpg","goods_pay_price":"0.00","store_id":"8","buyer_id":"98906","goods_type":"5","promotions_id":"0","commis_rate":"0","gc_id":"0","goods_spec":null,"goods_contractid":"","goods_commonid":"0","add_time":"1542773019","is_dis":"0","dis_commis_rate":"0.000","dis_member_id":"0","dis_type":"0","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_240.jpg"},{"rec_id":"1428","order_id":"1291","goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_price":"1500.00","goods_num":"1","goods_image":"8_05581955944170345.jpg","goods_pay_price":"1490.00","store_id":"8","buyer_id":"98906","goods_type":"10","promotions_id":"0","commis_rate":"25","gc_id":"1173","goods_spec":null,"goods_contractid":"","goods_commonid":"100150","add_time":"1542773019","is_dis":"1","dis_commis_rate":"7.200","dis_member_id":"0","dis_type":"1","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_240.jpg"}]
         */

        private StoreInfoBean store_info;
        private List<OrderGoodsBean> order_goods;

        public StoreInfoBean getStore_info() {
            return store_info;
        }

        public void setStore_info(StoreInfoBean store_info) {
            this.store_info = store_info;
        }

        public List<OrderGoodsBean> getOrder_goods() {
            return order_goods;
        }

        public void setOrder_goods(List<OrderGoodsBean> order_goods) {
            this.order_goods = order_goods;
        }

        public static class StoreInfoBean {
            /**
             * store_id : 8
             * store_name : 福气廊
             * is_own_shop : 0
             */

            private String store_id;
            private String store_name;
            private String is_own_shop;

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

            public String getIs_own_shop() {
                return is_own_shop;
            }

            public void setIs_own_shop(String is_own_shop) {
                this.is_own_shop = is_own_shop;
            }
        }

        public static class OrderGoodsBean {
            /**
             * rec_id : 1429
             * order_id : 1291
             * goods_id : 100157
             * goods_name : 万和灶具 C3-12T-L16Z（Y)
             * goods_price : 0.00
             * goods_num : 1
             * goods_image : 8_05581955944170345.jpg
             * goods_pay_price : 0.00
             * store_id : 8
             * buyer_id : 98906
             * goods_type : 5
             * promotions_id : 0
             * commis_rate : 0
             * gc_id : 0
             * goods_spec : null
             * goods_contractid :
             * goods_commonid : 0
             * add_time : 1542773019
             * is_dis : 0
             * dis_commis_rate : 0.000
             * dis_member_id : 0
             * dis_type : 0
             * goods_image_url : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_240.jpg
             */

            private String rec_id;
            private String order_id;
            private String goods_id;
            private String goods_name;
            private String goods_price;
            private String goods_num;
            private String goods_image;
            private String goods_pay_price;
            private String store_id;
            private String buyer_id;
            private String goods_type;
            private String promotions_id;
            private String commis_rate;
            private String gc_id;
            private Object goods_spec;
            private String goods_contractid;
            private String goods_commonid;
            private String add_time;
            private String is_dis;
            private String dis_commis_rate;
            private String dis_member_id;
            private String dis_type;
            private String goods_image_url;

            public String getRec_id() {
                return rec_id;
            }

            public void setRec_id(String rec_id) {
                this.rec_id = rec_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
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

            public String getGoods_image() {
                return goods_image;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }

            public String getGoods_pay_price() {
                return goods_pay_price;
            }

            public void setGoods_pay_price(String goods_pay_price) {
                this.goods_pay_price = goods_pay_price;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getBuyer_id() {
                return buyer_id;
            }

            public void setBuyer_id(String buyer_id) {
                this.buyer_id = buyer_id;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }

            public String getPromotions_id() {
                return promotions_id;
            }

            public void setPromotions_id(String promotions_id) {
                this.promotions_id = promotions_id;
            }

            public String getCommis_rate() {
                return commis_rate;
            }

            public void setCommis_rate(String commis_rate) {
                this.commis_rate = commis_rate;
            }

            public String getGc_id() {
                return gc_id;
            }

            public void setGc_id(String gc_id) {
                this.gc_id = gc_id;
            }

            public Object getGoods_spec() {
                return goods_spec;
            }

            public void setGoods_spec(Object goods_spec) {
                this.goods_spec = goods_spec;
            }

            public String getGoods_contractid() {
                return goods_contractid;
            }

            public void setGoods_contractid(String goods_contractid) {
                this.goods_contractid = goods_contractid;
            }

            public String getGoods_commonid() {
                return goods_commonid;
            }

            public void setGoods_commonid(String goods_commonid) {
                this.goods_commonid = goods_commonid;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getIs_dis() {
                return is_dis;
            }

            public void setIs_dis(String is_dis) {
                this.is_dis = is_dis;
            }

            public String getDis_commis_rate() {
                return dis_commis_rate;
            }

            public void setDis_commis_rate(String dis_commis_rate) {
                this.dis_commis_rate = dis_commis_rate;
            }

            public String getDis_member_id() {
                return dis_member_id;
            }

            public void setDis_member_id(String dis_member_id) {
                this.dis_member_id = dis_member_id;
            }

            public String getDis_type() {
                return dis_type;
            }

            public void setDis_type(String dis_type) {
                this.dis_type = dis_type;
            }

            public String getGoods_image_url() {
                return goods_image_url;
            }

            public void setGoods_image_url(String goods_image_url) {
                this.goods_image_url = goods_image_url;
            }
        }
    }
}
