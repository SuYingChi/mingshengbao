package com.msht.minshengbao.functionActivity.invoiceModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.AbstractCallBackUtil;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.MathUtil;
import com.msht.minshengbao.custom.Dialog.DonutProgressDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/6/17  
 */

public class AndroidPdfViewActivity extends BaseActivity {
    private WebView mWebView;
    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()+
                    "/MsbApp/Msbdownloads/";
    private DonutProgressDialog donutProgressDialog;
    private String fileUrl = "https:\\/\\/yesfp.yonyoucloud.com\\/output-tax\\/s\\/downloadPdf?pwd=N09G&authCode=958b224a54d855f1f0151ba96f7390d4";
    private static final String PDF_HTML = "file:///android_asset/pdf.html";
    private ProgressBar pro;
    public static final int LOAD_JAVASCRIPT = 0X01;
    private final MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        private WeakReference<AndroidPdfViewActivity> mWeakReference;
        private MyHandler (AndroidPdfViewActivity activity ) {
            mWeakReference=new WeakReference<AndroidPdfViewActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final  AndroidPdfViewActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            String javaScript = "javascript: getpdf2('"+ activity.fileUrl +"')";
            activity.mWebView.loadUrl(javaScript);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pdf_view);
        context=this;
        mPageName="电子发票";
        Intent data=getIntent();
        fileUrl =data.getStringExtra("url");
        donutProgressDialog=new DonutProgressDialog(this,"正在保存到相册");
        setCommonHeader(mPageName);
        initHeader();
        initWebView();
    }
    private void initHeader() {
        TextView tvRightText=(TextView)findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("保存");
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestLimit();
            }
        });
    }
    private void onRequestLimit() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                downLoadPdf();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许权限通过，部分功能将无法使用");
                            }
                        }).start();
            }else {
                downLoadPdf();
            }
        } else {
            downLoadPdf();
        }
    }
    private void downLoadPdf() {
        donutProgressDialog.show();
        String fileName=DateUtils.getCurTimeLong()+"pdf.pdf";
        OkHttpRequestManager.getInstance(getApplicationContext()).okHttpDownloadFile(fileUrl, new AbstractCallBackUtil.CallBackFile(DOWNLOAD_PATH,fileName) {
            @Override
            public void onFailure(Call call, Exception e) {
                if (donutProgressDialog!=null&&donutProgressDialog.isShowing()){
                    donutProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(File response) {
                if (donutProgressDialog!=null&&donutProgressDialog.isShowing()){
                    donutProgressDialog.dismiss();
                }
                onSaveFile(response);
            }
            @Override
            public void onProgress(float progress, long total) {
                donutProgressDialog.setDonutProgress(MathUtil.getFloatDecimal(progress,1));
            }
        });
    }
    private void onSaveFile(File response) {
        FileUtil.saveImageToGallery(getApplicationContext(),BitmapUtil.pdfToBitmap(getApplicationContext(),response));
        CustomToast.showSuccessLong("pdf文件保存在:"+DOWNLOAD_PATH);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.id_pdf_view);
        pro = (ProgressBar) findViewById(R.id.pro);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.addJavascriptInterface(new JsObject(this, fileUrl), "client");
        mWebView.loadUrl(PDF_HTML);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pro.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                myHandler.sendEmptyMessage(LOAD_JAVASCRIPT);
                ToastUtil.ToastText(context,"请稍后...");
            }
        });
    }
    class JsObject {
        Activity mActivity;
        String url ;
        public JsObject(Activity activity,String url) {
            mActivity = activity;
            this.url= url;
        }
        //    测试方法
        @JavascriptInterface
        public String dismissProgress() {
            pro.setVisibility(View.GONE);
            return this.url;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (donutProgressDialog!=null&&donutProgressDialog.isShowing()){
            donutProgressDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);

    }
}
