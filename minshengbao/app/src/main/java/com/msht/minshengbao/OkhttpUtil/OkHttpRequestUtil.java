package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.msht.minshengbao.androidShop.util.MLoggerInterceptor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Demo class
 *
 * @author hong
 * @date 2018/6/27
 */
public class OkHttpRequestUtil implements ReqCallBack{

    public static final String ERROR_NETWORK = "网络连接异常，请查看网络状态";
    public static final String ERROR_SERVICE = "服务器异常，请稍后再试";
    public static final String ERROR_OVER_TIME = "网络连接超时，请检查您的网络";
    public static final int SUCCESS=1;
    public static final int FAILURE=0;
    /**单利引用 **/
    private static volatile OkHttpRequestUtil mInstance;
    /**get请求 **/
    public static final int TYPE_GET = 0;
    /**post请求参数为json **/
    public static final int TYPE_POST_JSON = 1;
    /**post请求参数为表单 **/
    public static final int TYPE_POST_FORM = 2;
    /**post Multipart 请求参数为表单 **/
    public static final int TYPE_POST_MULTIPART = 3;
    /**okHttpClient 实例 **/
    private OkHttpClient mOkHttpClient;
    //同步请求类
    private OkHttpRequestSyn okhttpRequestSyn;
    //异步请求类
    private OkHttpRequestAsyn okhttpRequestAsyn;
    private Handler reCallHandler;

    /**
     * 初始化RequestManager
     */
    public OkHttpRequestUtil(Context context) {

        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
               // .addInterceptor(new MLoggerInterceptor("http", true))
                //设置超时时间
                .connectTimeout(15, TimeUnit.SECONDS)
                //设置读取超时时间
                .readTimeout(12, TimeUnit.SECONDS)
                //设置写入超时时间
                .writeTimeout(12, TimeUnit.SECONDS)
                .addInterceptor(new MLoggerInterceptor("http", true))
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        //初始化同步类OkhttpRequestSyn
        okhttpRequestSyn= OkHttpRequestSyn.getInstance(context,mOkHttpClient);
        //注册回调函数
        okhttpRequestSyn.setReqCallBack(this);

        //初始化异步类OkhttpRequestAsyn
        okhttpRequestAsyn= OkHttpRequestAsyn.getInstance(context,mOkHttpClient);
        //注册回到函数
        okhttpRequestAsyn.setReqCallBack(this);
    }

    /**
     * 获取单例引用
     * @return
     */
    public static OkHttpRequestUtil getInstance(Context context) {
        OkHttpRequestUtil inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestUtil.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestUtil(context.getApplicationContext());
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
    public void requestSyn(final String actionUrl, int requestType, final HashMap<String, String> paramsMap,Handler mHandler) {
        this.reCallHandler=mHandler;
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
            default:
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
    public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, String> paramsMap,Handler mHandler) {
        Call call = null;
        this.reCallHandler=mHandler;
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
        Message msg = new Message();
        msg.obj = result;
        msg.what = SUCCESS;
        reCallHandler.sendMessage(msg);
    }
    @Override
    public void onRequestFailed(String errorMsg) {
        Message msg = new Message();
        msg.what = FAILURE;
        msg.obj=ERROR_NETWORK;
        reCallHandler.sendMessage(msg);
    }
    @Override
    public void onRequestServiceFailed(String errorMsg) {
        Message msg = new Message();
        msg.what =FAILURE;
        msg.obj=ERROR_SERVICE;
        reCallHandler.sendMessage(msg);
    }
}
