package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingSex extends BaseActivity {
    private RadioGroup Sexgroup;
    private RadioButton maleradio,femaleradio,secretradio;
    private String gender;
    private String fsex,password,userId;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    Handler sexHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (Results.equals("success")){
                            Judgedata();
                        }else {
                            failure(Error);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    failure(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }

    };
    private void failure(String error) {
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
    private void Judgedata() {
        SharedPreferences share = this.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=share.edit();
        editor.remove(SharedPreferencesUtil.Sex);
        SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.Sex,gender);
        Intent sexN=new Intent();
        sexN.putExtra("gender", gender);
        setResult(5, sexN);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sex);
        setCommonHeader("设置性别");
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent sexs=getIntent();
        fsex=sexs.getStringExtra("SEX");
        initfindViewByid();
        Setpersonalsex();
    }
    private void initfindViewByid() {
        Sexgroup=(RadioGroup)findViewById(R.id.id_sex_radiogroup);
        secretradio=(RadioButton)findViewById(R.id.id_sercetradio);
        maleradio=(RadioButton)findViewById(R.id.id_maleradio);
        femaleradio=(RadioButton)findViewById(R.id.id_femaleradio);
        if (fsex.equals("男")){     //设置默认选择
            maleradio.setChecked(true);
        }else if (fsex.equals("保密")){
            secretradio.setChecked(true);
        }else if (fsex.equals("女")){
            femaleradio.setChecked(true);
        }
    }
    private void Setpersonalsex() {
        Sexgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_sercetradio:
                        gender = "保密";
                        break;
                    case R.id.id_maleradio:
                        gender = "男";
                        break;
                    case R.id.id_femaleradio:
                        gender = "女";
                        break;
                }
                requestSevice();
            }
        });
    }
    private void requestSevice() {
        String validateURL = UrlUtil.GasmodifyInfo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("sex", gender);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                sexHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                sexHandler.sendMessage(msg);
            }
        });
    }
}
