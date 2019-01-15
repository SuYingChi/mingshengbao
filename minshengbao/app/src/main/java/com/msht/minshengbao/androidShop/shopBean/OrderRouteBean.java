package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class OrderRouteBean {

    /**
     * code : 200
     * datas : {"express_code":"JD","express_name":"京东物流","express_service_phone":"950616","shipping_code":"534341415","shipping_time":"2018-11-07 10:54:48","state":"无状态","deliver_info":["2018-11-07 10:54:48  卖家已发货"]}
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
         * express_code : JD
         * express_name : 京东物流
         * express_service_phone : 950616
         * shipping_code : 534341415
         * shipping_time : 2018-11-07 10:54:48
         * state : 无状态
         * deliver_info : ["2018-11-07 10:54:48  卖家已发货"]
         */

        private String express_code;
        private String express_name;
        private String express_service_phone;
        private String shipping_code;
        private String shipping_time;
        private String state;
        private List<String> deliver_info;

        public String getExpress_code() {
            return express_code;
        }

        public void setExpress_code(String express_code) {
            this.express_code = express_code;
        }

        public String getExpress_name() {
            return express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
        }

        public String getExpress_service_phone() {
            return express_service_phone;
        }

        public void setExpress_service_phone(String express_service_phone) {
            this.express_service_phone = express_service_phone;
        }

        public String getShipping_code() {
            return shipping_code;
        }

        public void setShipping_code(String shipping_code) {
            this.shipping_code = shipping_code;
        }

        public String getShipping_time() {
            return shipping_time;
        }

        public void setShipping_time(String shipping_time) {
            this.shipping_time = shipping_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<String> getDeliver_info() {
            return deliver_info;
        }

        public void setDeliver_info(List<String> deliver_info) {
            this.deliver_info = deliver_info;
        }
    }
}
