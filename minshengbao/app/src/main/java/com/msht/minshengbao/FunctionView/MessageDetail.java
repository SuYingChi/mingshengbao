package com.msht.minshengbao.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageDetail extends BaseActivity {
    private TextView tv_message,tv_time;
    private String userId, password,id,type;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject json=object.getJSONObject("data");
                        if(results.equals("success")) {
                            initView(json);
                        }else {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initView(JSONObject json) {
        String types=json.optString("type");
        String title=json.optString("title");
        String content=json.optString("content");
        String time=json.optString("time");
        tv_message.setText(content);
        tv_time.setText(time);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        id=data.getStringExtra("id");
        type=data.getStringExtra("type");
        if (type.equals("1")){
            setCommonHeader("订单消息");
        }else if (type.equals("2")){
            setCommonHeader("公告");
        }else {
            setCommonHeader("消息详情");
        }
        mPageName="消息详情";
        tv_message=(TextView)findViewById(R.id.id_message_detail);
        tv_time=(TextView)findViewById(R.id.id_time) ;
        initData();
    }
    private void initData() {
        customDialog.show();
        String messageUrl= UrlUtil.Inform_detail;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        HttpUrlconnectionUtil.executepost(messageUrl,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                messageHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                messageHandler.sendMessage(msg);
            }
        });
    }
}
