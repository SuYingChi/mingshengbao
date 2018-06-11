package com.msht.minshengbao.FunctionActivity.WaterApp;

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
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OnlinePlaceOrder extends BaseActivity implements View.OnClickListener {
    private Button    subtract1,subtract2;
    private Button    addition1,addition2;
    private Button    waterCount,bucketCount;
    private Button    btn_ensure;
    private EditText  et_doorNo;
    private TextView  tv_estate,tv_deliveryTime;
    private TextView  waterTotal,bucketTotal;
    private TextView  waterAmount,bucketAmount;
    private TextView  totalAmount,estateName;
    private TextView  tv_watrePrice,tv_bucketPrice;
    private static    double  waterPrice=2.25;
    private static    double  bucketPrice=3.5;
    private int       mWatercount=1,mBucketcount=1;
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
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requestCode=0;
    Handler priceHandler = new Handler() {
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
                            if (requestCode==0){
                                JSONObject json =object.optJSONObject("data");
                                initPriceData(json);
                            }else if (requestCode==1){
                                successDialog();
                            }
                        }else {
                            showdialogs("提示",message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    ToastUtil.ToastText(context,msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
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
        tv_watrePrice.setText("¥"+waterPrice+"/桶");
        tv_bucketPrice.setText("¥"+bucketPrice+"/个");
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
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                priceHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                priceHandler.sendMessage(msg);
            }
        });
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
        btn_ensure.setOnClickListener(this);
    }
    private void initView() {
        btn_ensure=(Button)findViewById(R.id.id_btn_ensure);
        subtract1=(Button)findViewById(R.id.id_subtract_btn1);
        subtract2=(Button)findViewById(R.id.id_subtract_btn2);
        addition1=(Button)findViewById(R.id.id_add_btn1);
        addition2=(Button)findViewById(R.id.id_add_btn2);
        waterCount=(Button)findViewById(R.id.id_tv_num1);
        bucketCount=(Button)findViewById(R.id.id_tv_num2);
        et_doorNo=(EditText)findViewById(R.id.id_door_num);
        tv_bucketPrice=(TextView)findViewById(R.id.id_tv_bucketPrice) ;
        tv_watrePrice=(TextView)findViewById(R.id.id_tv_waterPrice) ;
        tv_estate=(TextView)findViewById(R.id.id_tv_estate);
        tv_deliveryTime=(TextView)findViewById(R.id.id_appoint_time);
        waterTotal=(TextView)findViewById(R.id.id_water_num);
        bucketTotal=(TextView)findViewById(R.id.id_bucket_num);
        waterAmount=(TextView)findViewById(R.id.id_water_amount);
        bucketAmount=(TextView)findViewById(R.id.id_bucket_amount);
        totalAmount =(TextView)findViewById(R.id.id_tv_total);
        estateName=(TextView)findViewById(R.id.id_tv_estate);
        findViewById(R.id.id_estate_layout).setOnClickListener(this);
        findViewById(R.id.id_layout_deliveryTime).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_subtract_btn1:
                if (mWatercount>1){
                    mWatercount--;
                    calculateData();
                }
                break;
            case R.id.id_subtract_btn2:
                if (mBucketcount>=1){
                    mBucketcount--;
                    calculateData();
                }
                break;
            case R.id.id_add_btn1:
                mWatercount++;
                calculateData();
                break;
            case R.id.id_add_btn2:
                mBucketcount++;
                calculateData();
                break;
            case R.id.id_btn_ensure:
                if (mBucketcount<mWatercount){
                    DialogTip2();
                }else {
                    JudgeData();
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
                        tv_deliveryTime.setText(text);
                    }
                }).show();
    }
    private void selectEstate() {
        Intent intent=new Intent(context,SelectEstate.class);
        startActivityForResult(intent,1);
    }
    private void JudgeData() {
        if (TextUtils.isEmpty(tv_estate.getText().toString())){
            ToastText("请选择您的小区");
        }else if (TextUtils.isEmpty(et_doorNo.getText().toString())){
            ToastText("请填写您的楼栋和门牌号");
        }else if (TextUtils.isEmpty(tv_deliveryTime.getText().toString())){
            ToastText("请配送时间");
        }else {
            requestService();
        }
    }
    private void requestService() {
        String sign=getSign();
        String extParams=getextParams();
        customDialog.show();
        requestCode=1;
        String validateURL= UrlUtil.WaterCreate_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                priceHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                priceHandler.sendMessage(msg);
            }
        });

    }
    private String getSign() {
        String bucketNum=String.valueOf(mBucketcount);
        String waterNum=String.valueOf(mWatercount);
        String communityName=estateName.getText().toString().trim();
        String doorplate=et_doorNo.getText().toString().trim();
        String applyDeliveryTime=tv_deliveryTime.getText().toString().trim();
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
        String sign= SecretKeyUtil.getKeySign(treeMap);
        return sign;
    }

    private String getextParams() {
        String bucketNum=String.valueOf(mBucketcount);
        String waterNum=String.valueOf(mWatercount);
        String communityName=estateName.getText().toString().trim();
        String doorplate=et_doorNo.getText().toString().trim();
        String applyDeliveryTime=tv_deliveryTime.getText().toString().trim();
        String jsonresult="";
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
            jsonresult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        String extParams=SecretKeyUtil.getKeyextParams(jsonresult);
        return extParams;
    }
    private void ToastText(String s) {
        Toast.makeText(context,s ,Toast.LENGTH_SHORT).show();
    }
    private void DialogTip2() {
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
        double waterValue=mWatercount*waterPrice;
        double bucketValue=mBucketcount*bucketPrice;
        double tatolValue=waterValue+bucketValue;
        waterValue= VariableUtil.twoDecinmal2(waterValue);
        bucketValue= VariableUtil.twoDecinmal2(bucketValue);
        tatolValue= VariableUtil.twoDecinmal2(tatolValue);
        waterFee=String.valueOf(waterValue);
        bucketFee=String.valueOf(bucketValue);
        mWaterTotal=String.valueOf(tatolValue);
        mWaterPrice=String.valueOf(waterPrice);
        mBucketPrice=String.valueOf(bucketPrice);
        if (mBucketcount>=1){
            bucketFlag="1";
        }else {
            bucketFlag="0";
        }
        waterCount.setText(String.valueOf(mWatercount));
        bucketCount.setText(String.valueOf(mBucketcount));
        waterTotal.setText(String.valueOf("x"+mWatercount));
        bucketTotal.setText(String.valueOf("x"+mBucketcount));
        waterAmount.setText("¥"+waterFee);
        bucketAmount.setText("¥"+bucketFee);
        totalAmount.setText("¥"+mWaterTotal);
    }
}
