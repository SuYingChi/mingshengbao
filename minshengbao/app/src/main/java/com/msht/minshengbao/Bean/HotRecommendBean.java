package com.msht.minshengbao.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/26  
 */
public class HotRecommendBean {

    /**
     * result : success
     * data : [{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/test/20190225/null/978f1e5422b84402ad7138a0d58180cd.png","code":"gas_stove_repair","isUrl":"1","title":"燃气灶维修","url":"","desc":"修的放心，用的舒心！\\n以最快的速度给您最好的服务","cid":"10"},{"img":"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/test/20190225/null/9d85cb9812374db1ac6caf74af1c0a7f.png","code":"household_clean","isUrl":"2","title":"家电清洗","url":"http://test.msbapp.cn:8080/household_cleaning/index.html","desc":"高温杀毒，杜绝尘螨！\\n明码标价给您最优惠的选择","cid":"0"}]
     * result_code : 0
     * error : null
     */

    private String result;
    private int result_code;
    private String error;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * img : https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/test/20190225/null/978f1e5422b84402ad7138a0d58180cd.png
         * code : gas_stove_repair
         * isUrl : 1
         * title : 燃气灶维修
         * url :
         * desc : 修的放心，用的舒心！\n以最快的速度给您最好的服务
         * cid : 10
         */

        private String img;
        private String code;
        private String parent_name;
        private String child_name;
        private String title;
        private String url;
        private String desc;
        private String cid;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getParent_name() {
            return parent_name;
        }

        public void setParent_name(String parentName) {
            this.parent_name = parentName;
        }

        public String getChild_name() {
            return child_name;
        }

        public void setChild_name(String childName) {
            this.child_name = childName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }
    }
}
