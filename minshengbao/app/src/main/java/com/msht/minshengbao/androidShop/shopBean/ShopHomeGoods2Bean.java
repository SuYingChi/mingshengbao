package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopHomeGoods2Bean {

    /**
     * goods_2 : {"title":"甄选推荐","item":[{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05901498855702171.jpg","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/product_list.html?keyword=蟹"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911836574039705.jpg","type":"goods","data":"102809"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05912050669022879.png","type":"goods","data":"101847"}]}
     */

    private Goods2Bean goods_2;

    public Goods2Bean getGoods_2() {
        return goods_2;
    }

    public void setGoods_2(Goods2Bean goods_2) {
        this.goods_2 = goods_2;
    }

    public static class Goods2Bean {
        /**
         * title : 甄选推荐
         * item : [{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05901498855702171.jpg","type":"url","data":"http://shop.msbapp.cn:8090/wap/tmpl/product_list.html?keyword=蟹"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05911836574039705.jpg","type":"goods","data":"102809"},{"image":"http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05912050669022879.png","type":"goods","data":"101847"}]
         */

        private String title;
        private List<ItemBean> item;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * image : http://shop.msbapp.cn:8090/data/upload/mobile/special/s0/s0_05901498855702171.jpg
             * type : url
             * data : http://shop.msbapp.cn:8090/wap/tmpl/product_list.html?keyword=蟹
             */

            private String image;
            private String type;
            private String data;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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
