package com.example.jim.okhttptest.okttputils;

/**
 * Created by Jim on 2016/11/9.
 */

public interface OkHttpReqCallBack<T> {
    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);
}
