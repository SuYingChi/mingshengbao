package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

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
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.functionActivity.repairService.RepairPaymentActivity;
import com.pingplusplus.android.Pingpp;
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
 * @date 2018/7/9  
 */
public class LpgPayOrderActivity extends BaseActivity {
    private Button btnSend;
    private String userId;
    private String msbUserId;
    private String password;
    private String payChannel;
    private String orderId;
    private String realAmount;
    private static final String PAGE_NAME="支付订单(LPG)";
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private PayWayAdapter mAdapter;
    private CustomDialog customDialog;
    private JSONArray jsonArray;
    private int requestCode=0;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgPayOrderActivity> mWeakReference;
        public RequestHandler(LpgPayOrderActivity activity) {
            mWeakReference = new WeakReference<LpgPayOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgPayOrderActivity activity=mWeakReference.get();
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
                                JSONObject jsonObject =object.optJSONObject("data");
                                activity.onGetBalanceData(jsonObject);
                            }else if (activity.requestCode==1){
                                activity.jsonArray =object.getJSONArray("data");
                                activity.onGetPayWayData();
                            }else if (activity.requestCode==2){
                                JSONObject jsonObject=object.optJSONObject("data");
                                activity.onChargePayWay(jsonObject);
                            }else if (activity.requestCode==3){
                                JSONObject json=object.getJSONObject("data");
                                activity.onPayResult(json);
                            }
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onGetBalanceData(JSONObject jsonObject) {
        double doubleBalance=jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(realAmount);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        onPayWayData();
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
    private void onPayResult(JSONObject json) {
        String orderId=json.optString("orderId");
        int orderStatus=json.optInt("orderStatus");
        if (orderStatus==3){
            onSuccessActivity(orderId,"7");
        }else {
            onSuccessActivity(orderId,"5");
        }
    }
    private void onSuccessActivity(String orderId, String s) {
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("type",s);
        success.putExtra("url","");
        success.putExtra("orderId",orderId);
        startActivity(success);
        finish();
    }
    private void onGetPayWayData() {
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
    private void onChargePayWay(JSONObject jsonObject) {
        String charge=jsonObject.optString("charge");
        switch (payChannel){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(LpgPayOrderActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(LpgPayOrderActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(LpgPayOrderActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(LpgPayOrderActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (realAmount.equals(VariableUtil.VALUE_ZERO1)|| realAmount.equals(VariableUtil.VALUE_ZERO2)
                        || realAmount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x001);
                    onDelayRequest();
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
                if (realAmount.equals(VariableUtil.VALUE_ZERO1)|| realAmount.equals(VariableUtil.VALUE_ZERO2)
                        || realAmount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(0x001);
                    onDelayRequest();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
            default:
                setResult(0x001);
                onDelayRequest();
                break;
        }
    }
    private void requestResult() {
        requestCode=3;
        String orderType="1";
        String validateURL= UrlUtil.LPG_QUERY_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        textParams.put("orderType",orderType);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_pay_order);
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        if (data!=null){
            orderId=data.getStringExtra("orderId");
            realAmount=data.getStringExtra("realAmount");
        }
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("支付订单");
        VariableUtil.payPos =-1;
        initFindViewId();
        ListViewForScrollView mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        mAdapter=new PayWayAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        initBalanceData();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                payChannel=mList.get(thisPosition).get("channel");
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =position;
                mAdapter.notifyDataSetChanged();
                payChannel=mList.get(position).get("channel");
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendService();
            }
        });
    }

    private void onSendService() {
        requestCode=2;
        customDialog.setDialogContent("正在加载");
        customDialog.show();
        String validateURL= UrlUtil.LPG_ORDER_PAY;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("msbUserId",userId);
        textParams.put("Id",orderId);
        textParams.put("payChannel",payChannel);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initBalanceData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(context).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onPayWayData() {
        requestCode=1;
        String source="app_lpg_pay_method";
        customDialog.setDialogContent("正在加载");
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        TextView tvOrderId=(TextView)findViewById(R.id.id_tv_order);
        TextView tvRealAmount=(TextView)findViewById(R.id.id_tv_real_amount);
        String orderIdText="送气订单："+orderId;
        String amountText="¥"+realAmount;
        tvOrderId.setText(orderIdText);
        tvRealAmount.setText(amountText);
        btnSend=(Button)findViewById(R.id.id_btn_pay);
        btnSend.setEnabled(false);
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
                 * "cancel"  - user canceled
                 * "invalid" - payment plugin not installed
                 */
                // 错误信息
                String errorMsg = data.getStringExtra("error_msg");
                String extraMsg = data.getStringExtra("extra_msg");
                showMsg(result);
            }
        }else {
            if (data!=null){
                if (payChannel.equals(ConstantUtil.VALUE_TEN)){
                    String result = data.getStringExtra("pay_result");
                    showMsg(result);
                }else {
                    switch (resultCode){
                        case ConstantUtil.VALUE_MINUS1:
                            setResult(0x001);
                            onDelayRequest();
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
                onDelayRequest();
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

    private void onDelayRequest() {
        customDialog.setDialogContent("正在获取支付结果");
        customDialog.show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                requestResult();
            }
        }, 2000);
    }
    private void onShowDialogs(final String str) {
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
