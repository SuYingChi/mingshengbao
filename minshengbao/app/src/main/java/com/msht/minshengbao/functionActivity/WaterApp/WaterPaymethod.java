package com.msht.minshengbao.functionActivity.WaterApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.functionActivity.Public.PaySuccessActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
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

public class WaterPaymethod extends BaseActivity {
    private Button btnSend;
    private TextView tvRealAmount;
    private ListViewForScrollView forScrollView;
    private View layoutView;
    private String userId;
    private String password;
    private String userphone;
    private String charge;
    private String id;
    private String orderId;
    private String channels;
    private String type="7";
    private String mBalance ="";
    private String sign,extParams;
    private String amount;
    private String resultCode=null;
    private String resultMsg=null;
    private int requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private PayWayAdapter mAdapter;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();

    Handler waterPayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("message");
                        jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            if (requestCode==1){
                                watercharge();
                            }
                        }else {
                            customDialog.dismiss();
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



    Handler balanceHandler = new Handler() {
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
                            }else if (requestCode==2){
                                payresult();
                            }
                        }else {
                            customDialog.dismiss();
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
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.getJSONArray("data");
                        if(Results.equals("success")) {
                            Paywayshow();
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
    Handler chargesHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        resultMsg = object.optString("message");
                        resultCode= object.optString("code");
                        if(Results.equals("success")) {
                            orderId =object.optString("data");
                            layoutView.setVisibility(View.VISIBLE);
                        }else {
                            layoutView.setVisibility(View.INVISIBLE);
                            showfaiture(resultMsg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context,msg.obj.toString() ,
                            Toast.LENGTH_SHORT).show();
                    layoutView.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    private void Paywayshow() {
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
    private void payresult() {
        String status=jsonObject.optString("status");
        String chargeId=jsonObject.optString("chargeId");
        String lottery=jsonObject.optString("lottery");
        if (status.equals("0")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                showdialogs("新订单");
            }

        }else if (status.equals("1")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                Intent success=new Intent(context,PaySuccessActivity.class);
                success.putExtra("type","0");
                startActivity(success);
                finish();
            }
        }else if (status.equals("2")){
            showdialogs("充值失败");
        }else if (status.equals("3")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                showdialogs("正在支付");
            }
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
    private void showcharge() {
        try {
            id = jsonObject.optString("id");
            charge = jsonObject.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pingpp.createPayment(WaterPaymethod.this, charge);
    }
    private void watercharge() {
        showdialogs("支付成功");
    }
    private void ShowBalance() {
        double doublebalance=jsonObject.optDouble("balance");
        double doubleamount=Double.valueOf(amount);
        if (doubleamount<=doublebalance){
            VariableUtil.balance=jsonObject.optString("balance");
        }else {
            VariableUtil.balance="余额不足";
        }
        initData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_paymethod);
        context=this;
        setCommonHeader("水卡充值");
        customDialog=new CustomDialog(this, "正在加载");
        VariableUtil.balance=null;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userphone=SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent getdata=getIntent();
        amount=getdata.getStringExtra("payFee");
        String giveFee=getdata.getStringExtra("giveFee");
        String icType="2";
        String userNum= getdata.getStringExtra("account");
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(userNum);
        bean.setTypes(icType);
        bean.setGiveFee(giveFee);
        bean.setPayFee(amount);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        initView();
        mAdapter=new PayWayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initBalance();
        initOrder();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });
    }
    private void initOrder() {
        String validateURL= UrlUtil.WaterCard_Recharge;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendrequestUtil.executepostTwo(validateURL,textParams, new ResultListener() {
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

    private void initView() {
        layoutView =findViewById(R.id.id_layout_view);
        TextView tvRealAmount =(TextView)findViewById(R.id.id_real_fee);
        btnSend =(Button)findViewById(R.id.id_btn_pay) ;
        tvRealAmount.setText("¥"+amount);
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        btnSend.setEnabled(false);
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
                        requestCode=1;
                        requestSevice();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void requestSevice() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String payTime=DateUtils.getDateToString(time,pattern);
        WaterAppBean bean=new WaterAppBean();
        bean.setOrderNo(orderId);
        bean.setPayAccount(userphone);
        bean.setPayType("1");
        bean.setPayTime(payTime);
        bean.setAmount(amount);
        bean.setResultCode(resultCode);
        bean.setResultMsg(resultMsg);
        String signs= SecretKeyUtil.getStringSign(bean);
        String extParam=SecretKeyUtil.getextParams(bean);
        String validateURL= UrlUtil.WaterCard_PayUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",signs);
        textParams.put("extParams",extParam);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                waterPayHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                waterPayHandler.sendMessage(msg);
            }
        });
    }
    private void initData() {
        customDialog.show();
        String source="";
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
    private void initBalance() {
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
                balanceHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                balanceHandler.sendMessage(msg);
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
                balanceHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                balanceHandler.sendMessage(msg);
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
    private void showMsg(String result, String msg1, String msg2) {
        String str = result;
        if (str.equals(SendrequestUtil.SUCCESS_VALUE)){
            str="缴费成功";
        }else if (str.equals(SendrequestUtil.FAILURE_VALUE)){
            str="缴费失败";
        }else if (str.equals(SendrequestUtil.CANCEL_VALUE)){
            str="已取消缴费";
        }
        if (result.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(0x002);
            requestResult();
        }else {
            showdialogs(str);
        }
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
                        finish();

                    }
                }).show();
    }
}
