package com.msht.minshengbao.androidShop.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;
import com.zzhoujay.richtext.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class WarnMessageDetailActivity extends ShopBaseActivity implements IWarnMessageDetailView {
    @BindView(R.id.back)
    ImageView ivback;
 /*   @BindView(R.id.webview)
    WebView webView;*/
   @BindView(R.id.text)
   TextView textview;
    private String id;
    private RichText richText;

    @Override
    protected void setLayout() {
        setContentView(R.layout.warn_message);
    }

    @Override
    protected void initImmersionBar() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       /* WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);*/
        ShopPresenter.getMessageDetail(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), id);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onGetDetailSuccess(String s) {
        try {
            JSONObject json = new JSONObject(s);
            String htmlText = json.optJSONObject("data").optString("htmltext");
       /*     webView.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);*/
            RichText.initCacheDir(this);
            richText = RichText.from(htmlText).into(textview);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        richText.clear();
        richText = null;
    }
}
