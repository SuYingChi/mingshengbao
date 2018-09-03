package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Defaultcontent;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ShareMenuActivity extends BaseActivity {
    private String userId,password;
    private ShareAction mShareAction;
    private static final String  WEI_XIN_PLATFORM="WEIXIN_FAVORITE";
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ShareMenuActivity> mWeakReference;
        public RequestHandler(ShareMenuActivity activity) {
            mWeakReference=new WeakReference<ShareMenuActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ShareMenuActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onShareSuccess();
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onShareSuccess() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("已确认您的分享")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        setResult(1);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_menu);
        setCommonHeader("分享应用");
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initPlatforms();
        initView();
    }
    private void initPlatforms() {
        mShareAction=new ShareAction(ShareMenuActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(Defaultcontent.url);
                        web.setTitle(Defaultcontent.title);
                        web.setDescription(Defaultcontent.text+"——来自民生宝分享面板");
                        web.setThumb(new UMImage(ShareMenuActivity.this, Defaultcontent.imageurl));
                        new ShareAction(ShareMenuActivity.this).withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(umShareListener)
                                .share();
                    }
                });
    }
    private void initView() {
        findViewById(R.id.share_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAction.open();
            }
        });
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(!platform.name().equals(WEI_XIN_PLATFORM)){
                if (password!=null) {
                    shareEnsure();
                }else {
                    ToastUtil.ToastText(context,platform + " 分享成功啦");
                }
            }else{
                ToastUtil.ToastText(context,platform + " 收藏成功啦");
            }
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.ToastText(context,platform + " 分享失败啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.ToastText(context,platform + " 分享取消了");
        }
    };
    private void shareEnsure() {
        String validateURL = UrlUtil.Shara_appUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        mShareAction.open(config);
    }
}
