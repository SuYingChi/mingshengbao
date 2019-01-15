package com.msht.minshengbao.androidShop.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class MLoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public MLoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = "OkHttpUtils";
        }

        this.showResponse = showResponse;
        this.tag = tag;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        this.logForRequest(request);
        Response response = chain.proceed(request);
        return this.logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            Response.Builder e = response.newBuilder();
            Response clone = e.build();
            if (this.showResponse) {
                Log.e(this.tag, "code : " + clone.code());
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        if (this.isText(mediaType)) {
                            String resp = body.string();
                            Log.e(this.tag, "responseBody\'s content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        }

                        Log.e(this.tag, "responseBody\'s content :  maybe [file part] , too large too print , ignored!");
                    }
                }
            }


            Log.e(this.tag, "========response\'log=======end");
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            if (this.showResponse) {
                RequestBody requestBody = request.body();
                String e = request.url().toString();
                Headers headers = request.headers();
                Log.e(this.tag, "========request\'log=======");
                Log.e(this.tag, "url : " + e);
                if (headers != null && headers.size() > 0) {
                    Log.e(this.tag, "headers : " + headers.toString());
                }
                if ("POST".equals(request.method())) {

                    if (requestBody instanceof FormBody) {
                        for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                            Log.e(this.tag, "key = " + ((FormBody) requestBody).encodedName(i));
                            Log.e(this.tag, "value = " + (((FormBody) requestBody).encodedValue(i)));
                        }
                    } else {
                        //buffer流
                        if (request.body().contentType() != null && isText(request.body().contentType())) {
                            Buffer buffer = new Buffer();
                            requestBody.writeTo(buffer);

                            String oldParamsJson = buffer.readUtf8();
                            Log.e(this.tag, "parameter : \n" + oldParamsJson.replace("&", "\n"));
                        }
                    }
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null
                && "text".equals(mediaType.type()) || mediaType.subtype() != null
                && ("json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype()));
    }

}
