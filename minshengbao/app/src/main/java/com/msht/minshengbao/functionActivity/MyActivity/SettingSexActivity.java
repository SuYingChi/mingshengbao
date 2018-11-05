package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SettingSexActivity extends BaseActivity {
    private RadioGroup sexGroup;
    private String gender;
    private String mSex,password,userId;
    private final SexHandler sexHandler=new SexHandler(this);
    private static class SexHandler extends Handler{
        private WeakReference<SettingSexActivity>mWeakReference;
        public SexHandler(SettingSexActivity activity) {
            mWeakReference=new WeakReference<SettingSexActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SettingSexActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)){
                            activity.onReceivePersionalData();
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
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
    private void onReceivePersionalData() {
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
        Intent sexData=getIntent();
        mSex =sexData.getStringExtra("SEX");
        initfindViewByid();
        setPersonalSex();
    }
    private void initfindViewByid() {
        sexGroup =(RadioGroup)findViewById(R.id.id_sex_radiogroup);
        RadioButton secretRadioButton =(RadioButton)findViewById(R.id.id_sercetradio);
        RadioButton maleRadioButton =(RadioButton)findViewById(R.id.id_maleradio);
        RadioButton femaleRadioButton =(RadioButton)findViewById(R.id.id_femaleradio);
        //设置默认选择
        switch (mSex){
            case "男":
                maleRadioButton.setChecked(true);
                break;
            case "女":
                femaleRadioButton.setChecked(true);
                break;
            case "保密":
                secretRadioButton.setChecked(true);
                break;
            default:
                break;
        }
    }
    private void setPersonalSex() {
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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
                    default:
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
        SendRequestUtil.postDataFromService(validateURL,textParams,sexHandler);
    }
}
