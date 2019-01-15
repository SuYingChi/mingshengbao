package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopOrderDetailBean {

    /**
     * code : 200
     * datas : {"order_info":{"order_id":"1509","order_sn":"1000000000149001","pay_sn":"740598012012973906","store_id":"8","store_name":"福气廊","add_time":"2018-12-13 10:26:52","payment_time":"2018-12-13 10:27:16","shipping_time":"","finnshed_time":"","order_amount":"27440.00","shipping_fee":"0.00","real_pay_amount":"27440.00","refund_state":"0","order_state":"20","state_desc":"待发货","payment_name":"微信支付[客户端]","order_message":"","reciver_phone":"15501862693","reciver_name":"利民","reciver_addr":"河北 唐山市 丰润区 会葫芦","store_member_id":"30","store_default_im":"30","store_phone":"","chain_code":"","order_tips":"","promotion":[["满即送","单笔订单满2000元，立减100元"]],"invoice":"类型：普通发票  抬头：家里面 纳税人识别号：加啦加酱油 内容：饮料","if_deliver":false,"if_buyer_cancel":false,"if_refund_cancel":false,"if_receive":false,"if_evaluation":false,"if_delivery_receive":false,"if_evaluation_again":false,"if_lock":true,"order_type":"1","pintuan_info":null,"goods_list":[{"rec_id":"1707","goods_id":"100097","goods_name":"万家乐热水器JSQ32-16SH3（Y)","goods_price":"9180.00","goods_num":"3","goods_spec":null,"image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580310229266409_240.jpg","refund":0}],"zengpin_list":[{"goods_name":"自酿红酒","goods_num":"1"}],"ownshop":false}}
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
         * order_info : {"order_id":"1509","order_sn":"1000000000149001","pay_sn":"740598012012973906","store_id":"8","store_name":"福气廊","add_time":"2018-12-13 10:26:52","payment_time":"2018-12-13 10:27:16","shipping_time":"","finnshed_time":"","order_amount":"27440.00","shipping_fee":"0.00","real_pay_amount":"27440.00","refund_state":"0","order_state":"20","state_desc":"待发货","payment_name":"微信支付[客户端]","order_message":"","reciver_phone":"15501862693","reciver_name":"利民","reciver_addr":"河北 唐山市 丰润区 会葫芦","store_member_id":"30","store_default_im":"30","store_phone":"","chain_code":"","order_tips":"","promotion":[["满即送","单笔订单满2000元，立减100元"]],"invoice":"类型：普通发票  抬头：家里面 纳税人识别号：加啦加酱油 内容：饮料","if_deliver":false,"if_buyer_cancel":false,"if_refund_cancel":false,"if_receive":false,"if_evaluation":false,"if_delivery_receive":false,"if_evaluation_again":false,"if_lock":true,"order_type":"1","pintuan_info":null,"goods_list":[{"rec_id":"1707","goods_id":"100097","goods_name":"万家乐热水器JSQ32-16SH3（Y)","goods_price":"9180.00","goods_num":"3","goods_spec":null,"image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580310229266409_240.jpg","refund":0}],"zengpin_list":[{"goods_name":"自酿红酒","goods_num":"1"}],"ownshop":false}
         */

        private OrderInfoBean order_info;

        public OrderInfoBean getOrder_info() {
            return order_info;
        }

        public void setOrder_info(OrderInfoBean order_info) {
            this.order_info = order_info;
        }

        public static class OrderInfoBean {
            /**
             * order_id : 1509
             * order_sn : 1000000000149001
             * pay_sn : 740598012012973906
             * store_id : 8
             * store_name : 福气廊
             * add_time : 2018-12-13 10:26:52
             * payment_time : 2018-12-13 10:27:16
             * shipping_time :
             * finnshed_time :
             * order_amount : 27440.00
             * shipping_fee : 0.00
             * real_pay_amount : 27440.00
             * refund_state : 0
             * order_state : 20
             * state_desc : 待发货
             * payment_name : 微信支付[客户端]
             * order_message :
             * reciver_phone : 15501862693
             * reciver_name : 利民
             * reciver_addr : 河北 唐山市 丰润区 会葫芦
             * store_member_id : 30
             * store_default_im : 30
             * store_phone :
             * chain_code :
             * order_tips :
             * promotion : [["满即送","单笔订单满2000元，立减100元"]]
             * invoice : 类型：普通发票  抬头：家里面 纳税人识别号：加啦加酱油 内容：饮料
             * if_deliver : false
             * if_buyer_cancel : false
             * if_refund_cancel : false
             * if_receive : false
             * if_evaluation : false
             * if_delivery_receive : false
             * if_evaluation_again : false
             * if_lock : true
             * order_type : 1
             * pintuan_info : null
             * goods_list : [{"rec_id":"1707","goods_id":"100097","goods_name":"万家乐热水器JSQ32-16SH3（Y)","goods_price":"9180.00","goods_num":"3","goods_spec":null,"image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580310229266409_240.jpg","refund":0}]
             * zengpin_list : [{"goods_name":"自酿红酒","goods_num":"1"}]
             * ownshop : false
             */

            private String order_id;
            private String order_sn;
            private String pay_sn;
            private String store_id;
            private String store_name;
            private String add_time;
            private String payment_time;
            private String shipping_time;
            private String finnshed_time;
            private String order_amount;
            private String shipping_fee;
            private String real_pay_amount;
            private String refund_state;
            private String order_state;
            private String state_desc;
            private String payment_name;
            private String order_message;
            private String reciver_phone;
            private String reciver_name;
            private String reciver_addr;
            private String store_member_id;
            private String store_default_im;
            private String store_phone;
            private String chain_code;
            private String order_tips;
            private String invoice;
            private boolean if_deliver;
            private boolean if_buyer_cancel;
            private boolean if_refund_cancel;
            private boolean if_receive;
            private boolean if_evaluation;
            private boolean if_delivery_receive;
            private boolean if_evaluation_again;
            private boolean if_lock;
            private String order_type;
            private Object pintuan_info;
            private boolean ownshop;
            private List<List<String>> promotion;
            private List<GoodsListBean> goods_list;
            private List<ZengpinListBean> zengpin_list;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public String getPay_sn() {
                return pay_sn;
            }

            public void setPay_sn(String pay_sn) {
                this.pay_sn = pay_sn;
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

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getPayment_time() {
                return payment_time;
            }

            public void setPayment_time(String payment_time) {
                this.payment_time = payment_time;
            }

            public String getShipping_time() {
                return shipping_time;
            }

            public void setShipping_time(String shipping_time) {
                this.shipping_time = shipping_time;
            }

            public String getFinnshed_time() {
                return finnshed_time;
            }

            public void setFinnshed_time(String finnshed_time) {
                this.finnshed_time = finnshed_time;
            }

            public String getOrder_amount() {
                return order_amount;
            }

            public void setOrder_amount(String order_amount) {
                this.order_amount = order_amount;
            }

            public String getShipping_fee() {
                return shipping_fee;
            }

            public void setShipping_fee(String shipping_fee) {
                this.shipping_fee = shipping_fee;
            }

            public String getReal_pay_amount() {
                return real_pay_amount;
            }

            public void setReal_pay_amount(String real_pay_amount) {
                this.real_pay_amount = real_pay_amount;
            }

            public String getRefund_state() {
                return refund_state;
            }

            public void setRefund_state(String refund_state) {
                this.refund_state = refund_state;
            }

            public String getOrder_state() {
                return order_state;
            }

            public void setOrder_state(String order_state) {
                this.order_state = order_state;
            }

            public String getState_desc() {
                return state_desc;
            }

            public void setState_desc(String state_desc) {
                this.state_desc = state_desc;
            }

            public String getPayment_name() {
                return payment_name;
            }

            public void setPayment_name(String payment_name) {
                this.payment_name = payment_name;
            }

            public String getOrder_message() {
                return order_message;
            }

            public void setOrder_message(String order_message) {
                this.order_message = order_message;
            }

            public String getReciver_phone() {
                return reciver_phone;
            }

            public void setReciver_phone(String reciver_phone) {
                this.reciver_phone = reciver_phone;
            }

            public String getReciver_name() {
                return reciver_name;
            }

            public void setReciver_name(String reciver_name) {
                this.reciver_name = reciver_name;
            }

            public String getReciver_addr() {
                return reciver_addr;
            }

            public void setReciver_addr(String reciver_addr) {
                this.reciver_addr = reciver_addr;
            }

            public String getStore_member_id() {
                return store_member_id;
            }

            public void setStore_member_id(String store_member_id) {
                this.store_member_id = store_member_id;
            }

            public String getStore_default_im() {
                return store_default_im;
            }

            public void setStore_default_im(String store_default_im) {
                this.store_default_im = store_default_im;
            }

            public String getStore_phone() {
                return store_phone;
            }

            public void setStore_phone(String store_phone) {
                this.store_phone = store_phone;
            }

            public String getChain_code() {
                return chain_code;
            }

            public void setChain_code(String chain_code) {
                this.chain_code = chain_code;
            }

            public String getOrder_tips() {
                return order_tips;
            }

            public void setOrder_tips(String order_tips) {
                this.order_tips = order_tips;
            }

            public String getInvoice() {
                return invoice;
            }

            public void setInvoice(String invoice) {
                this.invoice = invoice;
            }

            public boolean isIf_deliver() {
                return if_deliver;
            }

            public void setIf_deliver(boolean if_deliver) {
                this.if_deliver = if_deliver;
            }

            public boolean isIf_buyer_cancel() {
                return if_buyer_cancel;
            }

            public void setIf_buyer_cancel(boolean if_buyer_cancel) {
                this.if_buyer_cancel = if_buyer_cancel;
            }

            public boolean isIf_refund_cancel() {
                return if_refund_cancel;
            }

            public void setIf_refund_cancel(boolean if_refund_cancel) {
                this.if_refund_cancel = if_refund_cancel;
            }

            public boolean isIf_receive() {
                return if_receive;
            }

            public void setIf_receive(boolean if_receive) {
                this.if_receive = if_receive;
            }

            public boolean isIf_evaluation() {
                return if_evaluation;
            }

            public void setIf_evaluation(boolean if_evaluation) {
                this.if_evaluation = if_evaluation;
            }

            public boolean isIf_delivery_receive() {
                return if_delivery_receive;
            }

            public void setIf_delivery_receive(boolean if_delivery_receive) {
                this.if_delivery_receive = if_delivery_receive;
            }

            public boolean isIf_evaluation_again() {
                return if_evaluation_again;
            }

            public void setIf_evaluation_again(boolean if_evaluation_again) {
                this.if_evaluation_again = if_evaluation_again;
            }

            public boolean isIf_lock() {
                return if_lock;
            }

            public void setIf_lock(boolean if_lock) {
                this.if_lock = if_lock;
            }

            public String getOrder_type() {
                return order_type;
            }

            public void setOrder_type(String order_type) {
                this.order_type = order_type;
            }

            public Object getPintuan_info() {
                return pintuan_info;
            }

            public void setPintuan_info(Object pintuan_info) {
                this.pintuan_info = pintuan_info;
            }

            public boolean isOwnshop() {
                return ownshop;
            }

            public void setOwnshop(boolean ownshop) {
                this.ownshop = ownshop;
            }

            public List<List<String>> getPromotion() {
                return promotion;
            }

            public void setPromotion(List<List<String>> promotion) {
                this.promotion = promotion;
            }

            public List<GoodsListBean> getGoods_list() {
                return goods_list;
            }

            public void setGoods_list(List<GoodsListBean> goods_list) {
                this.goods_list = goods_list;
            }

            public List<ZengpinListBean> getZengpin_list() {
                return zengpin_list;
            }

            public void setZengpin_list(List<ZengpinListBean> zengpin_list) {
                this.zengpin_list = zengpin_list;
            }

            public static class GoodsListBean {
                /**
                 * rec_id : 1707
                 * goods_id : 100097
                 * goods_name : 万家乐热水器JSQ32-16SH3（Y)
                 * goods_price : 9180.00
                 * goods_num : 3
                 * goods_spec : null
                 * image_url : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580310229266409_240.jpg
                 * refund : 0
                 */

                private String rec_id;
                private String goods_id;
                private String goods_name;
                private String goods_price;
                private String goods_num;
                private Object goods_spec;
                private String image_url;
                private int refund;

                public String getRec_id() {
                    return rec_id;
                }

                public void setRec_id(String rec_id) {
                    this.rec_id = rec_id;
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

                public Object getGoods_spec() {
                    return goods_spec;
                }

                public void setGoods_spec(Object goods_spec) {
                    this.goods_spec = goods_spec;
                }

                public String getImage_url() {
                    return image_url;
                }

                public void setImage_url(String image_url) {
                    this.image_url = image_url;
                }

                public int getRefund() {
                    return refund;
                }

                public void setRefund(int refund) {
                    this.refund = refund;
                }
            }

            public static class ZengpinListBean {
                /**
                 * goods_name : 自酿红酒
                 * goods_num : 1
                 */

                private String goods_name;
                private String goods_num;

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
            }
        }
    }
}