package com.msht.minshengbao.functionActivity.Invoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.functionActivity.GasService.GasInstallActivity;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/6/17  
 */

public class AndroidPdfViewActivity extends BaseActivity {
    private WebView mWebView;
    private String fileUrl = "https:\\/\\/yesfp.yonyoucloud.com\\/output-tax\\/s\\/downloadPdf?pwd=N09G&authCode=958b224a54d855f1f0151ba96f7390d4";
    private static final String PDF_HTML = "file:///android_asset/pdf.html";
    private ProgressBar pro;
    public static final int LOAD_JAVASCRIPT = 0X01;
    private final MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        private WeakReference<AndroidPdfViewActivity> mWeakReference;
        private MyHandler (AndroidPdfViewActivity activity ) {
            mWeakReference=new WeakReference<AndroidPdfViewActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final  AndroidPdfViewActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            String javaScript = "javascript: getpdf2('"+ activity.fileUrl +"')";
            activity.mWebView.loadUrl(javaScript);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pdf_view);
        context=this;
        mPageName="电子发票";
        Intent data=getIntent();
        fileUrl =data.getStringExtra("url");
        setCommonHeader(mPageName);
        initWebView();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.id_pdf_view);
        pro = (ProgressBar) findViewById(R.id.pro);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.addJavascriptInterface(new JsObject(this, fileUrl), "client");
        mWebView.loadUrl(PDF_HTML);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pro.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                myHandler.sendEmptyMessage(LOAD_JAVASCRIPT);
                ToastUtil.ToastText(context,"请稍后...");
            }
        });
    }
    class JsObject {
        Activity mActivity;
        String url ;
        public JsObject(Activity activity,String url) {
            mActivity = activity;
            this.url= url;
        }
        //    测试方法
        @JavascriptInterface
        public String dismissProgress() {
            pro.setVisibility(View.GONE);
            return this.url;
        }
    }
}
