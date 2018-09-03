package com.msht.minshengbao.Callback;

import android.content.Context;

import com.google.gson.Gson;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.callback.Callback;
import com.msht.minshengbao.Utils.ToastUtil;


import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


public abstract class BeanCallback<T> extends Callback<String> {

    public static final String TAG = BeanCallback.class.getSimpleName();

    private Type type;

    private Context activity;

    public void setContext(Context activity) {
        this.activity = activity;
    }

    public BeanCallback() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        type = parameterized.getActualTypeArguments()[0];
    }

    @Override
    public String parseNetworkResponse(Response response, int i) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        return body.string();
    }

    @Override
    public void onResponse(String resp, int i) {
//        Log.d(TAG, "onResponse: activity = " + activity + " , resp = " + resp);
        if (resp == null){
            return;
        }
        if (activity == null){
            throw new RuntimeException("activity must not be null");

        }
        try {
            JSONObject jsonObject = new JSONObject(resp);
            int code = jsonObject.optInt("code");
            String datas = jsonObject.optString("datas");
            if (activity instanceof BaseActivity) {
                BaseActivity act = (BaseActivity) activity;
                if (code == 200) {
                    if (type == String.class) {
                        if (!act.isOnDestroy())
                        {
                            response((T) datas);
                        }
                    } else {
                        T fromJson = new Gson().fromJson(datas, type);
                        if (!act.isOnDestroy())
                        {
                            response(fromJson);
                        }
                    }
                } else if (code == 401) {
                    clearToken();
                } else {
                    JSONObject object = new JSONObject(datas);
                    String error = object.optString("error");
                    if (!act.isOnDestroy())
                    {
                        fail(error);
                    }
                }
            } else {
                if (code == 200) {
                    if (type == String.class) {
                        response((T) datas);
                    } else {
                        T fromJson = new Gson().fromJson(datas, type);
                        response(fromJson);
                    }
                } else if (code == 401) {
                    clearToken();
                } else {
                    JSONObject object = new JSONObject(datas);
                    String error = object.optString("error");
                    fail(error);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        error(call, e, i);
    }

    //code==200时的处理方法
    public abstract void response(T data);

    //code!=200时的处理方法
    public void fail(String error) {
        ToastUtil.ToastText(activity, error);
    }

    //code==401时的处理方法
    public void clearToken() {
        ToastUtil.ToastText(activity, "http未授权");
        //MyShopApplication.getInstance().setMemberInfo(null);
    }

    public void error(Call call, Exception e, int i) {
        //空实现
    }

}
