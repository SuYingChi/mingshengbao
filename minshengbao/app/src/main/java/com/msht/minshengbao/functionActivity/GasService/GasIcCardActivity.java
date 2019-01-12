package com.msht.minshengbao.functionActivity.GasService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.IccardViewAdapter;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/9/22  
 */
public class GasIcCardActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_iccard);
        context=this;
        setCommonHeader("IC卡充值");
        initView();
    }
    private void initView() {
        findViewById(R.id.id_goback).setOnClickListener(this);
        findViewById(R.id.id_btn_scan).setOnClickListener(this);
        findViewById(R.id.id_operation_step).setOnClickListener(this);
        findViewById(R.id.id_near_machine).setOnClickListener(this);
        findViewById(R.id.id_price_explain).setOnClickListener(this);
        TextView tvPrice=(TextView)findViewById(R.id.id_tv_rightText);
        tvPrice.setOnClickListener(this);
        tvPrice.setVisibility(View.VISIBLE);
        tvPrice.setText("充值记录");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_tv_rightText:
                onRechargeRecord();
                break;
            case R.id.id_btn_scan:
                requestPermission();
                break;
            case R.id.id_operation_step:
                onOperationStep();
                break;
            case R.id.id_near_machine:
                onNearMachine();
                break;
            case R.id.id_price_explain:
                onGasPrice();
                break;
            default:
                break;
        }
    }
    private void onRechargeRecord() {
        Intent intent=new Intent(context,GasIcCardNumberList.class);
        startActivity(intent);
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                goScanActivity();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许拍照权限，二维码扫描无法使用！");
                            }
                        }).start();
            }else {
                goScanActivity();
            }
        }else {
            goScanActivity();
        }
    }
    private void goScanActivity() {
        Intent intent =new Intent(context, QrCodeScanActivity.class);
        startActivity(intent);
    }
    private void onOperationStep() {
        String url= UrlUtil.IC_OPERATION_STEP_URL;
        Intent intent=new Intent(context,HtmlPageActivity.class);
        intent.putExtra("navigate","操作流程");
        intent.putExtra("url",url);
        startActivity(intent);
    }
    private void onNearMachine() {
        Intent intent =new Intent(context, GasIcCardMachineMap.class);
        startActivity(intent);
    }
    private void onGasPrice() {
        String url= UrlUtil.Gasprice_Url;
        Intent price=new Intent(context,HtmlPageActivity.class);
        price.putExtra("navigate","气价说明");
        price.putExtra("url",url);
        startActivity(price);
    }
}
