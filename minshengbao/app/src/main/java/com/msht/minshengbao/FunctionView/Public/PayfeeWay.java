package com.msht.minshengbao.FunctionView.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayfeeWay extends BaseActivity implements View.OnClickListener {
    private RadioButton Ryiwangtong,Raalipay,Rawechat;
    private RadioButton Rayinlian,Rbalance;
    private Button btn_send;
    private TextView tv_realamount;
    private TextView tv_balance;

    private String userId,id,voucherId;
    private String password;
    private String charge;   //
    private String channels;
    private String type;
    private String CustomerNo;
    private String amount;
    private int requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONObject jsonObject;
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
    private void showfaiture(String s) {
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
    private void ShowBalance() {
        double doublebalance=jsonObject.optDouble("balance");
        double doubleamount=Double.valueOf(amount);
        if (doubleamount<=doublebalance){
            Rbalance.setEnabled(true);
            String balance=jsonObject.optString("balance");
            tv_balance.setText(balance+"元");
        }else {
            tv_balance.setText("余额不足");
            tv_balance.setTextColor(0xfff96331);
            Rbalance.setEnabled(false);
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
          Pingpp.createPayment(PayfeeWay.this, charge);
        }else {
            setResult(0x002);
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","0");
            startActivity(success);
            finish();
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
        Intent getdata=getIntent();
        CustomerNo=getdata.getStringExtra("CustomerNo");
        amount=getdata.getStringExtra("amount");
        voucherId=getdata.getStringExtra("voucherid");
        type="1";
        initView();
        initData();
    }
    private void initView() {
        Ryiwangtong=(RadioButton)findViewById(R.id.id_radio_wangtong);
        Raalipay=(RadioButton)findViewById(R.id.id_radio_alipay);
        Rawechat=(RadioButton)findViewById(R.id.id_radio_wechat);
        Rayinlian=(RadioButton)findViewById(R.id.id_radio_yinlian);
        Rbalance=(RadioButton)findViewById(R.id.id_radio_balance);
        tv_balance=(TextView)findViewById(R.id.id_tv_balance);
        tv_realamount=(TextView)findViewById(R.id.id_real_fee);
        btn_send=(Button)findViewById(R.id.id_btn_pay) ;
        tv_realamount.setText("¥"+amount);
        btn_send.setEnabled(false);       //初始未选择支付方式不可点击
        btn_send.setBackgroundResource(R.drawable.shape_gray_corner_button);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_radio_alipay:
                channels = "1";
                Raalipay.setChecked(true);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);    //设置可点击
                btn_send.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_wechat:
                channels = "5";
                Raalipay.setChecked(false);
                Rawechat.setChecked(true);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                btn_send.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_yinlian:
                channels = "3";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(true);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                btn_send.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_wangtong:
                channels = "7";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(true);
                Rbalance.setChecked(false);
                btn_send.setEnabled(true);
                btn_send.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_balance:
                channels = "8";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                Rbalance.setChecked(true);
                btn_send.setEnabled(true);
                btn_send.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
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
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount",amount);
        textParams.put("customerNo",CustomerNo);
        textParams.put("couponId",voucherId);
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
    private void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (str.equals("success")){
            str="缴费成功";
        }else if (str.equals("fail")){
            str="缴费失败";
        }else if (str.equals("cancel")){
            str="已取消缴费";
        }
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        if (str.equals("缴费成功")){
            setResult(0x002);
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","0");
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
                        finish();

                    }
                }).show();
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
}
