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

import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.pingplusplus.android.Pingpp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        Intent getdata=getIntent();
        amount=getdata.getStringExtra("amount");
        shopUrl=getdata.getStringExtra("url");
        initView();
        mAdapter=new PayWayAdapter(context,list);
        mForScrollView.setAdapter(mAdapter);
        initPaywayData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=list.get(thisPosition).get("channel");
            }
        });
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
                // 错误信息
                String errorMsg = data.getExtras().getString("error_msg");
                String extraMsg = data.getExtras().getString("extra_msg");
                showMsg(result, errorMsg, extraMsg);
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
            validateURL=shopUrl+"&userId="+ URLEncoder.encode(userId, "UTF-8")
                    +"&password="+ URLEncoder.encode(password, "UTF-8")
                    +"&channel="+URLEncoder.encode(channels, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        requestCode=1;
        customDialog.show();
        SendrequestUtil.getDataFromService(validateURL,mHandler);
    }
    private void initPaywayData() {
        String source="app_shop_pay_method";
        requestCode=0;
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.postDataFromService(validateURL, textParams,mHandler);
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
                case SendrequestUtil.SUCCESS:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
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
                            reference.showfaiture(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    reference.showfaiture(msg.obj.toString());
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
        if (channels.equals(VariableUtil.VALUE_ONE)||channels.equals(VariableUtil.VALUE_THREE)||channels.equals(VariableUtil.VALUE_FIVE)||channels.equals(VariableUtil.VALUE_SEVER))
        {
            //实付金额为0，不调用ping++,
            if (amount.equals(VariableUtil.VALUE_ZERO1)||amount.equals(VariableUtil.VALUE_ZERO2)||amount.equals(VariableUtil.VALUE_ZERO)){
                setResult(PAY_CODE);
                requestResult();
            }else {
                Pingpp.createPayment(PublicPayWayActivity.this, charge);
            }
        }else {
            setResult(PAY_CODE);
            requestResult();
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
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals(VariableUtil.VALUE_ZERO)){
            if (lottery!=null&&(!TextUtils.isEmpty(lottery))){
                Intent success=new Intent(context,PaySuccessActivity.class);
                success.putExtra("url",lottery);
                success.putExtra("type","6");
                success.putExtra("orderId",orderId);
                startActivity(success);
                finish();
            }else {
                showdialogs("新订单");
            }
        }else if (status.equals(VariableUtil.VALUE_ONE)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","6");
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
            showdialogs("正在支付");
        }
    }
    private void showMsg(String title, String errorMsg, String extraMsg) {
        String str = title;
        if (str.equals(SendrequestUtil.SUCCESS_VALUE)){
            str="支付成功";
        }else if (str.equals(SendrequestUtil.FAILURE_VALUE)){
            str="支付失败";
        }else if (str.equals(SendrequestUtil.CANCEL_VALUE)){
            str="已取消支付";
        }
        if (title.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(PAY_CODE);
            requestResult();
        }else {
            showdialogs(str);
        }
    }
    private void showdialogs(String str) {
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
    private void showfaiture(String s) {
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
        SendrequestUtil.postDataFromService(validateURL,textParams,mHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}