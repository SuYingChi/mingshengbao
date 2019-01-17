package com.msht.minshengbao.OkhttpUtil;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public abstract class BaseCallback<T> extends AbstractMyCallBack<String> {

    /**
     * 请求结果
     * @param data
     */
    public abstract void responseRequestSuccess(T data);
    /**
     * 请求失败
     * @param data
     */
    public abstract void responseReqFailed(T data);
    @Override
    public void onResponseSuccess(String var1) {
        responseRequestSuccess((T) var1);
    }
    @Override
    public void onResponseFail(String var1) {
        responseReqFailed((T) var1);
    }
}
