package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText etPhoneNumber, etCode;
    private EditText etNewPassword;
    private ImageView clearImg, showImg;
    private Button btnGetCode, btnReset;
    private String PhoneNo, verifyCode;
    private String newPassword;
    private TimeCount time;
    private int requestCode = 0;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private CustomDialog customDialog;
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<FindPasswordActivity> mWeakReference;
        public RequestHandler(FindPasswordActivity activity) {
            mWeakReference = new WeakReference<FindPasswordActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final FindPasswordActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        if (results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode == 1) {
                                activity.onDisplayDialog("密码重置成功，返回登录界面");
                            }
                        } else {
                            if (activity.requestCode == 0) {
                                activity.btnGetCode.setText("获取验证码");
                                activity.btnGetCode.setClickable(true);
                            }
                            activity.onDisplayDialog(error);
                        }
                    } catch (Exception e) {
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
    private void onDisplayDialog(String s) {
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
        setContentView(R.layout.activity_findpassword);
        context = this;
        setCommonHeader("密码找回");
        customDialog = new CustomDialog(this, "正在加载");
        initView();
        time = new TimeCount(120000, 1000);
        initEvent();
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            btnGetCode.setClickable(false);
            btnGetCode.setText(millisUntilFinished / 1000 + "秒");
        }
        @Override
        public void onFinish() {
            btnGetCode.setText("获取验证码");
            btnGetCode.setClickable(true);
        }
    }
    private void initView() {
        clearImg = (ImageView) findViewById(R.id.id_clear);
        showImg = (ImageView) findViewById(R.id.id_image_show);
        showImg.setTag(0);
        btnGetCode = (Button) findViewById(R.id.id_btn_getcode);
        btnReset = (Button) findViewById(R.id.id_btn_resetpsw);
        etPhoneNumber = (EditText) findViewById(R.id.id_et_phonenumber);
        etCode = (EditText) findViewById(R.id.id_et_code);
        etNewPassword = (EditText) findViewById(R.id.id_et_newpassword);
        btnReset.setEnabled(false);
    }
    private void initEvent() {
        clearImg.setOnClickListener(this);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
                    btnGetCode.setEnabled(false);
                    btnGetCode.setTextColor(Color.parseColor("#FF545454"));
                    btnGetCode.setBackgroundResource(R.drawable.shape_white_border_rectangle);
                } else {
                    btnGetCode.setEnabled(true);
                    btnGetCode.setTextColor(Color.parseColor("#ffffffff"));
                    btnGetCode.setBackgroundResource(R.drawable.shape_redorange_corners_button);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNo = etPhoneNumber.getText().toString().trim();
                if (isPhone(PhoneNo)) {
                    btnGetCode.setText("正在发送...");
                    time.start();
                    requestCode = 0;
                    requestService();
                }
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(etCode.getText().toString()) || TextUtils.isEmpty(etNewPassword.getText().toString())
                        ) {
                    btnReset.setEnabled(false);
                } else {
                    btnReset.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(etCode.getText().toString()) || TextUtils.isEmpty(etNewPassword.getText().toString())
                        ) {
                    btnReset.setEnabled(false);
                } else {
                    btnReset.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode = etCode.getText().toString().trim();
                newPassword = etNewPassword.getText().toString().trim();
                customDialog.show();
                requestCode = 1;
                requestService();
            }
        });
        showImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) v.getTag();
                switch (tag) {
                    case 0:
                        showImg.setImageResource(R.drawable.password_red);
                        etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        v.setTag(1);
                        break;
                    case 1:
                        showImg.setImageResource(R.drawable.password_gray);
                        etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        v.setTag(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void requestService() {
        String validateURL = "";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("username", PhoneNo);
        if (requestCode == 0) {
            validateURL = UrlUtil.Captcha_CodeUrl;
        } else if (requestCode == 1) {
            validateURL = UrlUtil.Resetpwd_Url;
            textParams.put("password", newPassword);
            textParams.put("code", verifyCode);
        }
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    /**
     *
     * @param phoneNo  电话号码
     * @return
     */
    private boolean isPhone(String phoneNo) {
        Matcher matcher =NUMBER_PATTERN.matcher(phoneNo);
        if (matcher.matches()) {
            return true;
        } else {
            new PromptDialog.Builder(this)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("您输入电话号码不正确")
                    .setButton1("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_clear:
                etPhoneNumber.setText("");
                break;
            default:
                break;
        }
    }
}
