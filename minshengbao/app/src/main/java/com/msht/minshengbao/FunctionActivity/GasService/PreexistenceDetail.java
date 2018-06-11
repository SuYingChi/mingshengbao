package com.msht.minshengbao.FunctionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.PreexistenceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreexistenceDetail extends BaseActivity {
    private XListView mListView;
    private TextView  tv_customer;
    private TextView  Address;
    private TextView  tv_nodata;
    private String    CustomerNo;
    private String    name;
    private String    userId;
    private String    password;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;//数据解析
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    private PreexistenceAdapter adapter;
    Handler payrecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONObject jsonObject =object.optJSONObject("data");
                            jsonArray=jsonObject.getJSONArray("historyList");
                            mListView.stopRefresh(true);
                            recordList.clear();
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void failure(String error) {
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

    private void initShow() {
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
            tv_nodata.setVisibility(View.VISIBLE);
        }else {
            tv_nodata.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preexistence_detail);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("燃气预存款明细");
        Intent getdata=getIntent();
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        CustomerNo=getdata.getStringExtra("CustomerNo");
        name=getdata.getStringExtra("name");
        initView();
        initdata();

    }

    private void initdata() {
        loadData();
    }

    private void initView() {
        tv_customer=(TextView)findViewById(R.id.id_customerText);
        Address=(TextView)findViewById(R.id.id_address);
        tv_nodata=(TextView)findViewById(R.id.id_tv_nodata);
        Address.setText(name);
        tv_customer.setText(CustomerNo);
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
        String validateURL = UrlUtil.Pre_deposit_history;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",CustomerNo);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                payrecordHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                payrecordHandler.sendMessage(msg);
            }
        });
    }
}
