package com.msht.minshengbao.functionActivity.GasService;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class GasEmergencyRescueActivity extends BaseActivity implements View.OnClickListener {
    private ImageView phoneImg1, phoneImg2;
    private ImageView phoneImg3, phoneImg4;
    private TextView tel1,tel2, tel3,tel0;
    private TextView city0,city1,city2,city3;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private ArrayList<HashMap<String, String>> phoneList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasEmergencyRescueActivity> mWeakReference;
        public RequestHandler(GasEmergencyRescueActivity activity) {
            mWeakReference = new WeakReference<GasEmergencyRescueActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GasEmergencyRescueActivity activity=mWeakReference.get();
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
                        activity.jsonArray =object.optJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.initView();
                        }else {
                            activity.failure(error);
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
    private void failure(String error) {
        new PromptDialog.Builder(this)
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
    private void initView() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String city = jsonObject.getString("city");
                String tel = jsonObject.getString("tel");
                tel=tel.trim();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("city", city);
                map.put("tel", tel);
                phoneList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        city0.setText(phoneList.get(0).get("city"));
        tel0.setText(phoneList.get(0).get("tel"));
        city1.setText(phoneList.get(1).get("city"));
        tel1.setText(phoneList.get(1).get("tel"));
        city2.setText(phoneList.get(2).get("city"));
        tel2.setText(phoneList.get(2).get("tel"));
        city3.setText(phoneList.get(3).get("city"));
        tel3.setText(phoneList.get(3).get("tel"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_emergency_rescue);
        context=this;
        mPageName="燃气抢险";
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader(mPageName);
        intiView();
        initData();
        initEvent();
    }
    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.GAS_QIANGXIAN_URL;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private void intiView() {
        tel0 = (TextView) findViewById(R.id.id_tv_tel0);
        tel1 = (TextView) findViewById(R.id.id_tv_tel1);
        tel2 = (TextView) findViewById(R.id.id_tv_tel2);
        tel3 = (TextView) findViewById(R.id.id_tv_tel3);
        city0= (TextView) findViewById(R.id.id_tv1);
        city1= (TextView) findViewById(R.id.id_tv2);
        city2= (TextView) findViewById(R.id.id_tv3);
        city3= (TextView) findViewById(R.id.id_tv4);
        phoneImg1 =(ImageView)findViewById(R.id.id_phone_img1);
        phoneImg2 =(ImageView)findViewById(R.id.id_phone_img2);
        phoneImg3 =(ImageView)findViewById(R.id.id_phone_img3);
        phoneImg4 =(ImageView)findViewById(R.id.id_phone_img4);
    }
    private void initEvent() {
        tel0.setOnClickListener(this);
        tel1.setOnClickListener(this);
        tel2.setOnClickListener(this);
        tel3.setOnClickListener(this);

        phoneImg1.setOnClickListener(this);
        phoneImg2.setOnClickListener(this);
        phoneImg3.setOnClickListener(this);
        phoneImg4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_tv_tel1:
                onCallToHaiKou();
                break;
            case R.id.id_tv_tel2:
                onCallToLingShui();
                break;
            case R.id.id_tv_tel3:
                onCallToWanNing();
                break;
            case R.id.id_tv_tel0:
                onCallHotLine();
                break;
            case R.id.id_phone_img1:
                onCallHotLine();
                break;
            case R.id.id_phone_img2:
                onCallToHaiKou();
                break;
            case R.id.id_phone_img3:
                onCallToLingShui();
                break;
            case R.id.id_phone_img4:
                onCallToWanNing();
                break;
            default:
                break;
        }
    }
    private void onCallHotLine() {
        final String cityName =city0.getText().toString();
        final String phone = tel0.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityName)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
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
                            if (GasEmergencyRescueActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(GasEmergencyRescueActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void onCallToHaiKou() {
        final String cityName =city1.getText().toString();
        final String phone = tel1.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityName+"抢险电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (GasEmergencyRescueActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(GasEmergencyRescueActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void onCallToLingShui() {
        final String cityName =city2.getText().toString();
        final String phone = tel2.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityName+"抢险电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        // String phone = "0898-93359393";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (GasEmergencyRescueActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(GasEmergencyRescueActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void onCallToWanNing() {
        final String cityName =city3.getText().toString();
        final String phone = tel3.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityName+"抢险电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //String phone = "0898-36204455";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (GasEmergencyRescueActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(GasEmergencyRescueActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(GasEmergencyRescueActivity.this,"已授权，请重新拨打",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(GasEmergencyRescueActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
