package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Findpassword extends BaseActivity implements View.OnClickListener {
    private EditText  Etphonenumber, Etcode;
    private EditText  Etnewpassword;
    private ImageView clearimg, showimg;
    private Button Btngetcode, Btnreset;
    private String PhoneNo, verifycode;
    private String newpassword;
    private TimeCount time;     //倒计时时间
    private int requestCode = 0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results = object.optString("result");
                        String Error = object.optString("error");
                        if (Results.equals("success")) {
                            if (requestCode == 1) {
                                displayDialog("密码重置成功，返回登录界面");
                            }
                        } else {
                            if (requestCode == 0) {
                                Btngetcode.setText("获取验证码");
                                Btngetcode.setClickable(true);
                            }
                            displayDialog(Error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
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
            Btngetcode.setClickable(false);
            Btngetcode.setText(millisUntilFinished / 1000 + "秒");
        }
        @Override
        public void onFinish() {
            Btngetcode.setText("获取验证码");
            Btngetcode.setClickable(true);
        }
    }
    private void initView() {
        clearimg = (ImageView) findViewById(R.id.id_clear);
        showimg = (ImageView) findViewById(R.id.id_image_show); //显示密码
        showimg.setTag(0);
        Btngetcode = (Button) findViewById(R.id.id_btn_getcode);
        Btnreset = (Button) findViewById(R.id.id_btn_resetpsw);
        Etphonenumber = (EditText) findViewById(R.id.id_et_phonenumber);
        Etcode = (EditText) findViewById(R.id.id_et_code);
        Etnewpassword = (EditText) findViewById(R.id.id_et_newpassword);
        Btnreset.setEnabled(false);
    }
    private void initEvent() {
        clearimg.setOnClickListener(this);
        Etphonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Etphonenumber.getText().toString())) {
                    Btngetcode.setEnabled(false);            //设置无效点击、背景
                    Btngetcode.setTextColor(Color.parseColor("#FF545454"));
                    Btngetcode.setBackgroundResource(R.drawable.shape_white_border_rectangle);
                } else {
                    Btngetcode.setEnabled(true);            //有效点击，
                    Btngetcode.setTextColor(Color.parseColor("#ffffffff"));
                    Btngetcode.setBackgroundResource(R.drawable.shape_redorange_corners_button);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Btngetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNo = Etphonenumber.getText().toString().trim();
                if (isPhone(PhoneNo)) {
                    Btngetcode.setText("正在发送...");
                    time.start();
                    requestCode = 0;
                    requestService();
                }
            }
        });
        Etcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(Etcode.getText().toString()) || TextUtils.isEmpty(Etnewpassword.getText().toString())
                        ) {
                    Btnreset.setEnabled(false);
                } else {
                    Btnreset.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Etnewpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(Etcode.getText().toString()) || TextUtils.isEmpty(Etnewpassword.getText().toString())
                        ) {
                    Btnreset.setEnabled(false);
                } else {
                    Btnreset.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifycode = Etcode.getText().toString().trim();  //获取验证码
                newpassword = Etnewpassword.getText().toString().trim();  //获取密码
                customDialog.show();
                requestCode = 1;
                requestService();
            }
        });
        showimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) v.getTag();
                switch (tag) {
                    case 0:
                        showimg.setImageResource(R.drawable.password_red);
                        Etnewpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        v.setTag(1);
                        break;
                    case 1:
                        showimg.setImageResource(R.drawable.password_gray);
                        Etnewpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        v.setTag(0);
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
            textParams.put("password", newpassword);
            textParams.put("code", verifycode);
        }
        HttpUrlconnectionUtil.executepost(validateURL, textParams, new ResultListener() {
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
    /*验证电话号码是否正确*/
    private boolean isPhone(String phoneNo) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(phoneNo);
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
                Etphonenumber.setText("");
                break;
            default:
                break;
        }
    }
}
