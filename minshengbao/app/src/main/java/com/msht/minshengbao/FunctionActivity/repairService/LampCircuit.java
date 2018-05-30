package com.msht.minshengbao.FunctionActivity.repairService;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.SanitaryAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class LampCircuit extends BaseActivity {
    private WebView webView;
    private MyNoScrollGridView mGridView;
    private SanitaryAdapter homeAdapter;
    private String pid;
    private String cityId;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private CustomDialog customDialog;
    private   boolean lstate=false;
    private static final String my_url= UrlUtil.Service_noteUrl;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    Handler getfunctionhandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String Error = object.optString("error");
                        JSONArray json =object.getJSONArray("data");
                        if(result.equals("success")) {
                            initFunction(json);
                        }else {
                            Toast.makeText(context, Error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initFunction(JSONArray json) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String code = jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("code", code);
                functionList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (functionList.size() != 0) {
            homeAdapter.notifyDataSetChanged();
        } else {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_circuit);
        setCommonHeader("灯具电路");
        context=this;
        lstate= SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        cityId=data.getStringExtra("city_id");
        initView();
        homeAdapter=new SanitaryAdapter(context,functionList);
        mGridView.setAdapter(homeAdapter);
        initData();
        iniWebview();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lstate){
                    String Id=functionList.get(position).get("id");
                    String Name=functionList.get(position).get("name");
                    Intent intent=new Intent(context,PublishOrder.class);
                    intent.putExtra("id",Id);
                    intent.putExtra("name",Name);
                    intent.putExtra("maintype",mPageName);
                    startActivity(intent);
                }else {
                    Intent login=new Intent(context, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
    private void initView() {
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_function_view);
        webView=(WebView)findViewById(R.id.id_webview);
    }
    private void initData() {
        customDialog.show();
        String functionUrl= UrlUtil.SecondService_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&pid="+URLEncoder.encode(pid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendrequestUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                getfunctionhandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                getfunctionhandler.sendMessage(msg);
            }
        });
    }
    private void iniWebview() {
        webView.loadUrl(my_url);
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
