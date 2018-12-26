package com.msht.minshengbao.androidShop.shopBean;

public class LoginShopBean {

    /**
     * code : 200
     * datas : {"username":"13876183230","userid":"249023","key":"9712ab272d6141411ac6f8eceddffb3a"}
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
         * username : 13876183230
         * userid : 249023
         * key : 9712ab272d6141411ac6f8eceddffb3a
         */

        private String username;
        private String userid;
        private String key;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
