package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopHomeAdvBean {

    /**
     * adv_list : {"item":[{"image":"http://dev.msbapp.cn/data/upload/mobile/special/s0/s0_05877482545880730.jpg","type":"url","data":"http://dev.msbapp.cn/wap/tmpl/product_list.html?store_id=8&amp;xianshi=1"},{"image":"http://dev.msbapp.cn/data/upload/mobile/special/s0/s0_05877483123760839.jpg","type":"keyword","data":"热水器"},{"image":"http://dev.msbapp.cn/data/upload/mobile/special/s0/s0_05877486843417452.jpg","type":"goods","data":"100096"},{"image":"http://dev.msbapp.cn/data/upload/mobile/special/s0/s0_05877488245777446.jpg","type":"special","data":"4"}]}
     */

    private AdvListBean adv_list;

    public AdvListBean getAdv_list() {
        return adv_list;
    }

    public void setAdv_list(AdvListBean adv_list) {
        this.adv_list = adv_list;
    }

    public static class AdvListBean {
        private List<ItemBean> item;

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * image : http://dev.msbapp.cn/data/upload/mobile/special/s0/s0_05877482545880730.jpg
             * type : url
             * data : http://dev.msbapp.cn/wap/tmpl/product_list.html?store_id=8&amp;xianshi=1
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

    @Override
    public String toString() {
        return "ShopHomeAdvBean{" +
                "adv_list=" + adv_list +
                '}';
    }
}
