package com.msht.minshengbao.FunctionActivity.HtmlWeb;

import android.Manifest;
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
import com.msht.minshengbao.FunctionActivity.Public.PublicPayway;
import com.msht.minshengbao.MyAPI.MyWebChomeClient;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ImageUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class ShopActivity extends AppCompatActivity implements MyWebChomeClient.OpenFileChooserCallBack {
    private View Rnavigation;
    private WebView Shopweb;
    private ProgressBar progressBar;
    private String username="",password="";
    private String client="wap";
    private String shopCookie;
    private String  urls=null;
    private boolean lstate;
    private boolean shopstate;
    private int First=0;
    private Context mContext;
    private final String mPageName ="商城";
   // private String loginUrl="http://shop.msbapp.cn/mobile/index.php?m=default&c=user&a=login";
   // private String targeUrl="http://shop.msbapp.cn/mobile/index.php?m=default&c=index&a=index";
    private String loginUrl=UrlUtil.Shop_Login;
    private String targeUrl=UrlUtil.Shop_HomeUrl;
    private String loginHtml=UrlUtil.Shop_LoginHtml;
    private byte[] bytes;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMessage;
    public  ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 3;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int PAY_CODE=4;
    private static final int MY_CAMERA_REQUEST=2;
    private static String ImagrfileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mContext=this;
        username = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName, "");
        password = SharedPreferencesUtil.getpassw(this, SharedPreferencesUtil.passw, "");
        lstate= SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        shopstate=SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Shopstate, false);
        shopCookie= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.shopCookie, "");
        Intent data=getIntent();
        urls=data.getStringExtra("url");
        First=data.getIntExtra("first",0);
        ((TextView)findViewById(R.id.tv_navigation)).setText("商城");
        Rnavigation=findViewById(R.id.id_header) ;
        Shopweb=(WebView)findViewById(R.id.id_shangcheng_webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        initweb();
        initEvent();
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initweb() {
        final String data = "username="+ username + "&password=" + password+"&client="+client;
        try{
            bytes=data.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        SettingWeb();
        if (urls!=null){
            if (shopstate) {
                Shopweb.loadUrl(urls);
            }else {
                if (lstate){
                    Shopweb.postUrl(loginUrl,bytes);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Shopstate,true);
                }else {
                    Shopweb.loadUrl(urls);
                    First=0;
                }
            }
        }else {
            if (lstate) {
                if (shopstate){
                    Shopweb.loadUrl(targeUrl);
                }else {
                    Shopweb.postUrl(loginUrl, bytes);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Shopstate,true);
                    Shopweb.reload();
                }
            } else {
                Shopweb.loadUrl(targeUrl);
            }
        }
        Shopweb.requestFocusFromTouch();
        Shopweb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.reload();
                if (url!=null&&url.contains(loginHtml)){
                    if (!lstate){
                        Intent intent =new Intent(ShopActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        view.postUrl(loginUrl,bytes);
                        SharedPreferencesUtil.putLstate(mContext,SharedPreferencesUtil.Shopstate,true);
                    }
                }else if (url.contains("op=msbapppay")){
                    PingPay(url);
                }else {
                    if (First==1){
                        if (!shopstate){
                            view.loadUrl(urls);
                            First=0;
                            Rnavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(url);
                        }
                    }else {
                        if (url.contains(targeUrl)){
                            view.loadUrl(url);
                            Rnavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(url);
                            Rnavigation.setVisibility(View.GONE);
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String UrlString="";
                view.reload();
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    UrlString=request.getUrl().toString();
                }else {
                    UrlString=request.toString();
                }
                if (UrlString!=null&&UrlString.contains(loginHtml)){
                    if (!lstate){
                        Intent intent =new Intent(ShopActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        view.postUrl(loginUrl,bytes);
                        SharedPreferencesUtil.putLstate(mContext,SharedPreferencesUtil.Shopstate,true);
                    }
                }else {
                    if (First==1){
                        if (!shopstate){
                            view.loadUrl(urls);
                            First=0;
                            Rnavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(UrlString);
                        }
                    }else {
                        if (UrlString.contains(targeUrl)){
                            view.loadUrl(UrlString);
                            Rnavigation.setVisibility(View.VISIBLE);
                        }else {
                            view.loadUrl(UrlString);
                            Rnavigation.setVisibility(View.GONE);
                        }
                    }
                }
                return true;
            }*/
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                if (url.contains(targeUrl)){
                    Rnavigation.setVisibility(View.VISIBLE);
                }else {
                    if (urls!=null){
                        Rnavigation.setVisibility(View.VISIBLE);
                    }else {
                        Rnavigation.setVisibility(View.GONE);
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
        Shopweb.setWebChromeClient(new MyWebChomeClient(ShopActivity.this));
    }
    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    private void PingPay(String shopurl) {
        String pay_amount=Uri.parse(shopurl).getQueryParameter("pay_amount");
        String pay_url=replace(shopurl,"op","pay_new");
        Intent pay=new Intent(mContext, PublicPayway.class);
        pay.putExtra("amount",pay_amount);
        pay.putExtra("url",pay_url);
        startActivityForResult(pay,PAY_CODE);
    }
    private String replace(String shopurl, String key, String value) {
        if (!TextUtils.isEmpty(shopurl)&&!TextUtils.isEmpty(key)){
            shopurl=shopurl.replaceAll("("+key+"=[^&]*)",key+"="+value);
        }
        return shopurl;
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
                                        SelectPicture();
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
                                SelectPicture();
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                               if (isPermission(1)){
                                   try {
                                       TakePicture();
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
                                TakePicture();
                            }
                        }
                    }
                }
        );
        alertDialog.show();
    }
    private void TakePicture() {
        ImagrfileName=System.currentTimeMillis() + ".jpg";
        mSourceIntent = ImageUtil.takeBigPicture(mContext,ImagrfileName);
        startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
    }
    private void SelectPicture() {
        mSourceIntent = ImageUtil.choosePicture();
        startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
    }

    private void requestPermissionsAndroidM(final int limit) {
        MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted(int Code) {
                if (Code==MY_CAMERA_REQUEST){
                    if (limit==0){
                        SelectPicture();
                    }else {
                        TakePicture();
                    }
                }
            }
            @Override
            public void onPermissionDenied(int Code) {
                restoreUploadMsg();
                Toast.makeText(mContext,"没有权限您将无法进行扫描操作！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isPermission(int limit) {
        if (limit==0){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                return false;
            }else {
                return true;
            }
        }else {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                return false;
            }else {
                return true;
            }
        }
    }

    private void SettingWeb() {
        WebSettings settings=Shopweb.getSettings();
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
        settings.setBuiltInZoomControls(true);
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
        Shopweb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if(Shopweb.canGoBack()){
                            Shopweb.goBack();
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
    @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PAY_CODE){
            if (resultCode==PAY_CODE){
                Shopweb.loadUrl(UrlUtil.Shop_OrderList);
            }
        }
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            if (mUploadCallbackAboveL != null) {         // for android 5.0+
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
                        if (mUploadCallbackAboveL == null) {        // for android 5.0+
                            Toast.makeText(mContext,"空的",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            String filepath=ImageUtil.getExistPath()+ImagrfileName;
                            File photoFile=new File(filepath);
                            if (photoFile!=null){
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
            case REQUEST_CODE_PICK_IMAGE: {
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
                        if (mUploadCallbackAboveL == null) {        // for android 5.0+
                            Toast.makeText(mContext,"空的",Toast.LENGTH_SHORT).show();
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
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {

        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
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
        }else {

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  unregisterReceiver(broadcastReceiver);
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
