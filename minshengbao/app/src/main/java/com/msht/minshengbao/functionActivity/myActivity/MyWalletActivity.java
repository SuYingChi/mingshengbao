package com.msht.minshengbao.functionActivity.myActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.Dialog.SelfPayDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 *
 * @author hong
 * @date 2017/6/5
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvBalance;
    private String   password,userId;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final BindingHandler bindingHandler=new BindingHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<MyWalletActivity> mWeakReference;
        public RequestHandler(MyWalletActivity activity) {
            mWeakReference=new WeakReference<MyWalletActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MyWalletActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject data=object.getJSONObject("data");
                            String balance=data.optString("balance");
                            activity.tvBalance.setText(balance);
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class BindingHandler  extends Handler{
        private WeakReference<MyWalletActivity> mWeakReference;
        public BindingHandler(MyWalletActivity activity) {
            mWeakReference=new WeakReference<MyWalletActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MyWalletActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            if (jsonArray.length()==0){
                                activity.noticeDialogs();
                            }
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void displayDialog(String error) {
        new PromptDialog.Builder(context)
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

    private void noticeDialogs() {
        final SelfPayDialog selfPayDialog=new SelfPayDialog(context);
        selfPayDialog.setOnpositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agree=new Intent(context, AutomatePayActivity.class);
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
        TextView  tvRightText =(TextView)findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("明细");
        tvBalance =(TextView)findViewById(R.id.id_balance);
        tvRightText.setOnClickListener(this);
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
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void initBinding() {
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        String validateURL = UrlUtil.AutomataPay_Url;
        SendRequestUtil.postDataFromService(validateURL,textParams,bindingHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_card:
                Intent discount=new Intent(context, DiscountCouponActivity.class);
                startActivityForResult(discount,2);
                break;
            case R.id.id_layout_recharge:
                Intent recharge=new Intent(context, RechargeValueActivity.class);
                startActivityForResult(recharge,1);
                break;
            case R.id.id_layout_gaspayfee:
                Intent agree=new Intent(context, AutomatePayActivity.class);
                startActivity(agree);
                break;
            case R.id.id_tv_rightText:
                Intent detail=new Intent(context, IncomeExpenseActivity.class);
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
                //充值成功
                if (resultCode==1){
                    initData();
                }
                break;
            case 2:
                //卡卷包使用，返回触发
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
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
