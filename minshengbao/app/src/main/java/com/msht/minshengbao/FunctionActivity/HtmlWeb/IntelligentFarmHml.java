package com.msht.minshengbao.FunctionActivity.HtmlWeb;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.MyAPI.MyWebChomeClient;
import com.msht.minshengbao.R;

public class IntelligentFarmHml extends BaseActivity implements MyWebChomeClient.OpenFileChooserCallBack {

    private WebView mWebview;
    private String    Url,naviga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_farm_hml);
        Intent data=getIntent();
        Url=data.getStringExtra("url");
        naviga=data.getStringExtra("navigate");
        mPageName=naviga;
        initHeader();
        initWeBView();
        initEvent();
    }
    private void initHeader() {
        backimg = (ImageView) findViewById(R.id.id_goback);
        tv_navigaTile = (TextView) findViewById(R.id.tv_navigation);
        tv_navigaTile.setText(naviga);
    }
    private void initWeBView() {
        mWebview=(WebView)findViewById(R.id.id_web_html);
        mWebview.loadUrl(Url);
        WebSettings settings= mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
       // settings.setBuiltInZoomControls(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebview.requestFocusFromTouch();
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    view.loadUrl(request.getUrl().toString());
                }else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
        });

        mWebview.setWebChromeClient(new MyWebChomeClient(IntelligentFarmHml.this));
    }
    private void initEvent() {
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
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {}
    @Override
    public void onProgressChangeds(WebView view, int newProgress) {
        if (newProgress==100){

        }
    }
    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }
}
