package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;
import android.os.Build;

import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jim on 2016/11/9.
 */

public class OkHttpRequestSyn {
    /**单利引用 **/
    private static volatile OkHttpRequestSyn mInstance;
    private final String TAG = OkHttpRequestSyn.class.getSimpleName();
    private Context context;
    private OkHttpClient mOkHttpClient;
    private ReqCallBack reqCallBack;

    /**mdiatype 这个需要和服务端保持一致 **/
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    /**mdiatype 这个需要和服务端保持一致**/
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    /**
     * 获取单例引用
     * @return
     */
    public static OkHttpRequestSyn getInstance(Context context, OkHttpClient okHttpClient) {
        OkHttpRequestSyn inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestUtil.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestSyn(context,okHttpClient);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 初始化RequestManager
     */
    public OkHttpRequestSyn(Context context, OkHttpClient okHttpClient) {

        this.context=context;
        this.mOkHttpClient=okHttpClient;

    }

    /**
     * 注册回调函数
     */
    public  void setReqCallBack(ReqCallBack reqCallBack){
        this.reqCallBack=reqCallBack;
    }

    /**
     * okHttp get同步请求
     * @param actionUrl  接口地址
     * @param paramsMap   请求参数
     */
    public void requestGetBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        String requestUrl;
        try {
            //处理参数
            int pos = 0;

            if (paramsMap!=null){
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    //对参数进行URLEncoder
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
                //补全请求地址
                requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
            }else {
                requestUrl = actionUrl;
            }

            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行同步请求
            final Response response = call.execute();
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                reqCallBack.onRequestSuccess(response.body().string());
               // reqCallBack.onRequestServiceSuccess(response.body().string());
            }
        } catch (Exception e) {
            reqCallBack.onRequestFailed(e.toString());
        }
    }

    /**
     * okHttp post同步请求
     * @param requestUrl  接口地址
     * @param paramsMap   请求参数
     */
    public void requestPostBySyn(String requestUrl, HashMap<String, String> paramsMap) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行同步请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                reqCallBack.onRequestSuccess(response.body().string());
               // reqCallBack.onRequestServiceSuccess(response.body().string());
            }
        } catch (Exception e) {
            reqCallBack.onRequestFailed(e.toString());
        }
    }

    /**
     * okHttp post同步请求表单提交
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数
     */
    public void requestPostBySynWithForm(String requestUrl, HashMap<String, String> paramsMap) {
        try {
            //创建 表单的一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行同步请求
            Response response = call.execute();
            if (response.isSuccessful()) {
               // reqCallBack.onRequestServiceSuccess(response.body().string());
                reqCallBack.onRequestSuccess(response.body().string());
            }
        } catch (Exception e) {
            reqCallBack.onRequestFailed(e.toString());
        }
    }

    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");
        return builder;
    }
}
