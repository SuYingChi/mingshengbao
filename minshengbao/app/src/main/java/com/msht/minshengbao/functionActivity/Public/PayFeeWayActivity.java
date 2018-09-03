package com.msht.minshengbao.functionActivity.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 *  燃气支付
 * @author chenhong
 * @date 2016/10/31
 */
public class PayFeeWayActivity extends BaseActivity implements View.OnClickListener {
    private Button btnSend;
    private TextView tvRealAmount;
    private TextView tvSubtract;
    private TextView tvShouldAmount;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private String userId,voucherId;
    private String payId;
    private String password;
    private String charge;
    private String channels;
    private String type;
    private String discountAmt="0";
    private String orderId="";
    private String amount;
    private String realAmount;
    private JSONArray jsonArray;
    private int requestCode=0;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private final SubtractHandler subtractHandler=new SubtractHandler(this);
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<PayFeeWayActivity> mWeakReference;
        public RequestHandler(PayFeeWayActivity activity) {
            mWeakReference = new WeakReference<PayFeeWayActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PayFeeWayActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.jsonArray =object.getJSONArray("data");
                                activity.onGetPayWayData();
                            }else if (activity.requestCode==2){
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
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class SubtractHandler extends Handler{
        private WeakReference<PayFeeWayActivity> mWeakReference;
        public SubtractHandler(PayFeeWayActivity activity) {
            mWeakReference = new WeakReference<PayFeeWayActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PayFeeWayActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        JSONObject optJSONObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onSubtractAmount(optJSONObject);
                        }else {
                            activity.realAmount =activity.amount;
                            activity.tvSubtract.setText("¥"+activity.discountAmt);
                            activity.tvRealAmount.setText(activity.realAmount);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    activity.realAmount =activity.amount;
                    activity.tvSubtract.setText("¥"+activity.discountAmt);
                    activity.tvRealAmount.setText(activity.realAmount);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class BalanceHandler extends Handler{
        private WeakReference<PayFeeWayActivity> mWeakReference;
        public BalanceHandler(PayFeeWayActivity activity) {
            mWeakReference = new WeakReference<PayFeeWayActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PayFeeWayActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.onGetBalanceData();
                            }else if (activity.requestCode==1){
                                activity.onChargePayWay();
                            }
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onSubtractAmount(JSONObject optJSONObject) {
        try{
            String showType=optJSONObject.optString("showType");
            JSONObject showData=optJSONObject.getJSONObject("showData");
            double subtractAmount=showData.optDouble("subtract_amount");
            discountAmt=String.valueOf(subtractAmount);
            tvSubtract.setText("¥"+subtractAmount);
            double shouldAmount=Double.parseDouble(amount);
            double real=shouldAmount-subtractAmount;
            real=VariableUtil.twoDecinmal2(real);
            realAmount =String.valueOf(real);
            tvRealAmount.setText(realAmount);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals(VariableUtil.VALUE_ZERO)){
            if (lottery!=null&&(!TextUtils.isEmpty(lottery))){
                Intent success=new Intent(context,PaySuccessActivity.class);
                success.putExtra("url",lottery);
                success.putExtra("type","0");
                success.putExtra("orderId",orderId);
                startActivity(success);
                finish();
            }else {
                onShowDialogs("新订单");
            }
        }else if (status.equals(VariableUtil.VALUE_ONE)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","0");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals(VariableUtil.VALUE_TWO)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
        }else if (status.equals(VariableUtil.VALUE_THREE)){
            onShowDialogs("正在支付");
        }
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
                List.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (List.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onFailure(String s) {
        new PromptDialog.Builder(this)
                .setTitle("缴费提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void onGetBalanceData() {
        double doubleBalance=jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(amount);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        onPayWayData();
    }
    private void onChargePayWay() {
        try {
            orderId=jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channels.equals(VariableUtil.VALUE_ONE)||channels.equals(VariableUtil.VALUE_THREE)||channels.equals(VariableUtil.VALUE_TWO)||channels.equals(VariableUtil.VALUE_FIVE)
                ||channels.equals(VariableUtil.VALUE_SEVER))
        {
            //实付金额为0，不调用ping++,
            if (realAmount.equals(VariableUtil.VALUE_ZERO1)|| realAmount.equals(VariableUtil.VALUE_ZERO2)
                    || realAmount.equals(VariableUtil.VALUE_ZERO)){
                setResult(0x002);
                requestResult();
            }else {
                Pingpp.createPayment(PayFeeWayActivity.this, charge);
            }
        }else {
            setResult(0x002);
            requestResult();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payfee_way);
        context=this;
        setCommonHeader("燃气支付");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        amount=data.getStringExtra("amount");
        payId =data.getStringExtra("id");
        type="1";
        initView();
        mAdapter=new PayWayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initSubtract();
        initData();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });
    }
    private void initView() {
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        tvRealAmount =(TextView)findViewById(R.id.id_real_fee);
        tvSubtract =(TextView)findViewById(R.id.id_subtract_amount);
        tvShouldAmount =(TextView)findViewById(R.id.id_should_fee);
        btnSend =(Button)findViewById(R.id.id_btn_pay) ;
        tvShouldAmount.setText("¥"+amount+"元");
        tvRealAmount.setText("¥"+amount);
        tvSubtract.setText("¥"+discountAmt);
        realAmount =amount;
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void initSubtract() {
        customDialog.show();
        String validateURL= UrlUtil.PaySubtract_Url;
        Map<String, String> textParams = new HashMap<String, String>(3);
        textParams.put("userId",userId);
        textParams.put("event_code","gas_pay_before");
        textParams.put("event_relate_id", payId);
        SendRequestUtil.postDataFromService(validateURL,textParams,subtractHandler);
    }
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,balanceHandler);
    }

    private void onPayWayData() {
        String source="";
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_goback:
                finish();
                break;
            default:
                break;
        }
    }
    private void showTips() {
        new PromptDialog.Builder(this)
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
        String validateURL= UrlUtil.GAS_EXPENSE_PAY;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", payId);
        textParams.put("type",type);
        textParams.put("amount", realAmount);
        textParams.put("discountAmt",discountAmt);
        textParams.put("channel",channels);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,balanceHandler);
    }
    private void showMsg(String title, String msg1, String msg2) {
        String str = title;
        switch (title){
            case SendRequestUtil.SUCCESS_VALUE:
                str="缴费成功";
                setResult(0x002);
                requestResult();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                str="缴费失败";
                onShowDialogs(str);
                break;
            case SendRequestUtil.CANCEL_VALUE:
                str="已取消缴费";
                onShowDialogs(str);
                break;
                default:
                    break;
        }
        /*if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }*/
    }
    private void onShowDialogs(String str) {
        new PromptDialog.Builder(this)
                .setTitle("缴费提示")
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
                String errorMsg = data.getStringExtra("error_msg");
                String extraMsg = data.getStringExtra("extra_msg");
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
