package com.msht.minshengbao.androidShop.shopBean;


import java.util.List;

/**
 * @author mshtyfb
 */
public class StoreNewGoodBean {
    public StoreNewGoodBean(String date, List<GoodBean> list){
        this.date=date;
        this.list=list;
    }
    private String date;
    private List<GoodBean> list;

    public List<GoodBean> getGoodList(){
        return list;
    }

    public void setList(List<GoodBean> list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class GoodBean {

        /**
         * goods_id : 104146
         * store_id : 15
         * store_name : 时食本味
         * goods_name : 限50份桥头板栗地瓜 约8斤/大果（6至9两/颗） 超大实惠8斤装
         * goods_price : 39.00
         * goods_marketprice : 60.00
         * goods_image : 15_06083746892927178.jpg
         * goods_salenum : 2
         * evaluation_good_star : 5
         * evaluation_count : 0
         * is_virtual : 0
         * is_presell : 0
         * is_fcode : 0
         * have_gift : 0
         * goods_addtime : 1554959414
         * sole_flag : false
         * group_flag : false
         * xianshi_flag : false
         * goods_image_url : http://shop.msbapp.cn:8090/data/upload/shop/store/goods/15/15_06083746892927178_360.jpg
         * goods_addtime_text : 2019年04月11日
         */

        private String goods_id;
        private String store_id;
        private String store_name;
        private String goods_name;
        private String goods_price;
        private String goods_marketprice;
        private String goods_image;
        private String goods_salenum;
        private String evaluation_good_star;
        private String evaluation_count;
        private String is_virtual;
        private String is_presell;
        private String is_fcode;
        private String have_gift;
        private String goods_addtime;
        private boolean sole_flag;
        private boolean group_flag;
        private boolean xianshi_flag;
        private String goods_image_url;
        private String goods_addtime_text;



        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_marketprice() {
            return goods_marketprice;
        }

        public void setGoods_marketprice(String goods_marketprice) {
            this.goods_marketprice = goods_marketprice;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_salenum() {
            return goods_salenum;
        }

        public void setGoods_salenum(String goods_salenum) {
            this.goods_salenum = goods_salenum;
        }

        public String getEvaluation_good_star() {
            return evaluation_good_star;
        }

        public void setEvaluation_good_star(String evaluation_good_star) {
            this.evaluation_good_star = evaluation_good_star;
        }

        public String getEvaluation_count() {
            return evaluation_count;
        }

        public void setEvaluation_count(String evaluation_count) {
            this.evaluation_count = evaluation_count;
        }

        public String getIs_virtual() {
            return is_virtual;
        }

        public void setIs_virtual(String is_virtual) {
            this.is_virtual = is_virtual;
        }

        public String getIs_presell() {
            return is_presell;
        }

        public void setIs_presell(String is_presell) {
            this.is_presell = is_presell;
        }

        public String getIs_fcode() {
            return is_fcode;
        }

        public void setIs_fcode(String is_fcode) {
            this.is_fcode = is_fcode;
        }

        public String getHave_gift() {
            return have_gift;
        }

        public void setHave_gift(String have_gift) {
            this.have_gift = have_gift;
        }

        public String getGoods_addtime() {
            return goods_addtime;
        }

        public void setGoods_addtime(String goods_addtime) {
            this.goods_addtime = goods_addtime;
        }

        public boolean isSole_flag() {
            return sole_flag;
        }

        public void setSole_flag(boolean sole_flag) {
            this.sole_flag = sole_flag;
        }

        public boolean isGroup_flag() {
            return group_flag;
        }

        public void setGroup_flag(boolean group_flag) {
            this.group_flag = group_flag;
        }

        public boolean isXianshi_flag() {
            return xianshi_flag;
        }

        public void setXianshi_flag(boolean xianshi_flag) {
            this.xianshi_flag = xianshi_flag;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public String getGoods_addtime_text() {
            return goods_addtime_text;
        }

        public void setGoods_addtime_text(String goods_addtime_text) {
            this.goods_addtime_text = goods_addtime_text;
        }
    }
}
