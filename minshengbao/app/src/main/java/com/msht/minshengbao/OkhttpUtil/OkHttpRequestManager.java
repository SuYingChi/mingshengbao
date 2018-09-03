package com.example.jim.okhttptest.okttputils;

import android.content.Context;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Jim on 2016/11/9.
 */

public class OkhttpRequestManager implements ReqCallBack{

    private static volatile OkhttpRequestManager mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    private OkHttpClient mOkHttpClient;//okHttpClient 实例


    //同步请求类
    private OkhttpRequestSyn okhttpRequestSyn;
    //异步请求类
    private OkhttpRequestAsyn okhttpRequestAsyn;

    //请求成功回调接口
    private OkHttpReqCallBack okHttpReqCallBack;

    /**
     * 初始化RequestManager
     */
    public OkhttpRequestManager(Context context) {

        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        //初始化同步类OkhttpRequestSyn
        okhttpRequestSyn=OkhttpRequestSyn.getInstance(context,mOkHttpClient);
        //注册回调函数
        okhttpRequestSyn.setReqCallBack(this);

        //初始化异步类OkhttpRequestAsyn
        okhttpRequestAsyn=OkhttpRequestAsyn.getInstance(context,mOkHttpClient);
        //注册回到函数
        okhttpRequestAsyn.setReqCallBack(this);
    }

    /**
     * 获取单例引用
     * @return
     */
    public static OkhttpRequestManager getInstance(Context context) {
        OkhttpRequestManager inst = mInstance;
        if (inst == null) {
            synchronized (OkhttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkhttpRequestManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    ///////////////////////////////////////////////////////同步请求///////////////////////////////////////////////////
    /**
     *  okHttp同步请求统一入口
     * @param actionUrl  接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     */
    public void requestSyn(final String actionUrl, int requestType, final HashMap<String, String> paramsMap,OkHttpReqCallBack okHttpReqCallBack) {

        this.okHttpReqCallBack=okHttpReqCallBack;

        switch (requestType) {
            case TYPE_GET:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpRequestSyn.requestGetBySyn(actionUrl, paramsMap);
                    }
                }).start();
                break;
            case TYPE_POST_JSON:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpRequestSyn.requestPostBySyn(actionUrl, paramsMap);
                    }
                }).start();

                break;
            case TYPE_POST_FORM:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpRequestSyn.requestPostBySynWithForm(actionUrl, paramsMap);
                    }
                }).start();

                break;
        }
    }


    //////////////////////////////////////////////////////异步请求////////////////////////////////////////////
    /**
     * okHttp异步请求统一入口
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param <T> 数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, String> paramsMap,OkHttpReqCallBack okHttpReqCallBack) {
        Call call = null;
        this.okHttpReqCallBack=okHttpReqCallBack;
        switch (requestType) {
            case TYPE_GET:
                call = okhttpRequestAsyn.requestGetByAsyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_JSON:
                call = okhttpRequestAsyn.requestPostByAsyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_FORM:
                call = okhttpRequestAsyn.requestPostByAsynWithForm(actionUrl, paramsMap);
                break;
        }
        return call;
    }

    @Override
    public void onReqSuccess(Object result) {
        okHttpReqCallBack.onReqSuccess(result);
    }

    @Override
    public void onReqFailed(String errorMsg) {
        okHttpReqCallBack.onReqFailed(errorMsg);

    }
}
