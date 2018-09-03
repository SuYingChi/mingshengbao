package com.msht.minshengbao.functionActivity.GasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.GasServiceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.umeng.analytics.MobclickAgent;


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
 * @date 2016/8/26  
 */
public class GasServiceActivity extends BaseActivity {
    private MyNoScrollGridView mGridView;
    private GasServiceAdapter homeAdapter;
    private String pid="null";
    private String cityId="null";
    private   boolean loginState =false;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler  extends Handler{
        private WeakReference<GasServiceActivity > mWeakReference;
        public RequestHandler(GasServiceActivity activity) {
            mWeakReference = new WeakReference<GasServiceActivity >(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasServiceActivity activity=mWeakReference.get();
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
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_service);
        context=this;
        setCommonHeader("燃气业务");
        loginState = SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        cityId=data.getStringExtra("city_id");
        initView();
        homeAdapter=new GasServiceAdapter(context,functionList);
        mGridView.setAdapter(homeAdapter);
        initData();
        initStartActivity();

    }
    private void initStartActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (loginState){
                    String codes=functionList.get(position).get("code");
                    switch (codes){
                        case "gas_repair":
                            onGasRepair();
                            break;
                        case "gas_meter":
                            onWriteTable();
                            break;
                        case "gas_pay":
                            onPayFee();
                            break;
                        case "gas_install":
                            installDevice();
                            break;
                        case "gas_rescue":
                            onQianXian();
                            break;
                        case "gas_introduce":
                            onIntroduce();
                            break;
                        default:
                            break;
                    }
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
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(function, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private void onPayFee() {
        Intent select=new Intent(context,GasPayFeeActivity.class);
        startActivity(select);
    }

    private void onQianXian() {
        Intent select=new Intent(context,GasEmergencyRescueActivity.class);
        startActivity(select);
    }
    private void onIntroduce() {
        Intent select=new Intent(context,GasIntroduceActivity.class);
        startActivity(select);
    }
    private void onGasRepair() {
        Intent select=new Intent(context,GasRepairActivity.class);
        startActivity(select);
    }
    private void onWriteTable() {
        Intent select=new Intent(context,GasWriteTableActivity.class);
        startActivity(select);
    }
    private void installDevice() {
        Intent select=new Intent(context,GasInstallAcitivity.class);
        startActivity(select);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
