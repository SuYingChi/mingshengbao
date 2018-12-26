package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class NativePayMethodsBean implements Serializable{

    /**
     * result : success
     * data : [{"code":"wx_pay","name":"微信支付","channel":5,"tips":null},{"code":"ali_pay","name":"支付宝支付","channel":1,"tips":null},{"code":"upacp_pay","name":"银联支付","channel":3,"tips":null}]
     * result_code : 0
     * error : null
     */

    private String result;
    private int result_code;
    private Object error;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * code : wx_pay
         * name : 微信支付
         * channel : 5
         * tips : null
         */

        private String code;
        private String name;
        private int channel;
        private Object tips;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getChannel() {
            return channel;
        }

        public void setChannel(int channel) {
            this.channel = channel;
        }

        public Object getTips() {
            return tips;
        }

        public void setTips(Object tips) {
            this.tips = tips;
        }
    }
}
