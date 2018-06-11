package com.msht.minshengbao.FunctionActivity.insurance;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;


public class InsuranceDetail extends BaseActivity {
    private View lineImg;
    private LinearLayout liView;
    private TextView  tv_consult;
    private TextView  tv_type,tv_buyamount;
    private TextView  tv_insurance_amount;
    private TextView  tv_deadline;
    private TextView  tv_yiwai,tv_jijiu,tv_jiating;
    private TextView  tv_zeren,tv_zhufang,tv_fuzhu;
    private String    id;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_detail);
        context=this;
        setCommonHeader("保险套餐详情");
        Intent data=getIntent();
        id=data.getStringExtra("id");
        initHeader();
        initView();
        showview();
        initEvent();
    }
    private void initHeader() {
        tv_consult=(TextView)findViewById(R.id.id_tv_rightText);
        tv_consult.setVisibility(View.VISIBLE);
        tv_consult.setText("咨询");
    }
    private void showview() {
        if (id.equals("1537980")){
            tv_type.setText("保险套餐四");
            tv_buyamount.setText("¥30元");
            tv_insurance_amount.setText("¥303600元");
            tv_deadline.setText("1年");
            tv_yiwai.setText("130000元");
            tv_jijiu.setText("20000元");
            tv_jiating.setText("100000元");
            tv_zeren.setText("50000元");
            tv_zhufang.setText("3600元");
            liView.setVisibility(View.INVISIBLE);
            lineImg.setVisibility(View.INVISIBLE);
        }else if (id.equals("1533994")){
            tv_type.setText("保险套餐三");
            tv_buyamount.setText("¥100元");
            tv_insurance_amount.setText("¥1156000元");
            tv_deadline.setText("1年");
            tv_yiwai.setText("250000元");
            tv_jijiu.setText("50000元");
            tv_jiating.setText("400000元");
            tv_zeren.setText("300000元");
            tv_zhufang.setText("6000元");
            tv_fuzhu.setText("150000");
            liView.setVisibility(View.VISIBLE);
            lineImg.setVisibility(View.VISIBLE);

        }else if (id.equals("1534041")){
            tv_type.setText("保险套餐二");
            tv_buyamount.setText("¥200元");
            tv_insurance_amount.setText("¥856000元");
            tv_deadline.setText("3年");
            tv_yiwai.setText("200000元");
            tv_jijiu.setText("50000元");
            tv_jiating.setText("300000元");
            tv_zeren.setText("200000元");
            tv_zhufang.setText("6000元");
            tv_fuzhu.setText("100000");
            liView.setVisibility(View.VISIBLE);
            lineImg.setVisibility(View.VISIBLE);

        }else if (id.equals("1537981")){
            tv_type.setText("保险套餐一");
            tv_buyamount.setText("¥300元");
            tv_insurance_amount.setText("¥942000元");
            tv_deadline.setText("5年");
            tv_yiwai.setText("250000元");
            tv_jijiu.setText("50000元");
            tv_jiating.setText("300000元");
            tv_zeren.setText("180000元");
            tv_zhufang.setText("12000元");
            tv_fuzhu.setText("150000");
            liView.setVisibility(View.VISIBLE);
            lineImg.setVisibility(View.VISIBLE);
        }
    }
    private void initEvent() {
        tv_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallHotline();
            }
        });
    }
    private void CallHotline() {
        final String phone = "963666";
        new PromptDialog.Builder(this)
                .setTitle("客服电话")
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
                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(InsuranceDetail.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "963666"));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(context,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void initView() {
        lineImg =findViewById(R.id.id_view5);
        liView =(LinearLayout)findViewById(R.id.id_li_view);
        tv_type=(TextView)findViewById(R.id.id_title);
        tv_buyamount=(TextView)findViewById(R.id.id_buy_amount);
        tv_insurance_amount=(TextView)findViewById(R.id.id_insurance_amount);
        tv_deadline=(TextView)findViewById(R.id.id_deadline);
        tv_yiwai=(TextView)findViewById(R.id.id_amont1);
        tv_jijiu=(TextView)findViewById(R.id.id_amont2);
        tv_jiating=(TextView)findViewById(R.id.id_amont3);;
        tv_zeren=(TextView)findViewById(R.id.id_amont4);
        tv_zhufang=(TextView)findViewById(R.id.id_amont5);
        tv_fuzhu=(TextView)findViewById(R.id.id_amont6);
    }
}
