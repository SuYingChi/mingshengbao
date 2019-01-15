package com.msht.minshengbao.androidShop.Fragment;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;

import butterknife.BindView;

public class DetailFragment extends ShopBaseLazyFragment{

    private String goodsid;
    private TypedArray actionbarSizeTypedArray;
    private int toolbarHeight;
    @BindView(R.id.web)
    WebView webView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            goodsid = arg.getString("goodsid");
        }
        actionbarSizeTypedArray = getContext().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        toolbarHeight = (int) (actionbarSizeTypedArray.getDimension(0, 0) * 2+ ImmersionBar.getStatusBarHeight(getActivity()));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.shop_webview_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup.LayoutParams params  = webView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        marginParams.setMargins(0,toolbarHeight,0,0);
        webView.setLayoutParams(marginParams);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onError("网页加载错误，请稍后重试");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        webView.loadUrl(ShopConstants.GOODS_DETAIL_HTML+ goodsid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionbarSizeTypedArray.recycle();
    }
}
