package com.msht.minshengbao.FunctionView.HtmlWeb;

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

public class PriceMenu extends BaseActivity {
    private WebView priceview;
    private String url;
    private String workid;
    private static final String head= UrlUtil.URL_HEADS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_menu);
        context=this;
        setCommonHeader("参考价格");
        Intent data=getIntent();
        workid=data.getStringExtra("reid");
        initview();
        initWeBView();
    }
    private void initview() {
        priceview=(WebView)findViewById(R.id.id_webview);
        if (workid.equals("5")){
            url=head+"/repair_h5/weiyu_shuiguan.html";
        }else if (workid.equals("6")){
            url=head+"/repair_h5/weiyu_shuilongtou.html";
        }
        else if (workid.equals("7")){
            url=head+"/repair_h5/weiyu_huasa.html";
        }else if (workid.equals("8")){
            url=head+"/repair_h5/weiyu_matong.html";
        }else if (workid.equals("9")){
            url=head+"/repair_h5/weiyu_yushigui.html";
        }else if (workid.equals("10")){
            url=head+"/repair_h5/jiadian_ranqizao.html";
        }else if (workid.equals("11")){
            url=head+"/repair_h5/jiadian_reshuiqi.html";
        }else if (workid.equals("12")){
            url=head+"/repair_h5/jiadian_youyanji.html";
        }else if (workid.equals("13")){
            url=head+"/repair_h5/jiadian_xiaodugui.html";
        }else if (workid.equals("14")){
            url=head+"/repair_h5/jiadian_diannao.html";
        }else if (workid.equals("15")){
            url=head+"/repair_h5/jiadian_kongtiao.html";
        }else if (workid.equals("16")){
            url=head+"/repair_h5/jiadian_xiyiji.html";
        }else if (workid.equals("17")){
            url=head+"/repair_h5/jiadian_bingxiang.html";
        }else if (workid.equals("18")){
            url=head+"/repair_h5/dengju_dengju.html";
        }else if (workid.equals("19")){
            url=head+"/repair_h5/dengju_kaiguanchazuo.html";
        }else if (workid.equals("20")){
            url=head+"/repair_h5/dengju_dianlu.html";
        }else if (workid.equals("21")){
            url=head+"/repair_h5/other_kaisuohuansuo.html";
        }else if (workid.equals("22")){
            url=head+"/repair_h5/other_guandaoshutong.html";
        }else if (workid.equals("24")){
            url=head+"/repair_h5/other_qiangmiandakong.html";
        }else if (workid.equals("25")){
            url=head+"/repair_h5/other_jiaju.html";
        }else if (workid.equals("26")){
            url=head+"/repair_h5/other_men.html";
        }else if (workid.equals("27")){
            url=head+"/repair_h5/other_chuang.html";
        }else if (workid.equals("28")){
            url=head+"/repair_h5/other_yijiawujin.html";
        }else if (workid.equals("29")){
            url=head+"/repair_h5/other_fangdaowang.html";
        }else if (workid.equals("30")){
            url=head+"/repair_h5/qingxi_ranqizao.html";
        }else if (workid.equals("31")){
            url=head+"/repair_h5/qingxi_reshuiqi.html";
        }else if (workid.equals("32")){
            url=head+"/repair_h5/qingxi_youyanji.html";
        }else if (workid.equals("33")){
            url=head+"/repair_h5/qingxi_kongtiao.html";
        }else if (workid.equals("34")){
            url=head+"/repair_h5/qingxi_bingxiang.html";
        }else if (workid.equals("35")){
            url=head+"/repair_h5/qingxi_xiyiji.html";
        }
    }
    private void initWeBView() {
        priceview.loadUrl(url);
        WebSettings settings = priceview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        priceview.requestFocusFromTouch();
        priceview.setWebViewClient(new WebViewClient() {
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
