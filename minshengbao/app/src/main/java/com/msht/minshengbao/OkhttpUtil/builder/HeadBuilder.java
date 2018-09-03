package com.msht.minshengbao.OkhttpUtil.builder;


import com.msht.minshengbao.OkhttpUtil.OkHttpManager;
import com.msht.minshengbao.OkhttpUtil.request.OtherRequest;
import com.msht.minshengbao.OkhttpUtil.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpManager.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
