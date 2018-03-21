package com.msht.minshengbao.FunctionView.WaterApp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.HtmlWeb.HtmlPage;
import com.msht.minshengbao.FunctionView.Myview.RechargeValue;
import com.msht.minshengbao.FunctionView.Public.ScanCode;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WaterHome extends BaseActivity implements View.OnClickListener {

    private String account="";
    private TextView  tv_totalAmount;
    private TextView  tv_payAmount;
    private TextView  tv_giveAmount;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static  final int MY_CAMERA_REQUEST=1;
    private CustomDialog customDialog;
    Handler balanceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String message = object.optString("message");
                        if(Results.equals("success")) {
                            JSONObject json =object.optJSONObject("data");
                            initAccount(json);
                        }else {
                            showdialogs("提示",message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showdialogs("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    private void initAccount(JSONObject json) {
        String id=json.optString("id");
        String type=json.optString("type");
        String accounts=json.optString("account");
        String payAmount=json.optString("payBalance");
        String giveAmount=json.optString("giveBalance");
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        String totalAmount=String.valueOf(totalBalance);
        tv_giveAmount.setText(giveAmount);
        tv_payAmount.setText(payAmount);
        tv_totalAmount.setText(totalAmount);

    }
    private void showdialogs(String title, String s) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
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
        setContentView(R.layout.activity_water_home);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader("直饮水账户");
        initView();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            initData();
        }
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WaterAccount_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",account);
        HttpUrlconnectionUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                balanceHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                balanceHandler.sendMessage(msg);
            }
        });
    }
    private void initView() {
        right_img=(ImageView)findViewById(R.id.id_right_img);
        findViewById(R.id.id_layout_place).setOnClickListener(this);
        findViewById(R.id.id_layout_recharge).setOnClickListener(this);
        findViewById(R.id.id_scan_layout).setOnClickListener(this);
        findViewById(R.id.id_tv_detail).setOnClickListener(this);
        findViewById(R.id.id_forward_img).setOnClickListener(this);
        tv_giveAmount=(TextView)findViewById(R.id.id_give_fee);
        tv_payAmount=(TextView)findViewById(R.id.id_pay_fee);
        tv_totalAmount=(TextView)findViewById(R.id.id_total_amount);
        right_img.setImageResource(R.drawable.water_help_xh);
        right_img.setVisibility(View.VISIBLE);
        right_img.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_place:
                placeOrder();
                break;
            case R.id.id_layout_recharge:
                RechargeAmount();
                break;
            case R.id.id_tv_detail:
                BalanceDtail();
                break;
            case R.id.id_forward_img:
                BalanceDtail();
                break;
            case R.id.id_scan_layout:
                requestPermission();
                break;
            case R.id.id_right_img:
                waterHelp();
                break;
            default:
                break;
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        ScanQrcode();
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        Toast.makeText(context,"没有权限您将无法进行扫描操作！",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                ScanQrcode();
            }
        }else {
            ScanQrcode();
        }
    }

    private void waterHelp() {
        String url="http://msbapp.cn/water_h5/sbtips.html";
        Intent intent =new Intent(context, HtmlPage.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","帮助");
        startActivity(intent);
    }
    private void ScanQrcode() {
        Intent intent =new Intent(context, ScanCode.class);
        startActivity(intent);
    }
    private void BalanceDtail() {
        Intent intent =new Intent(context,WaterBalanceDetail.class);
        startActivity(intent);
    }
    private void RechargeAmount() {
        Intent intent=new Intent(context,WaterRecharge.class);
        startActivityForResult(intent,2);
    }
    private void placeOrder() {
        showdialogs("民生宝","近期上线");
        /*Intent intent=new Intent(context,OnlinePlaceOrder.class);
        startActivity(intent);*/
    }
}
