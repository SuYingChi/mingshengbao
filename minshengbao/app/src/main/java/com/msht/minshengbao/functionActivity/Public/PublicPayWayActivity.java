package com.msht.minshengbao.functionActivity.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.functionActivity.WaterApp.WaterPayRechargeActivity;
import com.pingplusplus.android.Pingpp;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */

public class PublicPayWayActivity extends BaseActivity {
    private Button btnSend;
    private ListViewForScrollView mForScrollView;
    private PayWayAdapter mAdapter;
    private String shopUrl;
    private String userId;
    private String password;
    private String charge;
    private String channels;
    private String orderId="";
    private String amount;
    private String userName;
    private static final int PAY_CODE=0x004;
    private int requestCode=0;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private final PayWayHandler mHandler = new PayWayHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_payway);
        context=this;
        mPageName="订单支付";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userName=SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent receiveData=getIntent();
        amount=receiveData.getStringExtra("amount");
        shopUrl=receiveData.getStringExtra("url");
        VariableUtil.payPos =-1;
        initView();
        mAdapter=new PayWayAdapter(context,list);
        mForScrollView.setAdapter(mAdapter);
        initPayWayData();
        mAdapter.setOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void itemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=list.get(thisPosition).get("channel");
            }
        });
        mForScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =position;
                mAdapter.notifyDataSetChanged();
                channels= list.get(position).get("channel");
            }
        });
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
                            setResult(PAY_CODE);
                            onStartActivity("","6");
                          //  requestResult();
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
    private void initView() {
        mForScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        TextView tvShouldAmount =(TextView)findViewById(R.id.id_should_fee);
        btnSend =(Button)findViewById(R.id.id_btn_pay) ;
        tvShouldAmount.setText("¥"+amount+"元");
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendServices();
            }
        });
    }
    private void sendServices() {
        String validateURL="";
        try{
            validateURL=shopUrl+"&username="+ URLEncoder.encode(userName, "UTF-8")+"&userId="+ URLEncoder.encode(userId, "UTF-8")
                    +"&password="+ URLEncoder.encode(password, "UTF-8")
                    +"&channel="+URLEncoder.encode(channels, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        requestCode=1;
        customDialog.show();
        SendRequestUtil.getDataFromService(validateURL,mHandler);
    }
    private void initPayWayData() {
        String source="app_shop_pay_method";
        requestCode=0;
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendRequestUtil.postDataFromService(validateURL, textParams,mHandler);
    }
    private static class PayWayHandler extends Handler {

        private WeakReference<PublicPayWayActivity> mWeakReference;
        public PayWayHandler(PublicPayWayActivity reference) {
            mWeakReference = new WeakReference<PublicPayWayActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final PublicPayWayActivity reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestCode==0){
                                JSONArray jsonArray =object.getJSONArray("data");
                                reference.payWayShow(jsonArray);
                           }else if (reference.requestCode==1){
                                JSONObject json=object.getJSONObject("data");
                                reference.getChargeData(json);
                                reference.payResult(json);
                            } else if (reference.requestCode==2){
                                JSONObject json=object.getJSONObject("data");
                                reference.payResult(json);
                            }
                        }else {
                            reference.showFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    reference.showFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }
    private void getChargeData(JSONObject json) {
        try {
            orderId = json.optString("id");
            charge = json.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (channels){
            case VariableUtil.VALUE_ONE:
                Pingpp.createPayment(PublicPayWayActivity.this, charge);
                break;
            case VariableUtil.VALUE_THREE:
                Pingpp.createPayment(PublicPayWayActivity.this, charge);
                break;
            case VariableUtil.VALUE_FIVE:
                Pingpp.createPayment(PublicPayWayActivity.this, charge);
                break;
            case VariableUtil.VALUE_SEVER:
                Pingpp.createPayment(PublicPayWayActivity.this, charge);
                break;
            case ConstantUtil.VALUE_TEN:
                if (amount.equals(VariableUtil.VALUE_ZERO1)|| amount.equals(VariableUtil.VALUE_ZERO2)
                        || amount.equals(VariableUtil.VALUE_ZERO)){
                    setResult(PAY_CODE);
                    onStartActivity("","6");
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
                    setResult(PAY_CODE);
                    onStartActivity("","6");
                    //requestResult();
                }else {
                    Gson gson = new Gson();
                    YiPayModel model = gson.fromJson(charge, YiPayModel.class);
                    PaymentTask mPaymentTask = new PaymentTask(this);
                    mPaymentTask.pay(ParamsUtil.buildPayParams(model));
                }
                break;
            default:
                setResult(PAY_CODE);
                onStartActivity("","6");
               // requestResult();
                break;
        }
    }
    private void payWayShow(JSONArray jsonArray) {
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
                list.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (list.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void payResult(JSONObject json) {
        String status=json.optString("status");
        String lottery="";
        if (json.has("lottery")){
           lottery=json.optString("lottery");
        }
        switch (status){
            case VariableUtil.VALUE_ZERO:
                onStartActivity(lottery,"1");
                break;
            case VariableUtil.VALUE_ONE:
                onStartActivity(lottery,"1");
                break;
            case VariableUtil.VALUE_TWO:
                onStartActivity(lottery,"0");
                break;
            case VariableUtil.VALUE_THREE:
                onShowDialogs("正在支付");
                break;
                default:
                    break;
        }
    }
    private void onStartActivity(String lottery, String s) {
        String pageUrl=UrlUtil.APP_PAY_SUCCESS_PAGE +"userId="+userId+"&event_code=shop_pay_success";
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("url",lottery);
        success.putExtra("type",s);
        success.putExtra("pageUrl",pageUrl);
        success.putExtra("navigation","商城支付");
        startActivity(success);
        finish();
    }
    private void showMsg(String title) {
        switch (title){
            case SendRequestUtil.SUCCESS_VALUE:
                setResult(PAY_CODE);
                onStartActivity("","6");
              //  requestResult();
                break;
            case SendRequestUtil.FAILURE_VALUE:
                onShowDialogs("支付失败");
                break;
            case SendRequestUtil.CANCEL_VALUE:
                onShowDialogs("已取消支付");
                break;
                default:
                    break;
        }
    }
    private void  onShowDialogs(String str) {
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
    private void showFailure(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",orderId);
        SendRequestUtil.postDataFromService(validateURL,textParams,mHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}