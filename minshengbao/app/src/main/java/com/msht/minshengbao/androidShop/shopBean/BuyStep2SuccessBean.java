package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;

public class BuyStep2SuccessBean{

    /**
     * code : 200
     * datas : {"pay_sn":"770595502677581023","payment_code":"online","key":null,"order_id":6643}
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
         * pay_sn : 770595502677581023
         * payment_code : online
         * key : null
         * order_id : 6643
         */

        private String pay_sn;
        private String payment_code;
        private Object key;
        private int order_id;

        public String getPay_sn() {
            return pay_sn;
        }

        public void setPay_sn(String pay_sn) {
            this.pay_sn = pay_sn;
        }

        public String getPayment_code() {
            return payment_code;
        }

        public void setPayment_code(String payment_code) {
            this.payment_code = payment_code;
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
    }
}
