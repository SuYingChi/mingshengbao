package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class SiteBean implements Serializable{

    /**
     * code : 200
     * datas : {"addr_list":[{"dlyp_id":"27","dlyp_name":"18876997791","dlyp_passwd":"e10adc3949ba59abbe56e057f20f883e","dlyp_truename":null,"dlyp_mobile":"18876997791","dlyp_telephony":"","dlyp_address_name":"珊瑚湾 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸岛二西路珊瑚湾小区B2栋1层2号铺面","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1536054483","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"26","dlyp_name":"15501837229","dlyp_passwd":"9fddbfd4c0e356500bfb27208046de14","dlyp_truename":null,"dlyp_mobile":"15501837229","dlyp_telephony":"","dlyp_address_name":"水岸阳光A区 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海口市美兰区海甸岛二东路水岸阳光a小区四栋 104号","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1536032971","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"24","dlyp_name":"13976876886","dlyp_passwd":"44c97d338f001d5599ed1a430008717d","dlyp_truename":null,"dlyp_mobile":"13976876886","dlyp_telephony":"","dlyp_address_name":"海名轩 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海口市和平大道15-8号海名轩小区南10F","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1533194587","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"23","dlyp_name":"18907649926","dlyp_passwd":"e10adc3949ba59abbe56e057f20f883e","dlyp_truename":null,"dlyp_mobile":"18907649926","dlyp_telephony":"","dlyp_address_name":"水岸听涛小区","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸岛二东路水岸听涛小区","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1533118787","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"20","dlyp_name":"18808960821","dlyp_passwd":"3d6cf2261a213ad55c39c8db3370b9e3","dlyp_truename":null,"dlyp_mobile":"18808960821","dlyp_telephony":"","dlyp_address_name":"海景花园 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海景花园A1栋101","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532999038","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"14","dlyp_name":"13648663969","dlyp_passwd":"7d9ff029b66444a08115ddb29908ebc8","dlyp_truename":null,"dlyp_mobile":"13648663969","dlyp_telephony":"","dlyp_address_name":"荣域 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸六东路荣域小区港丽乐超市","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532947780","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"13","dlyp_name":"18976859537","dlyp_passwd":"e10adc3949ba59abbe56e057f20f883e","dlyp_truename":null,"dlyp_mobile":"13005001651","dlyp_telephony":"","dlyp_address_name":"江南城 民生宝服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸岛和平大道66-56商铺-C2","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532938098","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"11","dlyp_name":"18808985871","dlyp_passwd":"e10adc3949ba59abbe56e057f20f883e","dlyp_truename":null,"dlyp_mobile":"18789691219","dlyp_telephony":"","dlyp_address_name":"诚田大厦","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3271","dlyp_area_4":"0","dlyp_area":"3271","dlyp_area_info":"海南 海口市 龙华区","dlyp_address":"诚田大厦","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532935639","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"龙华区"},{"dlyp_id":"9","dlyp_name":"18789691219","dlyp_passwd":"432253ae7001c70997b752ce02acb18a","dlyp_truename":null,"dlyp_mobile":"18889893175","dlyp_telephony":"","dlyp_address_name":"民生大厦","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸岛四东路民生大厦","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532934429","dlyp_state":"1","dlyp_fail_reason":null,"dlyp_area_name":"美兰区"},{"dlyp_id":"6","dlyp_name":"15348845566","dlyp_passwd":"9738afdc3b8261ea5b08b2e6d3045410","dlyp_truename":null,"dlyp_mobile":"15348845566","dlyp_telephony":"","dlyp_address_name":"伟达雅郡服务站","dlyp_area_1":"21","dlyp_area_2":"324","dlyp_area_3":"3270","dlyp_area_4":"0","dlyp_area":"3270","dlyp_area_info":"海南 海口市 美兰区","dlyp_address":"海甸岛四东路伟达雅郡一楼铺面20-12号大糖堡烘焙坊","dlyp_idcard":null,"dlyp_idcard_image":null,"dlyp_addtime":"1532076730","dlyp_state":"1","dlyp_fail_reason":"","dlyp_area_name":"美兰区"}]}
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

    public static class DatasBean implements Serializable{
        private List<AddrListBean> addr_list;

        public List<AddrListBean> getAddr_list() {
            return addr_list;
        }

        public void setAddr_list(List<AddrListBean> addr_list) {
            this.addr_list = addr_list;
        }

        public static class AddrListBean implements Serializable{
            /**
             * dlyp_id : 27
             * dlyp_name : 18876997791
             * dlyp_passwd : e10adc3949ba59abbe56e057f20f883e
             * dlyp_truename : null
             * dlyp_mobile : 18876997791
             * dlyp_telephony :
             * dlyp_address_name : 珊瑚湾 民生宝服务站
             * dlyp_area_1 : 21
             * dlyp_area_2 : 324
             * dlyp_area_3 : 3270
             * dlyp_area_4 : 0
             * dlyp_area : 3270
             * dlyp_area_info : 海南 海口市 美兰区
             * dlyp_address : 海甸岛二西路珊瑚湾小区B2栋1层2号铺面
             * dlyp_idcard : null
             * dlyp_idcard_image : null
             * dlyp_addtime : 1536054483
             * dlyp_state : 1
             * dlyp_fail_reason : null
             * dlyp_area_name : 美兰区
             */

            private String dlyp_id;
            private String dlyp_name;
            private String dlyp_passwd;
            private Object dlyp_truename;
            private String dlyp_mobile;
            private String dlyp_telephony;
            private String dlyp_address_name;
            private String dlyp_area_1;
            private String dlyp_area_2;
            private String dlyp_area_3;
            private String dlyp_area_4;
            private String dlyp_area;
            private String dlyp_area_info;
            private String dlyp_address;
            private Object dlyp_idcard;
            private Object dlyp_idcard_image;
            private String dlyp_addtime;
            private String dlyp_state;
            private Object dlyp_fail_reason;
            private String dlyp_area_name;

            public String getDlyp_id() {
                return dlyp_id;
            }

            public void setDlyp_id(String dlyp_id) {
                this.dlyp_id = dlyp_id;
            }

            public String getDlyp_name() {
                return dlyp_name;
            }

            public void setDlyp_name(String dlyp_name) {
                this.dlyp_name = dlyp_name;
            }

            public String getDlyp_passwd() {
                return dlyp_passwd;
            }

            public void setDlyp_passwd(String dlyp_passwd) {
                this.dlyp_passwd = dlyp_passwd;
            }

            public Object getDlyp_truename() {
                return dlyp_truename;
            }

            public void setDlyp_truename(Object dlyp_truename) {
                this.dlyp_truename = dlyp_truename;
            }

            public String getDlyp_mobile() {
                return dlyp_mobile;
            }

            public void setDlyp_mobile(String dlyp_mobile) {
                this.dlyp_mobile = dlyp_mobile;
            }

            public String getDlyp_telephony() {
                return dlyp_telephony;
            }

            public void setDlyp_telephony(String dlyp_telephony) {
                this.dlyp_telephony = dlyp_telephony;
            }

            public String getDlyp_address_name() {
                return dlyp_address_name;
            }

            public void setDlyp_address_name(String dlyp_address_name) {
                this.dlyp_address_name = dlyp_address_name;
            }

            public String getDlyp_area_1() {
                return dlyp_area_1;
            }

            public void setDlyp_area_1(String dlyp_area_1) {
                this.dlyp_area_1 = dlyp_area_1;
            }

            public String getDlyp_area_2() {
                return dlyp_area_2;
            }

            public void setDlyp_area_2(String dlyp_area_2) {
                this.dlyp_area_2 = dlyp_area_2;
            }

            public String getDlyp_area_3() {
                return dlyp_area_3;
            }

            public void setDlyp_area_3(String dlyp_area_3) {
                this.dlyp_area_3 = dlyp_area_3;
            }

            public String getDlyp_area_4() {
                return dlyp_area_4;
            }

            public void setDlyp_area_4(String dlyp_area_4) {
                this.dlyp_area_4 = dlyp_area_4;
            }

            public String getDlyp_area() {
                return dlyp_area;
            }

            public void setDlyp_area(String dlyp_area) {
                this.dlyp_area = dlyp_area;
            }

            public String getDlyp_area_info() {
                return dlyp_area_info;
            }

            public void setDlyp_area_info(String dlyp_area_info) {
                this.dlyp_area_info = dlyp_area_info;
            }

            public String getDlyp_address() {
                return dlyp_address;
            }

            public void setDlyp_address(String dlyp_address) {
                this.dlyp_address = dlyp_address;
            }

            public Object getDlyp_idcard() {
                return dlyp_idcard;
            }

            public void setDlyp_idcard(Object dlyp_idcard) {
                this.dlyp_idcard = dlyp_idcard;
            }

            public Object getDlyp_idcard_image() {
                return dlyp_idcard_image;
            }

            public void setDlyp_idcard_image(Object dlyp_idcard_image) {
                this.dlyp_idcard_image = dlyp_idcard_image;
            }

            public String getDlyp_addtime() {
                return dlyp_addtime;
            }

            public void setDlyp_addtime(String dlyp_addtime) {
                this.dlyp_addtime = dlyp_addtime;
            }

            public String getDlyp_state() {
                return dlyp_state;
            }

            public void setDlyp_state(String dlyp_state) {
                this.dlyp_state = dlyp_state;
            }

            public Object getDlyp_fail_reason() {
                return dlyp_fail_reason;
            }

            public void setDlyp_fail_reason(Object dlyp_fail_reason) {
                this.dlyp_fail_reason = dlyp_fail_reason;
            }

            public String getDlyp_area_name() {
                return dlyp_area_name;
            }

            public void setDlyp_area_name(String dlyp_area_name) {
                this.dlyp_area_name = dlyp_area_name;
            }
        }
    }
}
