package com.msht.minshengbao.FunctionView.Myview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RechargeValue extends BaseActivity implements View.OnClickListener {
    private EditText et_value;
    private Button btn_recharge;
    private RadioButton Ryiwangtong,Raalipay;
    private RadioButton Rawechat,Rayinlian;
    private String  userId,id;
    private String  password;
    private String  charge,type="4";
    private String  amount;
    private String  channels;
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
                            showcharge();
                        }else {
                            showNotify(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showNotify(msg.obj.toString());
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
        Pingpp.createPayment(RechargeValue.this, charge);
    }
    private void showNotify(String string) {
        new PromptDialog.Builder(this)
                .setTitle("充值提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
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
        initView();
    }
    private void initView() {
        findViewById(R.id.id_balance_layout).setVisibility(View.GONE);  //无余额支付
        et_value=(EditText)findViewById(R.id.id_et_value);
        btn_recharge=(Button)findViewById(R.id.id_btn_recharge);
        Ryiwangtong=(RadioButton)findViewById(R.id.id_radio_wangtong);
        Raalipay=(RadioButton)findViewById(R.id.id_radio_alipay);
        Rawechat=(RadioButton)findViewById(R.id.id_radio_wechat);
        Rayinlian=(RadioButton)findViewById(R.id.id_radio_yinlian);
        btn_recharge.setEnabled(false);       //初始未选择支付方式不可点击
        btn_recharge.setBackgroundResource(R.drawable.shape_gray_corner_button);
        Raalipay.setOnClickListener(this);
        Rawechat.setOnClickListener(this);
        Rayinlian.setOnClickListener(this);
        Ryiwangtong.setOnClickListener(this);
        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void showTips() {
        amount=et_value.getText().toString().trim();
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
                            requestSevice();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            showNotify("请输入充值金额");
        }
    }
    private boolean matchText(String amount) {
        if (!TextUtils.isEmpty(amount)){
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
        textParams.put("amount",amount);
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
                btn_recharge.setEnabled(true);    //设置可点击
                btn_recharge.setBackgroundResource(R.drawable.selector_touch_button_change);
                Ryiwangtong.setChecked(false);
                break;
            case R.id.id_radio_wechat:
                channels = "5";
                Raalipay.setChecked(false);
                Rawechat.setChecked(true);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(false);
                btn_recharge.setEnabled(true);
                btn_recharge.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_yinlian:
                channels = "3";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(true);
                Ryiwangtong.setChecked(false);
                btn_recharge.setEnabled(true);
                btn_recharge.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            case R.id.id_radio_wangtong:
                channels = "7";
                Raalipay.setChecked(false);
                Rawechat.setChecked(false);
                Rayinlian.setChecked(false);
                Ryiwangtong.setChecked(true);
                btn_recharge.setEnabled(true);
                btn_recharge.setBackgroundResource(R.drawable.selector_touch_button_change);
                break;
            default:
                break;
        }
    }
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
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void showMsg(String result, String errorMsg, String extraMsg) {
        String str = result;
        if (str.equals("success")){
            str="缴费成功";
        }else if (str.equals("fail")){
            str="缴费失败";
        }else if (str.equals("cancel")){
            str="已取消缴费";
        }
        if (null !=errorMsg&& errorMsg.length() != 0) {
            str += "\n" + errorMsg;
        }
        if (null !=extraMsg &&extraMsg.length() != 0) {
            str += "\n" + extraMsg;
        }
        showdialogs(str);
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
}
