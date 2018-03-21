package com.msht.minshengbao.FunctionView.HtmlWeb;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;

public class ReplacePayAgree extends BaseActivity {
    private Button btn_agree,btn_refuse;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_pay_agree);
        context=this;
        setCommonHeader("燃气代缴协议");
        initWebView();
        initEvent();
    }
    private void initEvent() {
        btn_refuse=(Button)findViewById(R.id.id_btn_refuse);
        btn_agree=(Button)findViewById(R.id.id_btn_agree);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });
    }
    private void initWebView() {
        String my_url= UrlUtil.Replacepay_agreeUrl;
        mWebView=(WebView)findViewById(R.id.id_webview);
        mWebView.loadUrl(my_url);
        WebSettings settings= mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    view.loadUrl(request.getUrl().toString());
                }else {
                    view.loadUrl(request.toString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}
