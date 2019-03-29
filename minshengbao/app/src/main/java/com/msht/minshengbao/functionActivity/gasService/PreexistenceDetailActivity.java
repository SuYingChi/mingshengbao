package com.msht.minshengbao.functionActivity.gasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.PreexistenceAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

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
 * @date 2017/5/26 
 */
public class PreexistenceDetailActivity extends BaseActivity {
    private XListView mListView;
    private TextView  tvNoData;
    private String    mCustomerNo;
    private String    name;
    private String    userId;
    private String    password;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    private PreexistenceAdapter adapter;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<PreexistenceDetailActivity> mWeakReference;
        public RequestHandler(PreexistenceDetailActivity activity) {
            mWeakReference = new WeakReference<PreexistenceDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PreexistenceDetailActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject =object.optJSONObject("data");
                            activity.jsonArray=jsonObject.getJSONArray("historyList");
                            activity.mListView.stopRefresh(true);
                            activity.recordList.clear();
                            activity.adapter.notifyDataSetChanged();
                            activity.onReceiveData();
                        }else {
                            activity.mListView.stopRefresh(false);
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mListView.stopRefresh(false);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
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
    private void onReceiveData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String amount = jsonObject.getString("amount");
                String recordId=jsonObject.getString("recordId");
                String time = jsonObject.getString("time");
                String flag = jsonObject.getString("flag");
                String info=jsonObject.getString("info");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("amount", amount);
                map.put("recordId", recordId);
                map.put("time", time);
                map.put("flag",flag);
                map.put("info",info);
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_preexistence_detail);
        context=this;
        mPageName="燃气预存款明细";
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader(mPageName);
        Intent data=getIntent();
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        mCustomerNo =data.getStringExtra("mCustomerNo");
        name=data.getStringExtra("name");
        initView();
        initData();

    }
    private void initData() {
        loadData();
    }

    private void initView() {
        TextView tvCustomer =(TextView)findViewById(R.id.id_customerText);
        TextView tvAddress =(TextView)findViewById(R.id.id_address);
        tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvAddress.setText(name);
        tvCustomer.setText(mCustomerNo);
        mListView=(XListView)findViewById(R.id.id_preexistence_view);
        adapter = new PreexistenceAdapter(context,recordList);
        mListView.setAdapter(adapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
            @Override
            public void onLoadMore() {
                loadData();
            }
        });
    }
    private void loadData() {
        String validateURL = UrlUtil.PRE_DEPOSIT_HISTORY;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo", mCustomerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
