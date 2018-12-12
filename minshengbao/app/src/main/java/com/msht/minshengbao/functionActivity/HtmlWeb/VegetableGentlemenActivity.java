package com.msht.minshengbao.functionActivity.HtmlWeb;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import android.widget.Toast;

import com.msht.minshengbao.MyAPI.MyWebChromeClient;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.MD5;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

public class VegetableGentlemenActivity extends AppCompatActivity implements MyWebChromeClient.OpenFileChooserCallBack {
    private WebView mWebView;
    private ImageView backImage;
    private ProgressBar progressBar;
    private boolean   SecondExecute=false;
    private String    userPhone;
    private String    userId;
    private String    mPageName="海南蔬菜先生";
    private static final String    KEY="25ef33065ea746987885d44cd413409c";
    private Context   context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_gentlemen);
        StatusBarCompat.setStatusBar(this);
        context=this;
        userPhone = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName, "");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        mWebView =(WebView)findViewById(R.id.id_vegetable_webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        setCommonHeader();
        initmWebView();
        initEvent();
    }
    private void setCommonHeader() {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);
        }
        backImage = (ImageView) findViewById(R.id.id_goback);
        TextView tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        tvNavigationTile.setText(mPageName);
    }
    private void initmWebView() {
        String signString ="phone="+ userPhone +"&userid="+userId+"&key="+KEY;
        String sign = MD5.md5Digest(signString);
        final String data = "&userid="+ userId +"&phone=" + userPhone +"&sign="+sign;
        String loginUrl= UrlUtil.VEGETABLE_URL +data;
        settingWeb();
        mWebView.loadUrl(loginUrl);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")){
                    try{
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        SecondExecute=true;
                    }catch (ActivityNotFoundException e){
                        Toast.makeText(context,"请安装微信最新版本" , Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else {
                    return false;
                }
               // return super.shouldOverrideUrlLoading(view,url);
            }
           @Override
           public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
               if (url.startsWith("https://wx.tenpay.com")) {
                   if (SecondExecute){
                       mWebView.goBack();
                       SecondExecute=false;
                   }
               }
               super.doUpdateVisitedHistory(view, url, isReload);
           }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }
        });
        mWebView.setWebChromeClient(new MyWebChromeClient(VegetableGentlemenActivity.this));
    }
    private void settingWeb() {
        WebSettings settings= mWebView.getSettings();
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
       /// settings.setBuiltInZoomControls(true);
    }
    private void initEvent() {
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
        backImage.setOnClickListener(new View.OnClickListener() {
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
    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {}
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress==100){
            progressBar.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean openFileChooserCallBackAndroid5(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }
}
