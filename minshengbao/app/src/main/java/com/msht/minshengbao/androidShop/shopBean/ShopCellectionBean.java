package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopCellectionBean {

    /**
     * code : 200
     * hasmore : false
     * page_total : 1
     * datas : {"favorites_list":[{"goods_id":"100056","goods_name":"林内烟机CXW-200-JSQA（Y)","goods_image":"8_05579408670653998.jpg","store_id":"8","fav_id":"100056","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05579408670653998_240.jpg","goods_price":"4280.00"},{"goods_id":"100064","goods_name":"帅康消毒柜ZTD-100K-S2（Z）","goods_image":"8_05580067430121993.jpg","store_id":"8","fav_id":"100064","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580067430121993_240.jpg","goods_price":"4035.00"},{"goods_id":"100092","goods_name":"万家乐热水器JSQ32-16QH3(白）（Y)","goods_image":"8_05580289960559623.jpg","store_id":"8","fav_id":"100092","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580289960559623_240.jpg","goods_price":"9799.00"},{"goods_id":"100094","goods_name":"万家乐热水器JSQ32-16QH3（红）（Y)","goods_image":"8_05580292572724376.jpg","store_id":"8","fav_id":"100094","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580292572724376_240.jpg","goods_price":"9799.00"},{"goods_id":"100096","goods_name":"万家乐热水器JSQ32-16S2（Y)","goods_image":"8_05580306709188629.jpg","store_id":"8","fav_id":"100096","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580306709188629_240.jpg","goods_price":"5480.00"}]}
     */

    private int page_total;
    private DatasBean datas;

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private List<FavoritesListBean> favorites_list;

        public List<FavoritesListBean> getFavorites_list() {
            return favorites_list;
        }

        public void setFavorites_list(List<FavoritesListBean> favorites_list) {
            this.favorites_list = favorites_list;
        }

        public static class FavoritesListBean {
            /**
             * goods_id : 100056
             * goods_name : 林内烟机CXW-200-JSQA（Y)
             * goods_image : 8_05579408670653998.jpg
             * store_id : 8
             * fav_id : 100056
             * goods_image_url : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05579408670653998_240.jpg
             * goods_price : 4280.00
             */

            private String goods_id;
            private String goods_name;
            private String goods_image;
            private String store_id;
            private String fav_id;
            private String goods_image_url;
            private String goods_price;

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

            public String getGoods_image() {
                return goods_image;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getFav_id() {
                return fav_id;
            }

            public void setFav_id(String fav_id) {
                this.fav_id = fav_id;
            }

            public String getGoods_image_url() {
                return goods_image_url;
            }

            public void setGoods_image_url(String goods_image_url) {
                this.goods_image_url = goods_image_url;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }
        }
    }
}
