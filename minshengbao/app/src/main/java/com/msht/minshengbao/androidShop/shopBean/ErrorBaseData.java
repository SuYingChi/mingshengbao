package com.msht.minshengbao.androidShop.shopBean;

public class ErrorBaseData  {

    /**
     * code : 400
     * datas : {"error":"该订单曾尝试使用第三方支付平台支付，须在24小时以后才可取消"}
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
         * error : 该订单曾尝试使用第三方支付平台支付，须在24小时以后才可取消
         */

        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
