package com.msht.minshengbao.FunctionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 *  绑定账户
 * @author hong
 * @date 2018/06/26
 */
public class BindingAccountActivity extends BaseActivity implements View.OnClickListener {
    private EditText  etPhone;
    private EditText  etCode;
    private Button    btnEnsure;
    private Button    btnCode;
    private TimeCount time;
    private String    mPhone;
    private int       requestCode=0;
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<BindingAccountActivity> mWeakReference;
        public RequestHandler(BindingAccountActivity activity) {
            mWeakReference = new WeakReference<BindingAccountActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final BindingAccountActivity activity=mWeakReference.get();
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
                            if (activity.requestCode==1){
                                activity.displayDialog("您已绑定成功");
                                activity.setResult(1);
                            }
                        }else {
                            if (activity.requestCode==0) {
                                activity.btnCode.setText("获取验证码");
                                activity.btnCode.setClickable(true);
                            }
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void onFailure(String error) {
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

    private void displayDialog(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_binding_account);
        context=this;
        setCommonHeader("绑定账户");
        customDialog=new CustomDialog(this, "正在加载");
        initFindViewById();
        time=new TimeCount(60000,1000);
    }
    private void initFindViewById() {
        etPhone=(EditText)findViewById(R.id.id_et_phone);
        etCode=(EditText)findViewById(R.id.id_et_code);
        btnCode=(Button)findViewById(R.id.id_btn_code);
        btnEnsure=(Button)findViewById(R.id.id_btn_ensure);
        btnEnsure.setEnabled(false);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etCode.addTextChangedListener(myTextWatcher);
        etPhone.addTextChangedListener(myTextWatcher);
        findViewById(R.id.id_btn_apply).setOnClickListener(this);
        btnCode.setOnClickListener(this);
        btnEnsure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_apply:
                onCreateNewCustomer();
                break;
            case R.id.id_btn_ensure:
                onBindMobile();
                break;
            case R.id.id_btn_code:
                onGetVerification();
                break;
            default:
                break;
        }
    }

    private void onCreateNewCustomer() {
        Intent intent=new Intent(context,NewUserActivity.class);
        startActivity(intent);
    }

    private void onGetVerification() {
        mPhone = etPhone.getText().toString().trim();
        if (RegularExpressionUtil.isPhone(mPhone)) {
            btnCode.setText("正在发送...");
            time.start();
            requestCode=0;
            requestService();
        }else {
            new PromptDialog.Builder(context)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("您输入电话号码不正确")
                    .setButton1("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etPhone.getText().toString())) {
                btnCode.setEnabled(false);
                btnCode.setTextColor(Color.parseColor("#FF545454"));
                btnCode.setBackgroundResource(R.drawable.shape_white_border_rectangle);
            } else {
                //有效点击，
                btnCode.setEnabled(true);
                btnCode.setTextColor(Color.parseColor("#ffffffff"));
                btnCode.setBackgroundResource(R.drawable.shape_redorange_corners_button);
            }
            if(TextUtils.isEmpty(etPhone.getText().toString())||TextUtils.isEmpty(etCode.getText().toString())){
                btnEnsure.setEnabled(false);
            }else {
                btnEnsure.setEnabled(true);
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
    private void requestService() {
        requestCode=0;
        String requestUrl= UrlUtil.LPG_GET_CAPTCHA_URL;
        String mobile=etPhone.getText().toString().trim();
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("mobile",mobile);
        OkHttpRequestManager.getInstance(context).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_GET,textParams,requestHandler);

    }
    private void onBindMobile() {

        requestCode=1;
        String requestUrl= UrlUtil.LPG_BIND_MOBILE_URL;
        String mobile=etPhone.getText().toString().trim();
        String captchaCode=etCode.getText().toString().trim();
        String msbMobile= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
        String isApp="0";
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("mobile",mobile);
        textParams.put("captchaCode",captchaCode);
        textParams.put("msbMobile",msbMobile);
        textParams.put("isApp",isApp);
        customDialog.show();
        OkHttpRequestManager.getInstance(context).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_GET,textParams,requestHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
