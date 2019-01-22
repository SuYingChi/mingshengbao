package com.msht.minshengbao.androidShop.shopBean;

/**
 *
 */
public class BaseData {
    private int code;
    private String datas;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", datas='" + datas + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
