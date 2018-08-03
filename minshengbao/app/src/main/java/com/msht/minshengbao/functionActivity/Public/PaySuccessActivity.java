package com.msht.minshengbao.functionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

public class PaySuccessActivity extends BaseActivity {
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
        switch (type){
            case VariableUtil.VALUE_ZERO:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=gas_fee_pay_success"
                        +"&event_relate_id="+orderId;
                tvNavigation.setText("燃气缴费");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_ONE:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=repair_pay_success"+"&event_relate_id="+orderId;
                tvNavigation.setText("维修支付");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_TWO:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=iccard_pay_success";
                tvNavigation.setText("IC卡充值");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_THREE:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=wallet_recharge_pay_success"
                        +"&event_relate_id="+orderId;
                tvNavigation.setText("钱包充值");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_FOUR:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=water_recharge_pay_success"
                        +"&event_relate_id="+orderId;
                Log.d("水宝=",successUrl);
                tvNavigation.setText("水宝充值");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_FIVE:
                payImage.setImageResource(R.drawable.payfailure_3xh);
                tvNavigation.setText("支付结果");
                tvNotice.setText("支付失败");
                btnKnow.setText("重新支付");
                layoutMain.setVisibility(View.VISIBLE);
                successPage.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_SIX:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=shop_pay_success";
                tvNavigation.setText("商城支付");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_SEVER:
                successUrl=UrlUtil.ApppaySuccess_Page+"userId="+userId+"&event_code=lpg_order_pay_success"
                        +"&event_relate_id="+orderId;
                tvNavigation.setText("支付成功");
                initWebView();
                layoutMain.setVisibility(View.GONE);
                break;
                default:
                    break;

        }
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initWebView() {
        successPage=(WebView)findViewById(R.id.id_success_page);
        WebSettings settings= successPage.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        successPage.requestFocusFromTouch();
        successPage.setWebChromeClient(new MyWebChromeClient());
        successPage.loadUrl(successUrl);
        successPage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(btnUrl)){
                    if (activityUrl!=null&&(!TextUtils.isEmpty(activityUrl))){
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
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            new PromptDialog.Builder(context)
                    .setTitle(R.string.my_dialog_title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(message)
                    .setButton1("我知道了", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            result.cancel();
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
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
                PaySuccessActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
