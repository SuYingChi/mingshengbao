package com.msht.minshengbao.FunctionActivity.Public;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
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
import com.msht.minshengbao.Utils.VariableUtil;

public class PaySuccess extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGHT=10000;
    private WebView  successPage;
    private String successUrl=UrlUtil.ApppaySuccess_Page;
    private static final String btnUrl="http://get/event/activityBtn";
    private String activityUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        context=this;
        mPageName="支付结果";
        String userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        final String type=getIntent().getStringExtra("type");
        activityUrl=getIntent().getStringExtra("url");
        String orderId=getIntent().getStringExtra("orderId");
        ImageView payImage=(ImageView)findViewById(R.id.id_pay_img);
        TextView tvNotice=(TextView)findViewById(R.id.id_pay_text);
        TextView tvNavigation=(TextView)findViewById(R.id.tv_navigation);
        Button btnKnow=(Button)findViewById(R.id.id_btn_know) ;
        View layoutMain=findViewById(R.id.id_failure_layout);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        if (type.equals(VariableUtil.VALUE_ZERO)){
            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=gas_fee_pay_success"
                    +"&event_relate_id="+orderId;
            tvNavigation.setText("燃气缴费");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_ONE)){
            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=repair_pay_success"+"&event_relate_id="+orderId;
            tvNavigation.setText("维修支付");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_TWO)){
            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=iccard_pay_success";
            tvNavigation.setText("Ic卡充值");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_THREE)){

            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=wallet_recharge_pay_success"
                    +"&event_relate_id="+orderId;
            tvNavigation.setText("钱包充值");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_FOUR)){
            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=water_wallet_pay_success";
            tvNavigation.setText("水宝充值");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_FIVE)){
            payImage.setImageResource(R.drawable.payfailure_3xh);
            tvNavigation.setText("支付结果");
            tvNotice.setText("支付失败");
            btnKnow.setText("重新支付");
            layoutMain.setVisibility(View.VISIBLE);
            successPage.setVisibility(View.GONE);
        }else if (type.equals(VariableUtil.VALUE_SIX)){
            successUrl=UrlUtil.ApppaySuccess_Page+"&userId="+userId+"&event_code=shop_pay_success";
            tvNavigation.setText("商城支付");
            initWebView();
            layoutMain.setVisibility(View.GONE);
        }
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initEvent();
    }
    private void initWebView() {
        successPage=(WebView)findViewById(R.id.id_success_page);
        successPage.loadUrl(successUrl);
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
