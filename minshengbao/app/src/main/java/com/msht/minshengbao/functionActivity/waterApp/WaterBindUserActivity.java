package com.msht.minshengbao.functionActivity.waterApp;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBindUserActivity extends BaseActivity implements View.OnClickListener {
    private Button btnCode;
    private Button btnBind;
    private EditText etPhone;
    private EditText etCode;
    private TimeCount time;
    private String account;
    private String phone;
    private int    requestCode=0;

    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<WaterBindUserActivity> mWeakReference;
        public RequestHandler(WaterBindUserActivity activity) {
            mWeakReference = new WeakReference<WaterBindUserActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterBindUserActivity activity=mWeakReference.get();
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
                        String error = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==1){
                                activity.onBindSuccess();
                            }else if (activity.requestCode==2){
                                activity.onSwitchSuccess();
                            }
                        }else {
                            if (activity.requestCode==0) {
                                activity.btnCode.setText("获取验证码");
                                activity.btnCode.setClickable(true);
                            }
                            CustomToast.showErrorLong(error);
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

    private void onSwitchSuccess() {
        setResult(2);
        finish();
    }
    private void onBindSuccess() {
       setResult(1);
       onSwitchAccount();
    }
    private void onSwitchAccount() {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String openTime=DateUtils.getDateToString(time,pattern);
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",account);
            object.put("phone",phone);
            object.put("time",openTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("account",account);
        treeMap.put("phone",phone);
        treeMap.put("time",openTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonResult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("extParams",extParams);
        textParams.put("sign",sign);
        requestCode=2;
        String requestUrl= UrlUtil.WATER_SWITCH_ACCOUNT;
        customDialog.show();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_bind_user);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        mPageName="绑定账户(水宝)";
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader(mPageName);
        initFindViewId();
        time=new TimeCount(60000,1000);
    }
    private void initFindViewId() {
        btnBind=(Button)findViewById(R.id.id_btn_bind);
        btnCode=(Button)findViewById(R.id.id_btn_code);
        etCode=(EditText)findViewById(R.id.id_et_code);
        etPhone=(EditText)findViewById(R.id.id_et_phone);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etCode.addTextChangedListener(myTextWatcher);
        etPhone.addTextChangedListener(myTextWatcher);
        btnCode.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnCode.setEnabled(false);
        btnBind.setEnabled(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_bind:
                onBindMobile();
                break;
            case R.id.id_btn_code:
                onGetVerification();
                break;
            default:
                break;
        }
    }
    private void onGetVerification() {
        if (RegularExpressionUtil.isPhone(etPhone.getText().toString())) {
            btnCode.setText("正在发送...");
            time.start();
            requestCode=0;
            requestService();
        }else {
            CustomToast.showErrorLong("您输入电话号码不正确");
        }
    }
    private void requestService() {
        phone=etPhone.getText().toString().trim();
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String openTime=DateUtils.getDateToString(time,pattern);
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("type","3");
            object.put("phone",phone);
            object.put("time",openTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("type","3");
        treeMap.put("phone",phone);
        treeMap.put("time",openTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonResult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        requestCode=0;
        String requestUrl= UrlUtil.WATER_VERIFY_CODE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("extParams",extParams);
        textParams.put("sign",sign);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    private void onBindMobile() {

        String verifyCode=etCode.getText().toString().trim();
        String phone=etPhone.getText().toString().trim();
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String openTime=DateUtils.getDateToString(time,pattern);
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",account);
            object.put("verifyCode",verifyCode);
            object.put("phone",phone);
            object.put("time",openTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("account",account);
        treeMap.put("verifyCode",verifyCode);
        treeMap.put("phone",phone);
        treeMap.put("time",openTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonResult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        requestCode=1;
        String requestUrl= UrlUtil.WATER_BIND_ACCOUNT_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("extParams",extParams);
        textParams.put("sign",sign);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);

    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etPhone.getText().toString())) {
                btnCode.setEnabled(false);
            } else {
                //有效点击，
                btnCode.setEnabled(true);
            }
            if(TextUtils.isEmpty(etPhone.getText().toString())||TextUtils.isEmpty(etCode.getText().toString())){
                btnBind.setEnabled(false);
            }else {
                btnBind.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {  //计时过程
            btnCode.setClickable(false);
            btnCode.setText(millisUntilFinished/1000+"秒");
        }
        @Override
        public void onFinish() {
            btnCode.setText("获取验证码");
            btnCode.setClickable(true);
        }
    }
}
