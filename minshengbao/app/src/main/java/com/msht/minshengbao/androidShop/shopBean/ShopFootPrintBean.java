package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopFootPrintBean {

    /**
     * code : 200
     * hasmore : true
     * page_total : 2
     * datas : {"goodsbrowse_list":[{"day":"今天","goods_list":[{"goods_id":"100259","goods_name":"拉菲红酒","goods_promotion_price":"0.02","goods_promotion_type":"0","goods_marketprice":"0.02","goods_image":"8_05966275234516671.jpg","store_id":"8","gc_id":"1089","gc_id_1":"1062","gc_id_2":"1089","gc_id_3":"0","browsetime":"1543302418","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05966275234516671_360.jpg","browsetime_day":"今天","browsetime_text":"今天15:06"},{"goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_promotion_price":"2479.00","goods_promotion_type":"0","goods_marketprice":"3099.00","goods_image":"8_05581817268248993.jpg","store_id":"8","gc_id":"1148","gc_id_1":"1059","gc_id_2":"1081","gc_id_3":"1148","browsetime":"1543291501","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_360.jpg","browsetime_day":"今天","browsetime_text":"今天12:05"},{"goods_id":"100144","goods_name":"万和燃热水器JSQ30-16GT28（Y)","goods_promotion_price":"4858.00","goods_promotion_type":"0","goods_marketprice":"6080.00","goods_image":"8_05581772115130713.jpg","store_id":"8","gc_id":"1217","gc_id_1":"1057","gc_id_2":"1064","gc_id_3":"1217","browsetime":"1543284302","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581772115130713_360.jpg","browsetime_day":"今天","browsetime_text":"今天10:05"},{"goods_id":"100135","goods_name":"万家乐灶具JZT-QJ03T（Z）","goods_promotion_price":"1000.00","goods_promotion_type":"0","goods_marketprice":"1250.00","goods_image":"8_05581760845638145.png","store_id":"8","gc_id":"1178","gc_id_1":"1058","gc_id_2":"1070","gc_id_3":"1178","browsetime":"1543280701","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581760845638145_360.png","browsetime_day":"今天","browsetime_text":"今天09:05"},{"goods_id":"100090","goods_name":"万家乐热水器JSQ30-16A5（Y)","goods_promotion_price":"4219.00","goods_promotion_type":"0","goods_marketprice":"5279.00","goods_image":"8_05580287513613269.jpg","store_id":"8","gc_id":"1220","gc_id_1":"1057","gc_id_2":"1065","gc_id_3":"1220","browsetime":"1543277150","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580287513613269_360.jpg","browsetime_day":"今天","browsetime_text":"今天08:05"},{"goods_id":"100089","goods_name":"帅康灶具QA-118-A（T)","goods_promotion_price":"2642.00","goods_promotion_type":"0","goods_marketprice":"3302.00","goods_image":"8_05580286125821808.jpg","store_id":"8","gc_id":"1192","gc_id_1":"1058","gc_id_2":"1071","gc_id_3":"1192","browsetime":"1543277102","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580286125821808_360.jpg","browsetime_day":"今天","browsetime_text":"今天08:05"}]},{"day":"昨天","goods_list":[{"goods_id":"100144","goods_name":"万和燃热水器JSQ30-16GT28（Y)","goods_promotion_price":"4858.00","goods_promotion_type":"0","goods_marketprice":"6080.00","goods_image":"8_05581772115130713.jpg","store_id":"8","gc_id":"1217","gc_id_1":"1057","gc_id_2":"1064","gc_id_3":"1217","browsetime":"1543230342","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581772115130713_360.jpg","browsetime_day":"昨天","browsetime_text":"昨天19:05"},{"goods_id":"100239","goods_name":"测试酒类多规格商品发布是否重复显示 100还是发射点发生的故事广东分公司","goods_promotion_price":"100.01","goods_promotion_type":"0","goods_marketprice":"30.00","goods_image":"11_05901459950727004.jpg","store_id":"11","gc_id":"1088","gc_id_1":"1062","gc_id_2":"1088","gc_id_3":"0","browsetime":"1543223102","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/11/11_05901459950727004_360.jpg","browsetime_day":"昨天","browsetime_text":"昨天17:05"},{"goods_id":"100257","goods_name":"测试商品发布","goods_promotion_price":"100.00","goods_promotion_type":"0","goods_marketprice":"122.00","goods_image":"8_05965657823363030.png","store_id":"8","gc_id":"1173","gc_id_1":"1058","gc_id_2":"1069","gc_id_3":"1173","browsetime":"1543223102","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05965657823363030_360.png","browsetime_day":"昨天","browsetime_text":"昨天17:05"},{"goods_id":"100255","goods_name":"测试拼团","goods_promotion_price":"0.01","goods_promotion_type":"0","goods_marketprice":"1.00","goods_image":"8_05961312222215111.jpg","store_id":"8","gc_id":"1173","gc_id_1":"1058","gc_id_2":"1069","gc_id_3":"1173","browsetime":"1543223102","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05961312222215111_360.jpg","browsetime_day":"昨天","browsetime_text":"昨天17:05"}]}]}
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
        private List<GoodsbrowseListBean> goodsbrowse_list;

        public List<GoodsbrowseListBean> getGoodsbrowse_list() {
            return goodsbrowse_list;
        }

        public void setGoodsbrowse_list(List<GoodsbrowseListBean> goodsbrowse_list) {
            this.goodsbrowse_list = goodsbrowse_list;
        }

        public static class GoodsbrowseListBean {
            /**
             * day : 今天
             * goods_list : [{"goods_id":"100259","goods_name":"拉菲红酒","goods_promotion_price":"0.02","goods_promotion_type":"0","goods_marketprice":"0.02","goods_image":"8_05966275234516671.jpg","store_id":"8","gc_id":"1089","gc_id_1":"1062","gc_id_2":"1089","gc_id_3":"0","browsetime":"1543302418","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05966275234516671_360.jpg","browsetime_day":"今天","browsetime_text":"今天15:06"},{"goods_id":"100148","goods_name":"万和消毒柜ZTD100QE-1（Y)","goods_promotion_price":"2479.00","goods_promotion_type":"0","goods_marketprice":"3099.00","goods_image":"8_05581817268248993.jpg","store_id":"8","gc_id":"1148","gc_id_1":"1059","gc_id_2":"1081","gc_id_3":"1148","browsetime":"1543291501","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581817268248993_360.jpg","browsetime_day":"今天","browsetime_text":"今天12:05"},{"goods_id":"100144","goods_name":"万和燃热水器JSQ30-16GT28（Y)","goods_promotion_price":"4858.00","goods_promotion_type":"0","goods_marketprice":"6080.00","goods_image":"8_05581772115130713.jpg","store_id":"8","gc_id":"1217","gc_id_1":"1057","gc_id_2":"1064","gc_id_3":"1217","browsetime":"1543284302","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581772115130713_360.jpg","browsetime_day":"今天","browsetime_text":"今天10:05"},{"goods_id":"100135","goods_name":"万家乐灶具JZT-QJ03T（Z）","goods_promotion_price":"1000.00","goods_promotion_type":"0","goods_marketprice":"1250.00","goods_image":"8_05581760845638145.png","store_id":"8","gc_id":"1178","gc_id_1":"1058","gc_id_2":"1070","gc_id_3":"1178","browsetime":"1543280701","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05581760845638145_360.png","browsetime_day":"今天","browsetime_text":"今天09:05"},{"goods_id":"100090","goods_name":"万家乐热水器JSQ30-16A5（Y)","goods_promotion_price":"4219.00","goods_promotion_type":"0","goods_marketprice":"5279.00","goods_image":"8_05580287513613269.jpg","store_id":"8","gc_id":"1220","gc_id_1":"1057","gc_id_2":"1065","gc_id_3":"1220","browsetime":"1543277150","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580287513613269_360.jpg","browsetime_day":"今天","browsetime_text":"今天08:05"},{"goods_id":"100089","goods_name":"帅康灶具QA-118-A（T)","goods_promotion_price":"2642.00","goods_promotion_type":"0","goods_marketprice":"3302.00","goods_image":"8_05580286125821808.jpg","store_id":"8","gc_id":"1192","gc_id_1":"1058","gc_id_2":"1071","gc_id_3":"1192","browsetime":"1543277102","goods_image_url":"http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05580286125821808_360.jpg","browsetime_day":"今天","browsetime_text":"今天08:05"}]
             */

            private String day;
            private List<GoodsListBean> goods_list;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public List<GoodsListBean> getGoods_list() {
                return goods_list;
            }

            public void setGoods_list(List<GoodsListBean> goods_list) {
                this.goods_list = goods_list;
            }

            public static class GoodsListBean {
                /**
                 * goods_id : 100259
                 * goods_name : 拉菲红酒
                 * goods_promotion_price : 0.02
                 * goods_promotion_type : 0
                 * goods_marketprice : 0.02
                 * goods_image : 8_05966275234516671.jpg
                 * store_id : 8
                 * gc_id : 1089
                 * gc_id_1 : 1062
                 * gc_id_2 : 1089
                 * gc_id_3 : 0
                 * browsetime : 1543302418
                 * goods_image_url : http://dev.msbapp.cn/data/upload/shop/store/goods/8/8_05966275234516671_360.jpg
                 * browsetime_day : 今天
                 * browsetime_text : 今天15:06
                 */

                private String goods_id;
                private String goods_name;
                private String goods_promotion_price;
                private String goods_promotion_type;
                private String goods_marketprice;
                private String goods_image;
                private String store_id;
                private String gc_id;
                private String gc_id_1;
                private String gc_id_2;
                private String gc_id_3;
                private String browsetime;
                private String goods_image_url;
                private String browsetime_day;
                private String browsetime_text;

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

                public String getGoods_promotion_type() {
                    return goods_promotion_type;
                }

                public void setGoods_promotion_type(String goods_promotion_type) {
                    this.goods_promotion_type = goods_promotion_type;
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

                public String getStore_id() {
                    return store_id;
                }

                public void setStore_id(String store_id) {
                    this.store_id = store_id;
                }

                public String getGc_id() {
                    return gc_id;
                }

                public void setGc_id(String gc_id) {
                    this.gc_id = gc_id;
                }

                public String getGc_id_1() {
                    return gc_id_1;
                }

                public void setGc_id_1(String gc_id_1) {
                    this.gc_id_1 = gc_id_1;
                }

                public String getGc_id_2() {
                    return gc_id_2;
                }

                public void setGc_id_2(String gc_id_2) {
                    this.gc_id_2 = gc_id_2;
                }

                public String getGc_id_3() {
                    return gc_id_3;
                }

                public void setGc_id_3(String gc_id_3) {
                    this.gc_id_3 = gc_id_3;
                }

                public String getBrowsetime() {
                    return browsetime;
                }

                public void setBrowsetime(String browsetime) {
                    this.browsetime = browsetime;
                }

                public String getGoods_image_url() {
                    return goods_image_url;
                }

                public void setGoods_image_url(String goods_image_url) {
                    this.goods_image_url = goods_image_url;
                }

                public String getBrowsetime_day() {
                    return browsetime_day;
                }

                public void setBrowsetime_day(String browsetime_day) {
                    this.browsetime_day = browsetime_day;
                }

                public String getBrowsetime_text() {
                    return browsetime_text;
                }

                public void setBrowsetime_text(String browsetime_text) {
                    this.browsetime_text = browsetime_text;
                }
            }
        }
    }
}
