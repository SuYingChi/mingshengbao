package com.msht.minshengbao.OkhttpUtil;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public abstract class AbstractMyCallBack<T> {
    public static AbstractMyCallBack callBackDefault = new AbstractMyCallBack() {
        /*@Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }*/
        @Override
        public void onResponseFail(String call) {
        }
        @Override
        public void onResponseSuccess(String s) {
        }
    };

    public AbstractMyCallBack() {

    }
    public void onBefore(Request request, int id) {
    }

    public void onAfter(int id) {
    }

    public void inProgress(float progress, long total, int id) {
    }

    public boolean validateResponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * 请求网络
     * @param var1
     * @param var2
     * @return
     * @throws Exception
     */
    /*public abstract T parseNetworkResponse(Response var1, int var2) throws Exception;
*/
    /**
     * 请求失败
     * @param var1

     */
    public abstract void onResponseFail(String var1);

    /**
     * 请求响应
     * @param var1
     */
    public abstract void onResponseSuccess(String var1);
}
