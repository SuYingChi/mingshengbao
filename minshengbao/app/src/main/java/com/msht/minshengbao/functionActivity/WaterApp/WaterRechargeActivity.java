package com.msht.minshengbao.functionActivity.WaterApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.adapter.PayWayAdapter;
import com.msht.minshengbao.adapter.WaterMealAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.AgreeTreaty;
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
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.pingplusplus.android.Pingpp;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Demo class
 * 直饮水充值
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/3/2  
 */
public class WaterRechargeActivity extends BaseActivity {
    private String userId;
    private String password;
    private String userPhone;
    private String amount="0.0" ;
    private String balance;
    private String giveFee;
    private String orderId;
    private String payType="8";
    private String channels;
    private EditText etRecommend;
    private Button btnSend;
    private MyNoScrollGridView gridView;
    private ListViewForScrollView mListView;
    private PayWayAdapter mAdapter;
    private WaterMealAdapter waterMealAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> mListType = new ArrayList<HashMap<String, String>>();
    private static final String PAGE_NAME="直饮水账户充值";
    private int requestCode=0;
    private int requestType=0;
    private String charge,id;
    private CustomDialog customDialog;
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final MethodHandler methodHandler=new MethodHandler(this);
    private static class BalanceHandler extends Handler{
        private WeakReference<WaterRechargeActivity> mWeakReference;
        public BalanceHandler(WaterRechargeActivity acitvity) {
            mWeakReference=new WeakReference<WaterRechargeActivity>(acitvity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WaterRechargeActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            JSONObject json =object.optJSONObject("data");
                            if (activity.requestCode==0){
                                activity.onShowBalance(json);
                            }else if (activity.requestCode==1){
                                activity.onShowCharge(json);
                            }else if (activity.requestCode==2){
                                activity.onPayResult(json);
                            }
                        }else {
                            activity.onShowDialog("充值提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.onShowDialog("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class RequestHandler extends Handler{
        private WeakReference<WaterRechargeActivity> mWeakReference;
        public RequestHandler(WaterRechargeActivity acitvity) {
            mWeakReference=new WeakReference<WaterRechargeActivity>(acitvity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterRechargeActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONArray jsonArray =object.optJSONArray("data");
                                activity.saveData(jsonArray);
                            }else if (activity.requestType==1){
                                activity.orderId =object.optString("data");
                                activity.msbAppPay();
                            }
                        }else {
                            activity.onShowDialog("提示",message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.onShowDialog("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class MethodHandler extends Handler{
        private WeakReference<WaterRechargeActivity> mWeakReference;
        public MethodHandler(WaterRechargeActivity activity) {
            mWeakReference=new WeakReference<WaterRechargeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterRechargeActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            JSONArray array =object.getJSONArray("data");
                            activity.onPayWayShow(array);
                        }else {
                            activity.onShowDialog("提示",error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.onShowDialog("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onPayWayShow(JSONArray array) {
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
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void saveData(JSONArray jsonArray) {
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
                mListType.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mListType.size()!=0){
            waterMealAdapter.notifyDataSetChanged();
        }
    }
    private void onShowBalance(JSONObject json) {
        double doubleBalance=json.optDouble("balance");
        double doubleAmount=Double.valueOf(amount);
        balance=json.optString("balance");
        VariableUtil.balance=json.optString("balance");
        initPaymethod();
    }
    private void onShowCharge(JSONObject json) {
        try {
            id = json.optString("id");
            charge = json.optString("charge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channels.equals(VariableUtil.VALUE_ONE)||channels.equals(VariableUtil.VALUE_THREE)||channels.equals(VariableUtil.VALUE_FIVE)||channels.equals(VariableUtil.VALUE_SEVER))
        {
            Pingpp.createPayment(WaterRechargeActivity.this, charge);
        }else {
            setResult(0x002);
            rechargeSuccess(null);
        }
    }
    private void onPayResult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        switch (status){
            case VariableUtil.VALUE_ZERO:
                rechargeSuccess(lottery);
                break;
            case VariableUtil.VALUE_ONE:
                rechargeSuccess(lottery);
                break;
            case VariableUtil.VALUE_TWO:
                onShowDialog("充值提示","充值失败");
                break;
            case VariableUtil.VALUE_THREE:
                onShowDialog("充值提示","正在支付");
                break;
                default:
                    break;
        }
    }
    private void onShowDialog(String title, String str) {
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
    private void rechargeSuccess(String lottery) {
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("type","4");
        success.putExtra("url",lottery);
        success.putExtra("orderId",orderId);
        startActivity(success);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_recharge);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        VariableUtil.balance=null;
        VariableUtil.MealPos=-1;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        userPhone =SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader("选择充值套餐");
        initView();
        mAdapter=new PayWayAdapter(context, mList);
        mListView.setAdapter(mAdapter);
        waterMealAdapter=new WaterMealAdapter(context, mListType);
        gridView.setAdapter(waterMealAdapter);
        initBalance();
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
            @Override
            public void ItemClick(View view, int thisPosition) {
                btnSend.setEnabled(true);
                VariableUtil.payPos =thisPosition;
                mAdapter.notifyDataSetChanged();
                channels= mList.get(thisPosition).get("channel");
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                amount= mListType.get(position).get("amount");
                giveFee = mListType.get(position).get("giveFee");
                VariableUtil.MealPos=position;
                contrastBalance(amount);
                waterMealAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            //if (requestCode == REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK) {
                String result=data.getStringExtra("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getStringExtra("error_msg");
                String extraMsg = data.getStringExtra("extra_msg");
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }
    private void showMsg(String title, String msg1, String msg2) {
        String str = title;
        switch (title){
            case SendrequestUtil.SUCCESS_VALUE:
                str="缴费成功";
                break;
            case SendrequestUtil.FAILURE_VALUE:
                str="缴费失败";
                break;
            case SendrequestUtil.CANCEL_VALUE:
                str="已取消缴费";
                break;
                default:
                    break;
        }
        if (title.equals(SendrequestUtil.SUCCESS_VALUE)){
            setResult(0x002);
            requestResult();
        }else {
            onShowDialog("充值提示",str);
        }
    }
    private void contrastBalance(String amount) {
        double doubleBalance=Double.valueOf(balance);
        double doubleAmount=Double.valueOf(amount);
        if (doubleAmount>doubleBalance){
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
        SendrequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WaterRecharge_Meal;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void initView() {
        etRecommend=(EditText)findViewById(R.id.id_et_recommend);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        btnSend.setEnabled(false);
        gridView=(MyNoScrollGridView)findViewById(R.id.id_recharge_view);
        mListView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        findViewById(R.id.id_back_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=UrlUtil.Recharge_BackAgree;
                Intent intent=new Intent(context, AgreeTreaty.class);
                intent.putExtra("url",url);
                intent.putExtra("navigation","充返活动说明");
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendServiceShowTip();
            }
        });
    }
    private void onSendServiceShowTip() {
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
        String sign=getSign();
        String extParams=getExtParams();;
        String validateURL= UrlUtil.WaterCard_Recharge;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
}
    private String getExtParams() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String icType="1";
        String orderFrom="0";
        String payTime=DateUtils.getDateToString(time,pattern);
        String recommendCode=etRecommend.getText().toString().trim();
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",userPhone);
            object.put("type",icType);
            object.put("payFee",amount);
            object.put("giveFee",giveFee);
            object.put("payType",channels);
            object.put("payTime",payTime);
            object.put("orderFrom",orderFrom);
            object.put("recommendCode",recommendCode);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return SecretKeyUtil.getKeyextParams(jsonResult);
    }

    private String getSign() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String icType="1";
        String orderFrom="0";
        String payTime=DateUtils.getDateToString(time,pattern);
        String recommendCode=etRecommend.getText().toString().trim();
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("type",icType);
        treeMap.put("account",userPhone);
        treeMap.put("payFee",amount);
        treeMap.put("giveFee",giveFee);
        treeMap.put("payType",channels);
        treeMap.put("payTime",payTime);
        treeMap.put("orderFrom",orderFrom);
        treeMap.put("recommendCode",recommendCode);
        return SecretKeyUtil.getKeySign(treeMap);
    }

    private void initPaymethod() {
        String source="water_recharge";
        String validateURL= UrlUtil.PAY_METHOD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("source",source);
        SendrequestUtil.postDataFromService(validateURL,textParams,methodHandler);

    }
    private void msbAppPay() {
        String validateURL= UrlUtil.PayfeeWay_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("amount",amount);
        textParams.put("type",payType);
        textParams.put("orderId",orderId);
        textParams.put("channel",channels);
        SendrequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void requestResult() {
        requestCode=2;
        String validateURL= UrlUtil.PAY_RESULT_NOTARIZE;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.postDataFromServiceTwo(validateURL,textParams,balanceHandler);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
