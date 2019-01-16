package com.msht.minshengbao.functionActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
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
    private String userId, password,id;
    private WebView mWebView;
    private View layoutScroll;
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
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject json=object.getJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onMessageData(json);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
        String htmlText=json.optString("htmltext");
        String content=json.optString("content");
        String time=json.optString("time");
        tvMessage.setText(content);
        tvTime.setText(time);
        mWebView.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);
        if (TextUtils.isEmpty(htmlText)||htmlText.equals(ConstantUtil.NULL_VALUE)){
            mWebView.setVisibility(View.GONE);
            layoutScroll.setVisibility(View.VISIBLE);
        }else {
            mWebView.setVisibility(View.VISIBLE);
            layoutScroll.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        context=this;
        mPageName="消息详情";
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        id=data.getStringExtra("id");
        setCommonHeader("订单消息");
        initFindViewId();
        settingWeb();
        initData();
    }
    private void initFindViewId() {
        tvMessage=(TextView)findViewById(R.id.id_message_detail);
        tvTime=(TextView)findViewById(R.id.id_time);
        mWebView=(WebView)findViewById(R.id.id_RichText_view);
        layoutScroll=findViewById(R.id.id_scrollview);
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void settingWeb() {
        WebSettings settings= mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }
    private void initData() {
        customDialog.show();
        String messageUrl= UrlUtil.INFORM_DETAIL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendRequestUtil.postDataFromService(messageUrl,textParams,messageHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
