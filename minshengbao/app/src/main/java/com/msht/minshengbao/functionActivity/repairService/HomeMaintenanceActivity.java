package com.msht.minshengbao.functionActivity.repairService;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.widget.MyNoScrollGridView;
import com.msht.minshengbao.adapter.HouseApplianceFixAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/13  
 */
public class HomeMaintenanceActivity extends BaseActivity {

    private WebView webView;
    private MyNoScrollGridView mGridView;
    private HouseApplianceFixAdapter homeAdapter;
    private String pid;
    private String cityId;
    private CustomDialog customDialog;
    private static final String SERVICE_URL= UrlUtil.Service_noteUrl;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandle=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<HomeMaintenanceActivity> mWeakReference;
        private RequestHandler(HomeMaintenanceActivity activity) {
            mWeakReference = new WeakReference<HomeMaintenanceActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeMaintenanceActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        JSONArray json =object.getJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.initFunction(json);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);

        }
    }
    private void initFunction(JSONArray json) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                functionList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (functionList.size()!=0){
            homeAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);
        mPageName="家居维修";
        setCommonHeader(mPageName);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        String typeName=data.getStringExtra("typeName");
        setCommonHeader(typeName);
        cityId=VariableUtil.cityId;
        initView();
        homeAdapter=new HouseApplianceFixAdapter(context,functionList);
        mGridView.setAdapter(homeAdapter);
        initData();
        iniWebView();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mId=functionList.get(i).get("id");
                String mName=functionList.get(i).get("name");
                String code=functionList.get(i).get("code");
                AppActivityUtil.startActivityCode(context,code,mId,mName,"0");
            }
        });
    }
    private void initView() {
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_function_view);
        webView=(WebView)findViewById(R.id.id_webview);
    }

    private void initData() {
        if (!TextUtils.isEmpty(pid)){
            customDialog.show();
            String functionUrl= UrlUtil.SecondService_Url;
            try {
                functionUrl =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&pid="+URLEncoder.encode(pid, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(functionUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandle);
        }

    }

    private void iniWebView() {
        webView.loadUrl(SERVICE_URL);
        WebSettings settings=webView.getSettings();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}
