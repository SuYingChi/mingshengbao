package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopHomeGoodsBean {

    /**
     * goods : {"title":"猜你喜欢","item":[{"goods_id":"101783","goods_name":"海南文昌椰青9个装 新鲜水果（东北地区+5元运费，西北不发货）","goods_promotion_price":"78.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05870327906146718_360.jpg"},{"goods_id":"101842","goods_name":"周蓉茶油蒸文昌鸡四星级本地鲜（只限海口）","goods_promotion_price":"98.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05865163095834942_360.jpg"},{"goods_id":"100349","goods_name":"海南现摘白心番石榴芭乐5斤装新鲜清脆香甜番石榴","goods_promotion_price":"39.90","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750294144986879_360.jpg"},{"goods_id":"100350","goods_name":"海南古法红糖500克 一袋 老红糖块粉古法土红糖","goods_promotion_price":"19.90","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750298454657725_360.jpg"},{"goods_id":"101847","goods_name":"三禾椰娘冷榨椰子油 600ml/瓶（全国直邮，偏远地区除外）","goods_promotion_price":"138.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05868634671792822_360.jpg"},{"goods_id":"101833","goods_name":"三禾椰娘冷榨椰子油 45ml/瓶（全国直邮，偏远地区除外）","goods_promotion_price":"48.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05791907029753118_360.jpg"},{"goods_id":"100348","goods_name":"海南椰子冻2个新鲜椰冻奶酪椰奶冻果冻布丁胜菲诺越南","goods_promotion_price":"65.80","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750290020007761_360.jpg"},{"goods_id":"102800","goods_name":"海南特产 菠萝蜜 当季热带水果 全国包邮 18斤~22斤","goods_promotion_price":"88.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05894569175754319_360.jpg"},{"goods_id":"101990","goods_name":"万宁和乐蟹 海南特产名菜鲜活水产 肥美鲜甜螃蟹 国庆有礼","goods_promotion_price":"588.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05890724132404009_360.png"},{"goods_id":"101991","goods_name":"【大闸蟹礼券】阳澄湖大闸蟹（288款）当季好礼 2.6两公蟹×3，1.6两母蟹×3","goods_promotion_price":"288.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05891224633445148_360.png"},{"goods_id":"101992","goods_name":"【大闸蟹礼券】阳澄湖大闸蟹（366款）当季好礼 2.8-3.0两公蟹×3；1.8-2.0两母蟹×3","goods_promotion_price":"366.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05891225954035887_360.png"},{"goods_id":"102795","goods_name":"威妹黄灯笼辣椒酱，野山椒辣椒酱 30万辣度 两瓶起拍，全国包邮 110g威妹黄灯笼辣椒酱","goods_promotion_price":"18.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05860123891505481_360.jpg"}]}
     */

    private GoodsBean goods;

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * title : 猜你喜欢
         * item : [{"goods_id":"101783","goods_name":"海南文昌椰青9个装 新鲜水果（东北地区+5元运费，西北不发货）","goods_promotion_price":"78.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05870327906146718_360.jpg"},{"goods_id":"101842","goods_name":"周蓉茶油蒸文昌鸡四星级本地鲜（只限海口）","goods_promotion_price":"98.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05865163095834942_360.jpg"},{"goods_id":"100349","goods_name":"海南现摘白心番石榴芭乐5斤装新鲜清脆香甜番石榴","goods_promotion_price":"39.90","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750294144986879_360.jpg"},{"goods_id":"100350","goods_name":"海南古法红糖500克 一袋 老红糖块粉古法土红糖","goods_promotion_price":"19.90","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750298454657725_360.jpg"},{"goods_id":"101847","goods_name":"三禾椰娘冷榨椰子油 600ml/瓶（全国直邮，偏远地区除外）","goods_promotion_price":"138.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05868634671792822_360.jpg"},{"goods_id":"101833","goods_name":"三禾椰娘冷榨椰子油 45ml/瓶（全国直邮，偏远地区除外）","goods_promotion_price":"48.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05791907029753118_360.jpg"},{"goods_id":"100348","goods_name":"海南椰子冻2个新鲜椰冻奶酪椰奶冻果冻布丁胜菲诺越南","goods_promotion_price":"65.80","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/21/21_05750290020007761_360.jpg"},{"goods_id":"102800","goods_name":"海南特产 菠萝蜜 当季热带水果 全国包邮 18斤~22斤","goods_promotion_price":"88.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05894569175754319_360.jpg"},{"goods_id":"101990","goods_name":"万宁和乐蟹 海南特产名菜鲜活水产 肥美鲜甜螃蟹 国庆有礼","goods_promotion_price":"588.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05890724132404009_360.png"},{"goods_id":"101991","goods_name":"【大闸蟹礼券】阳澄湖大闸蟹（288款）当季好礼 2.6两公蟹×3，1.6两母蟹×3","goods_promotion_price":"288.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05891224633445148_360.png"},{"goods_id":"101992","goods_name":"【大闸蟹礼券】阳澄湖大闸蟹（366款）当季好礼 2.8-3.0两公蟹×3；1.8-2.0两母蟹×3","goods_promotion_price":"366.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05891225954035887_360.png"},{"goods_id":"102795","goods_name":"威妹黄灯笼辣椒酱，野山椒辣椒酱 30万辣度 两瓶起拍，全国包邮 110g威妹黄灯笼辣椒酱","goods_promotion_price":"18.00","goods_image":"http://shop.msbapp.cn:8090/data/upload/shop/store/goods/22/22_05860123891505481_360.jpg"}]
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
             * goods_id : 101783
             * goods_name : 海南文昌椰青9个装 新鲜水果（东北地区+5元运费，西北不发货）
             * goods_promotion_price : 78.00
             * goods_image : http://shop.msbapp.cn:8090/data/upload/shop/store/goods/25/25_05870327906146718_360.jpg
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
