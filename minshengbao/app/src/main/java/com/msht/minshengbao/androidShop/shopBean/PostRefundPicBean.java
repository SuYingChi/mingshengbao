package com.msht.minshengbao.androidShop.shopBean;

public class PostRefundPicBean {

    /**
     * code : 200
     * datas : {"file_name":"05962146611473733.gif","pic":"http://dev.msbapp.cn/data/upload/shop/refund/05962146611473733.gif"}
     */

    private DatasBean datas;

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * file_name : 05962146611473733.gif
         * pic : http://dev.msbapp.cn/data/upload/shop/refund/05962146611473733.gif
         */

        private String file_name;
        private String pic;

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
