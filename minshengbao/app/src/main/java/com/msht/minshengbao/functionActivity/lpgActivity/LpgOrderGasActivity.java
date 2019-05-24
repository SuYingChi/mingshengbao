package com.msht.minshengbao.functionActivity.lpgActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;
import com.msht.minshengbao.custom.Dialog.ActionSheetDialog;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/5  
 */
public class LpgOrderGasActivity extends BaseActivity implements View.OnClickListener {
    private ButtonM  mButtonSend;
    private TextView tvWeightFiveAmount;
    private TextView tvWeightFifteenAmount;
    private TextView tvWeightFiftyAmount;
    private TextView tvTransportationExpense;
    private TextView tvDeliveryTime;
    private TextView tvElevator;
    private TextView tvAddress;
    private TextView mTextTotal;
    private double weightFiveTotal;
    private double weightFifteenTotal;
    private double weightFiftyTotal;
    private String mTotalAmount;
    private String mDeliveryFeeTotal="0.0";
    private String payAmount;
    private int    weightFiveNum=0;
    private int    weightFifteenNum=0;
    private int    weightFiftyNum=0;
    private String userId;
    private String lpgUserName;
    private String lpgMobile;
    private String lpgUserId;
    private String lpgSex;
    private String siteId;
    private String addressId;
    private String addressName;
    private String addressShort;
    private String longitude;
    private String latitude;
    private String isElevator;
    private String floors;
    private String unit;
    private String roomNum;
    private String city;
    private String area;
    private int requestCode=0;
    private CustomDialog customDialog;
    private static final String PAGE_NAME="提交订单(LPG)";
    /** SELECT_ADDRESS_CODE 地址选择返回标志 */
    private static final int SELECT_ADDRESS_CODE=1;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgOrderGasActivity> mWeakReference;
        public RequestHandler(LpgOrderGasActivity activity) {
            mWeakReference = new WeakReference<LpgOrderGasActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgOrderGasActivity activity=mWeakReference.get();
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
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                JSONObject objectInfo = object.optJSONObject("data");
                                activity.onReceiveData(objectInfo);
                            }else if (activity.requestCode==1){
                                JSONObject objectInfo = object.optJSONObject("data");
                                activity.onReceiveDelivery(objectInfo);
                            }else if (activity.requestCode==2){
                                String orderId=object.optString("orderId");
                                activity.onGoPayActivity();
                            }
                        }else {
                            activity.onFailure(error);
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
    private void onFailure(String error) {
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
    private void onReceiveData(JSONObject object) {
        JSONObject depositPriceObject=object.optJSONObject("depositPrice");
        JSONObject gasPriceObject=object.optJSONObject("gasPrice");
        double doublePrice1=gasPriceObject.optDouble("fivePrice");
        double doublePrice2=gasPriceObject.optDouble("fifteenPrice");
        double doublePrice3=gasPriceObject.optDouble("fiftyPrice");
        double doublePledge1=depositPriceObject.optDouble("fiveDepositPrice");
        double doublePledge2=depositPriceObject.optDouble("fifteenDepositPrice");
        double doublePledge3=depositPriceObject.optDouble("fiftyDepositPrice");
        weightFiveTotal=weightFiveNum*doublePrice1;
        weightFifteenTotal=weightFifteenNum*doublePrice2;
        weightFiftyTotal=weightFiftyNum*doublePrice3;

        weightFiveTotal=VariableUtil.twoDecinmal2(weightFiveTotal);
        weightFifteenTotal=VariableUtil.twoDecinmal2(weightFifteenTotal);
        weightFiftyTotal=VariableUtil.twoDecinmal2(weightFiftyTotal);
        String weightFiveTotalText="¥"+String.valueOf(weightFiveTotal);;
        String weightFifteenTotalText="¥"+String.valueOf(weightFifteenTotal);;
        String weightFiftyTotalText="¥"+String.valueOf(weightFiftyTotal);
        double totalValue=weightFiveTotal+weightFifteenTotal+weightFiftyTotal;
        totalValue= VariableUtil.twoDecinmal2(totalValue);
        onGetDeliveryFee();
        mTotalAmount=String.valueOf(totalValue);
        payAmount=String.valueOf(totalValue);
        String totalText="¥"+mTotalAmount;
        tvWeightFiveAmount.setText(weightFiveTotalText);
        tvWeightFifteenAmount.setText(weightFifteenTotalText);
        tvWeightFiftyAmount.setText(weightFiftyTotalText);
        mTextTotal.setText(totalText);
        mButtonSend.setEnabled(true);
    }
    private void onReceiveDelivery(JSONObject objectInfo) {
        JSONObject deliveryFeeObject=objectInfo.optJSONObject("deliveryFee");
        double fiveDeliveryFee=deliveryFeeObject.optDouble("fiveDeliveryFee");
        double fifteenDeliveryFee=deliveryFeeObject.optDouble("fifteenDeliveryFee");
        double fiftyDeliveryFee=deliveryFeeObject.optDouble("fiftyDeliveryFee");

        double deliveryFeeTotal=fiveDeliveryFee*weightFiveNum+fifteenDeliveryFee*weightFifteenNum+fiftyDeliveryFee*weightFiftyNum;
        deliveryFeeTotal=VariableUtil.twoDecinmal2(deliveryFeeTotal);
        double totalValue=weightFiveTotal+weightFifteenTotal+weightFiftyTotal+deliveryFeeTotal;
        totalValue= VariableUtil.twoDecinmal2(totalValue);
        mDeliveryFeeTotal=String.valueOf(deliveryFeeTotal);
        mTotalAmount=String.valueOf(totalValue);
        String totalText="¥"+mTotalAmount;
        String deliveryTotalText="¥"+mDeliveryFeeTotal;
        mTextTotal.setText(totalText);
        tvTransportationExpense.setText(deliveryTotalText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_order_gas);
        context=this;
        setCommonHeader("提交订单");
        customDialog=new CustomDialog(this, "正在加载");
        lpgMobile= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_MOBILE,"");
        lpgUserName= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_NAME,"");
        lpgUserId=SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_ID,"");
        lpgSex= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.LPG_SEX,"");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        Intent data=getIntent();
        /*weightFiveTotal=data.getDoubleExtra("weightFiveTotal",0);
        weightFifteenTotal=data.getDoubleExtra("weightFifteenTotal",0);
        weightFiftyTotal=data.getDoubleExtra("weightFiftyTotal",0);*/
        weightFiveNum=data.getIntExtra("weightFiveNum",0);
        weightFifteenNum=data.getIntExtra("weightFifteenNum",0);
        weightFiftyNum=data.getIntExtra("weightFiftyNum",0);
        initFondViewId();
    }
    private void initFondViewId() {
        mButtonSend=(ButtonM)findViewById(R.id.id_btn_send) ;
        tvWeightFiveAmount=(TextView)findViewById(R.id.id_tv_amount1);;
        tvWeightFifteenAmount=(TextView)findViewById(R.id.id_tv_amount2);;
        tvWeightFiftyAmount=(TextView)findViewById(R.id.id_tv_amount3);;
        TextView tvWeightFiveNum=(TextView)findViewById(R.id.id_tv_count1);
        TextView tvWeightFifteenNum=(TextView)findViewById(R.id.id_tv_count2);
        TextView tvWeightFiftyNum=(TextView)findViewById(R.id.id_tv_count3);
        tvTransportationExpense=(TextView)findViewById(R.id.id_transportation_expense);
        mTextTotal=(TextView)findViewById(R.id.id_total_amount);
        tvDeliveryTime=(TextView)findViewById(R.id.id_delivery_time);
        tvElevator=(TextView)findViewById(R.id.id_tv_elevator);
        tvAddress=(TextView)findViewById(R.id.id_tv_address);
        TextView tvName=(TextView)findViewById(R.id.id_tv_username);
        TextView tvPhone=(TextView)findViewById(R.id.id_tv_mobile);
        tvWeightFiveNum.setText(String.valueOf(weightFiveNum));
        tvWeightFifteenNum.setText(String.valueOf(weightFifteenNum));
        tvWeightFiftyNum.setText(String.valueOf(weightFiftyNum));
        findViewById(R.id.id_transportation_img).setOnClickListener(this);
        if (weightFiveNum!=0){
            findViewById(R.id.id_layout_five).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.id_layout_five).setVisibility(View.GONE);
        }
        if (weightFifteenNum!=0){
            findViewById(R.id.id_layout_fifteen).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.id_layout_fifteen).setVisibility(View.GONE);
        }
        if (weightFiftyNum!=0){
            findViewById(R.id.id_layout_fifty).setVisibility(View.VISIBLE);

        }else {
            findViewById(R.id.id_layout_fifty).setVisibility(View.GONE);
        }
        String weightFiveTotalText="¥"+String.valueOf(VariableUtil.twoDecinmal2(weightFiveTotal));
        String weightFifteenTotalText="¥"+String.valueOf(VariableUtil.twoDecinmal2(weightFifteenTotal));
        String weightFiftyTotalText="¥"+String.valueOf(VariableUtil.twoDecinmal2(weightFiftyTotal));
        double total=weightFiveTotal+weightFifteenTotal+weightFiftyTotal;
        String totalText="¥"+VariableUtil.twoDecinmal2(total);
       /* tvWeightFiveAmount.setText(weightFiveTotalText);
        tvWeightFifteenAmount.setText(weightFifteenTotalText);
        tvWeightFiftyAmount.setText(weightFiftyTotalText);*/
        mTextTotal.setText(totalText);
        findViewById(R.id.id_select_address_layout).setOnClickListener(this);
        findViewById(R.id.id_select_time_layout).setOnClickListener(this);
        findViewById(R.id.id_transportation_img).setOnClickListener(this);
        tvName.setText(lpgUserName);
        tvPhone.setText(lpgMobile);
        mButtonSend.setOnClickListener(this);
        mButtonSend.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_ADDRESS_CODE:
                if (resultCode==SELECT_ADDRESS_CODE){
                    if (data!=null){
                        siteId=data.getStringExtra("siteId");
                        addressId=data.getStringExtra("addressId");
                        addressName=data.getStringExtra("addressName");
                        addressShort=data.getStringExtra("addressShort");
                        longitude=data.getStringExtra("longitude");
                        latitude=data.getStringExtra("latitude");
                        floors=data.getStringExtra("floor");
                        isElevator=data.getStringExtra("isElevator");
                        unit=data.getStringExtra("unit");;
                        roomNum=data.getStringExtra("roomNum");
                        city=data.getStringExtra("city");
                        area=data.getStringExtra("area");
                        onGetPriceDeposit();
                        tvAddress.setText(addressName);
                        if (isElevator.equals(VariableUtil.VALUE_ONE)){
                            tvElevator.setText("有电梯");
                        }else {
                            tvElevator.setText("无电梯");
                        }
                    }
                }
                break;
                default:
                    break;
        }
    }
    private void onGetPriceDeposit() {
        requestCode=0;
        customDialog.show();
        String requestUrl= UrlUtil.LPG_GAS_AND_DEPOSIT_URL;
        HashMap<String, String> textParams = new HashMap<String, String>(4);
        textParams.put("siteId",siteId);
        textParams.put("addressId",addressId);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onGetDeliveryFee() {
        requestCode=1;
        customDialog.show();
        String requestUrl= UrlUtil.LPG_QUERY_ALL_DELIVERY_FEE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("floors",floors);
        textParams.put("isElevator",isElevator);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_select_address_layout:
                onGoSelectAddress();
                break;
            case R.id.id_select_time_layout:
                onSelectTime();
                break;
            case R.id.id_btn_send:
                if (!TextUtils.isEmpty(tvAddress.getText().toString())){
                    onSendServer();
                }else {
                    onFailure("请选择配送地址");
                }
                break;
            case R.id.id_transportation_img:
                startHtmlWebView();
                break;
            default:
                break;
        }
    }

    private void startHtmlWebView() {
        String url=UrlUtil.LPG_TRANSPORTATION_EXPENSE;
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","运费说明");
        startActivity(intent);
    }
    private void onGoPayActivity() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("订单已提交成功")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void onSendServer() {
        requestCode=2;
        String orderType="1";
        String orderSource="1";
        String deliveryTime=tvDeliveryTime.getText().toString().trim();
        customDialog.show();
        String requestUrl= UrlUtil.LPG_CREATE_NEW_ORDER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",lpgUserId);
        textParams.put("msbUserId",userId);
        textParams.put("realAmount",mTotalAmount);
        textParams.put("orderType",orderType);
        textParams.put("orderSource",orderSource);
        textParams.put("deliveryTime",deliveryTime);
        textParams.put("mobile",lpgMobile);
        textParams.put("sex",lpgSex);
        textParams.put("buyer",lpgUserName);
        textParams.put("fiveBottleCount",String.valueOf(weightFiveNum));
        textParams.put("payFiveAmount",String.valueOf(weightFiveTotal));
        textParams.put("fifteenBottleCount",String.valueOf(weightFifteenNum));
        textParams.put("payFifteenAmount",String.valueOf(weightFifteenTotal));
        textParams.put("fiftyBottleCount",String.valueOf(weightFiftyNum));
        textParams.put("payFiftyAmount",String.valueOf(weightFiftyTotal));
        textParams.put("payAmount",payAmount);
        textParams.put("deliveryAmount",mDeliveryFeeTotal);
        textParams.put("addressName",addressName);
        textParams.put("addressShort",addressShort);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        textParams.put("isElevator",isElevator);
        textParams.put("floor",floors);
        textParams.put("unit",unit);
        textParams.put("roomNum",roomNum);
        textParams.put("city",city);
        textParams.put("area",area);
        String data=textParams.toString();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onSelectTime() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new ActionSheetDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onClick(String text) {
                        tvDeliveryTime.setText(text);
                    }
                }).show();
    }
    private void onGoSelectAddress() {
        Intent intent=new Intent(context,LpgSelectAddressActivity.class);
        startActivityForResult(intent,SELECT_ADDRESS_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
