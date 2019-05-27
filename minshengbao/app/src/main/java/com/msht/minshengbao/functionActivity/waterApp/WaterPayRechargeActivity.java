package com.msht.minshengbao.functionActivity.waterApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bestpay.app.PaymentTask;
import com.google.gson.Gson;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.Model.YiPayModel;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.ParamsUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.Dialog.WaterRedPacketDialog;
import com.msht.minshengbao.custom.widget.ListViewForScrollView;
import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.functionActivity.htmlWeb.AgreeTreatyActivity;
import com.msht.minshengbao.functionActivity.publicModule.PaySuccessActivity;
import com.pingplusplus.android.Pingpp;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterPayRechargeActivity extends BaseActivity {
    private String userId;
    private String password;
    private String userPhone;
    private String amount="0.0";
    private String realAmount="0.0";
    private double doubleBalance;
    private String giveFee;
    private String orderId;
    private String couponCode="";
    private String packId;
    private String childType="0";
    private String channels;
    private int requestCode=0;
    private String charge,id;
    private EditText etRecommend;
    private TextView tvAmount;
    private TextView tvOriginAmount;
    private TextView tvDescribeText;
    private Button   btnSend;
    private ListViewForScrollView mListView;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private final MethodHandler methodHandler=new MethodHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class BalanceHandler extends Handler {
        private WeakReference<WaterPayRechargeActivity> mWeakReference;
        public BalanceHandler(WaterPayRechargeActivity actvity) {
            mWeakReference=new WeakReference<WaterPayRechargeActivity>(actvity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterPayRechargeActivity activity=mWeakReference.get();
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
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject json =object.optJSONObject("data");
                            if (activity.requestCode==0){
                                activity.onShowBalance(json);
                            }else if (activity.requestCode==1){
                                activity.onShowCharge(json);
                            }else if (activity.requestCode==2){
                                activity.onPayResult(json);
                            }
                        }else {
                            if (activity.requestCode==1){
                                error = object.optString("message");
                            }
                            activity.onShowDialog("充值提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowDialog("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class RequestHandler extends Handler{
        private WeakReference<WaterPayRechargeActivity> mWeakReference;
        public RequestHandler(WaterPayRechargeActivity actvity) {
            mWeakReference=new WeakReference<WaterPayRechargeActivity>(actvity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterPayRechargeActivity activity=mWeakReference.get();
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
                        String message = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.orderId =object.optString("data");
                            activity.msbAppPay();
                        }else {
                            CustomToast.showWarningLong(message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    CustomToast.showErrorLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class MethodHandler extends Handler{
        private WeakReference<WaterPayRechargeActivity> mWeakReference;
        public MethodHandler(WaterPayRechargeActivity activity) {
            mWeakReference=new WeakReference<WaterPayRechargeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterPayRechargeActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray array =object.getJSONArray("data");
                            activity.onPayWayShow(array);
                        }else {
                            CustomToast.showWarningLong(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    CustomToast.showErrorLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onPayWayShow(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                String tips = json.getString("tips");
                String name=json.getString("name");
                String code=json.getString("code");
                String channel=json.getString("channel");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tips", tips);
                map.put("name",name);
                map.put("code",code);
                map.put("channel",channel);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onShowBalance(JSONObject json) {
        doubleBalance=json.optDouble("balance");
        VariableUtil.balance=json.optString("balance");
        contrastBalance(amount);
        initPayMethod();
    }
    private void onShowCharge(JSONObject json) {
        try {
            id = json.optString("id");
            charge = json.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (channels){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(WaterPayRechargeActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(WaterPayRechargeActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(WaterPayRechargeActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(WaterPayRechargeActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (amount.equals(VariableUtil.VALUE_ZERO1)|| amount.equals(VariableUtil.VALUE_ZERO2)
                        || amount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x002);
                    requestResult();
                   // rechargeSuccess();
                }else {
                    /**
                     * channels=10
                     * 云闪付
                     */
                    String mode= BuildConfig.PAY_MODE;
                    int ret = UPPayAssistEx.startPay(this, null, null, charge, mode);
                }
                break;
            case ConstantUtil.VALUE_TWELVE:
                /**
                 * channels=12
                 * 翼支付
                 */
                if (amount.equals(VariableUtil.VALUE_ZERO1)|| amount.equals(VariableUtil.VALUE_ZERO2)
                        || amount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x002);
                    requestResult();
                   // rechargeSuccess();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
                default:
                    setResult(0x002);
                    requestResult();
                   // rechargeSuccess();
                    break;
        }
    }
    private void rechargeSuccess() {
        Intent success=new Intent(context,WaterSuccessActivity.class);
        success.putExtra("amount",amount);
        success.putExtra("giveFee",giveFee);
        startActivity(success);
    }
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        switch (status){
            case VariableUtil.VALUE_ZERO:
                onRechargeSuccess("","1");
                break;
            case VariableUtil.VALUE_ONE:
                onRechargeSuccess("","1");
                break;
            case VariableUtil.VALUE_TWO:
                CustomToast.showWarningDialog("支付失败");
                break;
            case VariableUtil.VALUE_THREE:
                CustomToast.showWarningDialog("正在支付");
                break;
            default:
                break;
        }
    }
    private void onShowDialog(String title, String str) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(str)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onRechargeSuccess(String lottery, String s){
        String pageUrl=UrlUtil.WATER_RECHARGE_SUCCESS_PAGE+"phone="+VariableUtil.waterAccount+"&payFee="+amount+"&giveFee="+giveFee;
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("url",lottery);
        success.putExtra("pageUrl",pageUrl);
        success.putExtra("type",s);
        success.putExtra("navigation","水宝充值");
        startActivity(success);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_pay_recharge);
        context=this;
        mPageName="支付订单(水宝)";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        VariableUtil.balance=null;
        VariableUtil.MealPos=-1;
        VariableUtil.payPos =-1;
        VariableUtil.mPos=-1;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userPhone =SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent data=getIntent();
        if (data!=null){
            amount=data.getStringExtra("amount");
            realAmount=data.getStringExtra("realAmount");;
            giveFee=data.getStringExtra("giveFee");
            packId=data.getStringExtra("packId");
            childType=data.getStringExtra("childType");
            couponCode=data.getStringExtra("couponCode");
        }
        initFindViewId();
        mAdapter=new PayWayAdapter(context, mList);
        mListView.setAdapter(mAdapter);
        initBalance();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels= mList.get(thisPosition).get("channel");
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =position;
                mAdapter.notifyDataSetChanged();
                channels= mList.get(position).get("channel");
            }
        });
    }
    private void contrastBalance(String amount) {
        if (!TextUtils.isEmpty(amount)){
            double doubleAmount=Double.valueOf(amount);
            if (doubleAmount>doubleBalance){
                VariableUtil.balance="余额不足";
            }else {
                VariableUtil.balance=String.valueOf(doubleBalance);
            }
        }else {
            VariableUtil.balance="余额不足";
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            //if (requestCode == REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK) {
                String result=data.getStringExtra("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getStringExtra("error_msg");
                String extraMsg = data.getStringExtra("extra_msg");
                showMsg(result);
            }
        }else {
            if (data!=null){
                if (channels.equals(ConstantUtil.VALUE_TEN)){
                    String result = data.getStringExtra("pay_result");
                    showMsg(result);
                }else {
                    switch (resultCode){
                        case ConstantUtil.VALUE_MINUS1:
                            setResult(0x002);
                            requestResult();
                          //  rechargeSuccess();
                            break;
                        case ConstantUtil.VALUE0:
                            CustomToast.showWarningDialog("取消支付");
                            break;
                        case ConstantUtil.VALUE1:
                            CustomToast.showWarningDialog("支付失败");
                            break;
                        default:
                            String result = data.getStringExtra("result");
                            ToastUtil.ToastText(context,result);
                            break;
                    }
                }
            }
        }
    }
    private void showMsg(String title) {
        switch (title){
            case SendRequestUtil.SUCCESS_VALUE:
                setResult(0x002);
                requestResult();
               // rechargeSuccess();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                CustomToast.showWarningDialog("充值失败");
                break;
            case SendRequestUtil.CANCEL_VALUE:
                CustomToast.showWarningDialog("已取消充值");
                break;
            default:
                CustomToast.showErrorDialog("未知错误");
                break;
        }
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendRequestUtil.postDataFromServiceTwo(validateURL,textParams,balanceHandler);
    }
    private void initBalance() {
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initPayMethod() {
        String source="water_recharge";
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendRequestUtil.postDataFromService(validateURL,textParams,methodHandler);
    }
    private void msbAppPay() {
        requestCode=1;
        String validateURL= UrlUtil.WATER_PAY_ORDER_URL;
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("type","8");
        treeMap.put("amount",realAmount);
        treeMap.put("orderId",orderId);
        treeMap.put("channel",channels);
        String sign=SecretKeyUtil.getKeySign(treeMap);
        JSONObject object=new JSONObject();
        try{
            object.put("type","8");
            object.put("amount",realAmount);
            object.put("orderId",orderId);
            object.put("channel",channels);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String extParams =SecretKeyUtil.getKeyextParams(object.toString());
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initFindViewId() {
        etRecommend=(EditText)findViewById(R.id.id_et_recommend);
        tvOriginAmount=(TextView)findViewById(R.id.id_origin_Amount);
        tvAmount=(TextView)findViewById(R.id.id_tv_amount);
        tvDescribeText=(TextView)findViewById(R.id.id_coupon_describe) ;
        TextView tvMealTip=(TextView)findViewById(R.id.id_meal_tip) ;
        btnSend =(Button)findViewById(R.id.id_btn_send);
        btnSend.setEnabled(false);
        String amountText="¥"+amount;
        String realAmountText="¥"+realAmount;
        tvOriginAmount.setText(amountText);
        tvOriginAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String mealTipText="充值"+amount+"元赠送"+giveFee+"元";
        tvMealTip.setText(mealTipText);
        tvAmount.setText(realAmountText);
        mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        findViewById(R.id.id_back_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= UrlUtil.RECHARGE_BACK_AGREE;
                Intent intent=new Intent(context, AgreeTreatyActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("navigation","充返活动协议");
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendServiceShowTip();
            }
        });
        findViewById(R.id.id_coupon_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCouponDialog();
            }
        });
    }

    private void onCouponDialog() {
        new WaterRedPacketDialog(context,amount).builder()
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new WaterRedPacketDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onSelectClick(String code, String discountAmount) {
                        couponCode=code;
                        realAmount=String.valueOf(TypeConvertUtil.convertToDouble(amount,0)-TypeConvertUtil.convertToDouble(discountAmount,0));
                        String amountText="¥"+realAmount;
                        tvAmount.setText(amountText);
                        if (TypeConvertUtil.convertToDouble(discountAmount,0)==0){
                            tvOriginAmount.setVisibility(View.GONE);
                            String describe="未使用红包优惠券";
                            tvDescribeText.setText(describe);
                        }else {
                            String describe="充值红包满"+amount+"减"+discountAmount;
                            tvOriginAmount.setVisibility(View.VISIBLE);
                            tvDescribeText.setText(describe);
                        }
                    }
                })
                .show();
    }
    private void onSendServiceShowTip() {
        customDialog.show();
        requestCode=1;
        requestService();
    }
    private void requestService() {
        String sign=getSign();
        String extParams=getExtParams();;
        String validateURL= UrlUtil.WATER_CARD_RECHARGE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private String getExtParams() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String icType="1";
        String orderFrom="0";
        String payTime=DateUtils.getDateToString(time,pattern);
        String recommendCode=etRecommend.getText().toString().trim();
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",VariableUtil.waterAccount);
            object.put("type",icType);
            object.put("payFee",amount);
            object.put("giveFee",giveFee);
            object.put("payType",channels);
            object.put("payTime",payTime);
            object.put("payAccount",VariableUtil.waterAccount);
            object.put("orderFrom",orderFrom);
            object.put("recommendCode",recommendCode);
            object.put("packId",packId);
            object.put("couponCode",couponCode);
            object.put("childType",childType);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return SecretKeyUtil.getKeyextParams(jsonResult);
    }
    private String getSign() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String icType="1";
        String orderFrom="0";
        String payTime=DateUtils.getDateToString(time,pattern);
        String recommendCode=etRecommend.getText().toString().trim();
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("type",icType);
        treeMap.put("account",VariableUtil.waterAccount);
        treeMap.put("payFee",amount);
        treeMap.put("giveFee",giveFee);
        treeMap.put("payType",channels);
        treeMap.put("payTime",payTime);
        treeMap.put("orderFrom",orderFrom);
        treeMap.put("payAccount",VariableUtil.waterAccount);
        treeMap.put("recommendCode",recommendCode);
        treeMap.put("packId",packId);
        treeMap.put("couponCode",couponCode);
        treeMap.put("childType",childType);
        return SecretKeyUtil.getKeySign(treeMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
