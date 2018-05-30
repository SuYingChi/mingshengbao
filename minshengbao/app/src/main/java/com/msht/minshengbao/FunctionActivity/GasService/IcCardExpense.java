package com.msht.minshengbao.FunctionActivity.GasService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.PaywayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.Public.PaySuccess;
import com.msht.minshengbao.FunctionActivity.Public.QRCodeScan;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IcCardExpense extends BaseActivity  {
    private Button btn_send;
    private TextView tv_realamount;
    private TextView tv_balance;
    private TextView tv_customerNo,tv_price;
    private TextView tv_address,tv_lastData;
    private TextView tv_lastAmount;
    private TextView tv_rechargeAmount;
    private TextView tv_purchargeGas;
    private String charge,amount,voucherId="0";
    private String purchaseAmount;
    private String type;
    private String CustomerNo;
    private String channels;
    private String userId,id;
    private String orderId="";
    private String source="";
    private long time1,overtime;
    private String password;
    private String payId,payTime;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private ListViewForScrollView forScrollView;
    private PaywayAdapter mAdapter;
    private int requestCode=0;
    private JSONObject jsonObject,Expenseobject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
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
                                jsonObject =object.optJSONObject("data");
                                ShowBalance();
                            }else if (requestCode==1){
                                jsonObject =object.optJSONObject("data");
                                showcharge();
                            }else if (requestCode==2){
                                JSONArray array=object.optJSONArray("data");
                                Paywayshow(array);
                            }else if (requestCode==3){
                                JSONObject json=object.getJSONObject("data");
                                payresult(json);
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
    Handler expensetHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            Expenseobject =object.optJSONObject("data");
                            time1= DateUtils.getCurTimeLong();
                            showRechargeData();
                        }else {
                            showIcfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showIcfaiture(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void Paywayshow(JSONArray array) {
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
                List.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (List.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void showcharge() {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channels.equals("1")||channels.equals("3")||channels.equals("5")||channels.equals("7"))
        {
            Pingpp.createPayment(IcCardExpense.this, charge);
        }else {
            setResult(0x002);
            requestResult();
        }
    }
    private void showfaiture(String s) {
        new PromptDialog.Builder(this)
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
    private void showIcfaiture(String s) {
        new PromptDialog.Builder(this)
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
            purchaseAmount = Expenseobject.getString("purchaseAmount");
            String purchargeGas = Expenseobject.getString("purchaseGas");
            String lastRechargeAmount = Expenseobject.getString("lastRechargeAmount");
            String price=Expenseobject.getString("price");
            String address=Expenseobject.getString("address");
            overtime=Expenseobject.optLong("countdown");
            String lastRechargeDate=Expenseobject.getString("lastRechargeDate");
            CustomerNo=Expenseobject.getString("customerNo");
            tv_address.setText(address);
            tv_lastAmount.setText("¥"+lastRechargeAmount);
            tv_price.setText(price);
            tv_purchargeGas.setText(purchargeGas+"立方");
            tv_rechargeAmount.setText("¥"+purchaseAmount);
            tv_lastData.setText(lastRechargeDate);
            tv_customerNo.setText(CustomerNo);
        }catch (JSONException e){
            e.printStackTrace();
        }
        initData();

    }
    private void ShowBalance() {
        double doublebalance=jsonObject.optDouble("balance");
        double doubleamount=Double.valueOf(purchaseAmount);
        if (doubleamount<=doublebalance){
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initpayway();
    }
    private void payresult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals("0")){
            //新订单
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","2");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals("1")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","2");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals("2")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","5");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals("3")){
            showdialogs("正在支付");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_card_expense);
        context=this;
        setCommonHeader("IC卡燃气费用");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        payId=getIntent().getStringExtra("payId");
        payTime=getIntent().getStringExtra("payTime");
        type="6";
        initView();
        mAdapter=new PaywayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initIcData();
        mAdapter.SetOnItemClickListener(new PaywayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btn_send.setEnabled(true);
                VariableUtil.paypos=thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });
    }
    private void initView() {
        tv_customerNo=(TextView)findViewById(R.id.id_customerNo);
        tv_price=(TextView)findViewById(R.id.id_tv_price);
        tv_address=(TextView)findViewById(R.id.id_address_text);
        tv_lastData=(TextView)findViewById(R.id.id_last_time);
        tv_lastAmount=(TextView)findViewById(R.id.id_last_amount);
        tv_rechargeAmount=(TextView)findViewById(R.id.id_tv_rechargeAmount);
        tv_purchargeGas=(TextView)findViewById(R.id.id_rechargeGas);
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        tv_balance=(TextView)findViewById(R.id.id_tv_balance);
        btn_send=(Button)findViewById(R.id.id_btn_pay) ;
        btn_send.setEnabled(false);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void initIcData() {
        customDialog.show();
        String validateURL= UrlUtil.IcRecharge_BillUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("payId",payId);
        textParams.put("payTime",payTime);
        SendrequestUtil.executepostTwo(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                expensetHandler.sendMessage(msg);
            }

            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                expensetHandler.sendMessage(msg);
            }
        });

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
                            requestSevice();
                        }else {
                            tipshow();
                        }

                    }
                })
                .show();
    }
    private void tipshow() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("时间超时")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        Intent intent=new Intent(IcCardExpense.this, QRCodeScan.class);
                        startActivity(intent);
                        finish();

                    }
                }).show();
    }

    private boolean overTime() {
        long time2= DateUtils.getCurTimeLong();
        long time=time2-time1;
        long second=time/1000;
        if (second<overtime){
            return true;
        }else {
            return false;
        }
    }
    private void requestSevice() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount",purchaseAmount);
        textParams.put("customerNo",CustomerNo);
        textParams.put("payId",payId);
        textParams.put("channel",channels);
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
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void showMsg(String result, String errorMsg, String extraMsg) {
        String str = result;
        if (str.equals("success")){
            str="充值成功";
        }else if (str.equals("fail")){
            str="充值失败";
        }else if (str.equals("cancel")){
            str="已取消充值";
        }
        if (null !=errorMsg && errorMsg.length() != 0) {
            str += "\n" + errorMsg;
        }
        if (null !=extraMsg && extraMsg.length() != 0) {
            str += "\n" + extraMsg;
        }
        if (str.equals("充值成功")){
            setResult(0x002);
            requestResult();
        }else {
            showdialogs(str);
        }
    }
    private void showdialogs(String str) {
        new PromptDialog.Builder(this)
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

    private void initpayway() {

        requestCode=2;
        String validateURL= UrlUtil.Paymethod_Url;
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
        requestCode=3;
        String validateURL= UrlUtil.PayResult_Notarize;
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
