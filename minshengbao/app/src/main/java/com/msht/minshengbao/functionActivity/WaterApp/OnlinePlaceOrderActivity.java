package com.msht.minshengbao.functionActivity.WaterApp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.ActionSheetDialog;
import com.msht.minshengbao.ViewUI.Dialog.ActionSheetDialog.OnSheetButtonOneClickListener;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.WaterDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OnlinePlaceOrderActivity extends BaseActivity implements View.OnClickListener {
    private Button    subtract1,subtract2;
    private Button    addition1,addition2;
    private Button    waterCount,bucketCount;
    private Button    btnEnsure;
    private EditText  etDoorNo;
    private TextView  tvDeliveryTime;
    private TextView  waterTotal,bucketTotal;
    private TextView  waterAmount,bucketAmount;
    private TextView  totalAmount,estateName;
    private TextView  tvWaterPrice, tvBucketPrice;
    private static    double  waterPrice=2.25;
    private static    double  bucketPrice=3.5;
    private int       mWaterCount =1, mBucketCount =1;
    private String    account="";
    private String    communityId="";
    private String    mAddress;
    private String    mWaterTotal;
    private String    waterFee;
    private String    bucketFee;
    private String    bucketFlag="0";
    private String    mWaterPrice="2.25";
    private String    mBucketPrice="";
    private CustomDialog customDialog;
    private int requestCode=0;
    private final PriceHandler priceHandler=new PriceHandler(this);
    private static class PriceHandler extends Handler{
        private WeakReference<OnlinePlaceOrderActivity> mWeakReference;
        public PriceHandler(OnlinePlaceOrderActivity activity) {
            mWeakReference=new WeakReference<OnlinePlaceOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final OnlinePlaceOrderActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                JSONObject json =object.optJSONObject("data");
                                activity.initPriceData(json);
                            }else if (activity.requestCode==1){
                                activity.successDialog();
                            }
                        }else {
                            activity.showdialogs("提示",message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void successDialog() {
        final WaterDialog waterDialog=new WaterDialog(context);
        waterDialog.setImageIcon(R.drawable.waterdialog_success_xh);
        waterDialog.setTitleText("订单提交成功");
        waterDialog.setContentText1("配送人员将与您联系并送水上门");
        waterDialog.getBtn_layout().setVisibility(View.GONE);
        waterDialog.setOnSingleNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
                setResult(0x001);
                finish();
            }
        });
        waterDialog.show();
    }
    private void initPriceData(JSONObject json) {
        waterPrice=json.optDouble("waterPrice");
        bucketPrice=json.optDouble("bucketPrice");
        tvWaterPrice.setText("¥"+waterPrice+"/桶");
        tvBucketPrice.setText("¥"+bucketPrice+"/个");
        calculateData();
    }
    private void showdialogs(String title, String message) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(message)
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
        setContentView(R.layout.activity_online_place_order);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("在线下单");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        initView();
        initData();
        initEvent();
    }
    private void initData() {
        customDialog.show();
        requestCode=0;
        String validateURL= UrlUtil.WaterPrice_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        SendrequestUtil.postDataFromService(validateURL,textParams,priceHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001){
                    String name=data.getStringExtra("name");
                    communityId=data.getStringExtra("id");
                    estateName.setText(name);
                    mAddress=data.getStringExtra("address");
                }
                break;
            default:

                break;
        }
    }
    private void initEvent() {
        subtract1.setOnClickListener(this);
        subtract2.setOnClickListener(this);
        addition1.setOnClickListener(this);
        addition2.setOnClickListener(this);
        btnEnsure.setOnClickListener(this);
    }
    private void initView() {
        btnEnsure =(Button)findViewById(R.id.id_btn_ensure);
        subtract1=(Button)findViewById(R.id.id_subtract_btn1);
        subtract2=(Button)findViewById(R.id.id_subtract_btn2);
        addition1=(Button)findViewById(R.id.id_add_btn1);
        addition2=(Button)findViewById(R.id.id_add_btn2);
        waterCount=(Button)findViewById(R.id.id_tv_num1);
        bucketCount=(Button)findViewById(R.id.id_tv_num2);
        etDoorNo =(EditText)findViewById(R.id.id_door_num);
        tvBucketPrice =(TextView)findViewById(R.id.id_tv_bucketPrice) ;
        tvWaterPrice =(TextView)findViewById(R.id.id_tv_waterPrice) ;
        estateName=(TextView)findViewById(R.id.id_tv_estate) ;
        tvDeliveryTime =(TextView)findViewById(R.id.id_appoint_time);
        waterTotal=(TextView)findViewById(R.id.id_water_num);
        bucketTotal=(TextView)findViewById(R.id.id_bucket_num);
        waterAmount=(TextView)findViewById(R.id.id_water_amount);
        bucketAmount=(TextView)findViewById(R.id.id_bucket_amount);
        totalAmount =(TextView)findViewById(R.id.id_tv_total);
        findViewById(R.id.id_estate_layout).setOnClickListener(this);
        findViewById(R.id.id_layout_deliveryTime).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_subtract_btn1:
                if (mWaterCount >1){
                    mWaterCount--;
                    calculateData();
                }
                break;
            case R.id.id_subtract_btn2:
                if (mBucketCount >=1){
                    mBucketCount--;
                    calculateData();
                }
                break;
            case R.id.id_add_btn1:
                mWaterCount++;
                calculateData();
                break;
            case R.id.id_add_btn2:
                mBucketCount++;
                calculateData();
                break;
            case R.id.id_btn_ensure:
                if (mBucketCount < mWaterCount){
                    dialogTip2();
                }else {
                    judgeData();
                }
                break;
            case R.id.id_estate_layout:
                selectEstate();
                break;
            case R.id.id_layout_deliveryTime:
                selectDate();
                break;
            default:
                break;
        }
    }

    private void selectDate() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new OnSheetButtonOneClickListener() {
                    @Override
                    public void onClick(String text) {
                        tvDeliveryTime.setText(text);
                    }
                }).show();
    }
    private void selectEstate() {
        Intent intent=new Intent(context,SelectEstateActivity.class);
        startActivityForResult(intent,1);
    }
    private void judgeData() {
        if (TextUtils.isEmpty(estateName.getText().toString())){
            ToastUtil.ToastText(context,"请选择您的小区");
        }else if (TextUtils.isEmpty(etDoorNo.getText().toString())){
            ToastUtil.ToastText(context,"请填写您的楼栋和门牌号");
        }else if (TextUtils.isEmpty(tvDeliveryTime.getText().toString())){
            ToastUtil.ToastText(context,"请配送时间");
        }else {
            requestService();
        }
    }
    private void requestService() {
        String sign=getSign();
        String extParams=getExtParams();
        customDialog.show();
        requestCode=1;
        String validateURL= UrlUtil.WaterCreate_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendrequestUtil.postDataFromService(validateURL,textParams,priceHandler);

    }
    private String getSign() {
        String bucketNum=String.valueOf(mBucketCount);
        String waterNum=String.valueOf(mWaterCount);
        String communityName=estateName.getText().toString().trim();
        String doorplate= etDoorNo.getText().toString().trim();
        String applyDeliveryTime= tvDeliveryTime.getText().toString().trim();
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("account",account);
        treeMap.put("amount",mWaterTotal);
        treeMap.put("waterFee",waterFee);
        treeMap.put("bucketFee",bucketFee);
        treeMap.put("bucketFlag",bucketFlag);
        treeMap.put("waterPrice",mWaterPrice);
        treeMap.put("waterNum",waterNum);
        treeMap.put("bucketPrice",mBucketPrice);
        treeMap.put("bucketNum",bucketNum);
        treeMap.put("address",mAddress);
        treeMap.put("communityName",communityName);
        treeMap.put("doorplate",doorplate);
        treeMap.put("communityId",communityId);
        treeMap.put("applyDeliveryTime",applyDeliveryTime);
        return SecretKeyUtil.getKeySign(treeMap);
    }

    private String getExtParams() {
        String bucketNum=String.valueOf(mBucketCount);
        String waterNum=String.valueOf(mWaterCount);
        String communityName=estateName.getText().toString().trim();
        String doorplate= etDoorNo.getText().toString().trim();
        String applyDeliveryTime= tvDeliveryTime.getText().toString().trim();
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",account);
            object.put("amount",mWaterTotal);
            object.put("waterFee",waterFee);
            object.put("bucketFee",bucketFee);
            object.put("bucketFlag",bucketFlag);
            object.put("waterPrice",mWaterPrice);
            object.put("waterNum",waterNum);
            object.put("bucketPrice",mBucketPrice);
            object.put("bucketNum",bucketNum);
            object.put("address",mAddress);
            object.put("communityName",communityName);
            object.put("doorplate",doorplate);
            object.put("communityId",communityId);
            object.put("applyDeliveryTime",applyDeliveryTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return SecretKeyUtil.getKeyextParams(jsonResult);
    }
    private void dialogTip2() {
        final WaterDialog waterDialog=new WaterDialog(context);
        waterDialog.setImageIcon(R.drawable.water_bucket_xh);
        waterDialog.setTitleText("您选购的水桶数量不足");
        waterDialog.setContentText1("请准备好水桶");
        waterDialog.setContentText2("配送人员将会与您联系并上门取桶");
        waterDialog.getBtnEnsure().setVisibility(View.GONE);
        waterDialog.setOnNegativeListener("取消",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });
        waterDialog.setOnpositiveListener("继续下单",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestService();
            }
        });
        waterDialog.show();
    }
    private void calculateData() {
        double waterValue= mWaterCount *waterPrice;
        double bucketValue= mBucketCount *bucketPrice;
        double tatolValue=waterValue+bucketValue;
        waterValue= VariableUtil.twoDecinmal2(waterValue);
        bucketValue= VariableUtil.twoDecinmal2(bucketValue);
        tatolValue= VariableUtil.twoDecinmal2(tatolValue);
        waterFee=String.valueOf(waterValue);
        bucketFee=String.valueOf(bucketValue);
        mWaterTotal=String.valueOf(tatolValue);
        mWaterPrice=String.valueOf(waterPrice);
        mBucketPrice=String.valueOf(bucketPrice);
        if (mBucketCount >=1){
            bucketFlag="1";
        }else {
            bucketFlag="0";
        }
        waterCount.setText(String.valueOf(mWaterCount));
        bucketCount.setText(String.valueOf(mBucketCount));
        waterTotal.setText(String.valueOf("x"+ mWaterCount));
        bucketTotal.setText(String.valueOf("x"+ mBucketCount));
        waterAmount.setText("¥"+waterFee);
        bucketAmount.setText("¥"+bucketFee);
        totalAmount.setText("¥"+mWaterTotal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }

    }
}
