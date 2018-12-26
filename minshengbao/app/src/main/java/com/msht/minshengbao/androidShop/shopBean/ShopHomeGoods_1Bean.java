package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopHomeGoods_1Bean {

    /**
     * goods_1 : {"title":"精选厨电","more_link":"http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1287","item":[{"goods_id":"100126","goods_name":"万和燃热水器JSQ20-10ET15（Y)","goods_promotion_price":"1799.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05758925627527552_360.png"},{"goods_id":"100037","goods_name":"林内热水器RUS-13FEH（Y)","goods_promotion_price":"4399.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05578564894636221_360.jpg"},{"goods_id":"100344","goods_name":"迅达燃气热水器 10L 强排 JSQ19-D1602S（Z）","goods_promotion_price":"2498.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05747754465380193_360.jpg"},{"goods_id":"100146","goods_name":"万和热水器JSQ48-24A（亲水E蓝)（Y)","goods_promotion_price":"4999.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05688911762784647_360.jpg"},{"goods_id":"100134","goods_name":"万和燃热水器JSQ25-13GT18（Y)","goods_promotion_price":"2980.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05638816958195525_360.jpg"},{"goods_id":"100123","goods_name":"万和热水器JSG13-6.5B（Y)","goods_promotion_price":"1829.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05581096026205954_360.jpg"}]}
     */

    private Goods1Bean goods_1;

    public Goods1Bean getGoods_1() {
        return goods_1;
    }

    public void setGoods_1(Goods1Bean goods_1) {
        this.goods_1 = goods_1;
    }

    public static class Goods1Bean {
        /**
         * title : 精选厨电
         * more_link : http://shop.msbapp.cn:8090/wap/tmpl/class_list.html?gc_id=1287
         * item : [{"goods_id":"100126","goods_name":"万和燃热水器JSQ20-10ET15（Y)","goods_promotion_price":"1799.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05758925627527552_360.png"},{"goods_id":"100037","goods_name":"林内热水器RUS-13FEH（Y)","goods_promotion_price":"4399.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05578564894636221_360.jpg"},{"goods_id":"100344","goods_name":"迅达燃气热水器 10L 强排 JSQ19-D1602S（Z）","goods_promotion_price":"2498.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05747754465380193_360.jpg"},{"goods_id":"100146","goods_name":"万和热水器JSQ48-24A（亲水E蓝)（Y)","goods_promotion_price":"4999.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05688911762784647_360.jpg"},{"goods_id":"100134","goods_name":"万和燃热水器JSQ25-13GT18（Y)","goods_promotion_price":"2980.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05638816958195525_360.jpg"},{"goods_id":"100123","goods_name":"万和热水器JSG13-6.5B（Y)","goods_promotion_price":"1829.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05581096026205954_360.jpg"}]
         */

        private String title;
        private String more_link;
        private List<ItemBean> item;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMore_link() {
            return more_link;
        }

        public void setMore_link(String more_link) {
            this.more_link = more_link;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * goods_id : 100126
             * goods_name : 万和燃热水器JSQ20-10ET15（Y)
             * goods_promotion_price : 1799.00
             * goods_image : http://shop.msbapp.cn:8090/data/upload/shop/store/goods/8/8_05758925627527552_360.png
             */

            private String goods_id;
            private String goods_name;
            private String goods_promotion_price;
            private String goods_image;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_promotion_price() {
                return goods_promotion_price;
            }

            public void setGoods_promotion_price(String goods_promotion_price) {
                this.goods_promotion_price = goods_promotion_price;
            }

            public String getGoods_image() {
                return goods_image;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }
        }
    }
}
