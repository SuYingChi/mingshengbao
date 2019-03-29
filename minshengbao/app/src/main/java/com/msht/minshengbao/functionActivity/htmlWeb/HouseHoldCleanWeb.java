package com.msht.minshengbao.functionActivity.htmlWeb;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.LinkUrlUtil;
import com.msht.minshengbao.Utils.QrCodeUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.ActionShareDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.QrCodeDialog;
import com.msht.minshengbao.functionActivity.myActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.publicModule.PaySuccessActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/20  
 */
public class HouseHoldCleanWeb extends BaseActivity {
    private WebView mWebView;
    private ProgressBar progressBar;
    private TextView tvRightText;
    private String id;
    private String  userId;
    private String  password;
    private String  title;
    private String  desc;
    private String  share="0";
    private String  shareTitle;
    private String  activityCode;
    private String  deviceToken;
    private String  phone;
    private String shareDesc="家电清洗";
    private Bitmap mBitmap;
    private static final String BTN_URL="add_address.html";
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<HouseHoldCleanWeb> mWeakReference;
        public RequestHandler(HouseHoldCleanWeb activity) {
            mWeakReference=new WeakReference<HouseHoldCleanWeb>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final HouseHoldCleanWeb activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_hold_clean_web);
        context=this;
        mPageName="家电清洗";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this,SharedPreferencesUtil.Password,"");
        phone=SharedPreferencesUtil.getUserName(this,SharedPreferencesUtil.UserName,"");
        Intent data=getIntent();
        if (data!=null){
            desc=data.getStringExtra("desc");
            shareTitle=data.getStringExtra("title");
            activityCode=data.getStringExtra("activityCode")+"_share";
        }
        if (!TextUtils.isEmpty(shareTitle)){
            mPageName=shareTitle;
        }else {
            mPageName="家电清洗";
        }
        desc="给家电洗洗澡，让洁净充满你的生活。搞活动有优惠哦，赶紧来下单吧！";
        initFindViewId();
        initHeader();
        initWebView();
    }

    private void initFindViewId() {
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mWebView=(WebView)findViewById(R.id.id_insurance_webView);
    }

    private void initHeader() {

        View mViewStatusBarPlace = findViewById(R.id.id_status_view);
        ViewGroup.LayoutParams params = mViewStatusBarPlace.getLayoutParams();
        params.height = StatusBarCompat.getStatusBarHeight(this);
        mViewStatusBarPlace.setLayoutParams(params);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            mViewStatusBarPlace.setVisibility(View.GONE);
        }
        tvRightText=(TextView)findViewById(R.id.id_tv_rightText);
        backImg = (ImageView) findViewById(R.id.id_back);
        tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("分享");
        TextView tvClose=(TextView)findViewById(R.id.id_close);
        tvClose.setVisibility(View.VISIBLE);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionShareDialog();
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                }else {
                    finish();
                }
            }
        });
        tvNavigationTile.setText(mPageName);
    }
    private void onActionShareDialog() {
        new ActionShareDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new ActionShareDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onClick(int text) {
                        switch (text){
                            case 0:
                                onWeiXin();
                                break;
                            case 1:
                                onFriendCircle();
                                break;
                            case 2:
                                onRequestPermission();
                                break;
                            case 3:
                                onLinkShare();
                                break;
                            default:
                                onWeiXin();
                                break;
                        }


                    }
                }).show();

    }

    private void initWebView() {
        String useUrl= UrlUtil.HOUSE_HOLD_CLEAN_WEB;
        try {
            useUrl = LinkUrlUtil.containMark(context,useUrl)+"&city_id="+VariableUtil.cityId+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(useUrl);
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setNeedInitialFocus(true);
        settings.setAppCacheEnabled(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        if(mWebView.canGoBack()){
                            mWebView.goBack();
                            return true;
                        }else {
                            finish();
                        }
                    }
                }
                return false;
            }
        });
    }


    private void onRequestShareSuccess() {

        String validateURL = UrlUtil.SUCCESS_SHARE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("share_code",activityCode);
        textParams.put("token","");
        textParams.put("phone",phone);
        textParams.put("device_token",deviceToken);
        textParams.put("relate_info",UrlUtil.HOUSE_HOLD_CLEAN_WEB);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                onQrCode();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许拍照权限，二维码扫描无法使用！");
                            }
                        }).start();
            }else {
                onQrCode();
            }
        }else {
            onQrCode();
        }
    }
    private void onWeiXin() {
        String shareUrl=UrlUtil.HOUSE_HOLD_CLEAN_WEB;
        try {
            if (!shareUrl.contains(ConstantUtil.MARK_QUESTION)){
                shareUrl=shareUrl+"?isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }else {
                shareUrl=shareUrl+"&isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(desc);
        web.setDescription(desc);
        web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        new ShareAction(HouseHoldCleanWeb.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
    }
    private void onFriendCircle() {
        String shareUrl=UrlUtil.HOUSE_HOLD_CLEAN_WEB;
        try {
            if (!shareUrl.contains(ConstantUtil.MARK_QUESTION)){
                shareUrl=shareUrl+"?isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }else {
                shareUrl=shareUrl+"&isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareDesc);
        web.setDescription(desc);
        web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        new ShareAction(HouseHoldCleanWeb.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener)
                .share();
    }

    private void onQrCode() {

        String shareUrl=UrlUtil.HOUSE_HOLD_CLEAN_WEB;
        try {
            if (!shareUrl.contains(ConstantUtil.MARK_QUESTION)){
                shareUrl=shareUrl+"?isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }else {
                shareUrl=shareUrl+"&isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (mBitmap!=null&&!mBitmap.isRecycled()){
            onShowQrCodeDialog(mBitmap);
        }else {
            mBitmap=QrCodeUtil.createQRCodeBitmap(shareUrl,300);
            onShowQrCodeDialog(mBitmap);
        }
    }
    private void onShowQrCodeDialog(final Bitmap mBitmap) {
        new QrCodeDialog(context,mBitmap).builder()
                .setOnSaveButtonClickListener(new QrCodeDialog.OnSaveButtonClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FileUtil.saveImageToGallery(context,mBitmap)){
                            ToastUtil.ToastText(context,"图片已保存");
                        }

                    }
                })
                .setOnShareButtonClickListener(new QrCodeDialog.OnShareButtonClickListener() {
                    @Override
                    public void onClick(View v) {
                        UMImage image = new UMImage(HouseHoldCleanWeb.this, mBitmap);
                        new ShareAction(HouseHoldCleanWeb.this).withMedia(image)
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(umShareListener)
                                .share();
                    }
                }).show();
    }

    private void onLinkShare() {
        String shareUrl=UrlUtil.HOUSE_HOLD_CLEAN_WEB;
        try {
            if (!shareUrl.contains(ConstantUtil.MARK_QUESTION)){
                shareUrl=shareUrl+"?isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }else {
                shareUrl=shareUrl+"&isShow=1"+"&recommend_code="+phone+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData=ClipData.newPlainText("Label",shareUrl);
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtil.ToastText(context,"已复制到剪切板");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE1:
                if (resultCode==ConstantUtil.VALUE1){
                    mWebView.reload();
                }
                break;
            default:
                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String tag="tel";
            if (url.startsWith("weixin://wap/pay?")){
                try{
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    ToastUtil.ToastText(context,"请安装微信最新版本");
                }
                return true;
            }else if (url.contains(tag)){
                String mobile=url.substring(url.lastIndexOf("/")+1);
                Intent mIntent=new Intent(Intent.ACTION_CALL);
                Uri data=Uri.parse(mobile);
                mIntent.setData(data);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                    startActivity(mIntent);
                    return true;
                }else {
                    ActivityCompat.requestPermissions(HouseHoldCleanWeb.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    return true;
                }
            }else if (url.contains(BTN_URL)){
                onManageAddress();
                return true;
            }else if (url.contains(UrlUtil.APP_PAY_SUCCESS_PAGE_URL)){
                onStartSuccess(url);
                return true;
            }else if (url.startsWith(ConstantUtil.MSB_APP)){
                AppActivityUtil.onAppActivityType(context,url,"民生宝","0","","","");
                return true;
            }else {
                return false;
            }
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有网站的证书
            super.onReceivedSslError(view, handler, error);
        }
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

    }

    private void onStartSuccess(String url) {
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("url","");
        success.putExtra("type","1");
        success.putExtra("pageUrl",url);
        success.putExtra("navigation","民生宝");
        startActivity(success);
        finish();
    }

    private void onManageAddress() {
        Intent intent=new Intent(context,AddressManageActivity.class);
        startActivityForResult(intent,ConstantUtil.VALUE1);
    }
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvNavigationTile.setText(title);
        }
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
            if (newProgress==100){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            onRequestShareSuccess();
            ToastUtil.ToastText(context, " 分享成功啦");
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.ToastText(context, " 分享失败啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.ToastText(context," 分享取消了");
        }
    };


}
