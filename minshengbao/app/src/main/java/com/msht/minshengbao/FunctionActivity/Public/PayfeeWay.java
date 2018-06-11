package com.msht.minshengbao.FunctionActivity.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.PayWayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
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

public class PayfeeWay extends BaseActivity implements View.OnClickListener {

    private Button btn_send;
    private TextView tv_realamount;
    private TextView tv_balance;
    private TextView tv_subtract;
    private TextView tv_shouldAmount;
    private ListViewForScrollView forScrollView;
    private PayWayAdapter mAdapter;
    private String userId,id,voucherId;
    private String PayId;
    private String password;
    private String charge;   //
    private String channels;
    private String type;
    private String discountAmt="0";
    private String orderId="";
    private String amount;
    private String realamount;
    private String source="";
    private JSONArray jsonArray;
    private int requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    Handler SubtractHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        JSONObject optJSONObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            SubtractAmount(optJSONObject);
                        }else {
                            realamount=amount;
                            tv_subtract.setText("¥"+discountAmt);
                            tv_realamount.setText(realamount);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    realamount=amount;
                    tv_subtract.setText("¥"+discountAmt);
                    tv_realamount.setText(realamount);
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
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
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
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
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
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requestCode==0){
                                jsonArray =object.getJSONArray("data");
                                Paywayshow();
                            }else if (requestCode==2){
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
                    if (customDialog!=null&&customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void SubtractAmount(JSONObject optJSONObject) {
        try{
            String showType=optJSONObject.optString("showType");
            JSONObject showData=optJSONObject.getJSONObject("showData");
            double subtract_amount=showData.optDouble("subtract_amount");
            discountAmt=String.valueOf(subtract_amount);
            tv_subtract.setText("¥"+subtract_amount);
            double shouldAmount=Double.parseDouble(amount);
            double real=shouldAmount-subtract_amount;
            real=VariableUtil.TwoDecinmal2(real);
            realamount=String.valueOf(real);
            tv_realamount.setText(realamount);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    private void payresult(JSONObject json) {
        String status=json.optString("status");
        String chargeId=json.optString("chargeId");
        String lottery=json.optString("lottery");
        if (status.equals("0")){
            if (lottery!=null&&(!lottery.equals(""))){
                Intent success=new Intent(context,PaySuccess.class);
                success.putExtra("url",lottery);
                success.putExtra("type","0");
                success.putExtra("orderId",orderId);
                startActivity(success);
                finish();
            }else {
                showdialogs("新订单");
            }
        }else if (status.equals("1")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","0");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
            finish();
        }else if (status.equals("2")){
            Intent success=new Intent(context,PaySuccess.class);
            success.putExtra("type","3");
            success.putExtra("url",lottery);
            success.putExtra("orderId",orderId);
            startActivity(success);
        }else if (status.equals("3")){
            showdialogs("正在支付");
        }
    }
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
            VariableUtil.balance="余额充足";
        }else {
            VariableUtil.balance="余额不足";
        }
        initpayway();
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
            //实付金额为0，不调用ping++,
            if (realamount.equals("0.0")||realamount.equals("0.00")||realamount.equals("0")){
                setResult(0x002);
                requestResult();
            }else {
                Pingpp.createPayment(PayfeeWay.this, charge);
            }
        }else {
            setResult(0x002);
            requestResult();
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
        amount=getdata.getStringExtra("amount");
        PayId=getdata.getStringExtra("id");
        type="1";
        initView();
        mAdapter=new PayWayAdapter(context,List);
        forScrollView.setAdapter(mAdapter);
        initSubtract();
        initData();
        mAdapter.SetOnItemClickListener(new PayWayAdapter.OnRadioItemClickListener() {
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
        forScrollView=(ListViewForScrollView)findViewById(R.id.id_payway_view);
        tv_balance=(TextView)findViewById(R.id.id_tv_balance);
        tv_realamount=(TextView)findViewById(R.id.id_real_fee);
        tv_subtract=(TextView)findViewById(R.id.id_subtract_amount);
        tv_shouldAmount=(TextView)findViewById(R.id.id_should_fee);
        btn_send=(Button)findViewById(R.id.id_btn_pay) ;
        tv_shouldAmount.setText("¥"+amount+"元");
        tv_realamount.setText("¥"+amount);
        tv_subtract.setText("¥"+discountAmt);
        realamount=amount;
        btn_send.setEnabled(false);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });
        PingppLog.DEBUG = true;
    }
    private void initSubtract() {
        customDialog.show();
        String validateURL= UrlUtil.PaySubtract_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("event_code","gas_pay_before");
        textParams.put("event_relate_id",PayId);
        SendrequestUtil.executepostTwo(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                SubtractHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                SubtractHandler.sendMessage(msg);
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

    private void initpayway() {
        customDialog.show();
        String validateURL= UrlUtil.PAYMETHOD_URL;
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        String validateURL= UrlUtil.GasExpense_Pay;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",PayId);
        textParams.put("type",type);
        textParams.put("amount",realamount);
        textParams.put("discountAmt",discountAmt);
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
