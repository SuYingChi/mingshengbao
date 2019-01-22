package com.msht.minshengbao.androidShop.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.MyAPI.MyWebChromeClient;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;

import butterknife.BindView;

public class ShopkefuActivity extends ShopBaseActivity implements MyWebChromeClient.OpenFileChooserCallBack, IWarnMessageDetailView {
    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView back;
    private String imChatUrl;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_kefu_layout);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true);
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String t_id = getIntent().getStringExtra("t_id");
      //  ShopPresenter.getMessageDetail(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), t_id);
        imChatUrl = ShopConstants.getImChatUrl(t_id, getKey());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onError("网页加载错误，请稍后重试");
            }

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
        webView.setWebChromeClient(new MyWebChromeClient(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl(imChatUrl);
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

    }


    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    public void onGetDetailSuccess(String s) {

    }
}
