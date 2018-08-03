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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Gasqianxian extends BaseActivity implements View.OnClickListener {
    private ImageView goback;
    private ImageView phoneimg1,phoneimg2;
    private ImageView phoneimg3,phoneimg4;
    private TextView tel1,tel2, tel3,tel0,tv_naviga;
    private TextView city0,city1,city2,city3;
    private final int SHOW_RESPONSE = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private ArrayList<HashMap<String, String>> phoneList = new ArrayList<HashMap<String, String>>();
    Handler telehandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOW_RESPONSE:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            initView();
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(Gasqianxian .this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
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
        setContentView(R.layout.activity_gasqianxian);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("燃气抢险");
        intiView();
        initData();
        initEvent();
    }
    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.GasQiangxian_Url;
        SendrequestUtil.executeGet(validateURL, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = success;
                telehandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message message = new Message();
                message.what = FAILURE;
                message.obj =fail;
                telehandler.sendMessage(message);
            }
        });
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
        phoneimg1=(ImageView)findViewById(R.id.id_phone_img1);
        phoneimg2=(ImageView)findViewById(R.id.id_phone_img2);
        phoneimg3=(ImageView)findViewById(R.id.id_phone_img3);
        phoneimg4=(ImageView)findViewById(R.id.id_phone_img4);
    }
    private void initEvent() {
        tel0.setOnClickListener(this);
        tel1.setOnClickListener(this);
        tel2.setOnClickListener(this);
        tel3.setOnClickListener(this);

        phoneimg1.setOnClickListener(this);
        phoneimg2.setOnClickListener(this);
        phoneimg3.setOnClickListener(this);
        phoneimg4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_tv_tel1:
                CallToHainkou();
                break;
            case R.id.id_tv_tel2:
                CallToLingshui();
                break;
            case R.id.id_tv_tel3:
                CallToWanning();
                break;
            case R.id.id_tv_tel0:
                callhotline();
                break;
            case R.id.id_phone_img1:
                callhotline();
                break;
            case R.id.id_phone_img2:
                CallToHainkou();
                break;
            case R.id.id_phone_img3:
                CallToLingshui();
                break;
            case R.id.id_phone_img4:
                CallToWanning();
                break;
            default:
                break;
        }
    }
    private void callhotline() {
        final String cityname =city0.getText().toString();
        final String phone = tel0.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityname)
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
                            if (Gasqianxian.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(Gasqianxian.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
    private void CallToHainkou() {
        final String cityname =city1.getText().toString();
        final String phone = tel1.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityname+"抢险电话")
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
                            if (Gasqianxian.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(Gasqianxian.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
    private void CallToLingshui() {
        final String cityname =city2.getText().toString();
        final String phone = tel2.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityname+"抢险电话")
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
                            if (Gasqianxian.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(Gasqianxian.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
    private void CallToWanning() {
        final String cityname =city3.getText().toString();
        final String phone = tel3.getText().toString();
        new PromptDialog.Builder(this)
                .setTitle(cityname+"抢险电话")
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
                            if (Gasqianxian.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(Gasqianxian.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
                Toast.makeText(Gasqianxian.this,"已授权，请重新拨打",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Gasqianxian.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
