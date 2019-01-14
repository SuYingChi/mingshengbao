package com.msht.minshengbao.androidShop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
   public static final String APP_ID = "wx33f335ace862eca1";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

//发送请求给微信 发送成功后会切回到第三方app
    @Override
    public void onReq(BaseReq req) {
      PopUtil.toastInBottom(req.toString());
    }
   // sendResp是微信向第三方app请求数据，第三方app回应数据之后会切回到微信界面。
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                PopUtil.toastInBottom("用户取消");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        PopUtil.toastInBottom("zk wx"+ "code = " + code);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        PopUtil.toastInBottom("微信分享成功");
                        break;
                        default:break;
                }
                break;
                default:break;
        }
        finish();
    }
}

