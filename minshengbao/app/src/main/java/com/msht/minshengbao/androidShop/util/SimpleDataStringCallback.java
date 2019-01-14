package com.msht.minshengbao.androidShop.util;

import android.text.TextUtils;

import com.msht.minshengbao.androidShop.shopBean.BaseData;
import com.msht.minshengbao.androidShop.viewInterface.IBaseView;
import com.msht.minshengbao.androidShop.viewInterface.ISimpleBaseView;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;

public class SimpleDataStringCallback  extends StringCallback{

    protected ISimpleBaseView iView;
    protected boolean isResponseSuccess;
    protected BaseData baseData;

    public SimpleDataStringCallback(ISimpleBaseView iView) {
        this.iView = iView;
    }



    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onResponse(String s, int i) {

        if (TextUtils.isEmpty(s) || TextUtils.equals("\"\"", s)) {
            isResponseSuccess = false;
        }else {
             baseData = JsonUtil.getBaseData(s);
             if(baseData==null){
                 isResponseSuccess = false;
             }else if(baseData.getCode()==400){
                 isResponseSuccess = false;
             }else if(baseData.getCode() == 200){
                 isResponseSuccess = true;
             }
        }
    }
}
