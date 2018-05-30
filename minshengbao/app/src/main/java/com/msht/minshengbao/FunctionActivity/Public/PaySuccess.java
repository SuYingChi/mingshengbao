package com.msht.minshengbao.FunctionActivity.Public;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;

public class PaySuccess extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGHT=10000;
    private WebView  successPage;
    private String Url=UrlUtil.ApppaySuccess_Page;
    private static final String btnUrl="http://get/event/activityBtn";
    private String activityUrl="";
    private String userId="";
    private String orderId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        context=this;
        mPageName="支付结果";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        final String type=getIntent().getStringExtra("type");
        activityUrl=getIntent().getStringExtra("url");
        orderId=getIntent().getStringExtra("orderId");
        ImageView payImage=(ImageView)findViewById(R.id.id_pay_img);
        TextView tv_notice=(TextView)findViewById(R.id.id_pay_text);
        TextView tv_naviga=(TextView)findViewById(R.id.tv_navigation);
        Button btn_know=(Button)findViewById(R.id.id_btn_know) ;
        View layout_main=findViewById(R.id.id_failure_layout);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        if (type.equals("0")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=gas_pay_success";
            tv_naviga.setText("燃气缴费");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }else if (type.equals("1")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=repair_pay_success"+"&event_relate_id="+orderId;
            tv_naviga.setText("维修支付");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }else if (type.equals("2")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=iccard_pay_success";
            tv_naviga.setText("Ic卡充值");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }else if (type.equals("3")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=wallet_pay_success";
            tv_naviga.setText("钱包充值");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }else if (type.equals("4")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=water_wallet_pay_success";
            tv_naviga.setText("水宝充值");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }else if (type.equals("5")){
            payImage.setImageResource(R.drawable.payfailure_3xh);
            tv_naviga.setText("支付结果");
            tv_notice.setText("支付失败");
            btn_know.setText("重新支付");
            layout_main.setVisibility(View.VISIBLE);
            successPage.setVisibility(View.GONE);
        }else if (type.equals("6")){
            Url=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=shop_pay_success";
            tv_naviga.setText("商城支付");
            initWebView();
            layout_main.setVisibility(View.GONE);
        }
        btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initEvent();
    }
    private void initWebView() {
        successPage=(WebView)findViewById(R.id.id_success_page);
        successPage.loadUrl(Url);
        WebSettings settings= successPage.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        successPage.requestFocusFromTouch();
        successPage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(btnUrl)){
                    if (activityUrl!=null&&(!activityUrl.equals(""))){
                        startAction();
                    }else {
                        finish();
                    }
                }else {
                    view.loadUrl(url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
    private void startAction() {
        Intent success=new Intent(context,HtmlPage.class);
        success.putExtra("navigate","活动");
        success.putExtra("url",activityUrl);
        startActivity(success);
        finish();
    }
    private void initEvent() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                PaySuccess.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
