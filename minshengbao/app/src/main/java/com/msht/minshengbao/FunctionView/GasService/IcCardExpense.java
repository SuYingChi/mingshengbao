package com.msht.minshengbao.FunctionView.GasService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.Public.PaySuccess;
import com.msht.minshengbao.FunctionView.Public.PayfeeWay;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IcCardExpense extends BaseActivity implements View.OnClickListener {
    private RadioButton Ryiwangtong,Raalipay;
    private RadioButton  Rawechat,Rayinlian,Rbalance;
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
    private String password;
    private String payId,payTime;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requestCode=0;
    private JSONObject jsonObject,Expenseobject;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            if (requestCode==0){
                                ShowBalance();
                            }else if (requestCode==1){
                                showcharge();
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
                        Expenseobject =object.optJSONObject("data");
                        if(Results.equals("success")) {
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
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","2");
            startActivity(success);
            finish();
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
            Rbalance.setEnabled(true);
            Rbalance.setChecked(true);
            channels = "8";
            String balance=jsonObject.optString("balance");
            tv_balance.setText(balance+"元");
        }else {
            tv_balance.setText("余额不足");
            tv_balance.setTextColor(0xfff96331);
            Rbalance.setEnabled(false);
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
        initIcData();
    }
    private void initView() {
        tv_customerNo=(TextView)findViewById(R.id.id_customerNo);
        tv_price=(TextView)findViewById(R.id.id_tv_price);
        tv_address=(TextView)findViewById(R.id.id_address_text);
        tv_lastData=(TextView)findViewById(R.id.id_last_time);
        tv_lastAmount=(TextView)findViewById(R.id.id_last_amount);
        tv_rechargeAmount=(TextView)findViewById(R.id.id_tv_rechargeAmount);
        tv_purchargeGas=(TextView)findViewById(R.id.id_rechargeGas);
        Ryiwangtong=(RadioButton)findViewById(R.id.id_radio_wangtong);
        Raalipay=(RadioButton)findViewById(R.id.id_radio_alipay);
        Rawechat=(RadioButton)findViewById(R.id.id_radio_wechat);
        Rayinlian=(RadioButton)findViewById(R.id.id_radio_yinlian);
        Rbalance=(RadioButton)findViewById(R.id.id_radio_balance);
        tv_balance=(TextView)findViewById(R.id.id_tv_balance);
        btn_send=(Button)findViewById(R.id.id_btn_pay) ;
        Raalipay.setOnClickListener(this);
        Rawechat.setOnClickListener(this);
        Rayinlian.setOnClickListener(this);
        Ryiwangtong.setOnClickListener(this);
        Rbalance.setOnClickListener(this);
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
        HttpUrlconnectionUtil.executepostTwo(validateURL, textParams, new ResultListener() {
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
        HttpUrlconnectionUtil.executepost(validateURL, textParams, new ResultListener() {
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
                        customDialog.show();
                        requestCode=1;
                        requestSevice();
                        dialog.dismiss();
                    }
                })
                .show();
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
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_radio_alipay:
                channels = "1";
                Raalipay.setChecked(true);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);    //设置可点击
                break;
            case R.id.id_radio_wechat:
                channels = "5";
                Raalipay.setChecked(false);
                Rawechat.setChecked(true);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                break;
            case R.id.id_radio_yinlian:
                channels = "3";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(true);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                break;
            case R.id.id_radio_wangtong:
                channels = "7";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(true);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                break;
            case R.id.id_radio_balance:
                channels = "8";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(true);
                btn_send.setEnabled(true);
                break;
            default:
                break;
        }
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
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","2");
            startActivity(success);
            finish();
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
}
