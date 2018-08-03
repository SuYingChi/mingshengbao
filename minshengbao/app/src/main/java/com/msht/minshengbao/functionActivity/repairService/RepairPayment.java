package com.msht.minshengbao.functionActivity.repairService;

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
import android.widget.Toast;

import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.functionActivity.Public.PaySuccessActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepairPayment extends BaseActivity  {

    private Button   btnSend;
    private TextView tvRealamount, tvOrderNo;
    private TextView tvBalance;
    private String  userId,password,couponId="0";
    private String  realmoney,channel,orderNo,type,orderId;
    private String id ;
    private String charge;
    private String source="repair_order";
    private JSONArray jsonArray;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();

    private int requestCode=0;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    Handler chargesHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonObject =object.optJSONObject("data");
                        if(Results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (requestCode==0){
                                onShowBalance();
                            }else if (requestCode==1){
                                onReceiveChargeData();
                            }
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requestCode==0){
                                jsonArray =object.getJSONArray("data");
                                onPayWayShow();
                            }else if (requestCode==2){
                                JSONObject json=object.getJSONObject("data");
                                onPayResult(json);
                            }
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals(VariableUtil.VALUE_ZERO)){
            //新订单
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","1");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals(VariableUtil.VALUE_ONE)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","1");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals(VariableUtil.VALUE_TWO)){
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","5");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals(VariableUtil.VALUE_THREE)){
            showdialogs("正在支付");
        }
    }
    private void onPayWayShow() {
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
    private void onShowBalance() {
        double doubleBalance=jsonObject.optDouble("balance");
        double doubleAmount=Double.valueOf(realmoney);
        if (doubleAmount<=doubleBalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initPayway();
    }
    private void showfaiture(String error) {
        new PromptDialog.Builder(this)
                .setTitle("缴费提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveChargeData() {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channel.equals(VariableUtil.VALUE_ONE)||channel.equals(VariableUtil.VALUE_THREE)||channel.equals(VariableUtil.VALUE_FIVE)||channel.equals(VariableUtil.VALUE_SEVER)) {
            Pingpp.createPayment(RepairPayment.this, charge);
        }else if (channel.equals(VariableUtil.VALUE_EIGHT)||channel.equals(VariableUtil.VALUE_SIX)){
            setResult(0x005);
            Intent success=new Intent(context,PaySuccessActivity.class);
            success.putExtra("type","1");
            success.putExtra("url","");
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
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
        realmoney=data.getStringExtra("realmoney");
        type="2";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initfindViewByid();
        mAdapter=new PayWayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initData();
        initEvent();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channel=List.get(thisPosition).get("channel");
            }
        });
    }
    private void initfindViewByid() {
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);

        tvBalance =(TextView)findViewById(R.id.id_tv_balance);
        btnSend =(Button)findViewById(R.id.id_evaluate_order);
        btnSend.setEnabled(false);
        tvRealamount =(TextView)findViewById(R.id.id_real_fee);
        tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
        tvRealamount.setText("¥"+realmoney);
        tvOrderNo.setText(orderNo);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            //if (requestCode == REQUEST_CODE_PAYMENT){
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
    private void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (str.equals(SendrequestUtil.SUCCESS_VALUE)){
            str="缴费成功";
        }else if (str.equals(SendrequestUtil.FAILURE_VALUE)){
            str="缴费失败";
        }else if (str.equals(SendrequestUtil.CANCEL_VALUE)){
            str="已取消缴费";
        }
        if (title.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(0x005);
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
    private void initData() {
        requestCode=0;
        String validateURL= UrlUtil.Mywallet_balanceUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                chargesHandler.sendMessage(msg);
            }

            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                chargesHandler.sendMessage(msg);
            }
        });
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
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount",realmoney);
        textParams.put("orderId",orderId);
        if (!TextUtils.isEmpty(couponId)) {
            textParams.put("couponId", couponId);
        }
        textParams.put("channel",channel);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                chargesHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                chargesHandler.sendMessage(msg);
            }
        });
    }
    private void initPayway() {
        customDialog.show();
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }

            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });

    }
}
