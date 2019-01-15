package com.msht.minshengbao.androidShop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.DrawbleUtil;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;
import com.yanzhenjie.permission.Permission;
import com.zzhoujay.richtext.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class WarnMessageDetailActivity extends ShopBaseActivity implements IWarnMessageDetailView {
    @BindView(R.id.back)
    ImageView ivback;
       @BindView(R.id.webview)
       WebView webView;
    @BindView(R.id.text)
    TextView textview;
    @BindView(R.id.scl)
    ScrollView scl;
    private String id;
    private RichText richText;
    private String mobile;

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
         WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient(){
            @SuppressLint("MissingPermission")
            @Override
            public boolean shouldOverrideUrlLoading
                    (WebView view, String url) {
                //判断用户单击的是那个超连接
                String tag = "tel:";;
               if (url != null && url.contains(tag)) {
                    mobile = url.substring(url.lastIndexOf(":") + 1);
                   if (Build.VERSION.SDK_INT >= 23) {
                       PermissionUtils.requestPermissions(WarnMessageDetailActivity.this, new PermissionUtils.PermissionRequestFinishListener() {
                           @Override
                           public void onPermissionRequestSuccess(List<String> permissions) {
                               Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                               startActivity(intent);
                           }
                       }, Permission.CALL_PHONE);
                   } else {
                       Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                       startActivity(intent);
                   }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ShopPresenter.getMessageDetail(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), id);
    }
    @Override
    public void onGetDetailSuccess(String s) {
        try {
            JSONObject json = new JSONObject(s);
            String htmlText = json.optJSONObject("data").optString("htmltext");
            /*     webView.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);*/
            if (TextUtils.isEmpty(htmlText) || "null".equals(htmlText)) {
                scl.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                textview.setText(json.optJSONObject("data").optString("content"));
            } else {
                scl.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
               /* RichText.initCacheDir(this);
                richText = RichText.from(htmlText).into(textview);*/
                webView.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(richText!=null) {
            richText.clear();
            richText = null;
        }
    }

    /*private int CALL_PERMISSIONS_REQUEST = 300;
    private PermissionListener listener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == CALL_PERMISSIONS_REQUEST) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            ToastUtil.ToastText(WarnMessageDetailActivity.this, "没有权限");
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }*/
}
