package com.msht.minshengbao.OkhttpUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.ToastUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager.ERROR_SERVICE;

/**
 * Created by hong on 2016/11/9.
 */
/**
 * Demo class
 *
 * @author hong
 * @date 2016/10/31
 */
public class OkHttpRequestAsyn {

    /**单利引用**/
    private static volatile OkHttpRequestAsyn mInstance;
    private final String TAG = OkHttpRequestAsyn.class.getSimpleName();
    private Context context;
    private  OkHttpClient mOkHttpClient;
    private ReqCallBack reqCallBack;
    public static final MediaType MEDIA_TYPE_NORMAL_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    /**既可以提交普通键值对，也可以提交(多个)文件键值对。**/
    public static final MediaType MEDIA_TYPE_MULTIPART_FORM = MediaType.parse("multipart/form-data;charset=utf-8");
    /**只能提交二进制，而且只能提交一个二进制，如果提交文件的话，只能提交一个文件,后台接收参数只能有一个，而且只能是流（或者字节数组）**/
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    /**mediaType 这个需要和服务端保持一致  **/
    private static final MediaType MEDIA_TYPE_FORM=MediaType.parse("");
    /**mediaType 这个需要和服务端保持一致  **/
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    /**
     * 获取单例引用
     * @param context
     * @param okHttpClient
     * @return
     */
    public static OkHttpRequestAsyn getInstance(Context context, OkHttpClient okHttpClient) {
        OkHttpRequestAsyn inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestAsyn(context,okHttpClient);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 初始化RequestManager
     */
    public OkHttpRequestAsyn(Context context, OkHttpClient okHttpClient) {

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
     * okHttp get异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param <T> 数据泛型
     * @return
     */
    public  <T> Call requestGetByAsyn(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        String requestUrl;
        try {
            int pos = 0;
            if (paramsMap!=null){
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
                requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
            }else {
                requestUrl = actionUrl;
            }

            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);

            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    reqCallBack.onRequestFailed(e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string ="";
                        if (response.body()!=null){
                            string = response.body().string();
                        }
                        reqCallBack.onRequestServiceSuccess(string);
                    } else {
                        reqCallBack.onRequestServiceFailed(ERROR_SERVICE);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数
     * @param <T> 数据泛型
     * @return
     */
    public  <T> Call requestPostByAsyn(String requestUrl, HashMap<String, String> paramsMap) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    reqCallBack.onRequestFailed(e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string ="";
                        if (response.body()!=null){
                            string = response.body().string();
                        }
                        reqCallBack.onRequestServiceSuccess(string);
                    } else {
                        reqCallBack.onRequestServiceFailed(ERROR_SERVICE);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求表单提交
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数
     * @param <T> 数据泛型
     * @return
     */
    public  <T> Call requestPostByAsynWithForm(String requestUrl, HashMap<String, String> paramsMap) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            final Request request = addHeaderForm().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    reqCallBack.onRequestFailed(e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string="";
                        if (response.body()!=null){
                            string = response.body().string();
                        }
                        reqCallBack.onRequestServiceSuccess(string);
                    } else {
                        reqCallBack.onRequestServiceFailed(ERROR_SERVICE);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post Multipart异步请求表单提交
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数
     * @param <T> 数据泛型
     * @return
     */
    public  <T> Call requestPostByAsynWithMultipart(String requestUrl, HashMap<String, String> paramsMap) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            final Request request =addHeaderMultipart().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    reqCallBack.onRequestFailed(e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string="";
                        if (response.body()!=null){
                            string = response.body().string();
                        }
                        reqCallBack.onRequestServiceSuccess(string);
                    } else {
                        reqCallBack.onRequestServiceFailed(ERROR_SERVICE);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builders = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("accept","*/*");
        return builders;
    }
    private Request.Builder addHeaderMultipart(){
        Request.Builder builderMultipart = new Request.Builder()
                .addHeader("Charset", "UTF-8")
                .addHeader("ser-Agent", "Fiddler")
                .addHeader("Content-Type", "multipart/form-data");
        return builderMultipart;
    }
    private Request.Builder addHeaderForm(){
        Request.Builder builderForm = new Request.Builder()
                .addHeader("Charset", "UTF-8")
                .addHeader("ser-Agent", "Fiddler")
                .addHeader("Content-Type", "application/x-www-form-urlencoded");
        return builderForm;
    }
}
