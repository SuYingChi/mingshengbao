package com.msht.minshengbao.functionActivity.gasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.Bean.AdvertisingInfo;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.GasServiceAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/26  
 */
public class GasServiceActivity extends BaseActivity {
    private MyNoScrollGridView mGridView;
    private BGABanner mCubeBanner;
    private GasServiceAdapter homeAdapter;
    private String pid="";
    private String cityId="";
    private CustomDialog customDialog;
    private ArrayList<AdvertisingInfo> adInformation = new ArrayList<AdvertisingInfo>();
    private List<String> advertisingList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private final GetAdvertisingHandler getAdvertisingHandler=new GetAdvertisingHandler(this);
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
            activity.initBannerData();
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
    private static class GetAdvertisingHandler extends Handler{
        private WeakReference<GasServiceActivity> mWeakReference;
        public GetAdvertisingHandler(GasServiceActivity activity) {
            mWeakReference = new WeakReference<GasServiceActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasServiceActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error=object.optString("error");
                        JSONArray array =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (array.length()>0){
                                activity.initAdvertisingData(array);
                            }
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

    private void initAdvertisingData(JSONArray array) {
        adInformation.clear();
        advertisingList.clear();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                AdvertisingInfo info = new AdvertisingInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                info.setDesc(jsonObject.optString("desc"));
                info.setTitle(jsonObject.optString("title"));
                info.setShare(jsonObject.optString("share"));
                adInformation.add(info);
                advertisingList.add(jsonObject.optString("image"));

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (adInformation!=null&&adInformation.size()!=0){
            mCubeBanner.setVisibility(View.VISIBLE);
        }else {
            mCubeBanner.setVisibility(View.GONE);
        }
        mCubeBanner.setAutoPlayAble(advertisingList.size() > 1);
        mCubeBanner.setAdapter(new MyImageAdapter());
        mCubeBanner.setDelegate(new MyImageAdapter());
        mCubeBanner.setData(advertisingList, null);
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
        Intent data=getIntent();
        pid=data.getStringExtra("pid");
        cityId=VariableUtil.cityId;
        mPageName=data.getStringExtra("name");
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
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
                if (context!=null){
                    if (isLoginState(context)){
                        String codes=functionList.get(position).get("code");
                        String name=functionList.get(position).get("name");
                        switch (codes){
                            case "gas_repair":
                                onGasRepair(name);
                                break;
                            case "gas_meter":
                                onWriteTable(name);
                                break;
                            case "gas_pay":
                                onPayFee(name);
                                break;
                            case "gas_install":
                                installDevice(name);
                                break;
                            case "gas_rescue":
                                onQianXian(name);
                                break;
                            case "gas_introduce":
                                onIntroduce(name);
                                break;
                            default:
                                break;
                        }
                    }else {
                        AppActivityUtil.onStartLoginActivity(context,"");
                        finish();
                    }
                }
            }
        });
    }
    private void initView() {
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_function_view);
        mCubeBanner = findViewById(R.id.banner);
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
            OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(functionUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
        }
    }
    private void initBannerData() {
        String requestUrl=UrlUtil.ADVERTISING_URL;
        try {
            requestUrl =requestUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&code="+URLEncoder.encode("gas_start_activity", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,getAdvertisingHandler);
    }
    private class MyImageAdapter implements BGABanner.Adapter<ImageView, String>,BGABanner.Delegate<ImageView, String> {
        @Override
        public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
            Glide.with(itemView.getContext())
                    .load(model)
                    .apply(new RequestOptions().placeholder(R.drawable.icon_stub).error(R.drawable.icon_error).dontAnimate().centerCrop())
                    .into(itemView);
        }

        @Override
        public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
            AdvertisingInfo info=adInformation.get(position);
            String myUrl=info.getUrl();
            String title=info.getTitle();
            String share=info.getShare();
            String desc=info.getDesc();
            AppActivityUtil.onAppActivityType(context,myUrl,title,share,desc,"gas_start_activity","");
        }
    }
    private void onPayFee(String name) {
        Intent select=new Intent(context,GasPayFeeActivity.class);
        startActivity(select);
    }
    private void onQianXian(String name) {
        Intent select=new Intent(context,GasEmergencyRescueActivity.class);
        select.putExtra("name",name);
        startActivity(select);
    }
    private void onIntroduce(String name) {
        Intent select=new Intent(context,GasIntroduceActivity.class);
        select.putExtra("name",name);
        startActivity(select);
    }
    private void onGasRepair(String name) {
        Intent select=new Intent(context,GasRepairActivity.class);
        select.putExtra("name",name);
        startActivity(select);
    }
    private void onWriteTable(String name) {
        Intent select=new Intent(context,GasWriteTableActivity.class);
        startActivity(select);
    }
    private void installDevice(String name) {
        Intent select=new Intent(context,GasInstallActivity.class);
        select.putExtra("name",name);
        startActivity(select);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
