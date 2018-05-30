package com.msht.minshengbao.FunctionActivity.WaterApp;

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

import com.msht.minshengbao.Adapter.PaywayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.FunctionActivity.Public.PaySuccess;
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
    private Button btn_send;
    private TextView tv_realamount;
    private ListViewForScrollView forScrollView;
    private View layout_view;
    private String userId;
    private String password;
    private String userphone;
    private String charge;   //
    private String id;
    private String orderId;
    private String channels;
    private String type="7";
    private String Balance="";
    private String sign,extParams;
    private String source="";
    private String amount;
    private String givefee;
    private String usernum="";
    private String Ictype="2";
    private String resultCode=null;
    private String resultMsg=null;
    private int requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private PaywayAdapter mAdapter;
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
                            layout_view.setVisibility(View.VISIBLE);
                        }else {
                            layout_view.setVisibility(View.INVISIBLE);
                            showfaiture(resultMsg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context,msg.obj.toString() ,
                            Toast.LENGTH_SHORT).show();
                    layout_view.setVisibility(View.INVISIBLE);
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
                Intent success=new Intent(context,PaySuccess.class);
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
        if (channels.equals("1")||channels.equals("3")||channels.equals("5")||channels.equals("7"))
        {
            Pingpp.createPayment(WaterPaymethod.this, charge);
        }else {
            setResult(0x002);
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","0");
            startActivity(success);
            finish();
        }
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
        VariableUtil.balance=null;//
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userphone=SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent getdata=getIntent();
        amount=getdata.getStringExtra("payFee");
        givefee=getdata.getStringExtra("giveFee");
        usernum= getdata.getStringExtra("account");
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(usernum);
        bean.setTypes(Ictype);
        bean.setGiveFee(givefee);
        bean.setPayFee(amount);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        initView();
        mAdapter=new PaywayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initBalance();
        initOrder();
        mAdapter.SetOnItemClickListener(new PaywayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btn_send.setEnabled(true);       //选择支付方式可点击
                VariableUtil.paypos=thisPosition;
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
        /*textParams.put("type",Ictype);
        textParams.put("account",usernum);
        textParams.put("payFee",amount);
        textParams.put("giveFee",givefee);*/
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
        layout_view=findViewById(R.id.id_layout_view);
        tv_realamount=(TextView)findViewById(R.id.id_real_fee);
        btn_send=(Button)findViewById(R.id.id_btn_pay) ;
        tv_realamount.setText("¥"+amount);
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        btn_send.setEnabled(false);       //初始未选择支付方式不可点击

        btn_send.setOnClickListener(new View.OnClickListener() {
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
        String Paytime=DateUtils.getDateToString(time,pattern);
        WaterAppBean bean=new WaterAppBean();
        bean.setOrderNo(orderId);
        bean.setPayAccount(userphone);
        bean.setPayType("1");
        bean.setPayTime(Paytime);
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
    private void showMsg(String result, String msg1, String msg2) {
        String str = result;
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
