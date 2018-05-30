package com.msht.minshengbao.FunctionActivity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;


/**
 * A simple {@link Fragment} subclass.
 */
public class CounterFrag extends Fragment implements View.OnClickListener {
    private WebView Webcounter;
    private TextView SiteNotices;
    private static final String my_url= UrlUtil.Guitai_Url;
    private final String mPageName ="燃气介绍";
    public CounterFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_counter, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initweb();
        super.onActivityCreated(savedInstanceState);

    }
    private void initweb() {
        Webcounter=(WebView)getView().findViewById(R.id.id_web_counter);
        SiteNotices=(TextView)getView().findViewById(R.id.id_tv_counter);
        Webcounter.loadUrl(my_url);
        WebSettings settings=Webcounter.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        Webcounter.requestFocusFromTouch();
        Webcounter.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {}
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
