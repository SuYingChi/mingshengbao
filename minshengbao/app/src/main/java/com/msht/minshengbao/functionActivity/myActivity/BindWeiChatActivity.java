package com.msht.minshengbao.functionActivity.myActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.AgreeTreatyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class BindWeiChatActivity extends BaseActivity {
   private String unionId;
   private String name;
   private String gender;
   private String iconUrl;
   private String pushUrl;
   private EditText etPhone;
   private EditText etVerifyCode;
   private Button   btnVerifyCode;
   private Button   btnSend;
   private TimeCount time;
    public static final String MY_ACTION = "ui";
   private CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wei_chat);
        context=this;
        setCommonHeader("绑定手机");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
             unionId=data.getStringExtra("unionId");
             name=data.getStringExtra("name");
             gender=data.getStringExtra("gender");
             iconUrl=data.getStringExtra("iconUrl");
             pushUrl=data.getStringExtra("pushUrl");
        }
        initView();
        time=new TimeCount(120000,1000);
    }

    private void initView() {
        etPhone=(EditText)findViewById(R.id.id_et_phone);
        etVerifyCode=(EditText)findViewById(R.id.id_et_code);
        btnVerifyCode=(Button)findViewById(R.id.id_btn_code);
        btnSend=(Button)findViewById(R.id.id_ensure);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etVerifyCode.addTextChangedListener(myTextWatcher);
        etPhone.addTextChangedListener(myTextWatcher);
        findViewById(R.id.id_clear_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPhone.setText("");
            }
        });
        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String phoneNo = etPhone.getText().toString().trim();
                if (RegularExpressionUtil.isPhone(phoneNo)) {
                    if (time!=null){
                        btnVerifyCode.setText("正在发送...");
                        time.start();
                        requestService(phoneNo);
                    }
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RegularExpressionUtil.isPhone(etPhone.getText().toString())){
                    onRequestBind();
                }else {
                    CustomToast.showErrorLong("手机格式不正确");
                }

            }
        });
        findViewById(R.id.id_treaty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTreaty();
            }
        });
    }

    private void onTreaty() {
        String url=UrlUtil.AgreeTreayt_Url;
        Intent treaty=new Intent(context,AgreeTreatyActivity.class);
        treaty.putExtra("navigation","注册协议书");
        treaty.putExtra("url",url);
        startActivity(treaty);
    }
    private void onRequestBind() {
        String phoneNo=etPhone.getText().toString().trim();
        String code=etVerifyCode.getText().toString().trim();
        String versionName=AppPackageUtil.getPackageVersionName(getApplicationContext());
        versionName =versionName.replace("v","");
        String requestUrl=UrlUtil.BIND_WEI_CHAT_PHONE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("phone", phoneNo);
        textParams.put("captcha", code);
        textParams.put("unionid", unionId);
        textParams.put("nickName", name);
        textParams.put("headImg", iconUrl);
        textParams.put("city_id", VariableUtil.cityId);
        textParams.put("client","android");
        textParams.put("version", versionName);
        if (customDialog!=null){
            customDialog.show();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showErrorLong(data.toString());
            }
        });
    }
    private void onAnalysisData(String data) {
        try {
            JSONObject object = new JSONObject(data);
            String result=object.optString("result");
            String error = object.optString("error");
            JSONObject objectInfo = object.optJSONObject("data");
            if (result.equals(SendRequestUtil.SUCCESS_VALUE)){
                String isWeChatBind=objectInfo.optString("isWeChatBind");
                if (isWeChatBind.equals(ConstantUtil.VALUE_ONE)){
                    String userId=objectInfo.optString("id");
                    String password=objectInfo.optString("password");
                    String phone=objectInfo.optString("phone");
                    String nickname=objectInfo.optString("nickname");
                    String avatar=objectInfo.optString("avatar");
                    String shop=objectInfo.optString("shop");
                    String shopCookie=objectInfo.optString("shopCookie");
                    SharedPreferencesUtil.putUserId(this,SharedPreferencesUtil.UserId,userId);
                    SharedPreferencesUtil.putAvatarUrl(this,SharedPreferencesUtil.AvatarUrl,avatar);
                    SharedPreferencesUtil.putPassword(this,SharedPreferencesUtil.Password,password);
                    SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.NickName,nickname);
                    SharedPreferencesUtil.putUserName(this,SharedPreferencesUtil.UserName,phone);
                    SharedPreferencesUtil.putpassw(this,SharedPreferencesUtil.passw,password);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
                    SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.shopCookie,shopCookie);
                    SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,isWeChatBind);
                    Intent broadcast=new Intent();
                    broadcast.setAction(MY_ACTION);
                    broadcast.putExtra("broadcast", "1");
                    sendBroadcast(broadcast);
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("index",0);
                    intent.putExtra("pushUrl",pushUrl);
                    startActivity(intent);
                    MyApplication.removeAllActivity();
                    finish();
                }
            }else {
                CustomToast.showErrorLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void requestService(String phoneNo) {
        String requestUrl=UrlUtil.Captcha_CodeUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("username", phoneNo);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {}
            @Override
            public void responseReqFailed(Object data) {}
        });
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etPhone.getText().toString())) {
                btnVerifyCode.setEnabled(false);
            } else {
                //有效点击，
                btnVerifyCode.setEnabled(true);
            }
            if(TextUtils.isEmpty(etPhone.getText().toString())||TextUtils.isEmpty(etVerifyCode.getText().toString() )){
                btnSend.setEnabled(false);
            }else {
                btnSend.setEnabled(true);
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
            String stringTime=millisUntilFinished/1000+"秒";
            btnVerifyCode.setClickable(false);
            btnVerifyCode.setText(stringTime);
        }
        @Override
        public void onFinish() {
            btnVerifyCode.setText("获取验证码");
            btnVerifyCode.setClickable(true);
        }
    }
    private void removeTimeout() {
        if (time!=null){
            time.cancel();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeTimeout();
    }
}
