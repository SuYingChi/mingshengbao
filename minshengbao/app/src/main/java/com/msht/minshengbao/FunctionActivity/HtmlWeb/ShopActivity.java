package com.msht.minshengbao.FunctionActivity.HtmlWeb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.FunctionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.FunctionActivity.Public.PublicPayWayActivity;
import com.msht.minshengbao.MyAPI.MyWebChomeClient;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.ImageUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Demo class
 *
 * @author hong
 * @date 2016/05/20
 */
public class ShopActivity extends AppCompatActivity implements MyWebChomeClient.OpenFileChooserCallBack {
    private View layoutNavigation;
    private WebView shopWeb;
    private ProgressBar progressBar;
    private String  username="",password="";
    private String  client="wap";
    private String  shopCookie;
    private String  urls=null;
    private boolean loginState;
    private boolean shopState;
    private int First=0;
    private Context mContext;
    private final String mPageName ="商城";
    private String loginUrl=UrlUtil.Shop_Login;
    private String targetUrl=UrlUtil.Shop_HomeUrl;
    private String loginHtml=UrlUtil.Shop_LoginHtml;
    private byte[] bytes;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMessage;
    public  ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 3;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int PAY_CODE=4;
    private static final int MY_CAMERA_REQUEST=2;
    private static final String URL_VALUE="op=msbapppay";
    private static String imageFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mContext=this;
        username = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName, "");
        password = SharedPreferencesUtil.getpassw(this, SharedPreferencesUtil.passw, "");
        loginState= SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        shopState=SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Shopstate, false);
        shopCookie= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.shopCookie, "");
        Intent data=getIntent();
        urls=data.getStringExtra("url");
        First=data.getIntExtra("first",0);
        ((TextView)findViewById(R.id.tv_navigation)).setText("商城");
        layoutNavigation=findViewById(R.id.id_header) ;
        shopWeb=(WebView)findViewById(R.id.id_shangcheng_webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        initWebView();
        initEvent();
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWebView() {
        String versionName=AppPackageUtil.getPackageVersionName(mContext);
        versionName =versionName.replace("v","");
        final String data = "username="+ username + "&password=" + password+"&client="+client
                +"&version="+ versionName;
        try{
            bytes=data.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        settingWeb();
        if (urls!=null){
            if (shopState) {
                shopWeb.loadUrl(urls);
            }else {
                if (loginState){
                    shopWeb.postUrl(loginUrl,bytes);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Shopstate,true);
                }else {
                    shopWeb.loadUrl(urls);
                    First=0;
                }
            }
        }else {
            if (loginState) {
                if (shopState){
                    //shopWeb.loadUrl(targetUrl);
                    shopWeb.postUrl(loginUrl, bytes);
                    shopWeb.reload();
                }else {
                    shopWeb.postUrl(loginUrl, bytes);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Shopstate,true);
                    shopWeb.reload();
                }
            } else {
                shopWeb.loadUrl(targetUrl);
            }
        }
        shopWeb.requestFocusFromTouch();
        shopWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.reload();
                if (url!=null&&url.contains(loginHtml)){
                    if (!loginState){
                        Intent intent =new Intent(ShopActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        view.postUrl(loginUrl,bytes);
                        SharedPreferencesUtil.putLstate(mContext,SharedPreferencesUtil.Shopstate,true);
                    }
                }else if (url!=null&&url.contains(URL_VALUE)){
                    pingPay(url);
                }else {
                    if (First==1){
                        if (!shopState){
                            view.loadUrl(urls);
                            First=0;
                            layoutNavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(url);
                        }
                    }else {
                        if (url!=null&&url.contains(targetUrl)){
                            view.loadUrl(url);
                            layoutNavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(url);
                            layoutNavigation.setVisibility(View.GONE);
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                if (url.contains(targetUrl)){
                    layoutNavigation.setVisibility(View.VISIBLE);
                }else {
                    if (urls!=null){
                        layoutNavigation.setVisibility(View.VISIBLE);
                    }else {
                        layoutNavigation.setVisibility(View.GONE);
                    }
                }
                super.doUpdateVisitedHistory(view, url, isReload);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        fixDirPath();
        shopWeb.setWebChromeClient(new MyWebChomeClient(ShopActivity.this));
    }
    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    private void pingPay(String shopUrl) {
        String payAmount=Uri.parse(shopUrl).getQueryParameter("pay_amount");
        String payUrl=replace(shopUrl,"op","pay_new");
        Intent pay=new Intent(mContext, PublicPayWayActivity.class);
        pay.putExtra("amount",payAmount);
        pay.putExtra("url",payUrl);
        startActivityForResult(pay,PAY_CODE);
    }
    private String replace(String shopUrl, String key, String value) {
        if (!TextUtils.isEmpty(shopUrl)&&!TextUtils.isEmpty(key)){
            shopUrl=shopUrl.replaceAll("("+key+"=[^&]*)",key+"="+value);
        }
        return shopUrl;
    }
    @Override
    public void onProgressChangeds(WebView view, int newProgress) {
        if (newProgress==100){
            progressBar.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        showOptions();
    }
    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mUploadCallbackAboveL = filePathCallback;
        showOptions();
        return true;
    }
    private void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());
        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (isPermission(0)){
                                    try {
                                        selectPicture();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(ShopActivity.this,
                                                "请去\"设置\"中开启本应用的图片媒体访问权限",
                                                Toast.LENGTH_SHORT).show();
                                        restoreUploadMsg();
                                    }
                                }else {
                                    requestPermissionsAndroidM(0);
                                    return;
                                }
                            }else {
                                selectPicture();
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                               if (isPermission(1)){
                                   try {
                                       takePicture();
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                       Toast.makeText(ShopActivity.this,
                                               "请去\"设置\"中开启本应用的相机和图片媒体访问权限",
                                               Toast.LENGTH_SHORT).show();

                                       restoreUploadMsg();
                                   }
                               }else {
                                   requestPermissionsAndroidM(1);
                                   return;
                               }
                            }else {
                                takePicture();
                            }
                        }
                    }
                }
        );
        alertDialog.show();
    }
    private void takePicture() {
        imageFileName =System.currentTimeMillis() + ".jpg";
        mSourceIntent = ImageUtil.takeBigPicture(mContext, imageFileName);
        startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
    }
    private void selectPicture() {
        mSourceIntent = ImageUtil.choosePicture();
        startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
    }

    private void requestPermissionsAndroidM(final int limit) {
        MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted(int code) {
                if (code==MY_CAMERA_REQUEST){
                    if (limit==0){
                        selectPicture();
                    }else {
                        takePicture();
                    }
                }
            }
            @Override
            public void onPermissionDenied(int code) {
                restoreUploadMsg();
                ToastUtil.ToastText(mContext,"没有权限您将无法进行扫描操作！");
            }
        });
    }
    private boolean isPermission(int limit) {
        boolean permissionStatue;
        if (limit==0){
            permissionStatue=ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        }else {
            permissionStatue=ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        }
        return permissionStatue;
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void settingWeb() {
        WebSettings settings=shopWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setNeedInitialFocus(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
       // settings.setBuiltInZoomControls(true);
    }
    private void synCookies(String targetUrl){
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager=CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.setCookie(targetUrl,shopCookie);
        CookieSyncManager.getInstance().sync();
    }
    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }
    private void restoreUploadMsg() {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;

        } else if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(null);
            mUploadCallbackAboveL = null;
        }
    }
    private void initEvent() {
        shopWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if(shopWeb.canGoBack()){
                            shopWeb.goBack();
                            return true;
                        }
                        else {
                            finish();
                        }
                    }
                }
                return false;
            }
        });
    }
    @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PAY_CODE){
            if (resultCode==PAY_CODE){
                shopWeb.loadUrl(UrlUtil.Shop_OrderList);
            }
        }
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e("ShopActivity", "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMessage.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // for android 5.0+
                        if (mUploadCallbackAboveL == null) {
                            ToastUtil.ToastText(mContext,"空数据");
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            String filepath=ImageUtil.getExistPath()+ imageFileName;
                            File photoFile=new File(filepath);
                            if (photoFile.exists()){
                                Toast.makeText(mContext,filepath,Toast.LENGTH_SHORT).show();
                                Uri uri = Uri.fromFile(photoFile);
                                mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
                            }
                        }else {
                            String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                            if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                                Log.e("ShopActivity", "sourcePath empty or not exists.");
                                break;
                            }
                            Uri uri = Uri.fromFile(new File(sourcePath));
                            mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case REQUEST_CODE_PICK_IMAGE: 
                {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e("ShopActivity", "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMessage.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // for android 5.0+
                        if (mUploadCallbackAboveL == null) {
                            ToastUtil.ToastText(mContext,"空数据");
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e("ShopActivity", "sourcePath empty or not exists.");
                            break;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            Toast.makeText(mContext,sourcePath,Toast.LENGTH_SHORT).show();
                            File photoFile=new File(sourcePath);
                            if (photoFile!=null){
                                Uri uri = Uri.fromFile(photoFile);
                                mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
                            }
                        }else {
                            Uri uri = Uri.fromFile(new File(sourcePath));
                            mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                
            }
            default:
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {

        if (requestCode != FILE_CHOOSER_RESULT_CODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null){
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==MY_CAMERA_REQUEST){
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(shopWeb!=null){
            shopWeb.stopLoading();
            shopWeb.removeAllViews();
            shopWeb.destroy();
            shopWeb = null;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
      //  ZhugeSDK.getInstance().init(getApplicationContext());

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
}
