package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import com.msht.minshengbao.adapter.LpgBottleReplaceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
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
public class LpgReplaceBottleListActivity extends BaseActivity {
    private LpgBottleReplaceAdapter mAdapter;
    private CustomDialog customDialog;
    private String orderId;
    private static final String PAGE_NAME="钢瓶置换";
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgReplaceBottleListActivity> mWeakReference;
        public RequestHandler(LpgReplaceBottleListActivity activity) {
            mWeakReference = new WeakReference<LpgReplaceBottleListActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgReplaceBottleListActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            activity.onReceiveBottleData(jsonArray);
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
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
    private void onReceiveBottleData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String orderId=json.optString("orderId");
                String bottleWeight = json.getString("bottleWeight");
                String bottleCount=json.optString("bottleCount");
                String replacePrice=json.getString("replacePrice");
                String totalAmount=json.optString("totalAmount");
                String bottleYear=json.optString("bottleYear");
                String years=json.optString("years");
                String corrosionType=json.getString("corrosionType");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId",orderId);
                map.put("bottleWeight", bottleWeight);
                map.put("bottleCount",bottleCount);
                map.put("replacePrice",replacePrice);
                map.put("totalAmount",totalAmount);
                map.put("bottleYear",bottleYear);
                map.put("years",years);
                map.put("corrosionType",corrosionType);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
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
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
