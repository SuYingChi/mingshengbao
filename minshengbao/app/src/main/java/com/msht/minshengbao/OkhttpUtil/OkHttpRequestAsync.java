package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

import static com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil.ERROR_SERVICE;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class OkHttpRequestAsync {

    /**单利引用**/
    private static volatile OkHttpRequestAsync mInstance;
    private final String TAG = OkHttpRequestAsync.class.getSimpleName();
    private Context context;
    private OkHttpClient mOkHttpClient;
  //  private AbstractMyCallBack reqCallBack;
    private final Handler mHandler;
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

    public static OkHttpRequestAsync getInstance(Context context, OkHttpClient okHttpClient) {
        OkHttpRequestAsync inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestAsync(context,okHttpClient);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 初始化RequestManager
     */
    public OkHttpRequestAsync(Context context, OkHttpClient okHttpClient) {

        this.context=context;
        this.mOkHttpClient=okHttpClient;
        mHandler=new Handler(Looper.getMainLooper());

    }
    /**
     * 注册回调函数
     */

    /**
     * okHttp get异步请求
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @return
     */
    public  void requestGetByAsync(String actionUrl, HashMap<String, String> paramsMap,final AbstractMyCallBack reqCallBack) {
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
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {

                    onResponseRequestFailed(reqCallBack,e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            String string = response.body().string();
                            onResponseRequestSuccess(reqCallBack,string);
                        }
                    } else {
                        onResponseRequestFailed(reqCallBack,ERROR_SERVICE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * okHttp post异步请求
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数
     * @return
     */
    public  void requestPostByAsync(String requestUrl, HashMap<String, String> paramsMap,final AbstractMyCallBack reqCallBack) {
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
                public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                    onResponseRequestFailed(reqCallBack,e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            String string = response.body().string();
                            onResponseRequestSuccess(reqCallBack,string);
                        }
                    } else {
                        onResponseRequestFailed(reqCallBack,ERROR_SERVICE);
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    public  void requestPostByAsyncWithMultipart(String requestUrl, HashMap<String, String> paramsMap, final AbstractMyCallBack reqCallBack) {
        if (reqCallBack == null) {
         //  reqCallBack = AbstractMyCallBack.callBackDefault;
        }
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
                public void onFailure(@NonNull Call call, @NonNull  IOException e) {
                    onResponseRequestFailed(reqCallBack,e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            String string = response.body().string();
                            onResponseRequestSuccess(reqCallBack,string);
                        }
                    } else {
                        onResponseRequestFailed(reqCallBack,ERROR_SERVICE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * okHttp post异步请求表单提交
     * @param requestUrl 接口地址
     * @param paramsMap 请求参数

     * @return
     */
    public  void requestPostByAsyncWithForm(String requestUrl, HashMap<String, String> paramsMap,final AbstractMyCallBack reqCallBack) {
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
                public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                    onResponseRequestFailed(reqCallBack,e.toString());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            String string = response.body().string();
                            onResponseRequestSuccess(reqCallBack,string);
                        }
                    } else {
                        onResponseRequestFailed(reqCallBack,ERROR_SERVICE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public  void requestPostByAsyncWithMultipartFile(String requestUrl, HashMap<String, Object> paramsMap,final AbstractMyCallBack reqCallBack) {
        try {
            MultipartBody.Builder multipartBuilder=new MultipartBody.Builder();
            multipartBuilder.setType(MultipartBody.FORM);
            for (String key : paramsMap.keySet()) {

                multipartBuilder.addFormDataPart(key, paramsMap.get(key).toString(),RequestBody.create(MEDIA_TYPE_MULTIPART_FORM,(File)paramsMap.get(key)));
            }
            MultipartBody multipartBody=multipartBuilder.build();
            final Request request =addHeaderMultipart().url(requestUrl).post(multipartBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                    onResponseRequestFailed(reqCallBack,e.toString());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            String string = response.body().string();
                            onResponseRequestSuccess(reqCallBack,string);
                        }
                    } else {
                        onResponseRequestFailed(reqCallBack,ERROR_SERVICE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    public void onResponseRequestFailed(final AbstractMyCallBack reqCallBack,final String s){
        mHandler.post(new Runnable(){
            @Override
            public void run() {
                if (reqCallBack!=null){
                    reqCallBack.onResponseFail(s);
                }

            }
        });
    }
    public void onResponseRequestSuccess(final AbstractMyCallBack reqCallBack,final String s){
        mHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                if (reqCallBack!=null){
                                    reqCallBack.onResponseSuccess(s);
                                }
                            }
                        });
    }
    public void onRequestCancel(Object tag){
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
    /**
     * 统一为请求添加头信息
     * @return
     */
    private Request.Builder addHeaders() {
        return  new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("accept","*/*");
    }
    private Request.Builder addHeaderMultipart(){
        return new Request.Builder()
                .addHeader("Charset", "UTF-8")
                .addHeader("ser-Agent", "Fiddler")
                .addHeader("Content-Type", "multipart/form-data");
    }
    private Request.Builder addHeaderForm(){
        return new Request.Builder()
                .addHeader("Charset", "UTF-8")
                .addHeader("ser-Agent", "Fiddler")
                .addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    private Request.Builder setRequestHeader(HashMap<String, String> mHeaderMap){
        Request.Builder builder = new Request.Builder();
        if(mHeaderMap != null){
            for (String key: mHeaderMap.keySet()){
                builder.addHeader(key,mHeaderMap.get(key));
            }
        }else {
            builder.addHeader("Connection", "keep-alive");
            builder.addHeader("accept","*/*");
        }
        return builder;
    }

    private Request.Builder setRequestBuilderHeader(HashMap<String, String> mHeaderMap){
        Request.Builder builder = new Request.Builder();
        if(mHeaderMap != null){
            for (String key: mHeaderMap.keySet()){
                builder.addHeader(key,mHeaderMap.get(key));
            }
        }
        return builder;
    }
    public void okHttpGet( String url, HashMap<String, String> paramsMap, HashMap<String, String> headerMap,final AbstractCallBackUtil callBack){
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
                requestUrl = String.format("%s?%s", url, tempParams.toString());
            }else {
                requestUrl = url;
            }
            final Request request = setRequestHeader(headerMap).url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {
                    if(callBack != null){
                        callBack.onError(call,e);
                    }

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(callBack != null){
                        callBack.onSeccess(call,response);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    public void okHttpUploadFile(String requestUrl, File file, String fileKey,String fileType, HashMap<String, String> headerMap,final AbstractCallBackUtil callBack){
        try {
            MediaType mFileType = MediaType.parse(fileType);
            //json数据，
            RequestBody body = RequestBody.create(mFileType, file);

            final Request request = setRequestBuilderHeader(headerMap).post(new ProgressRequestBody(body,callBack)).url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {
                    if(callBack != null){
                        callBack.onError(call,e);
                    }

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(callBack != null){
                        callBack.onSeccess(call,response);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
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
    public void okHttpUploadFile(String requestUrl, File file, String fileKey,String fileType,HashMap<String, String> paramsMap, HashMap<String, String> headerMap,final AbstractCallBackUtil callBack){
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key: paramsMap.keySet()){
                builder.addFormDataPart(key,paramsMap.get(key));
            }
            builder.addFormDataPart(fileKey,file.getName(), RequestBody.create(MediaType.parse(fileType), file));
            final Request request = setRequestBuilderHeader(headerMap).post(new ProgressRequestBody(builder.build(),callBack)).url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {
                    if(callBack != null){
                        callBack.onError(call,e);
                    }

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(callBack != null){
                        callBack.onSeccess(call,response);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * post请求，上传多个文件，以list集合的形式,只有一个文件名
     * @param requestUrl：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public void okHttpUploadListFile(String requestUrl, HashMap<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, HashMap<String, String> headerMap, final AbstractCallBackUtil callBack){
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if(paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    builder.addFormDataPart(key, paramsMap.get(key));
                }
            }
            for (File f : fileList){
                builder.addFormDataPart(fileKey,f.getName(), RequestBody.create(MediaType.parse(fileType), f));
            }
            final Request request = setRequestBuilderHeader(headerMap).post(new ProgressRequestBody(builder.build(),callBack)).url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {
                    if(callBack != null){
                        callBack.onError(call,e);
                    }

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(callBack != null){
                        callBack.onSeccess(call,response);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    public void okHttpUploadMapFile(String requestUrl, HashMap<String, String> paramsMap, HashMap<String, File> fileMap,String fileType, HashMap<String, String> headerMap,final AbstractCallBackUtil callBack){
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if(paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    builder.addFormDataPart(key, paramsMap.get(key));
                }
            }

            for (String key : fileMap.keySet()){
                builder.addFormDataPart(key,fileMap.get(key).getName(), RequestBody.create(MediaType.parse(fileType), fileMap.get(key)));
            }
            final Request request = setRequestBuilderHeader(headerMap).post(new ProgressRequestBody(builder.build(),callBack)).url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            //okhttp异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull  final IOException e) {
                    if(callBack != null){
                        callBack.onError(call,e);
                    }

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(callBack != null){
                        callBack.onSeccess(call,response);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    /**
     * 自定义RequestBody类，得到文件上传的进度
     */
    private static class ProgressRequestBody extends RequestBody {
        //实际的待包装请求体
        private final RequestBody requestBody;
        //包装完成的BufferedSink
        private BufferedSink bufferedSink;
        private AbstractCallBackUtil callBack;

        ProgressRequestBody(RequestBody requestBody, AbstractCallBackUtil callBack) {
            this.requestBody = requestBody;
            this.callBack = callBack;
        }

        /** 重写调用实际的响应体的contentType*/
        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        /**重写调用实际的响应体的contentLength ，这个是文件的总字节数 */
        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        /** 重写进行写入*/
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                bufferedSink = Okio.buffer(sink(sink));
            }
            requestBody.writeTo(bufferedSink);
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();
        }

        /** 写入，回调进度接口*/
        private Sink sink(BufferedSink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;
                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);//这个方法会循环调用，byteCount是每次调用上传的字节数。
                    if (contentLength == 0) {
                        //获得总字节长度
                        contentLength = contentLength();
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount;
                    final float progress = bytesWritten*1.0f / contentLength;
                    AbstractCallBackUtil.mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onProgress(progress,contentLength);
                        }
                    });
                }
            };
        }
    }
}
