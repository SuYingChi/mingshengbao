package com.msht.minshengbao.Model;

import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RepairAdditionalBean {


    /**
     * name : 类目明细
     * code : category_detail
     * value : [{"name":"挂式机清洗","code":"code_33_125","value":"1"},{"name":"柜式机清洗","code":"code_33_126","value":"2"}]
     */

    private String name;
    private String code;
    private List<ValueBean> value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ValueBean> getValue() {
        return value;
    }

    public void setValue(List<ValueBean> value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * name : 挂式机清洗
         * code : code_33_125
         * value : 1
         */

        private String name;
        private String code;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
