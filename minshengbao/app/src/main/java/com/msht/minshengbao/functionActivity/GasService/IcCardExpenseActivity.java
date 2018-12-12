package com.msht.minshengbao.functionActivity.GasService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
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
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.Public.PaySuccessActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.functionActivity.repairService.RepairPaymentActivity;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.umeng.analytics.MobclickAgent;
import com.unionpay.UPPayAssistEx;

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
 * @date 2017/5/25  
 */
public class IcCardExpenseActivity extends BaseActivity  {
    private Button   btnSend;
    private TextView ttvCustomerNo, tvPrice;
    private TextView tvAddress, tvLastData;
    private TextView tvLastAmount;
    private TextView tvRechargeAmount;
    private TextView tvPurchaseGas;
    private String charge;
    private String purchaseAmount;
    private String type;
    private String mCustomerNo;
    private String channels;
    private String userId;
    private String orderId="";
    private long   time1,overtime;
    private String password;
    private String payId,payTime;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private int requestCode=0;
    private JSONObject jsonObject, expenseObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler =new RequestHandler(this);
    private final ExpenseHandler expenseHandler=new ExpenseHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<IcCardExpenseActivity> mWeakReference;
        public RequestHandler(IcCardExpenseActivity activity) {
            mWeakReference = new WeakReference<IcCardExpenseActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final IcCardExpenseActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.jsonObject =object.optJSONObject("data");
                                activity.onShowBalance();
                            }else if (activity.requestCode==1){
                                activity.jsonObject =object.optJSONObject("data");
                                activity.onReceiveChargeData();
                            }else if (activity.requestCode==2){
                                JSONArray array=object.optJSONArray("data");
                                activity.onPayWayShow(array);
                            }else if (activity.requestCode==3){
                                JSONObject json=object.getJSONObject("data");
                                activity.onPayResult(json);
                            }
                        }else {
                            activity.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class ExpenseHandler extends Handler{
        private WeakReference<IcCardExpenseActivity> mWeakReference;
        private ExpenseHandler(IcCardExpenseActivity activity) {
            mWeakReference = new WeakReference<IcCardExpenseActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final IcCardExpenseActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.expenseObject =object.optJSONObject("data");
                            activity.time1= DateUtils.getCurTimeLong();
                            activity.showRechargeData();
                        }else {
                            activity.onShowIcFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowIcFailure(msg.obj.toString());
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
    private void onReceiveChargeData() {
        try {
            orderId = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (channels){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(IcCardExpenseActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(IcCardExpenseActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(IcCardExpenseActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(IcCardExpenseActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (purchaseAmount.equals(VariableUtil.VALUE_ZERO1)|| purchaseAmount.equals(VariableUtil.VALUE_ZERO2)
                        || purchaseAmount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x002);
                   // onStartSuccess("","1");
                    requestResult();
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
                if (purchaseAmount.equals(VariableUtil.VALUE_ZERO1)|| purchaseAmount.equals(VariableUtil.VALUE_ZERO2)
                        || purchaseAmount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x002);
                   // onStartSuccess("","1");
                    requestResult();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
            default:
                setResult(0x002);
                //onStartSuccess("","1");
                requestResult();
                break;
        }
    }
    private void onShowFailure(String s) {
        new PromptDialog.Builder(context)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void onShowIcFailure(String s) {
        new PromptDialog.Builder(context)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                }).show();
    }
    private void showRechargeData() {
        try {
            purchaseAmount = expenseObject.getString("purchaseAmount");
            String purchaseGas = expenseObject.getString("purchaseGas");
            String lastRechargeAmount = expenseObject.getString("lastRechargeAmount");
            String price= expenseObject.getString("price");
            String address= expenseObject.getString("address");
            overtime= expenseObject.optLong("countdown");
            String lastRechargeDate= expenseObject.getString("lastRechargeDate");
            mCustomerNo = expenseObject.getString("customerNo");
            tvAddress.setText(address);
            tvLastAmount.setText("¥"+lastRechargeAmount);
            tvPrice.setText(price);
            tvPurchaseGas.setText(purchaseGas+"立方");
            tvRechargeAmount.setText("¥"+purchaseAmount);
            tvLastData.setText(lastRechargeDate);
            ttvCustomerNo.setText(mCustomerNo);
        }catch (JSONException e){
            e.printStackTrace();
        }
        initData();
    }
    private void onShowBalance() {
        double doubleBalance=jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(purchaseAmount);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initPayWay();
    }
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String lottery=json.optString("lottery");
        switch (status){
            case ConstantUtil.VALUE_ZERO:
                onStartSuccess(lottery,"1");
                break;
            case ConstantUtil.VALUE_ONE:
                onStartSuccess(lottery,"1");
                break;
            case ConstantUtil.VALUE_TWO:
                onStartSuccess(lottery,"0");
                break;
            case ConstantUtil.VALUE_THREE:
                onShowDialogs("正在支付");
                break;
                default:
                    break;
        }
    }
    private void onStartSuccess(String lottery, String type) {
        String pageUrl=UrlUtil.APP_PAY_SUCCESS_PAGE +"userId="+userId+"&event_code=iccard_pay_success";
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("type",type);
        success.putExtra("url",lottery);
        success.putExtra("pageUrl",pageUrl);
        success.putExtra("navigation","IC卡充值");
        startActivity(success);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_card_expense);
        context=this;
        mPageName="IC卡燃气费用";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        payId=getIntent().getStringExtra("payId");
        payTime=getIntent().getStringExtra("payTime");
        type="6";
        VariableUtil.payPos =-1;
        initView();
        mAdapter=new PayWayAdapter(context, mList);
        forScrollView.setAdapter(mAdapter);
        initIcData();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels= mList.get(thisPosition).get("channel");
            }
        });
    }
    private void initView() {
        ttvCustomerNo =(TextView)findViewById(R.id.id_customerNo);
        tvPrice =(TextView)findViewById(R.id.id_tv_price);
        tvAddress =(TextView)findViewById(R.id.id_address_text);
        tvLastData =(TextView)findViewById(R.id.id_last_time);
        tvLastAmount =(TextView)findViewById(R.id.id_last_amount);
        tvRechargeAmount =(TextView)findViewById(R.id.id_tv_rechargeAmount);
        tvPurchaseGas =(TextView)findViewById(R.id.id_rechargeGas);
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        TextView tvBalance =(TextView)findViewById(R.id.id_tv_balance);
        btnSend =(Button)findViewById(R.id.id_btn_pay) ;
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void initIcData() {
        customDialog.show();
        String validateURL= UrlUtil.IC_RECHARGE_BILL_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("payId",payId);
        textParams.put("payTime",payTime);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams,expenseHandler);
    }
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams,requestHandler);
    }
    private void showTips() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否要进行充值")
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestCode=1;
                        dialog.dismiss();
                        if (overTime()){
                            customDialog.show();
                            requestService();
                        }else {
                            onTipShow();
                        }

                    }
                })
                .show();
    }
    private void onTipShow() {
        new PromptDialog.Builder(context)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("时间超时")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        Intent intent=new Intent(IcCardExpenseActivity.this, QrCodeScanActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).show();
    }

    private boolean overTime() {
        long time2= DateUtils.getCurTimeLong();
        long time=time2-time1;
        long second=time/1000;
        return second<overtime;
    }
    private void requestService() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount",purchaseAmount);
        textParams.put("customerNo", mCustomerNo);
        textParams.put("payId",payId);
        textParams.put("channel",channels);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams,requestHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==Pingpp.REQUEST_CODE_PAYMENT) {
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
                // 错误信息
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
                           // onStartSuccess("","2");
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
    private void showMsg(String result) {
        switch (result){
            case SendRequestUtil.SUCCESS_VALUE:
                setResult(0x002);
               // onStartSuccess("","1");
                requestResult();
                break;
            case SendRequestUtil.CANCEL_VALUE:
                onShowDialogs("已取消充值");
                break;
            case SendRequestUtil.FAILURE_VALUE:
                onShowDialogs("充值失败");
                break;
            default:
                onShowDialogs("未知错误");
                break;
        }
    }
    private void onShowDialogs(String str) {
        new PromptDialog.Builder(context)
                .setTitle("缴费提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(str)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }

    private void initPayWay() {
        requestCode=2;
        String source="";
        String validateURL= UrlUtil.PAY_METHOD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams,requestHandler);
    }
    private void requestResult() {
        requestCode=3;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
