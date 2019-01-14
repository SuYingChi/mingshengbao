package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ReturnGoodDetailBean {

    /**
     * code : 200
     * datas : {"return_info":{"refund_id":"150","goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_num":"1","goods_state_v":"4","ship_state":"0","delay_state":"0","express_name":"品骏物流","invoice_no":"1234","order_id":"1299","refund_amount":"1990.00","refund_sn":"844108181121152644","return_type":"2","order_sn":"1000000000128101","add_time":"2018-11-21 15:26:44","goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_360.jpg","seller_state":"同意","admin_state":"已完成","store_id":"8","store_name":"福气廊","reason_info":"效果不好不喜欢","buyer_message":"","seller_message":"3434","admin_message":"3333"},"pic_list":["http://dev.msbapp.cn/data/upload/shop/refund/05961292040152273.jpg"],"detail_array":{"refund_code":"支付宝","pay_amount":"1990.00","pd_amount":"0.00","rcb_amount":"0.00"},"goods_list":[{"rec_id":"1442","order_id":"1299","goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_price":"2000.00","goods_num":"1","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_240.jpg","goods_pay_price":"1990.00","store_id":"8","buyer_id":"98906","goods_type":"10","promotions_id":"0","commis_rate":"200","gc_id":"1148","goods_spec":null,"goods_contractid":"","goods_commonid":"100141","add_time":"1542782078","is_dis":"1","dis_commis_rate":"7.200","dis_member_id":"0","dis_type":"1"}]}
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
         * return_info : {"refund_id":"150","goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_num":"1","goods_state_v":"4","ship_state":"0","delay_state":"0","express_name":"品骏物流","invoice_no":"1234","order_id":"1299","refund_amount":"1990.00","refund_sn":"844108181121152644","return_type":"2","order_sn":"1000000000128101","add_time":"2018-11-21 15:26:44","goods_img_360":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_360.jpg","seller_state":"同意","admin_state":"已完成","store_id":"8","store_name":"福气廊","reason_info":"效果不好不喜欢","buyer_message":"","seller_message":"3434","admin_message":"3333"}
         * pic_list : ["http://dev.msbapp.cn/data/upload/shop/refund/05961292040152273.jpg"]
         * detail_array : {"refund_code":"支付宝","pay_amount":"1990.00","pd_amount":"0.00","rcb_amount":"0.00"}
         * goods_list : [{"rec_id":"1442","order_id":"1299","goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_price":"2000.00","goods_num":"1","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_240.jpg","goods_pay_price":"1990.00","store_id":"8","buyer_id":"98906","goods_type":"10","promotions_id":"0","commis_rate":"200","gc_id":"1148","goods_spec":null,"goods_contractid":"","goods_commonid":"100141","add_time":"1542782078","is_dis":"1","dis_commis_rate":"7.200","dis_member_id":"0","dis_type":"1"}]
         */

        private ReturnInfoBean return_info;
        private DetailArrayBean detail_array;
        private List<String> pic_list;
        private List<GoodsListBean> goods_list;

        public ReturnInfoBean getReturn_info() {
            return return_info;
        }

        public void setReturn_info(ReturnInfoBean return_info) {
            this.return_info = return_info;
        }

        public DetailArrayBean getDetail_array() {
            return detail_array;
        }

        public void setDetail_array(DetailArrayBean detail_array) {
            this.detail_array = detail_array;
        }

        public List<String> getPic_list() {
            return pic_list;
        }

        public void setPic_list(List<String> pic_list) {
            this.pic_list = pic_list;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public static class ReturnInfoBean {
            /**
             * refund_id : 150
             * goods_id : 100148
             * goods_name : 万和消毒柜ZTD100QE-1（Y)
             * goods_num : 1
             * goods_state_v : 4
             * ship_state : 0
             * delay_state : 0
             * express_name : 品骏物流
             * invoice_no : 1234
             * order_id : 1299
             * refund_amount : 1990.00
             * refund_sn : 844108181121152644
             * return_type : 2
             * order_sn : 1000000000128101
             * add_time : 2018-11-21 15:26:44
             * goods_img_360 : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_360.jpg
             * seller_state : 同意
             * admin_state : 已完成
             * store_id : 8
             * store_name : 福气廊
             * reason_info : 效果不好不喜欢
             * buyer_message :
             * seller_message : 3434
             * admin_message : 3333
             */

            private String refund_id;
            private String goods_id;
            private String goods_name;
            private String goods_num;
            private String goods_state_v;
            private String ship_state;
            private String delay_state;
            private String express_name;
            private String invoice_no;
            private String order_id;
            private String refund_amount;
            private String refund_sn;
            private String return_type;
            private String order_sn;
            private String add_time;
            private String goods_img_360;
            private String seller_state;
            private String admin_state;
            private String store_id;
            private String store_name;
            private String reason_info;
            private String buyer_message;
            private String seller_message;
            private String admin_message;

            public String getRefund_id() {
                return refund_id;
            }

            public void setRefund_id(String refund_id) {
                this.refund_id = refund_id;
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

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public String getGoods_state_v() {
                return goods_state_v;
            }

            public void setGoods_state_v(String goods_state_v) {
                this.goods_state_v = goods_state_v;
            }

            public String getShip_state() {
                return ship_state;
            }

            public void setShip_state(String ship_state) {
                this.ship_state = ship_state;
            }

            public String getDelay_state() {
                return delay_state;
            }

            public void setDelay_state(String delay_state) {
                this.delay_state = delay_state;
            }

            public String getExpress_name() {
                return express_name;
            }

            public void setExpress_name(String express_name) {
                this.express_name = express_name;
            }

            public String getInvoice_no() {
                return invoice_no;
            }

            public void setInvoice_no(String invoice_no) {
                this.invoice_no = invoice_no;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getRefund_amount() {
                return refund_amount;
            }

            public void setRefund_amount(String refund_amount) {
                this.refund_amount = refund_amount;
            }

            public String getRefund_sn() {
                return refund_sn;
            }

            public void setRefund_sn(String refund_sn) {
                this.refund_sn = refund_sn;
            }

            public String getReturn_type() {
                return return_type;
            }

            public void setReturn_type(String return_type) {
                this.return_type = return_type;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getGoods_img_360() {
                return goods_img_360;
            }

            public void setGoods_img_360(String goods_img_360) {
                this.goods_img_360 = goods_img_360;
            }

            public String getSeller_state() {
                return seller_state;
            }

            public void setSeller_state(String seller_state) {
                this.seller_state = seller_state;
            }

            public String getAdmin_state() {
                return admin_state;
            }

            public void setAdmin_state(String admin_state) {
                this.admin_state = admin_state;
            }

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

            public String getReason_info() {
                return reason_info;
            }

            public void setReason_info(String reason_info) {
                this.reason_info = reason_info;
            }

            public String getBuyer_message() {
                return buyer_message;
            }

            public void setBuyer_message(String buyer_message) {
                this.buyer_message = buyer_message;
            }

            public String getSeller_message() {
                return seller_message;
            }

            public void setSeller_message(String seller_message) {
                this.seller_message = seller_message;
            }

            public String getAdmin_message() {
                return admin_message;
            }

            public void setAdmin_message(String admin_message) {
                this.admin_message = admin_message;
            }
        }

        public static class DetailArrayBean {
            /**
             * refund_code : 支付宝
             * pay_amount : 1990.00
             * pd_amount : 0.00
             * rcb_amount : 0.00
             */

            private String refund_code;
            private String pay_amount;
            private String pd_amount;
            private String rcb_amount;

            public String getRefund_code() {
                return refund_code;
            }

            public void setRefund_code(String refund_code) {
                this.refund_code = refund_code;
            }

            public String getPay_amount() {
                return pay_amount;
            }

            public void setPay_amount(String pay_amount) {
                this.pay_amount = pay_amount;
            }

            public String getPd_amount() {
                return pd_amount;
            }

            public void setPd_amount(String pd_amount) {
                this.pd_amount = pd_amount;
            }

            public String getRcb_amount() {
                return rcb_amount;
            }

            public void setRcb_amount(String rcb_amount) {
                this.rcb_amount = rcb_amount;
            }
        }

        public static class GoodsListBean {
            /**
             * rec_id : 1442
             * order_id : 1299
             * goods_id : 100148
             * goods_name : 万和消毒柜ZTD100QE-1（Y)
             * goods_price : 2000.00
             * goods_num : 1
             * goods_image : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_240.jpg
             * goods_pay_price : 1990.00
             * store_id : 8
             * buyer_id : 98906
             * goods_type : 10
             * promotions_id : 0
             * commis_rate : 200
             * gc_id : 1148
             * goods_spec : null
             * goods_contractid :
             * goods_commonid : 100141
             * add_time : 1542782078
             * is_dis : 1
             * dis_commis_rate : 7.200
             * dis_member_id : 0
             * dis_type : 1
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
        }
    }
}
