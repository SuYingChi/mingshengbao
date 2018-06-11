package com.msht.minshengbao.FunctionActivity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.GetRecordAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableRecordFrag extends Fragment implements XListView.IXListViewListener {
    private String    userId;
    private String    password;
    private XListView mListView;
    private TextView tvNotHavaData;
    private int refreshType;
    private JSONArray jsonArray;
    private GetRecordAdapter adapter;
    private int pageNo=1;
    private int pageIndex=0;
    private final String mPageName ="抄表记录";
    private Context mContext;
    private ArrayList<HashMap<String, String>> writeList = new ArrayList<HashMap<String, String>>();
    private final PayRecordHandler payRecordHandler=new PayRecordHandler(this);
    public TableRecordFrag() {
        // Required empty public constructor
    }
    private static class PayRecordHandler extends Handler{
        private WeakReference<TableRecordFrag> mWeakReference;
        public PayRecordHandler(TableRecordFrag tableRecordFrag) {
            mWeakReference = new WeakReference<TableRecordFrag>(tableRecordFrag);
        }
        @Override
        public void handleMessage(Message msg) {
            final TableRecordFrag reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray =object.optJSONArray("data");
                        if(results.equals("success")) {
                            if (reference.refreshType==0){
                                reference.mListView.stopRefresh(true);
                            }else if (reference.refreshType==1){
                                reference.mListView.stopLoadMore();
                            }
                            if (reference.jsonArray.length()>0){
                                if (reference.pageNo==1){
                                    reference.writeList.clear();
                                }
                                reference.onWriteTableData();
                            }
                        }else {
                            reference.mListView.stopRefresh(false);
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    reference.mListView.stopRefresh(false);
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
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

    private void onWriteTableData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String customerNo = jsonObject.getString("customerNo");
                String meter= jsonObject.getString("meter");
                String address = jsonObject.getString("address");
                String time=jsonObject.getString("time");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("customerNo", customerNo);
                map.put("meter", meter);
                map.put("address", address);
                map.put("time",time);
                writeList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (writeList.size()==0){
            tvNotHavaData.setVisibility(View.VISIBLE);
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
        userId= SharedPreferencesUtil.getUserId(mContext, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(mContext, SharedPreferencesUtil.Password,"");
        tvNotHavaData =(TextView)view.findViewById(R.id.id_tv_nodata);
        mListView=(XListView) view.findViewById(R.id.id_payrecord_listview);
        mListView.setPullLoadEnable(true);
        adapter = new GetRecordAdapter(getActivity(),writeList);
        mListView.setAdapter(adapter);
        initdata();
        return view;
    }
    private void initdata() {
        loadData(1);
        mListView.setXListViewListener(this);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.GasTable_data;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        SendrequestUtil.postDataFromService(validateURL,textParams,payRecordHandler);
    }
    @Override
    public void onRefresh() {
        refreshType=0;
        loadData(1);
    }
    @Override
    public void onLoadMore() {
        refreshType=1;
        loadData(pageIndex + 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
