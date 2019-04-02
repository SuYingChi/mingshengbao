package com.msht.minshengbao.Bean;

public class RepairNumBean {

    /**
     * result : success
     * error : null
     * data : {"unevalCount":"0","undoneCount":"0"}
     */

    private String result;
    private Object error;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * unevalCount : 0
         * undoneCount : 0
         */

        private String unevalCount;
        private String undoneCount;

        public String getUnevalCount() {
            return unevalCount;
        }

        public void setUnevalCount(String unevalCount) {
            this.unevalCount = unevalCount;
        }

        public String getUndoneCount() {
            return undoneCount;
        }

        public void setUndoneCount(String undoneCount) {
            this.undoneCount = undoneCount;
        }
    }
}
