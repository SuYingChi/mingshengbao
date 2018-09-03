package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class OkHttpRequestManager implements ReqCallBack{

    private static final String ERROR_NETWORK = "网络连接异常，请查看网络状态";
    private static final String ERROR_SERVICE = "服务器异常，请稍后再试";
    public static final String ERROR_OVER_TIME = "网络连接超时，请检查您的网络";
    private static volatile OkHttpRequestManager mInstance;
    /**get请求 **/
    public static final int TYPE_GET = 0;
    /**post请求参数为json **/
    public static final int TYPE_POST_JSON = 1;
    /**post请求参数为表单 **/
    public static final int TYPE_POST_FORM = 2;
    /**post Multipart 请求参数为表单 **/
    public static final int TYPE_POST_MULTIPART = 3;

    private OkHttpClient mOkHttpClient;//okHttpClient 实例


    //同步请求类
    private OkHttpRequestSyn okhttpRequestSyn;
    //异步请求类
    private OkHttpRequestAsyn okhttpRequestAsyn;

    //请求成功回调接口
    private OkHttpReqCallBack okHttpReqCallBack;

    /**
     * 初始化RequestManager
     */
    public OkHttpRequestManager(Context context) {

        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                //设置读取超时时间
                .readTimeout(12, TimeUnit.SECONDS)
                //设置写入超时时间
                .writeTimeout(12, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        //初始化同步类OkhttpRequestSyn
        okhttpRequestSyn=OkHttpRequestSyn.getInstance(context,mOkHttpClient);
        //注册回调函数
        okhttpRequestSyn.setReqCallBack(this);

        //初始化异步类OkhttpRequestAsyn
        okhttpRequestAsyn=OkHttpRequestAsyn.getInstance(context,mOkHttpClient);
        //注册回到函数
        okhttpRequestAsyn.setReqCallBack(this);
    }

    /**
     * 获取单例引用
     * @return
     */
    public static OkHttpRequestManager getInstance(Context context) {
        OkHttpRequestManager inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
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
            case TYPE_POST_MULTIPART:
                call=okhttpRequestAsyn.requestPostByAsynWithMultipart(actionUrl, paramsMap);
                break;
                default:
                    break;
        }
        return call;
    }
    @Override
    public void onRequestSuccess(String result) {
        okHttpReqCallBack.onRequestSuccess(result);
    }
    @Override
    public void onRequestFailed(String errorMsg) {
        okHttpReqCallBack.onReqFailed(ERROR_NETWORK);
    }
    @Override
    public void onRequestServiceFailed(String errorMsg) {
        okHttpReqCallBack.onReqFailed(ERROR_SERVICE);
    }
}
