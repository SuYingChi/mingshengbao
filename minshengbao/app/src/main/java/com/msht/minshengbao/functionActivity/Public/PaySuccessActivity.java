package com.msht.minshengbao.functionActivity.Public;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.PrizesGiftsActivity;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class PaySuccessActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_LENGHT=10000;
    private WebView  successPage;
    private String successUrl=UrlUtil.APP_PAY_SUCCESS_PAGE;
    private static final String BTN_URL ="http://get/event/activityBtn";
    private String activityUrl="";
    private String activityCode="";
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        context=this;
        mPageName="支付结果";
        String type="";
        String mNavigation="支付结果";
        String userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        userPhone =SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent data=getIntent();
        if (data!=null){
            type=getIntent().getStringExtra("type");
            mNavigation=getIntent().getStringExtra("navigation");
            activityUrl=getIntent().getStringExtra("url");
            successUrl=getIntent().getStringExtra("pageUrl");
        }
        if(TextUtils.isEmpty(successUrl)){
            activityCode=Uri.parse(successUrl).getQueryParameter("event_code");
        }
        ImageView payImage=(ImageView)findViewById(R.id.id_pay_img);
        TextView tvNotice=(TextView)findViewById(R.id.id_pay_text);
        TextView tvNavigation=(TextView)findViewById(R.id.tv_navigation);
        TextView tvRightText=(TextView)findViewById(R.id.id_tv_rightText);
        tvRightText.setText("完成");
        Button btnKnow=(Button)findViewById(R.id.id_btn_know) ;
        View layoutMain=findViewById(R.id.id_failure_layout);
        successPage=(WebView)findViewById(R.id.id_success_page);
        tvNavigation.setText(mNavigation);
        if (successUrl.contains(UrlUtil.WATER_RECHARGE_SUCCESS_PAGE)){
            tvRightText.setVisibility(View.VISIBLE);
        }else {
            tvRightText.setVisibility(View.GONE);
        }
        switch (type){
            case VariableUtil.VALUE_ZERO:
                payImage.setImageResource(R.drawable.payfailure_3xh);
                tvNavigation.setText("支付结果");
                tvNotice.setText("支付失败");
                btnKnow.setText("重新支付");
                layoutMain.setVisibility(View.VISIBLE);
                successPage.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_ONE:
                initWebView();
                layoutMain.setVisibility(View.GONE);
                successPage.setVisibility(View.VISIBLE);
                break;
                default:
                    initWebView();
                    layoutMain.setVisibility(View.GONE);
                    successPage.setVisibility(View.VISIBLE);
                    break;

        }
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (successUrl.contains(UrlUtil.WATER_RECHARGE_SUCCESS_PAGE)){
                    onGetGiftPrize();
                }
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings= successPage.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
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
                if (url.equals(BTN_URL)){
                    if (activityUrl!=null&&(!TextUtils.isEmpty(activityUrl))){
                        startAction();
                    }else {
                        finish();
                    }
                }else if (url.contains(UrlUtil.WATER_PRIZES_GIFTS)){
                   onGetGiftPrize();
                   return true;
                }else {
                    AppActivityUtil.onAppActivityType(context,url,"民生宝","0","",activityCode,"");
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
    }
    private void onGetGiftPrize() {
        String url=UrlUtil.WATER_PRIZES_GIFTS+"?phone="+userPhone;
        Intent intent=new Intent(context, PrizesGiftsActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","领取礼品");
        intent.putExtra("flag",0);
        startActivity(intent);
        finish();
    }

    private class MyWebChromeClient extends WebChromeClient {
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
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

    }
    private void startAction() {
        Intent success=new Intent(context,HtmlPageActivity.class);
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
