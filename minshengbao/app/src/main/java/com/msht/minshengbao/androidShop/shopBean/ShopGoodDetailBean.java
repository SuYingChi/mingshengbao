package com.msht.minshengbao.androidShop.shopBean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopGoodDetailBean {

    /**
     * code : 200
     * datas : {"goods_info":{"goods_name":"【直邮】海南文昌金鲳鱼 3斤/5斤 两种规格可选 外省顺丰包邮 国庆有礼 【3斤】海口包邮","goods_jingle":"生鲜产品下单后不能修改地址。  送货时需本人签收，签收请开箱验货。如发现问题请拒签，并马上联系客服（正常签收则视为无问题）客服电话：18789693040","gc_id_1":"1268","gc_id_2":"1269","gc_id_3":"0","spec_name":{"4":"尺码"},"spec_value":{"4":{"390":"【3斤】海口包邮","391":"【3斤】全国顺丰包邮","392":"【5斤】海口包邮","393":"【5斤】全国顺丰包邮"}},"goods_attr":null,"goods_custom":null,"mobile_body":"","goods_price":"69.00","goods_marketprice":"118.00","goods_costprice":"0.00","goods_discount":"58","goods_serial":"","goods_storage_alarm":"0","goods_barcode":"","transport_id":"0","transport_title":"","goods_freight":"0.00","goods_vat":"0","areaid_1":"0","areaid_2":"0","goods_stcids":"","plateid_top":"0","plateid_bottom":"0","is_virtual":"0","virtual_indate":"0","virtual_limit":"0","virtual_invalid_refund":"0","sup_id":"0","is_own_shop":"0","goods_trans_v":"0.00","is_dis":"1","dis_add_time":"1537955055","dis_commis_rate":"4.800","goods_video":"","sale_count":"0","click_count":"254","is_batch":"0","batch_price":[],"goods_inv":"0","dis_type":"0","pickup_self":"0","goods_id":"103019","goods_promotion_price":"69.00","goods_promotion_type":"0","goods_click":176,"goods_salenum":"0","goods_collect":"0","goods_spec":{"390":"【3斤】海口包邮"},"goods_storage":"500","color_id":"0","evaluation_good_star":"5","evaluation_count":"0","is_fcode":"0","is_presell":"0","presell_deliverdate":"0","is_book":"0","book_down_payment":"0.00","book_final_payment":"0.00","book_down_time":"0","book_buyers":"0","have_gift":"0","contract_1":"0","contract_2":"0","contract_3":"0","contract_4":"0","contract_5":"0","contract_6":"0","contract_7":"0","contract_8":"0","contract_9":"0","contract_10":"0","is_chain":"0","promotion_info":[],"sole_info":[],"groupbuy_info":null,"jjg_info":null,"cart":1,"buynow":1,"pintuan_promotion":"0","contractlist":[],"goods_url":"http://shop.msbapp.cn:8090/shop/index.php?act=goods&op=index&goods_id=103019"},"spec_image":["http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_60.jpg"],"image_list":[{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_1280.jpg"},{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_1280.jpg"},{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_1280.jpg"}],"video_path":null,"mansong_info":null,"gift_array":null,"goods_commend_list":[{},{},{},{},{}],"store_info":{"store_id":"25","store_name":"椰公主旗舰店","member_id":"239999","default_im":"239999","member_name":"yegongzhu","is_own_shop":"0","goods_count":"7","store_credit":{"store_desccredit":{"text":"描述","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_servicecredit":{"text":"服务","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_deliverycredit":{"text":"物流","credit":"4.8","percent":"2%","percent_class":"low","percent_text":"低"}}},"spec_list":{"390":"103019","391":"103020","392":"103021","393":"103022"},"goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_360.jpg,http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_360.jpg,http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_360.jpg","is_favorate":false,"goods_eval_list":[],"goods_evaluate_info":{"img":0,"good":0,"normal":0,"bad":0,"all":0,"good_percent":100,"normal_percent":0,"bad_percent":0,"good_star":5,"star_average":5},"goods_hair_info":{"content":"免运费","if_store_cn":"有货","if_store":true,"area_name":"全国"}}
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
        /**
         * goods_info : {"goods_name":"【直邮】海南文昌金鲳鱼 3斤/5斤 两种规格可选 外省顺丰包邮 国庆有礼 【3斤】海口包邮","goods_jingle":"生鲜产品下单后不能修改地址。  送货时需本人签收，签收请开箱验货。如发现问题请拒签，并马上联系客服（正常签收则视为无问题）客服电话：18789693040","gc_id_1":"1268","gc_id_2":"1269","gc_id_3":"0","spec_name":{"4":"尺码"},"spec_value":{"4":{"390":"【3斤】海口包邮","391":"【3斤】全国顺丰包邮","392":"【5斤】海口包邮","393":"【5斤】全国顺丰包邮"}},"goods_attr":null,"goods_custom":null,"mobile_body":"","goods_price":"69.00","goods_marketprice":"118.00","goods_costprice":"0.00","goods_discount":"58","goods_serial":"","goods_storage_alarm":"0","goods_barcode":"","transport_id":"0","transport_title":"","goods_freight":"0.00","goods_vat":"0","areaid_1":"0","areaid_2":"0","goods_stcids":"","plateid_top":"0","plateid_bottom":"0","is_virtual":"0","virtual_indate":"0","virtual_limit":"0","virtual_invalid_refund":"0","sup_id":"0","is_own_shop":"0","goods_trans_v":"0.00","is_dis":"1","dis_add_time":"1537955055","dis_commis_rate":"4.800","goods_video":"","sale_count":"0","click_count":"254","is_batch":"0","batch_price":[],"goods_inv":"0","dis_type":"0","pickup_self":"0","goods_id":"103019","goods_promotion_price":"69.00","goods_promotion_type":"0","goods_click":176,"goods_salenum":"0","goods_collect":"0","goods_spec":{"390":"【3斤】海口包邮"},"goods_storage":"500","color_id":"0","evaluation_good_star":"5","evaluation_count":"0","is_fcode":"0","is_presell":"0","presell_deliverdate":"0","is_book":"0","book_down_payment":"0.00","book_final_payment":"0.00","book_down_time":"0","book_buyers":"0","have_gift":"0","contract_1":"0","contract_2":"0","contract_3":"0","contract_4":"0","contract_5":"0","contract_6":"0","contract_7":"0","contract_8":"0","contract_9":"0","contract_10":"0","is_chain":"0","promotion_info":[],"sole_info":[],"groupbuy_info":null,"jjg_info":null,"cart":1,"buynow":1,"pintuan_promotion":"0","contractlist":[],"goods_url":"http://shop.msbapp.cn:8090/shop/index.php?act=goods&op=index&goods_id=103019"}
         * spec_image : ["http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_60.jpg"]
         * image_list : [{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_1280.jpg"},{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_1280.jpg"},{"_small":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_60.jpg","_mid":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_360.jpg","_big":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_1280.jpg"}]
         * video_path : null
         * mansong_info : null
         * gift_array : null
         * goods_commend_list : [{},{},{},{},{}]
         * store_info : {"store_id":"25","store_name":"椰公主旗舰店","member_id":"239999","default_im":"239999","member_name":"yegongzhu","is_own_shop":"0","goods_count":"7","store_credit":{"store_desccredit":{"text":"描述","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_servicecredit":{"text":"服务","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_deliverycredit":{"text":"物流","credit":"4.8","percent":"2%","percent_class":"low","percent_text":"低"}}}
         * spec_list : {"390":"103019","391":"103020","392":"103021","393":"103022"}
         * goods_image : http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912988505431519_360.jpg,http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990470559306_360.jpg,http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05912990489827892_360.jpg
         * is_favorate : false
         * goods_eval_list : []
         * goods_evaluate_info : {"img":0,"good":0,"normal":0,"bad":0,"all":0,"good_percent":100,"normal_percent":0,"bad_percent":0,"good_star":5,"star_average":5}
         * goods_hair_info : {"content":"免运费","if_store_cn":"有货","if_store":true,"area_name":"全国"}
         */

        private GoodsInfoBean goods_info;
        private Object video_path;
        private Object mansong_info;
        private Object gift_array;
        private StoreInfoBean store_info;
        private SpecListBean spec_list;
        private String goods_image;
        private boolean is_favorate;
        private GoodsEvaluateInfoBean goods_evaluate_info;
        private GoodsHairInfoBean goods_hair_info;
        private List<String> spec_image;
        private List<ImageListBean> image_list;
        private List<GoodsCommendListBean> goods_commend_list;
        private List<?> goods_eval_list;

        public GoodsInfoBean getGoods_info() {
            return goods_info;
        }

        public void setGoods_info(GoodsInfoBean goods_info) {
            this.goods_info = goods_info;
        }

        public Object getVideo_path() {
            return video_path;
        }

        public void setVideo_path(Object video_path) {
            this.video_path = video_path;
        }

        public Object getMansong_info() {
            return mansong_info;
        }

        public void setMansong_info(Object mansong_info) {
            this.mansong_info = mansong_info;
        }

        public Object getGift_array() {
            return gift_array;
        }

        public void setGift_array(Object gift_array) {
            this.gift_array = gift_array;
        }

        public StoreInfoBean getStore_info() {
            return store_info;
        }

        public void setStore_info(StoreInfoBean store_info) {
            this.store_info = store_info;
        }

        public SpecListBean getSpec_list() {
            return spec_list;
        }

        public void setSpec_list(SpecListBean spec_list) {
            this.spec_list = spec_list;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public boolean isIs_favorate() {
            return is_favorate;
        }

        public void setIs_favorate(boolean is_favorate) {
            this.is_favorate = is_favorate;
        }

        public GoodsEvaluateInfoBean getGoods_evaluate_info() {
            return goods_evaluate_info;
        }

        public void setGoods_evaluate_info(GoodsEvaluateInfoBean goods_evaluate_info) {
            this.goods_evaluate_info = goods_evaluate_info;
        }

        public GoodsHairInfoBean getGoods_hair_info() {
            return goods_hair_info;
        }

        public void setGoods_hair_info(GoodsHairInfoBean goods_hair_info) {
            this.goods_hair_info = goods_hair_info;
        }

        public List<String> getSpec_image() {
            return spec_image;
        }

        public void setSpec_image(List<String> spec_image) {
            this.spec_image = spec_image;
        }

        public List<ImageListBean> getImage_list() {
            return image_list;
        }

        public void setImage_list(List<ImageListBean> image_list) {
            this.image_list = image_list;
        }

        public List<GoodsCommendListBean> getGoods_commend_list() {
            return goods_commend_list;
        }

        public void setGoods_commend_list(List<GoodsCommendListBean> goods_commend_list) {
            this.goods_commend_list = goods_commend_list;
        }

        public List<?> getGoods_eval_list() {
            return goods_eval_list;
        }

        public void setGoods_eval_list(List<?> goods_eval_list) {
            this.goods_eval_list = goods_eval_list;
        }

        public static class GoodsInfoBean {
            /**
             * goods_name : 【直邮】海南文昌金鲳鱼 3斤/5斤 两种规格可选 外省顺丰包邮 国庆有礼 【3斤】海口包邮
             * goods_jingle : 生鲜产品下单后不能修改地址。  送货时需本人签收，签收请开箱验货。如发现问题请拒签，并马上联系客服（正常签收则视为无问题）客服电话：18789693040
             * gc_id_1 : 1268
             * gc_id_2 : 1269
             * gc_id_3 : 0
             * spec_name : {"4":"尺码"}
             * spec_value : {"4":{"390":"【3斤】海口包邮","391":"【3斤】全国顺丰包邮","392":"【5斤】海口包邮","393":"【5斤】全国顺丰包邮"}}
             * goods_attr : null
             * goods_custom : null
             * mobile_body :
             * goods_price : 69.00
             * goods_marketprice : 118.00
             * goods_costprice : 0.00
             * goods_discount : 58
             * goods_serial :
             * goods_storage_alarm : 0
             * goods_barcode :
             * transport_id : 0
             * transport_title :
             * goods_freight : 0.00
             * goods_vat : 0
             * areaid_1 : 0
             * areaid_2 : 0
             * goods_stcids :
             * plateid_top : 0
             * plateid_bottom : 0
             * is_virtual : 0
             * virtual_indate : 0
             * virtual_limit : 0
             * virtual_invalid_refund : 0
             * sup_id : 0
             * is_own_shop : 0
             * goods_trans_v : 0.00
             * is_dis : 1
             * dis_add_time : 1537955055
             * dis_commis_rate : 4.800
             * goods_video :
             * sale_count : 0
             * click_count : 254
             * is_batch : 0
             * batch_price : []
             * goods_inv : 0
             * dis_type : 0
             * pickup_self : 0
             * goods_id : 103019
             * goods_promotion_price : 69.00
             * goods_promotion_type : 0
             * goods_click : 176
             * goods_salenum : 0
             * goods_collect : 0
             * goods_spec : {"390":"【3斤】海口包邮"}
             * goods_storage : 500
             * color_id : 0
             * evaluation_good_star : 5
             * evaluation_count : 0
             * is_fcode : 0
             * is_presell : 0
             * presell_deliverdate : 0
             * is_book : 0
             * book_down_payment : 0.00
             * book_final_payment : 0.00
             * book_down_time : 0
             * book_buyers : 0
             * have_gift : 0
             * contract_1 : 0
             * contract_2 : 0
             * contract_3 : 0
             * contract_4 : 0
             * contract_5 : 0
             * contract_6 : 0
             * contract_7 : 0
             * contract_8 : 0
             * contract_9 : 0
             * contract_10 : 0
             * is_chain : 0
             * promotion_info : []
             * sole_info : []
             * groupbuy_info : null
             * jjg_info : null
             * cart : 1
             * buynow : 1
             * pintuan_promotion : 0
             * contractlist : []
             * goods_url : http://shop.msbapp.cn:8090/shop/index.php?act=goods&op=index&goods_id=103019
             */

            private String goods_name;
            private String goods_jingle;
            private String gc_id_1;
            private String gc_id_2;
            private String gc_id_3;
            private SpecNameBean spec_name;
            private SpecValueBean spec_value;
            private Object goods_attr;
            private Object goods_custom;
            private String mobile_body;
            private String goods_price;
            private String goods_marketprice;
            private String goods_costprice;
            private String goods_discount;
            private String goods_serial;
            private String goods_storage_alarm;
            private String goods_barcode;
            private String transport_id;
            private String transport_title;
            private String goods_freight;
            private String goods_vat;
            private String areaid_1;
            private String areaid_2;
            private String goods_stcids;
            private String plateid_top;
            private String plateid_bottom;
            private String is_virtual;
            private String virtual_indate;
            private String virtual_limit;
            private String virtual_invalid_refund;
            private String sup_id;
            private String is_own_shop;
            private String goods_trans_v;
            private String is_dis;
            private String dis_add_time;
            private String dis_commis_rate;
            private String goods_video;
            private String sale_count;
            private String click_count;
            private String is_batch;
            private String goods_inv;
            private String dis_type;
            private String pickup_self;
            private String goods_id;
            private String goods_promotion_price;
            private String goods_promotion_type;
            private int goods_click;
            private String goods_salenum;
            private String goods_collect;
            private GoodsSpecBean goods_spec;
            private String goods_storage;
            private String color_id;
            private String evaluation_good_star;
            private String evaluation_count;
            private String is_fcode;
            private String is_presell;
            private String presell_deliverdate;
            private String is_book;
            private String book_down_payment;
            private String book_final_payment;
            private String book_down_time;
            private String book_buyers;
            private String have_gift;
            private String contract_1;
            private String contract_2;
            private String contract_3;
            private String contract_4;
            private String contract_5;
            private String contract_6;
            private String contract_7;
            private String contract_8;
            private String contract_9;
            private String contract_10;
            private String is_chain;
            private Object groupbuy_info;
            private Object jjg_info;
            private int cart;
            private int buynow;
            private String pintuan_promotion;
            private String goods_url;
            private List<?> batch_price;
            private List<?> promotion_info;
            private List<?> sole_info;
            private List<?> contractlist;

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_jingle() {
                return goods_jingle;
            }

            public void setGoods_jingle(String goods_jingle) {
                this.goods_jingle = goods_jingle;
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

            public SpecNameBean getSpec_name() {
                return spec_name;
            }

            public void setSpec_name(SpecNameBean spec_name) {
                this.spec_name = spec_name;
            }

            public SpecValueBean getSpec_value() {
                return spec_value;
            }

            public void setSpec_value(SpecValueBean spec_value) {
                this.spec_value = spec_value;
            }

            public Object getGoods_attr() {
                return goods_attr;
            }

            public void setGoods_attr(Object goods_attr) {
                this.goods_attr = goods_attr;
            }

            public Object getGoods_custom() {
                return goods_custom;
            }

            public void setGoods_custom(Object goods_custom) {
                this.goods_custom = goods_custom;
            }

            public String getMobile_body() {
                return mobile_body;
            }

            public void setMobile_body(String mobile_body) {
                this.mobile_body = mobile_body;
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

            public String getGoods_costprice() {
                return goods_costprice;
            }

            public void setGoods_costprice(String goods_costprice) {
                this.goods_costprice = goods_costprice;
            }

            public String getGoods_discount() {
                return goods_discount;
            }

            public void setGoods_discount(String goods_discount) {
                this.goods_discount = goods_discount;
            }

            public String getGoods_serial() {
                return goods_serial;
            }

            public void setGoods_serial(String goods_serial) {
                this.goods_serial = goods_serial;
            }

            public String getGoods_storage_alarm() {
                return goods_storage_alarm;
            }

            public void setGoods_storage_alarm(String goods_storage_alarm) {
                this.goods_storage_alarm = goods_storage_alarm;
            }

            public String getGoods_barcode() {
                return goods_barcode;
            }

            public void setGoods_barcode(String goods_barcode) {
                this.goods_barcode = goods_barcode;
            }

            public String getTransport_id() {
                return transport_id;
            }

            public void setTransport_id(String transport_id) {
                this.transport_id = transport_id;
            }

            public String getTransport_title() {
                return transport_title;
            }

            public void setTransport_title(String transport_title) {
                this.transport_title = transport_title;
            }

            public String getGoods_freight() {
                return goods_freight;
            }

            public void setGoods_freight(String goods_freight) {
                this.goods_freight = goods_freight;
            }

            public String getGoods_vat() {
                return goods_vat;
            }

            public void setGoods_vat(String goods_vat) {
                this.goods_vat = goods_vat;
            }

            public String getAreaid_1() {
                return areaid_1;
            }

            public void setAreaid_1(String areaid_1) {
                this.areaid_1 = areaid_1;
            }

            public String getAreaid_2() {
                return areaid_2;
            }

            public void setAreaid_2(String areaid_2) {
                this.areaid_2 = areaid_2;
            }

            public String getGoods_stcids() {
                return goods_stcids;
            }

            public void setGoods_stcids(String goods_stcids) {
                this.goods_stcids = goods_stcids;
            }

            public String getPlateid_top() {
                return plateid_top;
            }

            public void setPlateid_top(String plateid_top) {
                this.plateid_top = plateid_top;
            }

            public String getPlateid_bottom() {
                return plateid_bottom;
            }

            public void setPlateid_bottom(String plateid_bottom) {
                this.plateid_bottom = plateid_bottom;
            }

            public String getIs_virtual() {
                return is_virtual;
            }

            public void setIs_virtual(String is_virtual) {
                this.is_virtual = is_virtual;
            }

            public String getVirtual_indate() {
                return virtual_indate;
            }

            public void setVirtual_indate(String virtual_indate) {
                this.virtual_indate = virtual_indate;
            }

            public String getVirtual_limit() {
                return virtual_limit;
            }

            public void setVirtual_limit(String virtual_limit) {
                this.virtual_limit = virtual_limit;
            }

            public String getVirtual_invalid_refund() {
                return virtual_invalid_refund;
            }

            public void setVirtual_invalid_refund(String virtual_invalid_refund) {
                this.virtual_invalid_refund = virtual_invalid_refund;
            }

            public String getSup_id() {
                return sup_id;
            }

            public void setSup_id(String sup_id) {
                this.sup_id = sup_id;
            }

            public String getIs_own_shop() {
                return is_own_shop;
            }

            public void setIs_own_shop(String is_own_shop) {
                this.is_own_shop = is_own_shop;
            }

            public String getGoods_trans_v() {
                return goods_trans_v;
            }

            public void setGoods_trans_v(String goods_trans_v) {
                this.goods_trans_v = goods_trans_v;
            }

            public String getIs_dis() {
                return is_dis;
            }

            public void setIs_dis(String is_dis) {
                this.is_dis = is_dis;
            }

            public String getDis_add_time() {
                return dis_add_time;
            }

            public void setDis_add_time(String dis_add_time) {
                this.dis_add_time = dis_add_time;
            }

            public String getDis_commis_rate() {
                return dis_commis_rate;
            }

            public void setDis_commis_rate(String dis_commis_rate) {
                this.dis_commis_rate = dis_commis_rate;
            }

            public String getGoods_video() {
                return goods_video;
            }

            public void setGoods_video(String goods_video) {
                this.goods_video = goods_video;
            }

            public String getSale_count() {
                return sale_count;
            }

            public void setSale_count(String sale_count) {
                this.sale_count = sale_count;
            }

            public String getClick_count() {
                return click_count;
            }

            public void setClick_count(String click_count) {
                this.click_count = click_count;
            }

            public String getIs_batch() {
                return is_batch;
            }

            public void setIs_batch(String is_batch) {
                this.is_batch = is_batch;
            }

            public String getGoods_inv() {
                return goods_inv;
            }

            public void setGoods_inv(String goods_inv) {
                this.goods_inv = goods_inv;
            }

            public String getDis_type() {
                return dis_type;
            }

            public void setDis_type(String dis_type) {
                this.dis_type = dis_type;
            }

            public String getPickup_self() {
                return pickup_self;
            }

            public void setPickup_self(String pickup_self) {
                this.pickup_self = pickup_self;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
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

            public int getGoods_click() {
                return goods_click;
            }

            public void setGoods_click(int goods_click) {
                this.goods_click = goods_click;
            }

            public String getGoods_salenum() {
                return goods_salenum;
            }

            public void setGoods_salenum(String goods_salenum) {
                this.goods_salenum = goods_salenum;
            }

            public String getGoods_collect() {
                return goods_collect;
            }

            public void setGoods_collect(String goods_collect) {
                this.goods_collect = goods_collect;
            }

            public GoodsSpecBean getGoods_spec() {
                return goods_spec;
            }

            public void setGoods_spec(GoodsSpecBean goods_spec) {
                this.goods_spec = goods_spec;
            }

            public String getGoods_storage() {
                return goods_storage;
            }

            public void setGoods_storage(String goods_storage) {
                this.goods_storage = goods_storage;
            }

            public String getColor_id() {
                return color_id;
            }

            public void setColor_id(String color_id) {
                this.color_id = color_id;
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

            public String getIs_fcode() {
                return is_fcode;
            }

            public void setIs_fcode(String is_fcode) {
                this.is_fcode = is_fcode;
            }

            public String getIs_presell() {
                return is_presell;
            }

            public void setIs_presell(String is_presell) {
                this.is_presell = is_presell;
            }

            public String getPresell_deliverdate() {
                return presell_deliverdate;
            }

            public void setPresell_deliverdate(String presell_deliverdate) {
                this.presell_deliverdate = presell_deliverdate;
            }

            public String getIs_book() {
                return is_book;
            }

            public void setIs_book(String is_book) {
                this.is_book = is_book;
            }

            public String getBook_down_payment() {
                return book_down_payment;
            }

            public void setBook_down_payment(String book_down_payment) {
                this.book_down_payment = book_down_payment;
            }

            public String getBook_final_payment() {
                return book_final_payment;
            }

            public void setBook_final_payment(String book_final_payment) {
                this.book_final_payment = book_final_payment;
            }

            public String getBook_down_time() {
                return book_down_time;
            }

            public void setBook_down_time(String book_down_time) {
                this.book_down_time = book_down_time;
            }

            public String getBook_buyers() {
                return book_buyers;
            }

            public void setBook_buyers(String book_buyers) {
                this.book_buyers = book_buyers;
            }

            public String getHave_gift() {
                return have_gift;
            }

            public void setHave_gift(String have_gift) {
                this.have_gift = have_gift;
            }

            public String getContract_1() {
                return contract_1;
            }

            public void setContract_1(String contract_1) {
                this.contract_1 = contract_1;
            }

            public String getContract_2() {
                return contract_2;
            }

            public void setContract_2(String contract_2) {
                this.contract_2 = contract_2;
            }

            public String getContract_3() {
                return contract_3;
            }

            public void setContract_3(String contract_3) {
                this.contract_3 = contract_3;
            }

            public String getContract_4() {
                return contract_4;
            }

            public void setContract_4(String contract_4) {
                this.contract_4 = contract_4;
            }

            public String getContract_5() {
                return contract_5;
            }

            public void setContract_5(String contract_5) {
                this.contract_5 = contract_5;
            }

            public String getContract_6() {
                return contract_6;
            }

            public void setContract_6(String contract_6) {
                this.contract_6 = contract_6;
            }

            public String getContract_7() {
                return contract_7;
            }

            public void setContract_7(String contract_7) {
                this.contract_7 = contract_7;
            }

            public String getContract_8() {
                return contract_8;
            }

            public void setContract_8(String contract_8) {
                this.contract_8 = contract_8;
            }

            public String getContract_9() {
                return contract_9;
            }

            public void setContract_9(String contract_9) {
                this.contract_9 = contract_9;
            }

            public String getContract_10() {
                return contract_10;
            }

            public void setContract_10(String contract_10) {
                this.contract_10 = contract_10;
            }

            public String getIs_chain() {
                return is_chain;
            }

            public void setIs_chain(String is_chain) {
                this.is_chain = is_chain;
            }

            public Object getGroupbuy_info() {
                return groupbuy_info;
            }

            public void setGroupbuy_info(Object groupbuy_info) {
                this.groupbuy_info = groupbuy_info;
            }

            public Object getJjg_info() {
                return jjg_info;
            }

            public void setJjg_info(Object jjg_info) {
                this.jjg_info = jjg_info;
            }

            public int getCart() {
                return cart;
            }

            public void setCart(int cart) {
                this.cart = cart;
            }

            public int getBuynow() {
                return buynow;
            }

            public void setBuynow(int buynow) {
                this.buynow = buynow;
            }

            public String getPintuan_promotion() {
                return pintuan_promotion;
            }

            public void setPintuan_promotion(String pintuan_promotion) {
                this.pintuan_promotion = pintuan_promotion;
            }

            public String getGoods_url() {
                return goods_url;
            }

            public void setGoods_url(String goods_url) {
                this.goods_url = goods_url;
            }

            public List<?> getBatch_price() {
                return batch_price;
            }

            public void setBatch_price(List<?> batch_price) {
                this.batch_price = batch_price;
            }

            public List<?> getPromotion_info() {
                return promotion_info;
            }

            public void setPromotion_info(List<?> promotion_info) {
                this.promotion_info = promotion_info;
            }

            public List<?> getSole_info() {
                return sole_info;
            }

            public void setSole_info(List<?> sole_info) {
                this.sole_info = sole_info;
            }

            public List<?> getContractlist() {
                return contractlist;
            }

            public void setContractlist(List<?> contractlist) {
                this.contractlist = contractlist;
            }

            public static class SpecNameBean {
                /**
                 * 4 : 尺码
                 */

                @SerializedName("4")
                private String _$4;

                public String get_$4() {
                    return _$4;
                }

                public void set_$4(String _$4) {
                    this._$4 = _$4;
                }
            }

            public static class SpecValueBean {
                /**
                 * 4 : {"390":"【3斤】海口包邮","391":"【3斤】全国顺丰包邮","392":"【5斤】海口包邮","393":"【5斤】全国顺丰包邮"}
                 */

                @SerializedName("4")
                private _$4Bean _$4;

                public _$4Bean get_$4() {
                    return _$4;
                }

                public void set_$4(_$4Bean _$4) {
                    this._$4 = _$4;
                }

                public static class _$4Bean {
                    /**
                     * 390 : 【3斤】海口包邮
                     * 391 : 【3斤】全国顺丰包邮
                     * 392 : 【5斤】海口包邮
                     * 393 : 【5斤】全国顺丰包邮
                     */

                    @SerializedName("390")
                    private String _$390;
                    @SerializedName("391")
                    private String _$391;
                    @SerializedName("392")
                    private String _$392;
                    @SerializedName("393")
                    private String _$393;

                    public String get_$390() {
                        return _$390;
                    }

                    public void set_$390(String _$390) {
                        this._$390 = _$390;
                    }

                    public String get_$391() {
                        return _$391;
                    }

                    public void set_$391(String _$391) {
                        this._$391 = _$391;
                    }

                    public String get_$392() {
                        return _$392;
                    }

                    public void set_$392(String _$392) {
                        this._$392 = _$392;
                    }

                    public String get_$393() {
                        return _$393;
                    }

                    public void set_$393(String _$393) {
                        this._$393 = _$393;
                    }
                }
            }

            public static class GoodsSpecBean {
                /**
                 * 390 : 【3斤】海口包邮
                 */

                @SerializedName("390")
                private String _$390;

                public String get_$390() {
                    return _$390;
                }

                public void set_$390(String _$390) {
                    this._$390 = _$390;
                }
            }
        }

        public static class StoreInfoBean {
            /**
             * store_id : 25
             * store_name : 椰公主旗舰店
             * member_id : 239999
             * default_im : 239999
             * member_name : yegongzhu
             * is_own_shop : 0
             * goods_count : 7
             * store_credit : {"store_desccredit":{"text":"描述","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_servicecredit":{"text":"服务","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"},"store_deliverycredit":{"text":"物流","credit":"4.8","percent":"2%","percent_class":"low","percent_text":"低"}}
             */

            private String store_id;
            private String store_name;
            private String member_id;
            private String default_im;
            private String member_name;
            private String is_own_shop;
            private String goods_count;
            private StoreCreditBean store_credit;

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

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getDefault_im() {
                return default_im;
            }

            public void setDefault_im(String default_im) {
                this.default_im = default_im;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public String getIs_own_shop() {
                return is_own_shop;
            }

            public void setIs_own_shop(String is_own_shop) {
                this.is_own_shop = is_own_shop;
            }

            public String getGoods_count() {
                return goods_count;
            }

            public void setGoods_count(String goods_count) {
                this.goods_count = goods_count;
            }

            public StoreCreditBean getStore_credit() {
                return store_credit;
            }

            public void setStore_credit(StoreCreditBean store_credit) {
                this.store_credit = store_credit;
            }

            public static class StoreCreditBean {
                /**
                 * store_desccredit : {"text":"描述","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"}
                 * store_servicecredit : {"text":"服务","credit":"5.0","percent":"--","percent_class":"equal","percent_text":"平"}
                 * store_deliverycredit : {"text":"物流","credit":"4.8","percent":"2%","percent_class":"low","percent_text":"低"}
                 */

                private StoreDesccreditBean store_desccredit;
                private StoreServicecreditBean store_servicecredit;
                private StoreDeliverycreditBean store_deliverycredit;

                public StoreDesccreditBean getStore_desccredit() {
                    return store_desccredit;
                }

                public void setStore_desccredit(StoreDesccreditBean store_desccredit) {
                    this.store_desccredit = store_desccredit;
                }

                public StoreServicecreditBean getStore_servicecredit() {
                    return store_servicecredit;
                }

                public void setStore_servicecredit(StoreServicecreditBean store_servicecredit) {
                    this.store_servicecredit = store_servicecredit;
                }

                public StoreDeliverycreditBean getStore_deliverycredit() {
                    return store_deliverycredit;
                }

                public void setStore_deliverycredit(StoreDeliverycreditBean store_deliverycredit) {
                    this.store_deliverycredit = store_deliverycredit;
                }

                public static class StoreDesccreditBean {
                    /**
                     * text : 描述
                     * credit : 5.0
                     * percent : --
                     * percent_class : equal
                     * percent_text : 平
                     */

                    private String text;
                    private String credit;
                    private String percent;
                    private String percent_class;
                    private String percent_text;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getCredit() {
                        return credit;
                    }

                    public void setCredit(String credit) {
                        this.credit = credit;
                    }

                    public String getPercent() {
                        return percent;
                    }

                    public void setPercent(String percent) {
                        this.percent = percent;
                    }

                    public String getPercent_class() {
                        return percent_class;
                    }

                    public void setPercent_class(String percent_class) {
                        this.percent_class = percent_class;
                    }

                    public String getPercent_text() {
                        return percent_text;
                    }

                    public void setPercent_text(String percent_text) {
                        this.percent_text = percent_text;
                    }
                }

                public static class StoreServicecreditBean {
                    /**
                     * text : 服务
                     * credit : 5.0
                     * percent : --
                     * percent_class : equal
                     * percent_text : 平
                     */

                    private String text;
                    private String credit;
                    private String percent;
                    private String percent_class;
                    private String percent_text;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getCredit() {
                        return credit;
                    }

                    public void setCredit(String credit) {
                        this.credit = credit;
                    }

                    public String getPercent() {
                        return percent;
                    }

                    public void setPercent(String percent) {
                        this.percent = percent;
                    }

                    public String getPercent_class() {
                        return percent_class;
                    }

                    public void setPercent_class(String percent_class) {
                        this.percent_class = percent_class;
                    }

                    public String getPercent_text() {
                        return percent_text;
                    }

                    public void setPercent_text(String percent_text) {
                        this.percent_text = percent_text;
                    }
                }

                public static class StoreDeliverycreditBean {
                    /**
                     * text : 物流
                     * credit : 4.8
                     * percent : 2%
                     * percent_class : low
                     * percent_text : 低
                     */

                    private String text;
                    private String credit;
                    private String percent;
                    private String percent_class;
                    private String percent_text;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getCredit() {
                        return credit;
                    }

                    public void setCredit(String credit) {
                        this.credit = credit;
                    }

                    public String getPercent() {
                        return percent;
                    }

                    public void setPercent(String percent) {
                        this.percent = percent;
                    }

                    public String getPercent_class() {
                        return percent_class;
                    }

                    public void setPercent_class(String percent_class) {
                        this.percent_class = percent_class;
                    }

                    public String getPercent_text() {
                        return percent_text;
                    }

                    public void setPercent_text(String percent_text) {
                        this.percent_text = percent_text;
                    }
                }
            }
        }

        public static class SpecListBean {
            /**
             * 390 : 103019
             * 391 : 103020
             * 392 : 103021
             * 393 : 103022
             */

            @SerializedName("390")
            private String _$390;
            @SerializedName("391")
            private String _$391;
            @SerializedName("392")
            private String _$392;
            @SerializedName("393")
            private String _$393;

            public String get_$390() {
                return _$390;
            }

            public void set_$390(String _$390) {
                this._$390 = _$390;
            }

            public String get_$391() {
                return _$391;
            }

            public void set_$391(String _$391) {
                this._$391 = _$391;
            }

            public String get_$392() {
                return _$392;
            }

            public void set_$392(String _$392) {
                this._$392 = _$392;
            }

            public String get_$393() {
                return _$393;
            }

            public void set_$393(String _$393) {
                this._$393 = _$393;
            }
        }

        public static class GoodsEvaluateInfoBean {
            /**
             * img : 0
             * good : 0
             * normal : 0
             * bad : 0
             * all : 0
             * good_percent : 100
             * normal_percent : 0
             * bad_percent : 0
             * good_star : 5
             * star_average : 5
             */

            private int img;
            private int good;
            private int normal;
            private int bad;
            private int all;
            private int good_percent;
            private int normal_percent;
            private int bad_percent;
            private int good_star;
            private int star_average;

            public int getImg() {
                return img;
            }

            public void setImg(int img) {
                this.img = img;
            }

            public int getGood() {
                return good;
            }

            public void setGood(int good) {
                this.good = good;
            }

            public int getNormal() {
                return normal;
            }

            public void setNormal(int normal) {
                this.normal = normal;
            }

            public int getBad() {
                return bad;
            }

            public void setBad(int bad) {
                this.bad = bad;
            }

            public int getAll() {
                return all;
            }

            public void setAll(int all) {
                this.all = all;
            }

            public int getGood_percent() {
                return good_percent;
            }

            public void setGood_percent(int good_percent) {
                this.good_percent = good_percent;
            }

            public int getNormal_percent() {
                return normal_percent;
            }

            public void setNormal_percent(int normal_percent) {
                this.normal_percent = normal_percent;
            }

            public int getBad_percent() {
                return bad_percent;
            }

            public void setBad_percent(int bad_percent) {
                this.bad_percent = bad_percent;
            }

            public int getGood_star() {
                return good_star;
            }

            public void setGood_star(int good_star) {
                this.good_star = good_star;
            }

            public int getStar_average() {
                return star_average;
            }

            public void setStar_average(int star_average) {
                this.star_average = star_average;
            }
        }

        public static class GoodsHairInfoBean {
            /**
             * content : 免运费
             * if_store_cn : 有货
             * if_store : true
             * area_name : 全国
             */

            private String content;
            private String if_store_cn;
            private boolean if_store;
            private String area_name;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIf_store_cn() {
                return if_store_cn;
            }

            public void setIf_store_cn(String if_store_cn) {
                this.if_store_cn = if_store_cn;
            }

            public boolean isIf_store() {
                return if_store;
            }

            public void setIf_store(boolean if_store) {
                this.if_store = if_store;
            }

            public String getArea_name() {
                return area_name;
            }

            public void setArea_name(String area_name) {
                this.area_name = area_name;
            }
        }

        public static class ImageListBean {
        }

        public static class GoodsCommendListBean {
        }
    }
}
