package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

import com.msht.minshengbao.adapter.LpgBottleReplaceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/13  
 */
public class ReplaceBottleListActivity extends BaseActivity {
    private LpgBottleReplaceAdapter mAdapter;
    private CustomDialog customDialog;

    private String orderId;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    private final RequestHandler requestHandler=new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<ReplaceBottleListActivity> mWeakReference;
        public RequestHandler(ReplaceBottleListActivity activity) {
            mWeakReference = new WeakReference<ReplaceBottleListActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ReplaceBottleListActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        Log.d("msg.obj=",msg.obj.toString());
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            //JSONObject jsonObject =object.optJSONObject("data");
                            activity.onReceiveBottleData();
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void onReceiveBottleData() {

    }
    private void onFailure(String error) {

        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_replace_bottle_list);
        context=this;
        setCommonHeader("钢瓶置换");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
            orderId=data.getStringExtra("orderId");
        }
        XListView mListView=(XListView)findViewById(R.id.id_bottle_information);
        mListView.setPullLoadEnable(false);
        mAdapter=new LpgBottleReplaceAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        initData();

    }
    private void initData() {

        customDialog.show();
        String validateURL= UrlUtil.LPG_REPLACE_BOTTLE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("orderId",orderId);
        OkHttpRequestManager.getInstance(context).requestAsyn(validateURL,OkHttpRequestManager.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
