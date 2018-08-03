package com.msht.minshengbao.functionActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class 消息详情
 *
 * @author hong
 * @date 2016/04/12
 */
public class MessageDetailActivity extends BaseActivity {
    private TextView tvMessage,tvTime;
    private String userId, password,id,type;
    private CustomDialog customDialog;
    private final MessageHandler messageHandler=new MessageHandler(this);
    private static class MessageHandler extends Handler{
        private WeakReference<MessageDetailActivity> mWeakReference;
        public MessageHandler(MessageDetailActivity activity) {
            mWeakReference = new WeakReference<MessageDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MessageDetailActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject json=object.getJSONObject("data");
                        if(results.equals("success")) {
                            activity.onMessageData(json);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     *
     * @param json  消息数据json
     */
    private void onMessageData(JSONObject json) {
        String types=json.optString("type");
        String title=json.optString("title");
        String content=json.optString("content");
        String time=json.optString("time");
        tvMessage.setText(content);
        tvTime.setText(time);
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
        tvMessage=(TextView)findViewById(R.id.id_message_detail);
        tvTime=(TextView)findViewById(R.id.id_time) ;
        initData();
    }
    private void initData() {
        customDialog.show();
        String messageUrl= UrlUtil.Inform_detail;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.postDataFromService(messageUrl,textParams,messageHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
