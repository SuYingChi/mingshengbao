package com.msht.minshengbao.androidShop.shopBean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShopHomeClassBean implements Serializable {

    /**
     * class : {"item":[{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804013689082.png","title":"民生拼团","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1268"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804077067575.png","title":"时令水果","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1272"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804126507434.png","title":"每日蔬菜","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1275"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804173555484.png","title":"海鲜肉蛋","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1281"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804377714907.png","title":"特产零食","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1286"},{},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804494116508.png","title":"社区货架","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1301"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804546125195.png","title":"鲜花蛋糕","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1305"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804604447615.png","title":"酒水茗茶","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1308"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804677398320.png","title":"更多商品","type":"url","data":"http://shop.msbapp.cn/wap/tmpl/product_first_categroy.html"}]}
     */

    @SerializedName("class")
    private ClassBean classX;

    public ClassBean getClassX() {
        return classX;
    }

    public void setClassX(ClassBean classX) {
        this.classX = classX;
    }

    public static class ClassBean implements Serializable{
        private List<ItemBean> item;

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean implements Serializable {
            /**
             * image : http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911804013689082.png
             * title : 民生拼团
             * type : url
             * data : http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1268
             */

            private String image;
            private String title;
            private String type;
            private String data;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }
    }
}
