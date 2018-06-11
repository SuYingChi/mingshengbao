package com.msht.minshengbao.MyAPI;

import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Demo class
 *
 * @author hong
 * @date 2017/08/20
 */
public class MyWebChomeClient extends WebChromeClient {

    private OpenFileChooserCallBack mOpenFileChooserCallBack;
    public MyWebChomeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mOpenFileChooserCallBack.onProgressChangeds(view,newProgress);
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
                                     FileChooserParams fileChooserParams) {
        return mOpenFileChooserCallBack.openFileChooserCallBackAndroid5(webView, filePathCallback, fileChooserParams);
    }

    public interface OpenFileChooserCallBack {
        /**
         *
         * @param uploadMsg
         * @param acceptType
         */
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);
        void onProgressChangeds(WebView view, int newProgress);

        /**
         *
         * @param webView
         * @param filePathCallback
         * @param fileChooserParams
         * @return
         */
        boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                                FileChooserParams fileChooserParams);
    }
}
