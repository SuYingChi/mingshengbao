package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SettingNickname extends BaseActivity {
    private EditText etNickname;
    private TextView tvEnsure;
    private String mNickname,password,userId;
    public static final String NICK_NAME = "NICK";
    private final NickHandler nickHandler=new NickHandler(this);
    private static class NickHandler extends Handler{
        private WeakReference<SettingNickname> mWeakReference;
        public NickHandler(SettingNickname activity) {
            mWeakReference=new WeakReference<SettingNickname>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SettingNickname activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onUpdateNickname(results);
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (JSONException e) {
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
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void onUpdateNickname(String results) {
        SharedPreferences share = this.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=share.edit();
        editor.remove(SharedPreferencesUtil.NickName);
        SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.NickName, mNickname);
        Intent nickN=new Intent();
        nickN.putExtra("nickname", mNickname);
        setResult(1, nickN);
        Intent broadcast=new Intent();
        broadcast.setAction(NICK_NAME);
        broadcast.putExtra("nickname", mNickname);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_nickname);
        setCommonHeader("设置昵称");
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewByid();
        initEvent();
    }
    private void initFindViewByid() {
        tvEnsure =(TextView)findViewById(R.id.id_tv_rightText);
        tvEnsure.setVisibility(View.VISIBLE);
        tvEnsure.setText("确定");
        etNickname =(EditText)findViewById(R.id.id_et_nickname);
        String nick=SharedPreferencesUtil.getNickName(this,SharedPreferencesUtil.NickName,"");
        etNickname.setText(nick);
        //光标尾处
        etNickname.setSelection(nick.length());
    }
    private void initEvent() {
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNickname = etNickname.getText().toString().trim();
                if(matchNicknameMsg()){
                    requestService();
                }
            }
        });
    }
    private void requestService() {
        mNickname = etNickname.getText().toString().trim();
        String validateURL = UrlUtil.GasmodifyInfo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("nickname", mNickname);
        SendRequestUtil.postDataFromService(validateURL,textParams,nickHandler);
    }
    private boolean matchNicknameMsg() {
        if(TextUtils.isEmpty(mNickname)){
            new PromptDialog.Builder(this)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("请输入有效值")
                    .setButton1("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
            return false;
        }
        return true;
    }
}
