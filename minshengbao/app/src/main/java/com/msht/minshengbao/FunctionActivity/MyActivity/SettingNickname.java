package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingNickname extends BaseActivity {
    private EditText Enickname;
    private TextView Tensure;
    private String Snickname,password,userId;
    public static final String NICK_NAME = "NICK";
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    Handler nickHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (Results.equals("success")) {
                            Judgedata(Results);
                        }else {
                            failure(Error);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(SettingNickname.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
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
                        finish();
                    }
                }).show();
    }
    private void Judgedata(String results) {
        SharedPreferences share = this.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=share.edit();
        editor.remove(SharedPreferencesUtil.NickName);
        SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.NickName,Snickname);
        Intent nickN=new Intent();
        nickN.putExtra("nickname", Snickname);
        setResult(1, nickN);
        Intent broadcast=new Intent();
        broadcast.setAction(NICK_NAME);
        broadcast.putExtra("nickname", Snickname);
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
        initfindViewByid();
        initEvent();
    }
    private void initfindViewByid() {
        Tensure=(TextView)findViewById(R.id.id_tv_rightText);
        Tensure.setVisibility(View.VISIBLE);
        Tensure.setText("确定");
        Enickname=(EditText)findViewById(R.id.id_et_nickname);
        String NICK=SharedPreferencesUtil.getNickName(this,SharedPreferencesUtil.NickName,"");
        Enickname.setText(NICK);//显示
        Enickname.setSelection(NICK.length());  //光标尾处
    }
    private void initEvent() {
        Tensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snickname= Enickname.getText().toString().trim();
                if(matchnicknameMsg()){
                    requestSevice();
                }
            }
        });
    }
    private void requestSevice() {
        Snickname = Enickname.getText().toString().trim();
        String validateURL = UrlUtil.GasmodifyInfo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("nickname", Snickname);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                nickHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                nickHandler.sendMessage(msg);
            }
        });
    }
    private boolean matchnicknameMsg() {
        if( Snickname.equals("")){
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
