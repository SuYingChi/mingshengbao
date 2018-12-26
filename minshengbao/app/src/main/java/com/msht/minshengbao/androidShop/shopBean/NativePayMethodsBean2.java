package com.msht.minshengbao.androidShop.shopBean;

import java.io.Serializable;
import java.util.List;

public class NativePayMethodsBean2 implements Serializable {

    public NativePayMethodsBean2(String code, String name, int channel, Object tips, boolean isCheck) {
        this.code = code;
        this.name = name;
        this.channel = channel;
        this.tips = tips;
        this.isCheck = isCheck;
    }

    /**
     * code : wx_pay
     * name : 微信支付
     * channel : 5
     * tips : null
     */

    private String code;
    private String name;
    private int channel;
    private Object tips;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    private boolean isCheck;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Object getTips() {
        return tips;
    }

    public void setTips(Object tips) {
        this.tips = tips;
    }

}
