package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class OkHttpRequestManager {
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
    /**okHttpClient 实例 */
    private OkHttpClient mOkHttpClient;


    /**同步请求类*/
    private OkHttpRequestSyn okhttpRequestSyn;
    /**异步请求类 */
    private OkHttpRequestAsync okHttpRequestAsync;

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
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        //初始化同步类OkhttpRequestSyn
        okhttpRequestSyn=OkHttpRequestSyn.getInstance(context,mOkHttpClient);
        //注册回调函数

        //初始化异步类OkhttpRequestAsyn
        okHttpRequestAsync =OkHttpRequestAsync.getInstance(context,mOkHttpClient);
        //注册回到函数

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
    public void postRequestAsync(String actionUrl, int requestType, HashMap<String, String> paramsMap,BaseCallback ballBack) {
        switch (requestType) {
            case TYPE_GET:
                 okHttpRequestAsync.requestGetByAsync(actionUrl, paramsMap,ballBack);
                break;
            case TYPE_POST_JSON:
               okHttpRequestAsync.requestPostByAsync(actionUrl, paramsMap,ballBack);
                break;
            case TYPE_POST_FORM:
                 okHttpRequestAsync.requestPostByAsyncWithForm(actionUrl, paramsMap,ballBack);
                break;
            case TYPE_POST_MULTIPART:
                okHttpRequestAsync.requestPostByAsyncWithMultipart(actionUrl, paramsMap,ballBack);
                break;
            default:
                break;
        }
    }
    public void requestCancel(Object tag){
        okHttpRequestAsync.onRequestCancel(tag);
    }
}
