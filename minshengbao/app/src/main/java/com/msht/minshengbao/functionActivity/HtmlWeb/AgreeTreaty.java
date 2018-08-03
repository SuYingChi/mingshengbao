package com.msht.minshengbao.functionActivity.HtmlWeb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 相关协议书说明
 * @author hong
 * @date 2016/7/2  
 */
public class AgreeTreaty extends BaseActivity {
    private String myUrl = UrlUtil.AgreeTreayt_Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_treayt);
        Intent data=getIntent();
        myUrl=data.getStringExtra("url");
        String mNavigation=data.getStringExtra("navigation");
        setCommonHeader(mNavigation);
        initWeBView();
    }
    private void initWeBView() {
        WebView webTreaty=(WebView)findViewById(R.id.id_web_treaty);
        webTreaty.loadUrl(myUrl);
        WebSettings settings= webTreaty.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        webTreaty.requestFocusFromTouch();
        webTreaty.setWebViewClient(new WebViewClient() {
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
