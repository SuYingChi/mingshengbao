package com.msht.minshengbao.FunctionActivity.WaterApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.msht.minshengbao.Adapter.PayWayAdapter;
import com.msht.minshengbao.Adapter.WaterMealAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.AgreeTreayt;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.HtmlPage;
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
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.pingplusplus.android.Pingpp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WaterRecharge extends BaseActivity {
    private String userId;
    private String password;
    private String userphone;
    private String amount="0.0" ;
    private String balance;
    private String sign,extParams;
    private String givefee;
    private String orderId;
    private String Ictype="1";
    private String source="water_recharge";
    private String payType="8";
    private String channels;
    private Button btn_send;
    private MyNoScrollGridView gridView;
    private ListViewForScrollView mListView;
    private PayWayAdapter mAdapter;
    private WaterMealAdapter waterMealAdapter;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> ListType = new ArrayList<HashMap<String, String>>();
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requestCode=0;
    private int requestType=0;
    private String charge,id;
    private CustomDialog customDialog;
    Handler balanceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog.isShowing()&&requestCode==1){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONObject json =object.optJSONObject("data");
                            if (requestCode==0){
                                ShowBalance(json);
                            }else if (requestCode==1){
                                showcharge(json);
                            }else if (requestCode==2){
                                payresult(json);
                            }
                        }else {
                            if (customDialog.isShowing()&&requestCode==1){
                                customDialog.dismiss();
                            }
                            showdialogs("充值提示",Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showdialogs("提示",msg.obj.toString());
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
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String message = object.optString("message");
                        if(Results.equals("success")) {
                            if (requestType==0){
                                JSONArray jsonArray =object.optJSONArray("data");
                                savaData(jsonArray);
                            }else if (requestType==1){
                                orderId =object.optString("data");
                                MsbappPay();
                            }
                        }else {
                            showdialogs("提示",message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    showdialogs("提示",msg.obj.toString());
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
                            showdialogs("提示",Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    showdialogs("提示",msg.obj.toString());
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
    private void savaData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString("id");
                String type=json.getString("type");
                String amount=json.getString("amount");
                String activityId="";
                String title="";
                String activityType="";
                String scope="";
                String giveFee="0";
                if (!json.isNull("activity")){
                    JSONObject object=json.getJSONObject("activity");
                    activityId=object.optString("id");
                    title=object.optString("title");
                    activityType=object.optString("type");
                    scope=object.optString("scope");
                    giveFee=object.optString("giveFee");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type",type);
                map.put("amount",amount);
                map.put("activityId",activityId);
                map.put("title",title);
                map.put("activityType",activityType);
                map.put("scoped",scope);
                map.put("giveFee",giveFee);
                ListType.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (ListType.size()!=0){
            waterMealAdapter.notifyDataSetChanged();
        }
    }

    private void ShowBalance(JSONObject json) {
        double doublebalance=json.optDouble("balance");
        double doubleamount=Double.valueOf(amount);
        balance=json.optString("balance");
        VariableUtil.balance=json.optString("balance");
        initPaymethod();
    }
    private void showcharge(JSONObject json) {
        try {
            id = json.optString("id");
            charge = json.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channels.equals("1")||channels.equals("3")||channels.equals("5")||channels.equals("7"))
        {
            Pingpp.createPayment(WaterRecharge.this, charge);
        }else {
            setResult(0x002);
            rechargeSuccess();
        }
    }
    private void payresult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals("0")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                showdialogs("充值提示","新订单");
            }

        }else if (status.equals("1")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                rechargeSuccess();
            }
        }else if (status.equals("2")){
            showdialogs("充值提示","充值失败");
        }else if (status.equals("3")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,HtmlPage.class);
                success.putExtra("url",lottery);
                success.putExtra("navigate","活动");
                startActivity(success);
                finish();
            }else {
                showdialogs("充值提示","正在支付");
            }
        }
    }
    private void showdialogs(String title,String str) {
            new PromptDialog.Builder(this)
                    .setTitle(title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(str)
                    .setButton1("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
    }
    private void rechargeSuccess() {
        Intent intent=new Intent(context,WaterSuccess.class);
        intent.putExtra("amount",amount);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_recharge);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        VariableUtil.balance=null;//
        VariableUtil.MealPos=-1;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userphone=SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader("直饮水账户充值");
        initView();
        mAdapter=new PayWayAdapter(context,List);
        mListView.setAdapter(mAdapter);
        waterMealAdapter=new WaterMealAdapter(context,ListType);
        gridView.setAdapter(waterMealAdapter);
        initBalance();
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btn_send.setEnabled(true);       //选择支付方式可点击
                VariableUtil.paypos=thisPosition;
                mAdapter.notifyDataSetChanged();
                channels=List.get(thisPosition).get("channel");
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                amount=ListType.get(position).get("amount");
                givefee=ListType.get(position).get("giveFee");
                VariableUtil.MealPos=position;
                contrastBalance(amount);
                waterMealAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
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
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
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
            requestResult();
        }else {
            showdialogs("充值提示",str);
        }
    }
    private void contrastBalance(String amount) {
        double doublebalance=Double.valueOf(balance);
        double doubleamount=Double.valueOf(amount);
        if (doubleamount>doublebalance){
            VariableUtil.balance="余额不足";
        }else {
            VariableUtil.balance=balance;
        }
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
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WaterRecharge_Meal;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        SendrequestUtil.executepostTwo(validateURL, textParams, new ResultListener() {
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
    private void initView() {
        btn_send=(Button)findViewById(R.id.id_btn_send);
        btn_send.setEnabled(false);
        gridView=(MyNoScrollGridView)findViewById(R.id.id_recharge_view);
        mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        findViewById(R.id.id_back_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AgreeTreayt.class);
                intent.putExtra("idNo","5");
                startActivity(intent);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTip();
            }
        });

    }
    private void showTip() {
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
                        requestType=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String Paytime=DateUtils.getDateToString(time,pattern);
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(userphone);
        bean.setTypes(Ictype);
        bean.setGiveFee(givefee);
        bean.setPayFee(amount);
        bean.setPayType(channels);
        bean.setPayTime(Paytime);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        String validateURL= UrlUtil.WaterCard_Recharge;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
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
    private void initPaymethod() {
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
    private void MsbappPay() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("amount",amount);
        textParams.put("type",payType);
        textParams.put("orderId",orderId);
        textParams.put("channel",channels);
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
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.executepostTwo(validateURL,textParams, new ResultListener() {
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

}
