package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public static final String FILE_TYPE_FILE = "file/*";
    public static final String FILE_TYPE_IMAGE = "image/*";
    public static final String FILE_TYPE_AUDIO = "audio/*";
    public static final String FILE_TYPE_VIDEO = "video/*";
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


    /**
     * post请求，上传单个文件
     * @param requestUrl：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public  void requestHttpUploadFile(String requestUrl, File file, String fileKey, String fileType, AbstractCallBackUtil callBack) {
        okHttpRequestAsync.okHttpUploadFile(requestUrl,file,fileKey,fileType,null,callBack);

    }

    /**
     * post请求，上传单个文件
     * @param requestUrl：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public  void requestHttpUploadFile(String requestUrl, File file, String fileKey, String fileType ,HashMap<String, String> paramsMap, HashMap<String, String> headerMap,AbstractCallBackUtil callBack) {
        okHttpRequestAsync.okHttpUploadFile(requestUrl,file,fileKey,fileType,paramsMap,headerMap,callBack);
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     * @param requestUrl：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public  void requestHttpUploadListFile(String requestUrl, HashMap<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, HashMap<String, String> headerMap, AbstractCallBackUtil callBack) {
        okHttpRequestAsync.okHttpUploadListFile(requestUrl,paramsMap,fileList,fileKey,fileType,headerMap,callBack);
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     * @param requestUrl：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public  void requestHttpUploadMapFile(String requestUrl, HashMap<String, String> paramsMap, HashMap<String, File> fileMap,String fileType, HashMap<String, String> headerMap, AbstractCallBackUtil callBack) {
        okHttpRequestAsync.okHttpUploadMapFile(requestUrl,paramsMap,fileMap,fileType,headerMap,callBack);
    }

    /**
     * 下载文件,不带参数
     */
    public  void okHttpDownloadFile(String url,AbstractCallBackUtil.CallBackFile callBack) {
        okHttpRequestAsync.okHttpGet(url, null, null, callBack);
    }

    /**
     * 下载文件,带参数
     */
    public  void okHttpDownloadFile(String url,HashMap<String, String> paramsMap,  AbstractCallBackUtil.CallBackFile callBack) {
        okHttpRequestAsync.okHttpGet(url, paramsMap, null, callBack);
    }
    /**
     * 加载图片
     */
    public  void okHttpGetBitmap(String url, AbstractCallBackUtil.CallBackBitmap callBack) {
        okHttpGetBitmap(url, null, callBack);
    }
    /**
     * 加载图片，带参数
     */
    public  void okHttpGetBitmap(String url,HashMap<String, String> paramsMap,  AbstractCallBackUtil.CallBackBitmap callBack) {
        okHttpRequestAsync.okHttpGet(url, paramsMap, null, callBack);
    }

    public void requestCancel(Object tag){
        okHttpRequestAsync.onRequestCancel(tag);
    }
}
