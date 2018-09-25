package com.msht.minshengbao.MyAPI;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Demo class
 *
 * @author hong
 * @date 2017/08/20
 */
public class MyWebChromeClient extends WebChromeClient {

    private OpenFileChooserCallBack mOpenFileChooserCallBack;
    public MyWebChromeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mOpenFileChooserCallBack.onProgressChanged(view,newProgress);
    }
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     MyWebChromeClient.FileChooserParams fileChooserParams) {
        return mOpenFileChooserCallBack.openFileChooserCallBackAndroid5(webView, filePathCallback, fileChooserParams);
    }

    public interface OpenFileChooserCallBack {
        /**
         *文件
         * @param uploadMsg
         * @param acceptType
         */
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);

        /**
         * 文件
         * @param view
         * @param newProgress
         */
        void onProgressChanged(WebView view, int newProgress);

        /**
         *文件
         * @param webView
         * @param filePathCallback
         * @param fileChooserParams
         * @return
         */
        boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                                MyWebChromeClient.FileChooserParams fileChooserParams);
    }
}
