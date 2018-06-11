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
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.Public.PaySuccess;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RechargeValue extends BaseActivity  {
    private EditText et_value;
    private Button btn_recharge;
    private ListViewForScrollView mListView;
    private String  userId,id;
    private String  password;
    private String  charge,type="4";
    private String  amount;
    private String  channels;
    private String  orderId="";
    private String  source="wallet_recharge";
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requestCode=0;
    private PayWayAdapter mAdapter;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
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
                        if(Results.equals("success")) {
                            JSONObject jsonObject =object.optJSONObject("data");
                            if (requestCode==0){
                                showcharge(jsonObject);
                            }else if (requestCode==1){
                                payresult(jsonObject);
                            }
                        }else {
                            showNotify("充值提示",Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showNotify("充值提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    Handler methodHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONArray Array =object.getJSONArray("data");
                            Paywayshow(Array);
                        }else {
                            showNotify("提示",Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    showNotify("提示",msg.obj.toString());
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
    private void showcharge(JSONObject jsonObject) {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pingpp.createPayment(RechargeValue.this, charge);
    }
    private void showNotify(String title ,String string) {
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

    private void payresult(JSONObject jsonObject) {

        String status=jsonObject.optString("status");
        String chargeId=jsonObject.optString("chargeId");
        String lottery=jsonObject.optString("lottery");
        if (status.equals("0")){
            //新订单
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            setResult(1);
            finish();
        }else if (status.equals("1")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            setResult(1);
            finish();
        }else if (status.equals("2")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","5");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
        }else if (status.equals("3")){
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
        mAdapter=new PayWayAdapter(context,List);
        mListView.setAdapter(mAdapter);
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                //选择支付方式可点击
                btn_recharge.setEnabled(true);
                VariableUtil.paypos=thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });

    }

    private void initData() {
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                methodHandler.sendMessage(msg);
            }

            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                methodHandler.sendMessage(msg);
            }
        });
    }

    private void initView() {
        mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        et_value=(EditText)findViewById(R.id.id_et_value);
        btn_recharge=(Button)findViewById(R.id.id_btn_recharge);
        btn_recharge.setEnabled(false);       //初始未选择支付方式不可点击
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
            showNotify("充值提示","请输入充值金额");
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
        requestCode=0;
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("amount",amount);
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
        if (str.equals("缴费成功")){
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
