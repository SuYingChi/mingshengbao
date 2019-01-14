package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopHomeGoods_3Bean {

    /**
     * goods_3 : {"title":"买遍全球","more_link":"http://dev.msbapp.cn/mobile/index.php?act=index","item":[{"goods_id":"100158","goods_name":"万和灶具B9-12T-L16Z（Y)","goods_promotion_price":"2589.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581963673902904_360.jpg"},{"goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_promotion_price":"1969.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_360.jpg"},{"goods_id":"100156","goods_name":"万和烟机CXW-230-J08BS（Y)","goods_promotion_price":"3938.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581860941988399_360.jpg"},{"goods_id":"100145","goods_name":"万家乐灶具JZY-QJ02B（Y)","goods_promotion_price":"1569.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581773092789069_360.jpg"},{"goods_id":"100144","goods_name":"万和燃热水器JSQ30-16GT28（Y)","goods_promotion_price":"4858.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581772115130713_360.jpg"},{"goods_id":"100138","goods_name":"万和燃热水器JSQ25-13GT36（Y)","goods_promotion_price":"3758.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581764694122777_360.png"},{"goods_id":"100136","goods_name":"万和燃热水器JSQ25-13GT28（Y)","goods_promotion_price":"3598.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581762299020981_360.jpg"}]}
     */

    private Goods3Bean goods_3;

    public Goods3Bean getGoods_3() {
        return goods_3;
    }

    public void setGoods_3(Goods3Bean goods_3) {
        this.goods_3 = goods_3;
    }

    public static class Goods3Bean {
        /**
         * title : 买遍全球
         * more_link : http://dev.msbapp.cn/mobile/index.php?act=index
         * item : [{"goods_id":"100158","goods_name":"万和灶具B9-12T-L16Z（Y)","goods_promotion_price":"2589.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581963673902904_360.jpg"},{"goods_id":"100157","goods_name":"万和灶具 C3-12T-L16Z（Y)","goods_promotion_price":"1969.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581955944170345_360.jpg"},{"goods_id":"100156","goods_name":"万和烟机CXW-230-J08BS（Y)","goods_promotion_price":"3938.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581860941988399_360.jpg"},{"goods_id":"100145","goods_name":"万家乐灶具JZY-QJ02B（Y)","goods_promotion_price":"1569.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581773092789069_360.jpg"},{"goods_id":"100144","goods_name":"万和燃热水器JSQ30-16GT28（Y)","goods_promotion_price":"4858.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581772115130713_360.jpg"},{"goods_id":"100138","goods_name":"万和燃热水器JSQ25-13GT36（Y)","goods_promotion_price":"3758.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581764694122777_360.png"},{"goods_id":"100136","goods_name":"万和燃热水器JSQ25-13GT28（Y)","goods_promotion_price":"3598.00","goods_image":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581762299020981_360.jpg"}]
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
             * goods_id : 100158
             * goods_name : 万和灶具B9-12T-L16Z（Y)
             * goods_promotion_price : 2589.00
             * goods_image : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581963673902904_360.jpg
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
