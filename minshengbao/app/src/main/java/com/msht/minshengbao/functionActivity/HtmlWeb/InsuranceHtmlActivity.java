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
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ShareDefaultContent;
import com.msht.minshengbao.Utils.AndroidWorkaround;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.QrCodeUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.ActionShareDialog;
import com.msht.minshengbao.ViewUI.Dialog.ActionSheetDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.QrCodeDialog;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;
import com.msht.minshengbao.androidShop.util.NavigationbarUtil;
import com.msht.minshengbao.functionActivity.GasService.AddCustomerNoActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterFriendShareActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/18  
 */
public class InsuranceHtmlActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressBar progressBar;
    private VerticalSwipeRefreshLayout mRefresh;
    private TextView tvRightText;
    private String id;
    private String  userId;
    private String  password;
    private String  title;
    private String  imageUrl;
    private String  desc;
    private static final  String shareTitle="居民燃气险";
    private String  phone;
    private Bitmap mBitmap;
    private String shareDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_html);
        context=this;
        mPageName="购买保险";
        Intent data=getIntent();
        if (data!=null){
            id=data.getStringExtra("id");
            title=data.getStringExtra("title");
            imageUrl=data.getStringExtra("imageUrl");
            desc=data.getStringExtra("desc");
        }
        shareDesc="安全无小事，保险保平安，这款"+title+"蛮实惠的，快来看看吧";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this,SharedPreferencesUtil.Password,"");
        phone=SharedPreferencesUtil.getUserName(this,SharedPreferencesUtil.UserName,"");
        initFindViewId();
        initHeader();
        initWebView();
    }

    private void initHeader() {
        View  mViewStatusBarPlace = findViewById(R.id.id_status_view);
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

    private void initFindViewId() {
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mWebView=(WebView)findViewById(R.id.id_insurance_webView);
        mRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_vertical_refresh);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //webview 会默认浸入显示，适配有虚拟导航栏的机型，顶部间隔状态栏高度，底部部要间隔导航栏高度，否则重叠
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });
        mRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return mWebView.getScrollY()>0;
            }
        });
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
        String insuranceDetailUrl=UrlUtil.INSURANCE_DETAIL_URL+"?id="+id+"&code="+phone+"&isShow=1";
        UMWeb web = new UMWeb(insuranceDetailUrl);
        web.setTitle(title);
        web.setDescription(shareDesc);
        web.setThumb(new UMImage(context, imageUrl));
        new ShareAction(InsuranceHtmlActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
    }

    private void onFriendCircle() {
        String insuranceDetailUrl=UrlUtil.INSURANCE_DETAIL_URL+"?id="+id+"&code="+phone+"&isShow=1";
        UMWeb web = new UMWeb(insuranceDetailUrl);
        web.setTitle(shareDesc);
        web.setDescription(shareDesc);
        web.setThumb(new UMImage(context, imageUrl));
        new ShareAction(InsuranceHtmlActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener)
                .share();
    }

    private void onQrCode() {
        String insuranceDetailUrl=UrlUtil.INSURANCE_DETAIL_URL+"?id="+id+"&code="+phone+"&isShow=1";
        if (mBitmap!=null&&!mBitmap.isRecycled()){
            onShowQrCodeDialog(mBitmap);
        }else {
            mBitmap=QrCodeUtil.createQRCodeBitmap(insuranceDetailUrl,300);
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
                        UMImage image = new UMImage(InsuranceHtmlActivity.this, mBitmap);
                        new ShareAction(InsuranceHtmlActivity.this).withMedia(image)
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(umShareListener)
                                .share();
                    }
                }).show();
    }

    private void onLinkShare() {
        String insuranceDetailUrl=UrlUtil.INSURANCE_DETAIL_URL+"?id="+id+"&code="+phone+"&isShow=1";
        ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData=ClipData.newPlainText("Label",insuranceDetailUrl);
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        String insuranceDetailUrl=UrlUtil.INSURANCE_DETAIL_URL+"?id="+id+"&userId="+userId+"&item="+password;
        mWebView.loadUrl(insuranceDetailUrl);
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setNeedInitialFocus(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new  MyWebChromeClient());
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
    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String tag="tel";
            if (url.contains(UrlUtil.INSURANCE_DETAIL_URL)){
                tvRightText.setVisibility(View.VISIBLE);
            }else {
                tvRightText.setVisibility(View.GONE);

            }
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
                    ActivityCompat.requestPermissions(InsuranceHtmlActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    return true;
                }
            }else if (url.equals(UrlUtil.INSURANCE_BTN_URL)){
                onNewCustomerNo();
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
            if (url.contains(UrlUtil.INSURANCE_DETAIL_URL)){
                tvRightText.setVisibility(View.VISIBLE);
            }else {
                tvRightText.setVisibility(View.GONE);
            }
        }
    }
    private void onNewCustomerNo() {
        Intent intent=new Intent(context,AddCustomerNoActivity.class);
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
                mRefresh.setRefreshing(false);
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
