package com.msht.minshengbao.functionActivity.electricVehicle;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.Callback.GlideImageLoader;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.BannerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/22  
 */
public class ElectricsStoreDetailActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private BannerLayout mBanner;
    private TextView tvName;
    private TextView tvAddress;
    private double latitude,longitude;
    private String storeName, storeAddress;
    private String distance, storeTelephone;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private JSONArray jsonArray;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ElectricsStoreDetailActivity> mWeakReference;
        public RequestHandler(ElectricsStoreDetailActivity activity) {
            mWeakReference=new WeakReference<ElectricsStoreDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ElectricsStoreDetailActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("code");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            activity.jsonArray=jsonObject.getJSONArray("images");
                            activity.onReceiveData(jsonObject);
                        }else {
                            activity.onShowFailure(error);
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
    private void onReceiveData(JSONObject jsonObject) {
        String storeId=jsonObject.optString("id");
        storeName =jsonObject.optString("name");
        storeTelephone =jsonObject.optString("telephone");
        storeAddress =jsonObject.optString("address");
        latitude=jsonObject.optDouble("latitude");
        longitude=jsonObject.optDouble("longitude");
        String imgUrl=jsonObject.optString("imgUrl");
        String lastModifyTime=jsonObject.optString("lastModifyTime");
        if (jsonArray.length()!=0&jsonArray!=null){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String pictureId=obj.optString("id");
                    String shopId=obj.optString("shopId");
                    String imgUrls=obj.optString("imgUrl");
                    imageUrls.add(imgUrls);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            imageUrls.add(imgUrl);
        }
        mBanner.setViewUrls(imageUrls);
        tvAddress.setText(storeAddress);
        tvName.setText(storeName);
    }
    private void onShowFailure(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrics_store_detail);
        context=this;
        mPageName="门店详情";
        setCommonHeader(mPageName);
        Intent data=getIntent();
        id=data.getStringExtra("store_id");
        distance=data.getStringExtra("distance");
        initView();
        initData();
    }
    private void initView() {
        mBanner=(BannerLayout)findViewById(R.id.id_banner);
        findViewById(R.id.id_re_name).setOnClickListener(this);
        findViewById(R.id.id_re_address).setOnClickListener(this);
        findViewById(R.id.id_phone_img).setOnClickListener(this);
        tvName =(TextView)findViewById(R.id.id_tv_name);
        tvAddress =(TextView)findViewById(R.id.id_tv_address);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("image", imageUrls);
                Intent intent=new Intent(context,ElectricPictureActivity.class);
                intent.putExtra("url",bundle);
                startActivity(intent);
            }
        });
    }
    private void initData() {
        String dataUrl = UrlUtil.STORE_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_name:
                break;
            case R.id.id_re_address:
                onStoreLocation();
                break;
            case R.id.id_phone_img:
                onCallPhone();
                break;
           default:
               break;

        }
    }

    private void onCallPhone() {
        new PromptDialog.Builder(this)
                .setTitle("店铺电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(storeTelephone)
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //String phone = "963666";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ElectricsStoreDetailActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + storeTelephone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(ElectricsStoreDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + storeTelephone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void onStoreLocation() {
        Intent intent=new Intent(context,StoreMapActivity.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("storeAddress", storeAddress);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("distance",distance);
        startActivity(intent);
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(ElectricsStoreDetailActivity.this,"已授权，请重新拨打",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ElectricsStoreDetailActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
