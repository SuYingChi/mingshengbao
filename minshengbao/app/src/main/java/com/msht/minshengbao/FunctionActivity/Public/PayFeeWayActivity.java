package com.msht.minshengbao.FunctionActivity.Public;

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

import com.msht.minshengbao.Adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
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

public class PayFeeWayActivity extends BaseActivity implements View.OnClickListener {

    private Button btnSend;
    private TextView tvRealAmount;
    private TextView tvSubtract;
    private TextView tvShouldAmount;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private String userId,voucherId;
    private String PayId;
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
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
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
                case SendrequestUtil.FAILURE:
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
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        JSONObject optJSONObject =object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
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
                case SendrequestUtil.FAILURE:
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
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
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
                case SendrequestUtil.FAILURE:
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
            double subtract_amount=showData.optDouble("subtract_amount");
            discountAmt=String.valueOf(subtract_amount);
            tvSubtract.setText("¥"+subtract_amount);
            double shouldAmount=Double.parseDouble(amount);
            double real=shouldAmount-subtract_amount;
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
                Intent success=new Intent(context,PaySuccess.class);
                success.putExtra("url",lottery);
                success.putExtra("type","0");
                success.putExtra("orderId",orderId);
                startActivity(success);
                finish();
            }else {
                onShowdialogs("新订单");
            }
        }else if (status.equals(VariableUtil.VALUE_ONE)){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","0");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals(VariableUtil.VALUE_TWO)){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
        }else if (status.equals(VariableUtil.VALUE_THREE)){
            onShowdialogs("正在支付");
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
        initpayway();
    }
    private void onChargePayWay() {
        try {
            orderId=jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channels.equals(VariableUtil.VALUE_ONE)||channels.equals(VariableUtil.VALUE_TWO)||channels.equals(VariableUtil.VALUE_FIVE)
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
        PayId=data.getStringExtra("id");
        type="1";
        initView();
        mAdapter=new PayWayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initSubtract();
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.paypos=thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });
    }

    private void initView() {
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
      //  tvBalance =(TextView)findViewById(R.id.id_tv_balance);
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
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("event_code","gas_pay_before");
        textParams.put("event_relate_id",PayId);
        SendrequestUtil.postDataFromService(validateURL,textParams,subtractHandler);
    }
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }

    private void initpayway() {
        String source="";
        customDialog.show();
        String validateURL= UrlUtil.PAYMETHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
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
                        requestSevice();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestSevice() {
        String validateURL= UrlUtil.GasExpense_Pay;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",PayId);
        textParams.put("type",type);
        textParams.put("amount", realAmount);
        textParams.put("discountAmt",discountAmt);
        textParams.put("channel",channels);
        SendrequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (str.equals(SendrequestUtil.SUCCESS_VALUE)){
            str="缴费成功";
        }else if (str.equals(SendrequestUtil.FAILURE_VALUE)){
            str="缴费失败";
        }else if (str.equals(SendrequestUtil.CANCEL_VALUE)){
            str="已取消缴费";
        }
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        if (title!=null&&title.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(0x002);
            requestResult();
        }else {
            onShowdialogs(str);
        }
    }
    private void onShowdialogs(String str) {
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
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg");
                String extraMsg = data.getExtras().getString("extra_msg");
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",orderId);
        SendrequestUtil.postDataFromService(validateURL,textParams, requestHandler);
    }
}
