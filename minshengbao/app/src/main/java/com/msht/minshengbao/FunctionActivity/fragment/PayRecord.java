package com.msht.minshengbao.FunctionActivity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.GetaddressAdapter;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.GasService.GasPayRecord;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayRecord extends Fragment implements XListView.IXListViewListener {
    private String    userId;
    private String    password;
    private XListView mListView;
    private int pos=-1;
    private TextView tv_nodata;
    private int refreshType;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;//数据解析
    private GetaddressAdapter adapter;
    private int pageNo=1;
    private Context mContext;
    private final String mPageName ="燃气缴费";
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    public PayRecord() {}
    Handler payrecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if (jsonArray.length()>0){
                                if (pageNo==1){
                                    recordList.clear();
                                }
                                initShow();
                            }
                        }else {
                            mListView.stopRefresh(false);
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case FAILURE:
                    mListView.stopRefresh(false);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void failure(String error) {
        new PromptDialog.Builder(getActivity())
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
                String address = jsonObject.getString("address");
                String customerNo = jsonObject.getString("customerNo");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", address);
                map.put("customerNo", customerNo);
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_table_record, container, false);
        mContext=getActivity();
        Bundle bundle=getArguments();
        userId=bundle.getString("id");             //获取从Activity传来的值
        password=bundle.getString("password");
        tv_nodata=(TextView)view.findViewById(R.id.id_tv_nodata);
        tv_nodata.setText("当前没有客户号");
        mListView=(XListView) view.findViewById(R.id.id_payrecord_listview);
        mListView.setPullLoadEnable(false);
        adapter = new GetaddressAdapter(mContext,recordList,pos);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position-1;
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(pos).get("customerNo");
                String address=recordList.get(pos).get("name");
                Intent name=new Intent(mContext, GasPayRecord.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","0");
                startActivity(name);
            }
        });
        adapter.setRadioButtonClickListener(new GetaddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(position).get("customerNo");
                String address=recordList.get(position).get("name");
                Intent name=new Intent(mContext, GasPayRecord.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","0");
                startActivity(name);
            }
        });
        initdata();
        return view;
    }
    private void initdata() {
        loadData(1);
        mListView.setXListViewListener(this);
    }
    @Override
    public void onRefresh() {
        refreshType=0;
        loadData(1);
    }
    @Override
    public void onLoadMore() {
        refreshType=1;
        loadData(1);
    }
    private void loadData(int i) {
        pageNo=i;
        String validateURL = UrlUtil.PayCustomerNo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
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
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);   //友盟统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
