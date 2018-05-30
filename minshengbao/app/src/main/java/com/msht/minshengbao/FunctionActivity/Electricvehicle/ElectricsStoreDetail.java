package com.msht.minshengbao.FunctionActivity.Electricvehicle;

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

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.GlideImageLoader;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.BannerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElectricsStoreDetail extends BaseActivity implements View.OnClickListener {
    private String id;
    private BannerLayout mBanner;
    private TextView tv_name;
    private TextView tv_address;
    private double latitude,longitude;
    private String store_name,store_address;
    private String distance,store_telephone;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private ArrayList<String> Imgurls = new ArrayList<>();
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    Handler sendmeterHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("code");
                        if(Results.equals("success")) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            jsonArray=jsonObject.getJSONArray("images");
                            initshowdata(jsonObject);
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private void initshowdata(JSONObject jsonObject) {
        String store_id=jsonObject.optString("id");
        store_name=jsonObject.optString("name");
        store_telephone=jsonObject.optString("telephone");
        store_address=jsonObject.optString("address");
        latitude=jsonObject.optDouble("latitude");
        longitude=jsonObject.optDouble("longitude");
        String imgUrl=jsonObject.optString("imgUrl");
        String lastModifyTime=jsonObject.optString("lastModifyTime");
        if (jsonArray.length()!=0&jsonArray!=null){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String picture_id=obj.optString("id");
                    String shopId=obj.optString("shopId");
                    String imgUrls=obj.optString("imgUrl");
                    Imgurls.add(imgUrls);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Imgurls.add(imgUrl);
        }
        mBanner.setViewUrls(Imgurls);
        tv_address.setText(store_address);
        tv_name.setText(store_name);
    }
    private void showfaiture(String error) {
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
        setCommonHeader("门店详情");
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
        tv_name=(TextView)findViewById(R.id.id_tv_name);
        tv_address=(TextView)findViewById(R.id.id_tv_address);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("image", Imgurls);
                Intent intent=new Intent(context,ElectricPicture.class);
                intent.putExtra("url",bundle);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        String dataurl = UrlUtil.Store_Detail;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",id);
        SendrequestUtil.executepost(dataurl,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                sendmeterHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                sendmeterHandler.sendMessage(msg);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_name:
                break;
            case R.id.id_re_address:
                Storelocation();
                break;
            case R.id.id_phone_img:
                callphone();
                break;
           default:
               break;

        }
    }

    private void callphone() {
        new PromptDialog.Builder(this)
                .setTitle("店铺电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(store_telephone)
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
                            if (ElectricsStoreDetail.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + store_telephone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(ElectricsStoreDetail.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + store_telephone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void Storelocation() {
        Intent intent=new Intent(context,StoreMap.class);
        intent.putExtra("store_name",store_name);
        intent.putExtra("store_address",store_address);
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
                Toast.makeText(ElectricsStoreDetail.this,"已授权，请重新拨打",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ElectricsStoreDetail.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
