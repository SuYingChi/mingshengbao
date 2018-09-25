package com.msht.minshengbao.OkhttpUtil;

/**
 *
 * @author Jim
 * @date 2016/11/9
 */

public interface OkHttpReqCallBack<T> {
    /**
     * 响应成功
     * @param result
     * void onReqSuccess(T result);
     */


    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);

    /**
     * 响应成功
     * @param result
     */
    void onRequestSuccess(String result);
}
