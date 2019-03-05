package com.msht.minshengbao.functionActivity.WaterApp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.PrizesGiftsActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/9  
 */
public class WaterMainActivity extends BaseActivity implements View.OnClickListener {
    private TextView  tvAccount;
    private TextView  tvBalance;
    private String    account="";
    private String    waterAccount;
    private int       requestType=0;
    private View layoutTip;
    private CustomDialog customDialog;
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private static class BalanceHandler extends Handler {
        private WeakReference<WaterMainActivity> mWeakReference;
        public BalanceHandler(WaterMainActivity activity) {
            mWeakReference=new WeakReference<WaterMainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterMainActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }else {
                if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                    activity.customDialog.dismiss();
                }
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        String code=object.optString("code");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONObject json =object.optJSONObject("data");
                                activity.onReceiveAccountData(json);
                            }else if (activity.requestType==1){
                                activity.initData();
                            }
                        }else {
                            if (activity.requestType==0&&code.equals(ConstantUtil.VALUE_CODE_0009)){
                                VariableUtil.waterAccount=activity.account;
                                activity.waterAccount=activity.account;
                                activity.onSetAccountData();
                                activity.onRegisterWaterAccount();
                            }else {
                                ToastUtil.ToastText(activity.context,message);
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
    private void onSetAccountData() {
        String accountText="ID:"+waterAccount;
        if (RegularExpressionUtil.isPhone(waterAccount)){
            accountText="ID:"+waterAccount.substring(0,3)+"****"+waterAccount.substring(7,waterAccount.length());
        }
        String balanceText="¥0.0";
        tvBalance.setText(balanceText);
        tvAccount.setText(accountText);
    }

    private void onReceiveAccountData(JSONObject json) {
        String id=json.optString("id");
        String type=json.optString("type");
        waterAccount=json.optString("account");
        if (!TextUtils.isEmpty(waterAccount)){
            VariableUtil.waterAccount=waterAccount;
        }else {
            VariableUtil.waterAccount=account;
            waterAccount=account;
        }
        String payAmount=json.optString("payBalance");
        String giveAmount=json.optString("giveBalance");
        String accountText="ID:"+waterAccount;
        if (RegularExpressionUtil.isPhone(waterAccount)){
            accountText="ID:"+waterAccount.substring(0,3)+"****"+waterAccount.substring(7,waterAccount.length());
        }
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        totalBalance= VariableUtil.twoDecinmal2(totalBalance);
        String balanceText="¥"+String.valueOf(totalBalance);
        tvBalance.setText(balanceText);
        tvAccount.setText(accountText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_main);
        context=this;
        mPageName="民生水宝";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        initFindViewId();
        initData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE3:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
                break;
            case ConstantUtil.VALUE4:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
            case ConstantUtil.VALUE2:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
                break;
                default:
                    break;
        }
    }
    private void initData() {
        requestType=0;
        customDialog.show();
        String validateURL= UrlUtil.WATER_ACCOUNT_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        textParams.put("account",account);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void onRegisterWaterAccount() {
        requestType=1;
        customDialog.show();
        String validateURL= UrlUtil.WATER_INNER_REGISTER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("phone",account);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,balanceHandler);
    }
    private void initFindViewId() {
        layoutTip=findViewById(R.id.id_tip_layout);
        findViewById(R.id.id_scan_layout).setOnClickListener(this);
        findViewById(R.id.id_balance_layout).setOnClickListener(this);
        findViewById(R.id.id_nearby_layout).setOnClickListener(this);
        findViewById(R.id.id_malfunction_layout).setOnClickListener(this);
        findViewById(R.id.id_service_layout).setOnClickListener(this);
        findViewById(R.id.id_btn_recharge).setOnClickListener(this);
        findViewById(R.id.id_replace_account).setOnClickListener(this);
        findViewById(R.id.id_ID_layout).setOnClickListener(this);
        findViewById(R.id.id_share_layout).setOnClickListener(this);
        findViewById(R.id.id_look_detail).setOnClickListener(this);
        tvAccount=(TextView)findViewById(R.id.id_ID);
        tvBalance=(TextView)findViewById(R.id.id_balance);
        findViewById(R.id.id_water_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTip.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_scan_layout:
                requestPermission();
                break;
            case R.id.id_balance_layout:
                balanceInformation();
                break;
            case R.id.id_nearby_layout:
                onNearbyMachine();
                break;
            case R.id.id_malfunction_layout:
                onMalfunction();
                break;
            case R.id.id_service_layout:
                onServiceCenter();
                break;
            case R.id.id_btn_recharge:
                rechargeAmount();
                break;
            case R.id.id_replace_account:
                switchAccount();
                break;
            case R.id.id_ID_layout:
                switchAccount();
                break;
            case R.id.id_share_layout:
                friendShare();
                break;
            case R.id.id_look_detail:
                layoutTip.setVisibility(View.GONE);
                rechargeAmount();
                break;
                default:
                    break;
        }
    }
    private void onServiceCenter() {
        String url=UrlUtil.WATER_SERVICE_CENTER;
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","客服中心");
        startActivity(intent);
    }
    private void friendShare() {
        Intent intent=new Intent(context,WaterFriendShareActivity.class);
        intent.putExtra("account",waterAccount);
        startActivity(intent);
    }
    private void onMalfunction() {
        Intent intent=new Intent(context,WaterMalfunctionActivity.class);
        startActivity(intent);
    }
    private void balanceInformation() {
        Intent intent=new Intent(context,WaterBalanceActivity.class);
        startActivityForResult(intent,4);
    }
    private void switchAccount() {
        Intent intent=new Intent(context,WaterSwitchAccountActivity.class);
        startActivityForResult(intent,3);
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, ConstantUtil.MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        scanQrCode();
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        ToastUtil.ToastText(context,"没有权限您将无法进行扫描操作！");
                    }
                });

            }else {
                scanQrCode();
            }
        }else {
            scanQrCode();
        }
    }
    private void scanQrCode() {
        Intent intent =new Intent(context, QrCodeScanActivity.class);
        startActivity(intent);
    }
    private void onNearbyMachine() {
        Intent intent=new Intent(context,WaterEquipmentMapActivity.class);
        startActivityForResult(intent,1);
    }
    private void rechargeAmount() {
        Intent intent=new Intent(context,WaterRechargeActivity.class);
        intent.putExtra("account",account);
        startActivityForResult(intent,2);
    }
}
