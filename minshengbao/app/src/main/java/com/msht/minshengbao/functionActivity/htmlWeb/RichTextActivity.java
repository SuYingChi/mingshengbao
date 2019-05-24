package com.msht.minshengbao.functionActivity.htmlWeb;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ShareDefaultContent;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RichTextActivity extends BaseActivity {
    private WebView mWebView;
    private String htmlText;
    private String id;
    private String url;
    private String title;
    private String picUrl;
    private String  userId;
    private String  deviceToken;
    private String  phone;
    private ShareAction mShareAction;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<RichTextActivity> mWeakReference;
        public RequestHandler(RichTextActivity activity) {
            mWeakReference=new WeakReference<RichTextActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RichTextActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    //ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        context=this;
        mPageName="民生头条";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        deviceToken=SharedPreferencesUtil.getDeviceData(this,SharedPreferencesUtil.DeviceToken,"");
        phone=SharedPreferencesUtil.getUserName(this,SharedPreferencesUtil.UserName,"");
        setCommonHeader(mPageName);
        Intent data=getIntent();
        if (data!=null){
            id=data.getStringExtra("id");
            title=data.getStringExtra("title");
            picUrl=data.getStringExtra("pic");
        }
        url=UrlUtil.MSB_APP_NEWS+"?id="+id;
        mWebView=(WebView)findViewById(R.id.id_RichText_view);
        initHeader();
        initWebViewSetting();
        initShareEvent();
        initSharePlatforms();
    }
    private void initSharePlatforms() {

        mShareAction=new ShareAction(RichTextActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA shareMedia) {
                        String shareUrl=url+"&isShow=1";
                        UMWeb web = new UMWeb(shareUrl);
                        web.setTitle(ShareDefaultContent.HeadLine);
                        web.setDescription(title);
                        if (TextUtils.isEmpty(picUrl)||picUrl.equals(ConstantUtil.NULL_VALUE)){
                            web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
                        }else {
                            web.setThumb(new UMImage(context, picUrl));
                        }
                        new ShareAction(RichTextActivity.this).withMedia(web)
                                .setPlatform(shareMedia)
                                .setCallback(umShareListener)
                                .share();
                    }
                });
    }
    private void initHeader() {
        TextView tvRightText=findViewById(R.id.id_tv_rightText);
        tvRightText.setText("分享");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAction.open();
            }
        });
    }
    private void initShareEvent() {
        findViewById(R.id.id_weiChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareToWeiChat();
            }
        });
        findViewById(R.id.id_friend_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareToFriendCircle();
            }
        });
    }
    private void onShareToFriendCircle() {
        String shareUrl=url+"&isShow=1";
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(ShareDefaultContent.HeadLine);
        web.setDescription(title);
        if (TextUtils.isEmpty(picUrl)||picUrl.equals(ConstantUtil.NULL_VALUE)){
            web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        }else {
            web.setThumb(new UMImage(context, picUrl));
        }
        new ShareAction(RichTextActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener)
                .share();
    }
    private void onShareToWeiChat() {
        String shareUrl=url+"&isShow=1";
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(ShareDefaultContent.HeadLine);
        web.setDescription(title);
        if (!TextUtils.isEmpty(picUrl)){
            web.setThumb(new UMImage(context, picUrl));
        }else {
            web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        }
        new ShareAction(RichTextActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
    }
    private void onRequestShareSuccess(){
        String validateURL = UrlUtil.SUCCESS_SHARE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("share_code","news_share");
        textParams.put("token","");
        textParams.put("phone",phone);
        textParams.put("device_token",deviceToken);
        textParams.put("relate_info",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        WebSettings settings= mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        String  mUrl=url+"&isShow=0";
        mWebView.loadUrl(mUrl);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }
        });
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            new PromptDialog.Builder(context)
                    .setTitle(R.string.my_dialog_title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(message)
                    .setButton1("我知道了", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            result.cancel();
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(!platform.name().equals(ConstantUtil.WEI_XIN_PLATFORM)){
                ToastUtil.ToastText(context, " 分享成功啦");
                onRequestShareSuccess();
            }else{
                ToastUtil.ToastText(context, " 收藏成功啦");
            }
            onRequestShareSuccess();
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
}
