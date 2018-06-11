package com.msht.minshengbao.FunctionActivity.HtmlWeb;

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

public class AgreeTreayt extends BaseActivity {
    private WebView Webtreaty;
    private String  idNo;
    private static String my_url= UrlUtil.AgreeTreayt_Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_treayt);
        Intent data=getIntent();
        idNo=data.getStringExtra("idNo");
        if (idNo.equals("0")){
            my_url= UrlUtil.AgreeTreayt_Url;
            setCommonHeader("协议书");
        }else if (idNo.equals("1")){
            setCommonHeader("投保须知");
            my_url="http://msbapp.cn/insurance/toubaoxuzhi.html";
        }else if (idNo.equals("2")){
            setCommonHeader("投保声明与授权");
            my_url="http://msbapp.cn/insurance/toubaoshengming.html";
        }else if (idNo.equals("3")){
            setCommonHeader("人身意外伤害保险条例");
            my_url="http://msbapp.cn/insurance/baoxiantiaokuan.html";
        }else if (idNo.equals("4")){
            setCommonHeader("发票说明");
            my_url=UrlUtil.Invoice_explain;
        }else if (idNo.equals("5")){
            setCommonHeader("充返活动说明");
            my_url=UrlUtil.Recharge_BackAgree;
        }else if (idNo.equals("6")){
            setCommonHeader("保险说明");
            my_url=UrlUtil.INSURANCE_EXPLAIN_URL;
        }
        initWeBView();
    }
    private void initWeBView() {
        Webtreaty=(WebView)findViewById(R.id.id_web_treaty);
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
