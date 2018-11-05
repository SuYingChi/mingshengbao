package com.msht.minshengbao.functionActivity.fragment;


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

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.GetAddressAdapter;
import com.msht.minshengbao.functionActivity.GasService.GasPayRecordActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
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
 * A simple {@link Fragment} subclass.
 */
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/2  
 */
public class PayRecord extends Fragment implements XListView.IXListViewListener {
    private String    userId;
    private String    password;
    private XListView mListView;
    private int pos=-1;
    private TextView tvNoData;
    private int refreshType;
    private JSONArray jsonArray;
    private GetAddressAdapter adapter;
    private int pageNo=1;
    private Context mContext;
    private final String mPageName ="燃气缴费";
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    private final PayRecordHandler payRecordHandler =new PayRecordHandler(this);
    public PayRecord() {}
    private static class PayRecordHandler extends Handler{

        private WeakReference<PayRecord> mWeakReference;
        public PayRecordHandler(PayRecord reference) {
            mWeakReference=new WeakReference<PayRecord>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final PayRecord reference=mWeakReference.get();
            if (reference==null||reference.isDetached()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.refreshType==0){
                                reference.mListView.stopRefresh(true);
                            }else if (reference.refreshType==1){
                                reference.mListView.stopLoadMore();
                            }
                            if (reference.jsonArray.length()>0){
                                if (reference.pageNo==1){
                                    reference.recordList.clear();
                                }
                                reference.onReceiveRecordData();
                            }
                        }else {
                            reference.mListView.stopRefresh(false);
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mListView.stopRefresh(false);
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(mContext)
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
    private void onReceiveRecordData() {
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
        if (recordList!=null&&recordList.size()!=0){
            adapter.notifyDataSetChanged();
        }else {
            tvNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_table_record, container, false);
        mContext=getActivity();
        Bundle bundle=getArguments();
        userId=bundle.getString("id");
        password=bundle.getString("password");
        tvNoData =(TextView)view.findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有客户号");
        mListView=(XListView) view.findViewById(R.id.id_payrecord_listview);
        mListView.setPullLoadEnable(false);
        adapter = new GetAddressAdapter(mContext,recordList,pos);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    pos=position-1;
                    adapter.notifyDataSetChanged();
                    String customerNo=recordList.get(pos).get("customerNo");
                    String address=recordList.get(pos).get("name");
                    Intent name=new Intent(mContext, GasPayRecordActivity.class);
                    name.putExtra("customerNo",customerNo);
                    name.putExtra("address",address);
                    name.putExtra("urlType","0");
                    startActivity(name);
                }
            }
        });
        adapter.setRadioButtonClickListener(new GetAddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(position).get("customerNo");
                String address=recordList.get(position).get("name");
                Intent name=new Intent(mContext, GasPayRecordActivity.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","0");
                startActivity(name);
            }
        });
        requestData();
        return view;
    }
    private void requestData() {
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
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,payRecordHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        //友盟统计页面
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
