package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.bestpay.app.PaymentTask;
import com.google.gson.Gson;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.Model.YiPayModel;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.ParamsUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.Public.PaySuccessActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Demo class
 * 燃气账户余额充值
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RechargeValueActivity extends BaseActivity  {
    private EditText etValue;
    private Button   btnRecharge;
    private ListViewForScrollView mListView;
    private String  userId;
    private String  password;
    private String  charge;
    private String  amount;
    private String  channels;
    private String  orderId="";
    private String  id;
    private int requestCode=0;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> payWayList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final MethodHandler methodHandler=new MethodHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<RechargeValueActivity> mWeakReference;
        public RequestHandler(RechargeValueActivity activity) {
            mWeakReference = new WeakReference<RechargeValueActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RechargeValueActivity activity =mWeakReference.get();
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
                            JSONObject jsonObject =object.optJSONObject("data");
                            if (activity.requestCode==0){
                                activity.onReceiveChargeData(jsonObject);
                            }else if (activity.requestCode==1){
                                activity.onRayResult(jsonObject);
                            }else if (activity.requestCode==2){
                                activity.onReceiveCreateOrderData(jsonObject);
                            }
                        }else {
                            activity.onShowNotify("充值提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowNotify("充值提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class MethodHandler extends Handler{
        private WeakReference<RechargeValueActivity> mWeakReference;
        public MethodHandler(RechargeValueActivity activity) {
            mWeakReference = new WeakReference<RechargeValueActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RechargeValueActivity activity =mWeakReference.get();
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
                            activity.onReceivePaywayData(array);
                        }else {
                            activity.onShowNotify("提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowNotify("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceivePaywayData(JSONArray array) {
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
                payWayList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (payWayList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onReceiveCreateOrderData(JSONObject jsonObject) {
        try {
            orderId = jsonObject.optString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestService();
    }
    private void onReceiveChargeData(JSONObject jsonObject) {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (channels){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(RechargeValueActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(RechargeValueActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(RechargeValueActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(RechargeValueActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (amount.equals(VariableUtil.VALUE_ZERO1)|| amount.equals(VariableUtil.VALUE_ZERO2)
                        || amount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x001);
                    onStartActivity("","3");
                   // requestResult();
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
                    setResult(0x001);
                    onStartActivity("","3");
                   // requestResult();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
            default:
                setResult(0x001);
                onStartActivity("","3");
               // requestResult();
                break;
        }
    }
    private void onShowNotify(String title , String string) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }

    private void onRayResult(JSONObject jsonObject) {
        String status=jsonObject.optString("status");
        String lottery="";
        if (jsonObject.has("lottery")){
            lottery=jsonObject.optString("lottery");
        }
        switch (status){
            case VariableUtil.VALUE_ZERO:
                onStartActivity(lottery,"3");
                break;
            case VariableUtil.VALUE_ONE:
                onStartActivity(lottery,"3");
                break;
            case VariableUtil.VALUE_TWO:
                onStartActivity(lottery,"5");
                break;
            case VariableUtil.VALUE_THREE:
                setResult(0x001);
                onShowDialogs("正在支付");
                break;
            default:
                break;
        }
    }
    private void onStartActivity(String lottery, String s) {
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("url",lottery);
        success.putExtra("type",s);
        success.putExtra("orderId",orderId);
        startActivity(success);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_value);
        setCommonHeader("余额充值");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        VariableUtil.MealPos=-1;
        VariableUtil.payPos =-1;
        initView();
        mAdapter=new PayWayAdapter(context,payWayList);
        mListView.setAdapter(mAdapter);
        initData();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                //选择支付方式可点击
                btnRecharge.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=payWayList.get(thisPosition).get("channel");
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnRecharge.setEnabled(true);
                VariableUtil.payPos =position;
                mAdapter.notifyDataSetChanged();
                channels= payWayList.get(position).get("channel");
            }
        });
    }
    private void initData() {
        String  source="wallet_recharge";
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendRequestUtil.postDataFromService(validateURL,textParams,methodHandler);
    }

    private void initView() {
        mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        etValue =(EditText)findViewById(R.id.id_et_value);
        btnRecharge =(Button)findViewById(R.id.id_btn_recharge);
        btnRecharge.setEnabled(false);
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void showTips() {
        amount= etValue.getText().toString().trim();
        if (!TextUtils.isEmpty(amount)){
            new PromptDialog.Builder(this)
                    .setTitle("充值提示")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("请确认是否要充值")
                    .setButton1("取消", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            customDialog.show();
                            placeAnOrder();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            onShowNotify("充值提示","请输入充值金额");
        }
    }
    private void requestService() {
        requestCode=0;
        String type="4";
        String validateURL= UrlUtil.RECHARGE_PAY_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("amount",amount);
        textParams.put("id",orderId);
        textParams.put("channel",channels);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void placeAnOrder(){
        requestCode=2;
        String validateURL= UrlUtil.RECHARGE_CREATE_ORDER_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("amount",amount);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                // 错误信息
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
                            setResult(0x001);
                            onStartActivity("","3");
                           // requestResult();
                            break;
                        case ConstantUtil.VALUE0:
                            onShowDialogs("取消支付");
                            break;
                        case ConstantUtil.VALUE1:
                            onShowDialogs("支付失败");
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
    private void showMsg(String result) {
        switch (result){
            case SendRequestUtil.SUCCESS_VALUE:
                setResult(0x001);
                onStartActivity("","3");
              //  requestResult();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                onShowDialogs("充值失败");
                break;
            case SendRequestUtil.CANCEL_VALUE:
                onShowDialogs("已取消充值");
                break;
                default:
                    onShowDialogs("未知错误");
                    break;
        }
    }
    private void requestResult() {
        requestCode=1;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void onShowDialogs(String str) {
        new PromptDialog.Builder(context)
                .setTitle("充值提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(str)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(1);
                        finish();

                    }
                }).show();
    }
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
