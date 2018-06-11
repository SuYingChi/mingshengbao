package com.msht.minshengbao.FunctionActivity.insurance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;

public class InsurancePay extends BaseActivity {
    private WebView insurance;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_pay);
        context=this;
        setCommonHeader("保险支付");
        Intent data=getIntent();
        final String insurance_data=data.getStringExtra("params");
       // String  url=data.getStringExtra("url");
        String url= UrlUtil.INSURANCE_PAY_URL+"?payType=0";
        String insurance_url=url+"&"+insurance_data;
        Log.d("insurance_url",insurance_url);
        insurance=(WebView)findViewById(R.id.id_Wview_insurancce);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        insurance.loadUrl(insurance_url);
        insurance.reload();
        WebSettings settings=insurance.getSettings();
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
        insurance.requestFocusFromTouch();
        insurance.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("保险",url);
                if (url.startsWith("weixin://wap/pay?")){
                    try{
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }catch (ActivityNotFoundException e){
                        Toast.makeText(context,"请安装微信最新版本" , Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else {
                   // view.loadUrl(url);
                    return false;
                }
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
