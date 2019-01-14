package com.msht.minshengbao.androidShop.shopBean;

public class UploadEvaluatePicBean {


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

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        private String file_id;
        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        private String file_url;

        public String getOrigin_file_name() {
            return origin_file_name;
        }

        public void setOrigin_file_name(String origin_file_name) {
            this.origin_file_name = origin_file_name;
        }

        private String origin_file_name;
        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "file_name='" + file_name + '\'' +
                    ", file_id='" + file_id + '\'' +
                    ", file_url='" + file_url + '\'' +
                    ", origin_file_name='" + origin_file_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UploadEvaluatePicBean{" +
                "datas=" + datas +
                '}';
    }
}
