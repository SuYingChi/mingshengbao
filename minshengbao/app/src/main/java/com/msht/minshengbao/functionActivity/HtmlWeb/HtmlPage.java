package com.msht.minshengbao.FunctionActivity.HtmlWeb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;

public class HtmlPage extends BaseActivity implements MyWebChomeClient.OpenFileChooserCallBack {
    private VerticalSwipeRefreshLayout mRefresh;
    private WebView   mWebview;
    private View      headLayout;
    private String    mUrl, mNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_page);
        Intent data=getIntent();
        mUrl =data.getStringExtra("url");
        mNavigation =data.getStringExtra("navigate");
        mPageName= mNavigation;
        initHeader();
        initWeBView();
        initEvent();
    }

    private void initHeader() {
        backImg = (ImageView) findViewById(R.id.id_goback);
        tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        tvNavigationTile.setText(mNavigation);
        backImg.setOnClickListener(new View.OnClickListener() {
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
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeBView() {
        mRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_vertical_refresh);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        headLayout =findViewById(R.id.id_re_second);
        mWebview=(WebView)findViewById(R.id.id_web_html);
        mWebview.loadUrl(mUrl);
        WebSettings settings= mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        //settings.setBuiltInZoomControls(true);
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

        mWebview.setWebChromeClient(new MyWebChomeClient(HtmlPage.this));
    }
    private void initEvent() {

       mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               mWebview.loadUrl(mUrl);
           }
       });
        mRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return mWebview.getScrollY()>0;
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
            mRefresh.setRefreshing(false);
        }
    }
    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
