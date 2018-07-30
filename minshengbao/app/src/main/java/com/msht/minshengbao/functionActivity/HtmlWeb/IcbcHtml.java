package com.msht.minshengbao.FunctionActivity.HtmlWeb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;

public class IcbcHtml extends BaseActivity {

    private WebView mWebview;
    private View Head_layout;
    private String    Url,naviga;
    private String    ICBC_Url="https://mims.icbc.com.cn/IMServiceServer/servlet/ICBCBaseReqNSServlet?dse_operationName=ApplyCreditCardOp&coreCode=HZDW000007461&paraPromoCode=EW0002201000000AD02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icbc_html);
        Intent data=getIntent();
        Url=data.getStringExtra("url");
        naviga=data.getStringExtra("navigate");
        setCommonHeader(naviga);
        initWeBView();
        initEvent();
    }
    private void initWeBView() {
        Head_layout=findViewById(R.id.id_re_second);
        mWebview=(WebView)findViewById(R.id.id_web_html);
        mWebview.loadUrl(Url);
        WebSettings settings= mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebview.requestFocusFromTouch();
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String UrlString="";
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    view.loadUrl(request.getUrl().toString());
                    UrlString=request.getUrl().toString();
                }else {
                    view.loadUrl(request.toString());
                    UrlString=request.toString();
                }
                if (UrlString.contains(ICBC_Url)){
                    Head_layout.setVisibility(View.VISIBLE);
                }else {
                    Head_layout.setVisibility(View.GONE);
                }
                return true;
            }
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                if (url.contains(ICBC_Url)){
                    Head_layout.setVisibility(View.VISIBLE);
                }else {
                    Head_layout.setVisibility(View.GONE);
                }
                super.doUpdateVisitedHistory(view, url, isReload);
            }
        });
    }
    private void initEvent() {
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if(mWebview.canGoBack()){
                            mWebview.goBack();
                            return true;
                        }
                        else
                        {
                            finish();
                        }
                    }
                }
                return false;
            }
        });
    }
}
