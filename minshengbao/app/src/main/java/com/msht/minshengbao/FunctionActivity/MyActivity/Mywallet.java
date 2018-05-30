package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelfPayDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Mywallet extends BaseActivity implements View.OnClickListener {
    private TextView  tv_rightText;
    private TextView  tv_banlance;
    private String    password,userId;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    public static final String MY_ACTION = "ui";
    private CustomDialog customDialog;
    Handler requestHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONObject data=object.getJSONObject("data");
                            String balance=data.optString("balance");
                            tv_banlance.setText(balance);
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler BindingHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            if (jsonArray.length()==0){
                                NoticeDialogs();
                            }
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void displayDialog(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
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
        setContentView(R.layout.activity_mywallet);
        setCommonHeader("我的账户");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
        initData();
        initBinding();
    }

    private void NoticeDialogs() {
        final SelfPayDialog selfPayDialog=new SelfPayDialog(context);
        selfPayDialog.setOnpositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agree=new Intent(context, AutomatePay.class);
                startActivity(agree);
            }
        });
        selfPayDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfPayDialog.dismiss();
            }
        });
        selfPayDialog.show();
    }
    private void initView() {
        tv_rightText=(TextView)findViewById(R.id.id_tv_rightText);
        tv_rightText.setVisibility(View.VISIBLE);
        tv_rightText.setText("明细");
        tv_banlance=(TextView)findViewById(R.id.id_balance);
        tv_rightText.setOnClickListener(this);
        findViewById(R.id.id_layout_recharge).setOnClickListener(this);
        findViewById(R.id.id_layout_gaspayfee).setOnClickListener(this);
        findViewById(R.id.id_layout_card).setOnClickListener(this);

    }
    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.Mywallet_balanceUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
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
    private void initBinding() {
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        String validateURL = UrlUtil.AutomataPay_Url;
        SendrequestUtil.executepostTwo(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                BindingHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                BindingHandler.sendMessage(msg);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_card:
                Intent discount=new Intent(context, DiscountCoupon.class);
                startActivityForResult(discount,2);
                break;
            case R.id.id_layout_recharge:
                Intent recharge=new Intent(context, RechargeValue.class);
                startActivityForResult(recharge,1);
                break;
            case R.id.id_layout_gaspayfee:
                Intent agree=new Intent(context, AutomatePay.class);
                startActivity(agree);
                break;
            case R.id.id_tv_rightText:
                Intent detail=new Intent(context, IncomeExpense.class);
                startActivity(detail);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==1){   //充值成功
                    initData();
                }
                break;
            case 2:
                if (resultCode==2){
                    setResult(0x004);
                    finish();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void sendBroad() {
        Intent broadcast=new Intent();
        broadcast.setAction(MY_ACTION);
        broadcast.putExtra("broadcast", "3");
        sendBroadcast(broadcast);
    }
}
