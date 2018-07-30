package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.Adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionActivity.Public.PaySuccessActivity;
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

public class RechargeValue extends BaseActivity  {
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
        private WeakReference<RechargeValue> mWeakReference;
        public RequestHandler(RechargeValue activity) {
            mWeakReference = new WeakReference<RechargeValue>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RechargeValue activity =mWeakReference.get();
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
                case SendrequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    activity.onShowNotify("充值提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class MethodHandler extends Handler{
        private WeakReference<RechargeValue> mWeakReference;
        public MethodHandler(RechargeValue activity) {
            mWeakReference = new WeakReference<RechargeValue>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RechargeValue activity =mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            JSONArray array =object.getJSONArray("data");
                            activity.onReceivePaywayData(array);
                        }else {
                            activity.onShowNotify("提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
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
        Pingpp.createPayment(RechargeValue.this, charge);
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
        String chargeId=jsonObject.optString("chargeId");
        String lottery=jsonObject.optString("lottery");
        if (status.equals(VariableUtil.VALUE_ZERO)){
            //新订单
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            setResult(1);
            finish();
        }else if (status.equals(VariableUtil.VALUE_ONE)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            setResult(1);
            finish();
        }else if (status.equals(VariableUtil.VALUE_TWO)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","5");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
        }else if (status.equals(VariableUtil.VALUE_THREE)){
            setResult(1);
            showdialogs("正在支付");
        }
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
        initView();
        mAdapter=new PayWayAdapter(context,payWayList);
        mListView.setAdapter(mAdapter);
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                //选择支付方式可点击
                btnRecharge.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=payWayList.get(thisPosition).get("channel");
            }
        });
    }
    private void initData() {
        String  source="wallet_recharge";
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.postDataFromService(validateURL,textParams,methodHandler);
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
        if (matchText(amount)){
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
    private boolean matchText(String amount) {
        if (!TextUtils.isEmpty(amount)){
            return true;
        }else {
            return false;
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
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void placeAnOrder(){
        requestCode=2;
        String validateURL= UrlUtil.RECHARGE_CREATE_ORDER_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("amount",amount);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                // 错误信息
                String errorMsg = data.getExtras().getString("error_msg");
                String extraMsg = data.getExtras().getString("extra_msg");
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void showMsg(String result, String errorMsg, String extraMsg) {
        String str = result;
        if (str.equals(SendrequestUtil.SUCCESS_VALUE)){
            str="缴费成功";
        }else if (str.equals(SendrequestUtil.FAILURE_VALUE)){
            str="缴费失败";
        }else if (str.equals(SendrequestUtil.CANCEL_VALUE)){
            str="已取消缴费";
        }
        if (result.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(0x005);
            requestResult();
        }else {
            showdialogs(str);
        }
    }

    private void requestResult() {
        requestCode=1;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    private void showdialogs(String str) {
        new PromptDialog.Builder(this)
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
