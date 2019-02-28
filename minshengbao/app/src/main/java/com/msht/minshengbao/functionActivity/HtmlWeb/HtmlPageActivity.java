package com.msht.minshengbao.functionActivity.HtmlWeb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.MyAPI.MyWebChromeClient;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ShareDefaultContent;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.LinkUrlUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.QrCodeUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.ActionShareDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.QrCodeDialog;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;
import com.msht.minshengbao.androidShop.util.NavigationbarUtil;
import com.msht.minshengbao.functionActivity.MyActivity.AddAddressActivity;
import com.msht.minshengbao.functionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONObject;

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
 * @date 2018/7/2  
 */
public class HtmlPageActivity extends BaseActivity {
    //private VerticalSwipeRefreshLayout mRefresh;
    private WebView mWebView;
    private TextView tvRightText;
    private ShareAction mShareAction;
    private String  mUrl, mNavigation="民生宝";
    private String  desc;
    private String  share="0";
    private String  shareTitle;
    private String  userId;
    private String  activityCode;
    private String  deviceToken;
    private String  phone;
    private String  password;
    private String  backUrl;
    private Bitmap mBitmap;
    private static final String BTN_URL="add_address.html";
    private final  BackUrlHandler backUrlHandler=new BackUrlHandler(this);

    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<HtmlPageActivity> mWeakReference;
        public RequestHandler(HtmlPageActivity activity) {
            mWeakReference=new WeakReference<HtmlPageActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final HtmlPageActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                   // ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class BackUrlHandler extends Handler {
        private WeakReference<HtmlPageActivity> mWeakReference;
        public BackUrlHandler(HtmlPageActivity activity) {
            mWeakReference=new WeakReference<HtmlPageActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final HtmlPageActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    //分享成功后刷新
                    activity.mWebView.reload();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            ToastUtil.ToastText(activity.context,"分享成功啦");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    // ToastUtil.ToastText(activity.context,msg.obj.toString());
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
        setContentView(R.layout.activity_html_page);
        context=this;
        Intent data=getIntent();
        if (data!=null){
            mUrl =data.getStringExtra("url");
            mNavigation =data.getStringExtra("navigate");
            desc=data.getStringExtra("desc");
            share=data.getStringExtra("share");
            shareTitle=data.getStringExtra("title");
            activityCode=data.getStringExtra("activityCode")+"_share";
            backUrl=data.getStringExtra("backUrl");

        }
        mPageName= mNavigation;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        deviceToken=SharedPreferencesUtil.getDeviceData(this,SharedPreferencesUtil.DeviceToken,"");
        phone=SharedPreferencesUtil.getUserName(this,SharedPreferencesUtil.UserName,"");
        password=SharedPreferencesUtil.getPassword(this,SharedPreferencesUtil.Password,"");
        if (TextUtils.isEmpty(shareTitle)||desc.equals(ConstantUtil.NULL_VALUE)){
            shareTitle=ShareDefaultContent.title;
        }
        if (TextUtils.isEmpty(desc)||desc.equals(ConstantUtil.NULL_VALUE)){
            desc="";
        }
        if (mUrl.contains(UrlUtil.HOUSE_HOLD_CLEAN_WEB)){
            shareTitle="家电清洗";
            desc="给家电洗洗澡，让洁净充满你的生活。搞活动有优惠哦，赶紧来下单吧！";
        }
        initHeader();
        initWeBView();
        initEvent();
       // initSharePlatforms();
    }
    private void initHeader() {
        View head = findViewById(R.id.id_re_layout);
        backImg = (ImageView) findViewById(R.id.id_back);
        tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        tvNavigationTile.setText(mNavigation);
         tvRightText=findViewById(R.id.id_tv_rightText);
        tvRightText.setText("分享");
        if ((!TextUtils.isEmpty(share))&&share.equals(ConstantUtil.VALUE_ONE)){
            tvRightText.setVisibility(View.VISIBLE);
        }else {
            tvRightText.setVisibility(View.GONE);
        }
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
                new ActionShareDialog(context)
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

               // mShareAction.open();
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
    }
    private void initSharePlatforms() {
        if (TextUtils.isEmpty(shareTitle)||desc.equals(ConstantUtil.NULL_VALUE)){
            shareTitle=ShareDefaultContent.title;
        }
        if (TextUtils.isEmpty(desc)||desc.equals(ConstantUtil.NULL_VALUE)){
            desc="";
        }
        mShareAction=new ShareAction(HtmlPageActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA shareMedia) {
                        String shareUrl=mUrl;
                        if (!shareUrl.contains(ConstantUtil.MARK_QUESTION)){
                            shareUrl=shareUrl+"?isShow=1"+"&recommend_code="+phone;
                        }else {
                            shareUrl=shareUrl+"&isShow=1"+"&recommend_code="+phone;
                        }
                        UMWeb web = new UMWeb(shareUrl);
                        web.setTitle(shareTitle);
                        web.setDescription(desc);
                        web.setThumb(new UMImage(HtmlPageActivity.this, R.mipmap.ic_launcher));
                        new ShareAction(HtmlPageActivity.this).withMedia(web)
                                .setPlatform(shareMedia)
                                .setCallback(umShareListener)
                                .share();
                    }
                });
    }
    private void onRequestShareSuccess(){
        String validateURL = UrlUtil.SUCCESS_SHARE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("share_code",activityCode);
        textParams.put("token","");
        textParams.put("phone",phone);
        textParams.put("device_token",deviceToken);
        textParams.put("relate_info",mUrl);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onRequestShareSuccessAward() {
        if (!TextUtils.isEmpty(backUrl)){
            String successBackUrl=backUrl;
            try {
                if (!backUrl.contains(ConstantUtil.MARK_QUESTION)){
                    successBackUrl=successBackUrl+"?username="+ URLEncoder.encode(phone, "UTF-8")+"&password="+URLEncoder.encode(password, "UTF-8");
                }else {
                    successBackUrl=successBackUrl+"&username="+ URLEncoder.encode(phone, "UTF-8")+"&password="+URLEncoder.encode(password, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SendRequestUtil.getDataFromService(successBackUrl,backUrlHandler);
           // OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(successBackUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
        }

    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeBView() {
       /* mRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_vertical_refresh);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
        //webview 会默认浸入显示，适配有虚拟导航栏的机型，顶部间隔状态栏高度，底部部要间隔导航栏高度，否则重叠
      /*  LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) mRefresh.getLayoutParams();
        para.setMargins(0, StatusBarCompat.getStatusBarHeight(this),0, NavigationbarUtil.getVirtualBarHeigh(this));
        mRefresh.setLayoutParams(para);*/
        mWebView =(WebView)findViewById(R.id.id_web_html);
        String useUrl= mUrl;
        try {
            useUrl = LinkUrlUtil.containMark(context,mUrl)+"&city_id="+VariableUtil.cityId+"&city_name="+URLEncoder.encode(VariableUtil.City, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(useUrl);
        WebSettings settings= mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }
    private void initEvent() {
//       mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//           @Override
//           public void onRefresh() {
//               mWebView.reload();
//           }
//       });
//        mRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
//            @Override
//            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                return mWebView.getScrollY()>0;
//            }
//        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
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

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(UrlUtil.HOUSE_HOLD_CLEAN_WEB)){
                tvRightText.setVisibility(View.VISIBLE);
            }else {
                if ((!TextUtils.isEmpty(share))&&share.equals(ConstantUtil.VALUE_ONE)){
                    tvRightText.setVisibility(View.VISIBLE);
                }else {
                    tvRightText.setVisibility(View.GONE);
                }
            }
            String tag="tel";
            if (url.contains(tag)){
                String mobile=url.substring(url.lastIndexOf("/")+1);
                Intent mIntent=new Intent(Intent.ACTION_CALL);
                Uri data=Uri.parse(mobile);
                mIntent.setData(data);
                if (ActivityCompat.checkSelfPermission(HtmlPageActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                    startActivity(mIntent);
                    return true;
                }else {
                    ActivityCompat.requestPermissions(HtmlPageActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    return true;
                }
            }else if (url.contains(BTN_URL)){
                onManageAddress();
                return true;
            }else if (url.startsWith(ConstantUtil.MSB_APP)){
                AppActivityUtil.onAppActivityType(context,url,"民生宝","0","",activityCode,"");
                return true;
            }else {
                view.loadUrl(url);
            }
            return true;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有网站的证书
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (url.contains(UrlUtil.HOUSE_HOLD_CLEAN_WEB)){
                tvRightText.setVisibility(View.VISIBLE);
            }else {
                if ((!TextUtils.isEmpty(share))&&share.equals(ConstantUtil.VALUE_ONE)){
                    tvRightText.setVisibility(View.VISIBLE);
                }else {
                    tvRightText.setVisibility(View.GONE);
                }
            }
        }
    }

    private void onManageAddress() {
        Intent intent=new Intent(context,AddressManageActivity.class);
        startActivityForResult(intent,ConstantUtil.VALUE1);
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
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)){
                tvNavigationTile.setText(title);
            }else {
                tvNavigationTile.setText(mNavigation);
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        /** attention to this below ,must add this**/
       // UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
        String shareUrl=mUrl;
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
        web.setTitle(shareTitle);
        web.setDescription(desc);
        web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        new ShareAction(HtmlPageActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
    }
    private void onFriendCircle() {
        String shareUrl=mUrl;
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
        web.setTitle(shareTitle);
        web.setDescription(desc);
        web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        new ShareAction(HtmlPageActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener)
                .share();
    }

    private void onQrCode() {
        String shareUrl=mUrl;
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
                        UMImage image = new UMImage(HtmlPageActivity.this, mBitmap);
                        new ShareAction(HtmlPageActivity.this).withMedia(image)
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(umShareListener)
                                .share();
                    }
                }).show();
    }

    private void onLinkShare() {
        String shareUrl=mUrl;
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


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(!platform.name().equals(ConstantUtil.WEI_XIN_PLATFORM)){
                ToastUtil.ToastText(context, " 分享成功啦");
                onRequestShareSuccess();
                onRequestShareSuccessAward();
            }else{
                ToastUtil.ToastText(context, " 收藏成功啦");
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

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        mShareAction.open(config);
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
