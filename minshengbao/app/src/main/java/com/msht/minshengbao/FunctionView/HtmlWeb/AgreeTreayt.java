package com.msht.minshengbao.FunctionView.HtmlWeb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;

public class AgreeTreayt extends AppCompatActivity {
    private WebView Webtreaty;
    private String  idNo;
    private static String my_url= UrlUtil.AgreeTreayt_Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_treayt);
        StatusBarCompat.setStatusBar(this);
        Webtreaty=(WebView)findViewById(R.id.id_web_treaty);
        TextView navigation=(TextView)findViewById(R.id.tv_navigation);
        findViewById(R.id.id_tv_rightText).setVisibility(View.GONE);
        Intent data=getIntent();
        idNo=data.getStringExtra("idNo");
        if (idNo.equals("0")){
            my_url= UrlUtil.AgreeTreayt_Url;
            navigation.setText("协议书");
        }else if (idNo.equals("1")){
            navigation.setText("投保须知");
            my_url="http://msbapp.cn/insurance/toubaoxuzhi.html";
        }else if (idNo.equals("2")){
            navigation.setText("投保声明与授权");
            my_url="http://msbapp.cn/insurance/toubaoshengming.html";
        }else if (idNo.equals("3")){
            navigation.setText("人身意外伤害保险条例");
            my_url="http://msbapp.cn/insurance/baoxiantiaokuan.html";
        }else if (idNo.equals("4")){
            navigation.setText("发票说明");
            my_url=UrlUtil.Invoice_explain;
        }
        initWeBView();
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initWeBView() {
        Webtreaty.loadUrl(my_url);
        WebSettings settings= Webtreaty.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        Webtreaty.requestFocusFromTouch();
        Webtreaty.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}
