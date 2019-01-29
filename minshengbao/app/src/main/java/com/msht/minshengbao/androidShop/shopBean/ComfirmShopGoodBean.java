package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class ComfirmShopGoodBean implements Serializable{

    /**
     * store_id : 25
     * store_name : 椰公主旗舰店
     * goods : [{"cart_id":"3251","buyer_id":"249023","store_id":"25","store_name":"椰公主旗舰店","goods_id":"103085","goods_name":"【直邮】正宗海南文昌鸡【传味银凤】140天以上 宰杀光鸡 【海口】一只78","goods_price":"78.00","goods_num":"1","goods_image":"25_05924964545082064.jpg","bl_id":"0","state":true,"storage_state":true,"goods_commonid":"100701","gc_id":"1269","transport_id":"0","goods_freight":"0.00","goods_trans_v":"0.00","goods_inv":"0","goods_vat":"0","goods_voucher":null,"goods_storage":"10000","goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","groupbuy_info":[],"xianshi_info":[],"promotion_info":[],"is_chain":"0","pickup_self":"0","is_dis":"1","is_book":"0","book_down_payment":"0.00","book_final_payment":"0.00","book_down_time":"0","goods_spec":"尺码：【海口】一只78","sole_info":[],"contractlist":[],"goods_image_url":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05924964545082064_240.jpg","goods_total":"78.00"}]
     */

    private String store_id;
    private String store_name;
    private List<GoodsBean> goods;
    private int storeDoorService;

    public ComfirmShopGoodBean() {
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

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public void setStoreDoorService(int storeDoorService) {
        this.storeDoorService = storeDoorService;
    }

    public int getStoreDoorService() {
        return storeDoorService;
    }

    public static class GoodsBean implements Serializable{
        /**
         * cart_id : 3251
         * buyer_id : 249023
         * store_id : 25
         * store_name : 椰公主旗舰店
         * goods_id : 103085
         * goods_name : 【直邮】正宗海南文昌鸡【传味银凤】140天以上 宰杀光鸡 【海口】一只78
         * goods_price : 78.00
         * goods_num : 1
         * goods_image : 25_05924964545082064.jpg
         * bl_id : 0
         * state : true
         * storage_state : true
         * goods_commonid : 100701
         * gc_id : 1269
         * transport_id : 0
         * goods_freight : 0.00
         * goods_trans_v : 0.00
         * goods_inv : 0
         * goods_vat : 0
         * goods_voucher : null
         * goods_storage : 10000
         * goods_storage_alarm : 0
         * is_fcode : 0
         * have_gift : 0
         * groupbuy_info : []
         * xianshi_info : []
         * promotion_info : []
         * is_chain : 0
         * pickup_self : 0
         * is_dis : 1
         * is_book : 0
         * book_down_payment : 0.00
         * book_final_payment : 0.00
         * book_down_time : 0
         * goods_spec : 尺码：【海口】一只78
         * sole_info : []
         * contractlist : []
         * goods_image_url : http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05924964545082064_240.jpg
         * goods_total : 78.00
         */

        private String cart_id;
        private String store_id;
        private String store_name;
        private String goods_id;
        private String goods_name;
        private String goods_price;
        private String goods_num;
        private String goods_image;
        private String goods_commonid;
        private String gc_id;
        private String goods_storage;
        private String goods_spec;
        private String goods_image_url;
        private String goods_total;

        public GoodsBean(String store_name, String storeId,String goods_image_url,String goods_name,String goods_num,String goods_price,String good_id) {
            this.store_name = store_name;
            this.store_id = storeId;
            this.goods_image_url = goods_image_url;
            this.goods_name = goods_name;
            this.goods_num  = goods_num;
            this.goods_price = goods_price;
            this.goods_id = good_id;
        }

        public GoodsBean(String store_name) {
            this.store_name = store_name;
        }

        public String getPickup_self() {
            return pickup_self;
        }

        public void setPickup_self(String pickup_self) {
            this.pickup_self = pickup_self;
        }

        private String pickup_self;


        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
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

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_commonid() {
            return goods_commonid;
        }

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public String getGoods_spec() {
            return goods_spec;
        }

        public void setGoods_spec(String goods_spec) {
            this.goods_spec = goods_spec;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public String getGoods_total() {
            return goods_total;
        }

        public void setGoods_total(String goods_total) {
            this.goods_total = goods_total;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GoodsBean goodsBean = (GoodsBean) o;

            if (cart_id != null ? !cart_id.equals(goodsBean.cart_id) : goodsBean.cart_id != null)
                return false;
            if (store_id != null ? !store_id.equals(goodsBean.store_id) : goodsBean.store_id != null)
                return false;
            if (store_name != null ? !store_name.equals(goodsBean.store_name) : goodsBean.store_name != null)
                return false;
            if (goods_id != null ? !goods_id.equals(goodsBean.goods_id) : goodsBean.goods_id != null)
                return false;
            if (goods_name != null ? !goods_name.equals(goodsBean.goods_name) : goodsBean.goods_name != null)
                return false;
            if (goods_price != null ? !goods_price.equals(goodsBean.goods_price) : goodsBean.goods_price != null)
                return false;
            if (goods_num != null ? !goods_num.equals(goodsBean.goods_num) : goodsBean.goods_num != null)
                return false;
            if (goods_image != null ? !goods_image.equals(goodsBean.goods_image) : goodsBean.goods_image != null)
                return false;
            if (goods_commonid != null ? !goods_commonid.equals(goodsBean.goods_commonid) : goodsBean.goods_commonid != null)
                return false;
            if (gc_id != null ? !gc_id.equals(goodsBean.gc_id) : goodsBean.gc_id != null)
                return false;
            if (goods_storage != null ? !goods_storage.equals(goodsBean.goods_storage) : goodsBean.goods_storage != null)
                return false;
            if (goods_spec != null ? !goods_spec.equals(goodsBean.goods_spec) : goodsBean.goods_spec != null)
                return false;
            if (goods_image_url != null ? !goods_image_url.equals(goodsBean.goods_image_url) : goodsBean.goods_image_url != null)
                return false;
            if (goods_total != null ? !goods_total.equals(goodsBean.goods_total) : goodsBean.goods_total != null)
                return false;
            return pickup_self != null ? pickup_self.equals(goodsBean.pickup_self) : goodsBean.pickup_self == null;
        }

        @Override
        public int hashCode() {
            int result = cart_id != null ? cart_id.hashCode() : 0;
            result = 31 * result + (store_id != null ? store_id.hashCode() : 0);
            result = 31 * result + (store_name != null ? store_name.hashCode() : 0);
            result = 31 * result + (goods_id != null ? goods_id.hashCode() : 0);
            result = 31 * result + (goods_name != null ? goods_name.hashCode() : 0);
            result = 31 * result + (goods_price != null ? goods_price.hashCode() : 0);
            result = 31 * result + (goods_num != null ? goods_num.hashCode() : 0);
            result = 31 * result + (goods_image != null ? goods_image.hashCode() : 0);
            result = 31 * result + (goods_commonid != null ? goods_commonid.hashCode() : 0);
            result = 31 * result + (gc_id != null ? gc_id.hashCode() : 0);
            result = 31 * result + (goods_storage != null ? goods_storage.hashCode() : 0);
            result = 31 * result + (goods_spec != null ? goods_spec.hashCode() : 0);
            result = 31 * result + (goods_image_url != null ? goods_image_url.hashCode() : 0);
            result = 31 * result + (goods_total != null ? goods_total.hashCode() : 0);
            result = 31 * result + (pickup_self != null ? pickup_self.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "cart_id='" + cart_id + '\'' +
                    ", store_id='" + store_id + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", goods_id='" + goods_id + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_price='" + goods_price + '\'' +
                    ", goods_num='" + goods_num + '\'' +
                    ", goods_image='" + goods_image + '\'' +
                    ", goods_commonid='" + goods_commonid + '\'' +
                    ", gc_id='" + gc_id + '\'' +
                    ", goods_storage='" + goods_storage + '\'' +
                    ", goods_spec='" + goods_spec + '\'' +
                    ", goods_image_url='" + goods_image_url + '\'' +
                    ", goods_total='" + goods_total + '\'' +
                    ", pickup_self='" + pickup_self + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComfirmShopGoodBean that = (ComfirmShopGoodBean) o;

        if (store_id != null ? !store_id.equals(that.store_id) : that.store_id != null)
            return false;
        if (store_name != null ? !store_name.equals(that.store_name) : that.store_name != null)
            return false;
        return goods != null ? goods.equals(that.goods) : that.goods == null;
    }

    @Override
    public int hashCode() {
        int result = store_id != null ? store_id.hashCode() : 0;
        result = 31 * result + (store_name != null ? store_name.hashCode() : 0);
        result = 31 * result + (goods != null ? goods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComfirmShopGoodBean{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", goods=" + goods +
                '}';
    }
}
