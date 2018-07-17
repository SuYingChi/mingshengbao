package com.msht.minshengbao.OkhttpUtil;

/**
 * Created by hong on 2016/11/9.
 */

public interface ReqCallBack<T> {
    /**
     * 响应成功
     * @param result
     */
    void onRequestServiceSuccess(T result);
    /**
     * 响应失败
     * @param errorMsg
     */
    void onRequestFailed(String errorMsg);
    /**
     * 服务器错误
     * @param errorMsg
     */
    void onRequestServiceFailed(String errorMsg);
}
