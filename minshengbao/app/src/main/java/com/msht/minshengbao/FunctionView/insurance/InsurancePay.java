package com.msht.minshengbao.FunctionView.insurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class InsurancePay extends BaseActivity {
    private WebView insurance;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_pay);
        Intent data=getIntent();
        context=this;
        setCommonHeader("保险支付");
        insurance=(WebView)findViewById(R.id.id_Wview_insurancce);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        final String insurance_data=data.getStringExtra("params");
        String url="http://msbapp.cn/Gas/views/insurance/pay.html";
        String insurance_url=url+"?"+insurance_data;
        insurance.loadUrl(insurance_url);
        insurance.reload();
        WebSettings settings=insurance.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        insurance.requestFocusFromTouch();
        insurance.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.reload();
                view.loadUrl(url);
                return true;
            }
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
            }
        });
        insurance.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
}
