package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class MessagePreviewBean {

    /**
     * result : success
     * error : null
     * data : [{"content":"您的报修工单已提交, 工单号为: 201811231629178440202491766573。工作人员将会在24小时内与您联系并上门为您服务。如遇紧急情况请拨热线电话963666。","id":1316,"unread_num":0,"time":"2018-11-23 16:29","title":"报修工单","type":1},{"content":"123123","id":1396,"unread_num":23,"time":"19小时前","title":"测试","type":2},{"content":"{\"order_id\":\"1468\",\"order_sn\":\"1000000000145001\",\"e_name\":\"\\u4eac\\u4e1c\\u7269\\u6d41\",\"e_code\":\"JD\",\"shipping_code\":\"3833491661359\",\"msg\":\"\\u60a8\\u7684\\u8ba2\\u5355\\u5df2\\u53d1\\u8d27\\uff0c\\u8bf7\\u6ce8\\u610f\\u67e5\\u6536\\u3002\\u4eac\\u4e1c\\u7269\\u6d41:3833491661359\",\"url\":\"http:\\/\\/dev.msbapp.cn\\/shop\\/index.php?act=member_order&op=show_order&order_id=1468\",\"goods_list\":[{\"goods_name\":\"\\u62c9\\u83f2\\u7ea2\\u9152\",\"goods_image\":\"http:\\/\\/dev.msbapp.cn\\/data\\/upload\\/shop\\/store\\/goods\\/8\\/8_05966275234516671_240.jpg\"}],\"type\":3}","id":1387,"unread_num":11,"time":"2018-11-30 11:33","title":"订单发货通知","type":3},{"content":"测试内容","id":1059,"unread_num":27,"time":"2018-10-23 14:46","title":"测试保存消息中心","type":4}]
     */

    private String result;
    private Object error;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public static class DataBean {
        /**
         * content : 您的报修工单已提交, 工单号为: 201811231629178440202491766573。工作人员将会在24小时内与您联系并上门为您服务。如遇紧急情况请拨热线电话963666。
         * id : 1316
         * unread_num : 0
         * time : 2018-11-23 16:29
         * title : 报修工单
         * type : 1
         */

        private String content;
        private int id;
        private int unread_num;
        private String time;
        private String title;
        private int type;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUnread_num() {
            return unread_num;
        }

        public void setUnread_num(int unread_num) {
            this.unread_num = unread_num;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
