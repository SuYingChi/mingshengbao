package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class ClassFirstBean implements Serializable{

    /**
     * code : 200
     * datas : {"class_list":[{"gc_id":"1320","gc_name":"社区菜园","type_id":"0","type_name":"","gc_parent_id":"0","commis_rate":"20","gc_sort":"1","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05997599039615745.png","text":"叶菜类/根茎菜/菌菇类/茄瓜类/辣椒/姜葱/豆菜/豆腐/面食类/肉禽蛋品"},{"gc_id":"1321","gc_name":"社区果园","type_id":"0","type_name":"","gc_parent_id":"0","commis_rate":"20","gc_sort":"2","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05997599162133740.png","text":"当季热销"},{"gc_id":"1268","gc_name":"民生拼团","type_id":"8","type_name":"民生拼团","gc_parent_id":"0","commis_rate":"10","gc_sort":"7","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892857563402192.png","text":"生鲜/其他"},{"gc_id":"1272","gc_name":"时令水果","type_id":"9","type_name":"新鲜水果","gc_parent_id":"0","commis_rate":"10","gc_sort":"8","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892857757719843.png","text":"热卖推荐"},{"gc_id":"1275","gc_name":"每日蔬菜","type_id":"20","type_name":"每日蔬菜","gc_parent_id":"0","commis_rate":"11","gc_sort":"9","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892857892716544.png","text":"叶菜类/根茎类/瓜果类/其他类/每日特惠"},{"gc_id":"1281","gc_name":"海鲜肉蛋","type_id":"6","type_name":"规格","gc_parent_id":"0","commis_rate":"10","gc_sort":"10","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858069467364.png","text":"熟食类/海鲜类/猪肉类/鸡鸭禽"},{"gc_id":"1286","gc_name":"特产零食","type_id":"14","type_name":"特产零食","gc_parent_id":"0","commis_rate":"10","gc_sort":"11","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858233925084.png","text":"全国直邮/海口同城"},{"gc_id":"1287","gc_name":"厨卫电器","type_id":"15","type_name":"厨卫电器","gc_parent_id":"0","commis_rate":"10","gc_sort":"12","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858362552699.png","text":"燃气灶/热水器/油烟机/消毒柜"},{"gc_id":"1301","gc_name":"粮油米面","type_id":"16","type_name":"社区货架","gc_parent_id":"0","commis_rate":"10","gc_sort":"13","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858538351843.png","text":"米面类/好油类/肉蛋类/特产零食/酒茶饮品"},{"gc_id":"1305","gc_name":"鲜花蛋糕","type_id":"17","type_name":"鲜花蛋糕","gc_parent_id":"0","commis_rate":"10","gc_sort":"14","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858712123377.png","text":"蛋糕类/鲜花类"},{"gc_id":"1308","gc_name":"酒水茗茶","type_id":"18","type_name":"酒水茗茶","gc_parent_id":"0","commis_rate":"10","gc_sort":"15","gc_virtual":"0","gc_title":"","gc_keywords":"","gc_description":"","show_type":"1","image":"http://shop.msbapp.cn:8090/data/upload/mobile/category/05892858851064707.png","text":"红酒类/白酒类/其他类"}]}
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
        private List<ClassListBean> class_list;

        public List<ClassListBean> getClass_list() {
            return class_list;
        }

        public void setClass_list(List<ClassListBean> class_list) {
            this.class_list = class_list;
        }

        public static class ClassListBean implements Serializable{
            /**
             * gc_id : 1320
             * gc_name : 社区菜园
             * type_id : 0
             * type_name :
             * gc_parent_id : 0
             * commis_rate : 20
             * gc_sort : 1
             * gc_virtual : 0
             * gc_title :
             * gc_keywords :
             * gc_description :
             * show_type : 1
             * image : http://shop.msbapp.cn:8090/data/upload/mobile/category/05997599039615745.png
             * text : 叶菜类/根茎菜/菌菇类/茄瓜类/辣椒/姜葱/豆菜/豆腐/面食类/肉禽蛋品
             */

            private String gc_id;
            private String gc_name;
            private String type_id;
            private String type_name;
            private String gc_parent_id;
            private String commis_rate;
            private String gc_sort;
            private String gc_virtual;
            private String gc_title;
            private String gc_keywords;
            private String gc_description;
            private String show_type;
            private String image;
            private String text;

            public String getGc_id() {
                return gc_id;
            }

            public void setGc_id(String gc_id) {
                this.gc_id = gc_id;
            }

            public String getGc_name() {
                return gc_name;
            }

            public void setGc_name(String gc_name) {
                this.gc_name = gc_name;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getGc_parent_id() {
                return gc_parent_id;
            }

            public void setGc_parent_id(String gc_parent_id) {
                this.gc_parent_id = gc_parent_id;
            }

            public String getCommis_rate() {
                return commis_rate;
            }

            public void setCommis_rate(String commis_rate) {
                this.commis_rate = commis_rate;
            }

            public String getGc_sort() {
                return gc_sort;
            }

            public void setGc_sort(String gc_sort) {
                this.gc_sort = gc_sort;
            }

            public String getGc_virtual() {
                return gc_virtual;
            }

            public void setGc_virtual(String gc_virtual) {
                this.gc_virtual = gc_virtual;
            }

            public String getGc_title() {
                return gc_title;
            }

            public void setGc_title(String gc_title) {
                this.gc_title = gc_title;
            }

            public String getGc_keywords() {
                return gc_keywords;
            }

            public void setGc_keywords(String gc_keywords) {
                this.gc_keywords = gc_keywords;
            }

            public String getGc_description() {
                return gc_description;
            }

            public void setGc_description(String gc_description) {
                this.gc_description = gc_description;
            }

            public String getShow_type() {
                return show_type;
            }

            public void setShow_type(String show_type) {
                this.show_type = show_type;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
