package com.msht.minshengbao.Model;

import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/17  
 */
public class InsuranceBusinessModel {

    /**
     * result_code : 0
     * error : null
     * result : success
     * data : [{"id":1,"s_index":1,"type":"燃气险","child":[{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png","amount":30,"company":"太平洋洋保险","id":"3","title":"商业燃气险","desc":"燃气安全无小事，燃气保险保平安"},{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png","amount":100,"company":"北冰洋保险","id":"4","title":"商业燃气险","desc":"燃气安全无小事，燃气保险保平安"}]},{"s_index":2,"id":2,"type":"意外险","child":[{"img":"http://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20181129/199/d7c89a2723fb4af2b12f3e74032912c9.jpg","amount":0,"company":"人寿保险","id":"5","title":"居民燃气险（意外险）","desc":"燃气安全无小事，燃气保险保平安"}]}]
     */

    private int result_code;
    private String error;
    private String result;
    private List<DataBean> data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * s_index : 1
         * type : 燃气险
         * child : [{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png","amount":30,"company":"太平洋洋保险","id":"3","title":"商业燃气险","desc":"燃气安全无小事，燃气保险保平安"},{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png","amount":100,"company":"北冰洋保险","id":"4","title":"商业燃气险","desc":"燃气安全无小事，燃气保险保平安"}]
         */

        private int id;
        private int s_index;
        private String type;
        private List<ChildBean> child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getS_index() {
            return s_index;
        }

        public void setS_index(int s_index) {
            this.s_index = s_index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            /**
             * img : https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png
             * amount : 30
             * company : 太平洋洋保险
             * id : 3
             * title : 商业燃气险
             * desc : 燃气安全无小事，燃气保险保平安
             */

            private String img;
            private double amount;
            private String company;
            private int id;
            private String title;
            private String desc;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
