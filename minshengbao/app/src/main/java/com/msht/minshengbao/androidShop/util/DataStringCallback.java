package com.msht.minshengbao.androidShop.util;

import android.text.TextUtils;

import com.msht.minshengbao.androidShop.shopBean.BaseData;
import com.msht.minshengbao.androidShop.viewInterface.IBaseView;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Request;

public class DataStringCallback extends StringCallback {

    protected    boolean isShowLoadingDialog = true;
    protected IBaseView iView;
    protected boolean isResponseSuccess;
    protected BaseData baseData;

    public DataStringCallback(IBaseView iView) {
        this.iView = iView;
    }
    public DataStringCallback(IBaseView iView,boolean isShowLoadingDialog) {
        this.iView = iView;
        this.isShowLoadingDialog = isShowLoadingDialog;
    }
    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        if(isShowLoadingDialog) {
            iView.showLoading();
        }
    }

    @Override
    public void onError(okhttp3.Call call, Exception e, int i) {
        e.printStackTrace();
        if(isShowLoadingDialog) {
            iView.dismissLoading();
        }
        if(e.getMessage().toString().contains("timeout")){
            iView.onError("网络连接超时，请重试");
        }else {
            iView.onError(e.getMessage().toString());
        }

    }

    @Override
    public void onResponse(String s, int i) {
        if(isShowLoadingDialog) {
            iView.dismissLoading();
        }
        if (TextUtils.isEmpty(s) || TextUtils.equals("\"\"", s)) {
            isResponseSuccess = false;
            iView.onError("接口返回空字符串");
        }else {
             baseData = JsonUtil.getBaseData(s);
             if(baseData==null){
                 iView.onError("GSON转换异常");
                 isResponseSuccess = false;
             }else if(baseData.getCode()==400){
                 iView.onError(baseData.getDatas());
                 isResponseSuccess = false;
             }else if(baseData.getCode() == 200){
                 isResponseSuccess = true;
             }
        }
    }
}
