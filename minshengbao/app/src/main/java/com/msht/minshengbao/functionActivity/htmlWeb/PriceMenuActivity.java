package com.msht.minshengbao.functionActivity.htmlWeb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class PriceMenuActivity extends BaseActivity {
    private WebView mPriceView;
    private String url;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_menu);
        context=this;
        mPageName="参考价格";
        setCommonHeader(mPageName);
        Intent data=getIntent();
        code =data.getStringExtra("code");
        if (TextUtils.isEmpty(code)){
            code="";
        }
        initViewUrl();
        initWeBView();
    }
    private void initViewUrl() {
        mPriceView =(WebView)findViewById(R.id.id_webview);
        switch (code){
            case ConstantUtil.WATER_PIPE:
                url=BuildConfig.URL_HEADS+"/repair_h5/weiyu_shuiguan.html";
                break;
            case ConstantUtil.WATER_TAP:
                url=BuildConfig.URL_HEADS+"/repair_h5/weiyu_shuilongtou.html";
                break;
            case ConstantUtil.SHOWER:
                url=BuildConfig.URL_HEADS+"/repair_h5/weiyu_huasa.html";
                break;
            case ConstantUtil.CLOSESTOOL:
                url=BuildConfig.URL_HEADS+"/repair_h5/weiyu_matong.html";
                break;
            case ConstantUtil.BATHROOM:
                url=BuildConfig.URL_HEADS+"/repair_h5/weiyu_yushigui.html";
                break;
            case ConstantUtil.CIRCUIT:
                url=BuildConfig.URL_HEADS+"/repair_h5/dengju_dianlu.html";
                break;
            case ConstantUtil.SWITCH_SOCKET:
                url=BuildConfig.URL_HEADS+"/repair_h5/dengju_kaiguanchazuo.html";;
                break;
            case ConstantUtil.LAMP:
                url=BuildConfig.URL_HEADS+"/repair_h5/dengju_dengju.html";
                break;
            case ConstantUtil.RUSH_PIPE:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_guandaoshutong.html";
                break;
            case ConstantUtil.UNLOCK_CHANGE:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_kaisuohuansuo.html";
                break;
            case ConstantUtil.FURNITURE:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_jiaju.html";
                break;
            case ConstantUtil.DOOR:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_men.html";
                break;
            case ConstantUtil.WINDOW:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_chuang.html";
                break;
            case ConstantUtil.HANGER_HARDWARE:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_yijiawujin.html";
                break;
            case ConstantUtil.BURGLAR_MESH:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_fangdaowang.html";
                break;
            case ConstantUtil.WALL_PERFORATION:
                url=BuildConfig.URL_HEADS+"/repair_h5/other_qiangmiandakong.html";
                break;
            case ConstantUtil.AIR_CONDITIONER_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_kongtiao.html";
                break;
            case ConstantUtil.WASHING_MACHINE_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_xiyiji.html";
                break;
            case ConstantUtil.REFRIGERATOR_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_bingxiang.html";
                break;
            case ConstantUtil.HEATER_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_reshuiqi.html";
                break;
            case ConstantUtil.GAS_STOVE_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_ranqizao.html";
                break;
            case ConstantUtil.HOODS_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_youyanji.html";
                break;
            case ConstantUtil.COMPUTER_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_diannao.html";
                break;
            case ConstantUtil.STERILIZER_REPAIR:
                url=BuildConfig.URL_HEADS+"/repair_h5/jiadian_xiaodugui.html";
                break;
            case ConstantUtil.AIR_CONDITIONER_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_kongtiao.html";
                break;
            case ConstantUtil.WASHING_MACHINE_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_xiyiji.html";
                break;
            case ConstantUtil.REFRIGERATOR_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_bingxiang.html";
                break;
            case ConstantUtil.HEATER_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_reshuiqi.html";
                break;
            case ConstantUtil.HOODS_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_youyanji.html";
                break;
            case ConstantUtil.GAS_STOVE_CLEAN:
                url=BuildConfig.URL_HEADS+"/repair_h5/qingxi_ranqizao.html";
                break;
            case ConstantUtil.HOUSE_CLEANING:
                url=BuildConfig.URL_HEADS+"/repair_h5/baojie_jiating.html";
                break;
            case ConstantUtil.SOFA_CLEANLINESS:
                url=BuildConfig.URL_HEADS+"/repair_h5/baojie_shafa.html";
                break;
            case ConstantUtil.MATTRESS_MITE:
                url=BuildConfig.URL_HEADS+"/repair_h5/baojie_chuangdian.html";
                break;
                default:
                    break;
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeBView() {
        mPriceView.loadUrl(url);
        WebSettings settings = mPriceView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mPriceView.requestFocusFromTouch();
        mPriceView.setWebViewClient(new WebViewClient() {
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
