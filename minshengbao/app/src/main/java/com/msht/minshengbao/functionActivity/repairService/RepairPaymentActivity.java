package com.msht.minshengbao.functionActivity.repairService;

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
import android.widget.TextView;

import com.bestpay.app.PaymentTask;
import com.google.gson.Gson;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.Model.YiPayModel;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.ParamsUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.events.UpdateDataEvent;
import com.msht.minshengbao.functionActivity.publicModule.PaySuccessActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.ListViewForScrollView;
import com.pingplusplus.android.Pingpp;
import com.unionpay.UPPayAssistEx;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/28 
 */
public class RepairPaymentActivity extends BaseActivity  {
    private Button   btnSend;
    private String  userId,password,couponId="0";
    private String realMoney,channel,orderNo,type,orderId;
    private String id ;
    private String charge;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private int requestCode=0;
    private CustomDialog customDialog;
    private final ChargeHandler chargeHandler=new ChargeHandler(this);
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<RepairPaymentActivity> mWeakReference;
        public RequestHandler(RepairPaymentActivity activity) {
            mWeakReference = new WeakReference<RepairPaymentActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepairPaymentActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestCode==0){
                                JSONArray jsonArray =object.getJSONArray("data");
                                reference.onPayWayShow(jsonArray);
                            }else if (reference.requestCode==2){
                                JSONObject json=object.getJSONObject("data");
                                reference.onPayResult(json);
                            }
                        }else {
                            reference.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.onShowFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class ChargeHandler extends Handler{
        private WeakReference<RepairPaymentActivity> mWeakReference;
        private ChargeHandler(RepairPaymentActivity activity) {
            mWeakReference = new WeakReference<RepairPaymentActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepairPaymentActivity activity =mWeakReference.get();
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject =object.optJSONObject("data");
                            if (activity.requestCode==0){
                                activity.onShowBalance(jsonObject);
                            }else if (activity.requestCode==1){
                                activity.onReceiveChargeData(jsonObject);
                            }
                        }else {
                            activity.onShowFailure(error);
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
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String lottery=json.optString("lottery");
        switch (status){
            case VariableUtil.VALUE_ZERO:
                onStartSuccess("1",lottery);
                break;
            case VariableUtil.VALUE_ONE:
                onStartSuccess("1",lottery);
                break;
            case VariableUtil.VALUE_TWO:
                onStartSuccess("0",lottery);
                break;
            case VariableUtil.VALUE_THREE:
                onShowDialogs("正在支付");
                break;
                default:
                    break;
        }
    }
    private void onStartSuccess(String successType, String lottery) {
        EventBus.getDefault().post(new UpdateDataEvent(true));
        String pageUrl=UrlUtil.APP_PAY_SUCCESS_PAGE +"userId="+userId+"&event_code=repair_pay_success"+"&event_relate_id="+orderId;
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("type",successType);
        success.putExtra("url",lottery);
        success.putExtra("pageUrl",pageUrl);
        success.putExtra("navigation","支付成功");
        startActivity(success);
        finish();
    }
    private void onPayWayShow(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
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
    private void onShowBalance(JSONObject jsonObject) {
        double doubleBalance= jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(realMoney);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initPayWay();
    }
    private void onShowFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("支付提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveChargeData(JSONObject jsonObject) {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (channel){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(RepairPaymentActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(RepairPaymentActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(RepairPaymentActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(RepairPaymentActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (realMoney.equals(VariableUtil.VALUE_ZERO1)|| realMoney.equals(VariableUtil.VALUE_ZERO2)
                        || realMoney.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x005);
                    onStartSuccess("1","");
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
                if (realMoney.equals(VariableUtil.VALUE_ZERO1)|| realMoney.equals(VariableUtil.VALUE_ZERO2)
                        || realMoney.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x005);
                    onStartSuccess("1","");
                    //requestResult();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
            default:
                setResult(0x005);
                onStartSuccess("1","");
               // requestResult();
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_payment);
        setCommonHeader("维修支付");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        orderNo=data.getStringExtra("orderNo");
        orderId=data.getStringExtra("orderId");
        couponId=data.getStringExtra("couponId");
        realMoney =data.getStringExtra("realMoney");
        type="2";
        VariableUtil.payPos=-1;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        mAdapter=new PayWayAdapter(context, mList);
        forScrollView.setAdapter(mAdapter);
        initData();
        initEvent();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channel= mList.get(thisPosition).get("channel");
            }
        });
        forScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =position;
                mAdapter.notifyDataSetChanged();
                channel= mList.get(position).get("channel");
            }
        });
    }
    private void initFindViewId() {
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        TextView tvBalance =(TextView)findViewById(R.id.id_tv_balance);
        btnSend =(Button)findViewById(R.id.id_evaluate_order);
        btnSend.setEnabled(false);
        TextView tvRealAmount =(TextView)findViewById(R.id.id_real_fee);
        TextView tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
        tvRealAmount.setText("¥"+ realMoney);
        tvOrderNo.setText(orderNo);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            //if (requestCode == REQUEST_CODE_PAYMENT){
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
                if (channel.equals(ConstantUtil.VALUE_TEN)){
                    String result = data.getStringExtra("pay_result");
                    showMsg(result);
                }else {
                    switch (resultCode){
                        case ConstantUtil.VALUE_MINUS1:
                            setResult(0x005);
                            //onStartSuccess("1","");
                            requestResult();
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
    private void showMsg(String title) {
        switch (title){
            case SendRequestUtil.SUCCESS_VALUE:
                setResult(0x005);
               // onStartSuccess("1","");
                requestResult();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                onShowDialogs("支付失败");
                break;
            case SendRequestUtil.CANCEL_VALUE:
                onShowDialogs("已取消支付");
                break;
                default:
                    onShowDialogs("未知错误");
                    break;
        }
    }
    private void onShowDialogs(String str) {
        new PromptDialog.Builder(this)
                .setTitle("支付提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(str)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,chargeHandler);
    }
    private void initEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
    }
    private void showTips() {
        new PromptDialog.Builder(context)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否要进行缴费")
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
                        requestCode=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount", realMoney);
        textParams.put("orderId",orderId);
        if (!TextUtils.isEmpty(couponId)) {
            textParams.put("couponId", couponId);
        }
        textParams.put("channel",channel);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,chargeHandler);
    }
    private void initPayWay() {
        String source="repair_order";
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
