package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.DeliveryInfoDialog;
import com.msht.minshengbao.ViewUI.Dialog.WaterPublicDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/1/2  
 */
public class ScanCodeResultActivity extends BaseActivity {
    private Button btnLaunch;
    private Button btnKnow;
    private View     layoutResult;
    private View     layoutError;
    private TextView tvFeeStandard;
    private TextView tvTDS;
    private TextView tvNotice;
    private TextView tvEquipment;
    private TextView tvEstate;
    private String   equipmentNo;
    private String   userPhone ="";
    private String   sign,extParams;
    private String   waterAccount="";
    private int      requestType=0;
    private CustomDialog customDialog;
    private final ResultHandler resultHandler=new ResultHandler(this);
    private static class ResultHandler extends Handler{
        private WeakReference<ScanCodeResultActivity> mWeakReference;
        private ResultHandler(ScanCodeResultActivity activity) {
            mWeakReference = new WeakReference<ScanCodeResultActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {

            final ScanCodeResultActivity activity=mWeakReference.get();
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
                        String results=object.optString("result");
                        String error = object.optString("message");
                        String code=object.optString("code");

                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONArray jsonArray=object.optJSONArray("data");
                                activity.onAccountData(jsonArray);
                            }else if (activity.requestType==1){
                                JSONObject jsonObject=object.optJSONObject("data");
                                activity.onEquipmentData(jsonObject);
                            }else if (activity.requestType==2){
                                activity.onResultTip(code,error);
                            }
                        }else {
                            if (activity.requestType==0){
                                activity.onQueryFailure();
                                activity.onGetEquipmentInformation();
                            }else {
                                JSONObject jsonObject=object.optJSONObject("data");
                                activity.onResultData(jsonObject);
                                activity.onResultTip(code,error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    if (activity.requestType==0){
                        activity.onQueryFailure();
                        activity.onGetEquipmentInformation();
                    }else {
                        activity.layoutResult.setVisibility(View.GONE);
                        activity.layoutError.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onQueryFailure() {
       waterAccount=userPhone;
    }
    private void onAccountData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String account=json.optString("account");
                int isDefault=json.optInt("isDefault");
                if (isDefault==1){
                    VariableUtil.waterAccount=account;
                    waterAccount=account;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        onGetEquipmentInformation();

    }
    private void onDataMD5() {
        JSONObject object=new JSONObject();
        try{
            object.put("account",waterAccount);
            object.put("equipmentNo",equipmentNo);
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("account",waterAccount);
        treeMap.put("equipmentNo",equipmentNo);
        extParams= SecretKeyUtil.getKeyextParams(object.toString());
        sign= SecretKeyUtil.getKeySign(treeMap);
    }
    private void onEquipmentData(JSONObject jsonObject) {
        String  equipment=jsonObject.optString("equipmentNo");
        String  communityName=jsonObject.optString("communityName");
        String  purifyWaterTds=jsonObject.optString("purifyWaterTds");
        int     networkStatus=jsonObject.optInt("networkStatus");
        int     workStatus=jsonObject.optInt("workStatus");
        tvEquipment.setText(equipment);
        tvEstate.setText(communityName);
        tvTDS.setText(purifyWaterTds);
        tvFeeStandard.setText("0.3元/升");
        if (networkStatus==1){
            if (workStatus==1){
                btnKnow.setVisibility(View.GONE);
                tvNotice.setVisibility(View.GONE);
                btnLaunch.setVisibility(View.VISIBLE);
            }else {
                btnKnow.setVisibility(View.VISIBLE);
                btnLaunch.setVisibility(View.GONE);
                tvNotice.setVisibility(View.VISIBLE);
                tvNotice.setText(R.string.result_notice7);
            }
        }else {
            btnKnow.setVisibility(View.VISIBLE);
            btnLaunch.setVisibility(View.GONE);
            tvNotice.setVisibility(View.VISIBLE);
            tvNotice.setText(R.string.result_notice2);
        }
    }
    private void onResultTip(String code, String error) {
        String result=error;
        switch (code){
            case ConstantUtil.VALUE_CODE_0000:
                result=context.getString(R.string.result_notice1);
                break;
            case ConstantUtil.VALUE_CODE_0009:
                result=context.getString(R.string.result_notice4);
                break;
            case ConstantUtil.VALUE_CODE_0010:
                result=context.getString(R.string.result_notice3);
                break;
            case ConstantUtil.VALUE_CODE_0011:
                result=context.getString(R.string.result_notice5);
                break;
            case ConstantUtil.VALUE_CODE_0012:
                result=context.getString(R.string.result_notice4);
                break;
            case ConstantUtil.VALUE_CODE_1008:
                result=context.getString(R.string.result_notice2);
                break;
            case ConstantUtil.VALUE_CODE_1009:
                result=context.getString(R.string.result_notice6);
                break;
                default:
                    result=context.getString(R.string.result_notice3);
                    break;
        }
        new WaterPublicDialog(context).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setButtonText("好的")
                .setNoticeText(result)
                .setImageVisibility(true)
                .setViewVisibility(false)
                .setCanceledOnTouchOutside(true)
                .setOnPositiveClickListener(new WaterPublicDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();

    }
    private void onResultData(JSONObject jsonObject) {
        String  equipment=jsonObject.optString("equipmentNo");
        String  communityName=jsonObject.optString("communityName");
        tvEquipment.setText(equipment);
        tvEstate.setText(communityName);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code_result);
        context=this;
        setCommonHeader("扫码结果");
        customDialog=new CustomDialog(this, "正在加载");
        equipmentNo=getIntent().getStringExtra("equipmentNo");
        userPhone = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        waterAccount= userPhone;
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(waterAccount);
        bean.setEquipmentNo(equipmentNo);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        initView();
        initAccountData();
    }
    private void initAccountData() {
        requestType=0;
        String requestUrl= UrlUtil.WATER_BIND_ACCOUNT_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("account",userPhone);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,resultHandler);
    }

    private void onGetEquipmentInformation() {
        requestType=1;
        customDialog.show();
        String validateURL= UrlUtil.WATER_EQUIPMENT_INFORMATION;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("equipmentNo",equipmentNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,resultHandler);
    }
    private void initView() {
        layoutError =findViewById(R.id.id_layout_neterror);
        layoutResult =findViewById(R.id.id_layout_success);
        btnKnow=(Button)findViewById(R.id.id_btn_know);
        btnLaunch=(Button)findViewById(R.id.id_btn_launch);
        tvFeeStandard=(TextView)findViewById(R.id.id_fee_standard) ;
        tvTDS=(TextView)findViewById(R.id.id_tv_TDS);
        tvNotice =(TextView)findViewById(R.id.id_result_notice);
        tvEstate =(TextView)findViewById(R.id.id_tv_estate);
        tvEquipment =(TextView)findViewById(R.id.id_tv_equipment);
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        findViewById(R.id.id_tds_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowTDSDialog();
            }
        });
        findViewById(R.id.id_malfunction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WaterMalfunctionActivity.class);
                startActivity(intent);
            }
        });
    }
    private void onShowTDSDialog() {
        new WaterPublicDialog(context).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setButtonText("我知道了")
                .setTitleText("TDS小贴士")
                .setImageVisibility(false)
                .setViewVisibility(true)
                .setCanceledOnTouchOutside(true)
                .show();
    }
    private void initData() {
        requestType=2;
        customDialog.show();
        String validateURL= UrlUtil.SCAN_CODE_BUY;
        onDataMD5();
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        textParams.put("account", waterAccount);
        textParams.put("equipmentNo",equipmentNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,resultHandler);
    }
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
