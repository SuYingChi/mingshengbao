package com.msht.minshengbao.FunctionView.HtmlWeb;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.phone.mrpc.core.MiscUtils;
import com.msht.minshengbao.MyAPI.MyWebChomeClient;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.MD5;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.TreeMap;

public class VegetableGentlemen extends AppCompatActivity implements MyWebChomeClient.OpenFileChooserCallBack {
    private WebView   mWebview;
    private ImageView backimg;
    private TextView  tv_navigaTile;
    private ProgressBar progressBar;
    private boolean   SecondExecute=false;
    private String    userphone;
    private String    userId;
    private String    sign;
    private String    mPageName="海南蔬菜先生";
    private String    key="25ef33065ea746987885d44cd413409c";
    private Context   context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_gentlemen);
        StatusBarCompat.setStatusBar(this);
        context=this;
        userphone = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName, "");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        mWebview=(WebView)findViewById(R.id.id_vegetable_webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        setCommonHeader();
        initWebView();
        initEvent();
    }
    private void setCommonHeader() {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);//状态栏View
        }
        backimg = (ImageView) findViewById(R.id.id_goback);
        tv_navigaTile = (TextView) findViewById(R.id.tv_navigation);
        tv_navigaTile.setText(mPageName);
    }
    private void initWebView() {
        String SignString ="phone="+userphone+"&userid="+userId+"&key="+key;
        sign = MD5.md5Digest(SignString);
        final String data = "&userid="+ userId +"&phone=" + userphone+"&sign="+sign;
        String loginUrl= UrlUtil.Vegetable_Url+data;
        SettingWeb();
        mWebview.loadUrl(loginUrl);
        mWebview.requestFocusFromTouch();
        mWebview.setWebViewClient(new WebViewClient(){
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
                       mWebview.goBack();
                       SecondExecute=false;
                   }
               }
               super.doUpdateVisitedHistory(view, url, isReload);
           }
        });
        mWebview.setWebChromeClient(new MyWebChomeClient(VegetableGentlemen.this));
    }
    private void SettingWeb() {
        WebSettings settings=mWebview.getSettings();
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
        settings.setBuiltInZoomControls(true);
    }
    private void initEvent() {
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if(mWebview.canGoBack()){
                            mWebview.goBack();
                            return true;
                        }else {
                            finish();
                        }
                    }
                }
                return false;
            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWebview.canGoBack()){
                    mWebview.goBack();
                }else {
                    finish();
                }
            }
        });
    }
    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {}
    @Override
    public void onProgressChangeds(WebView view, int newProgress) {
        if (newProgress==100){
            progressBar.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(mWebview.canGoBack()){
                mWebview.goBack();
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
        mWebview.stopLoading();
        mWebview.removeAllViews();
        mWebview.destroy();
        mWebview = null;
    }
}
