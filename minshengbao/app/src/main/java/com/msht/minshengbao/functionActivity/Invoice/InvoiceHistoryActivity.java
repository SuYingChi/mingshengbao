package com.msht.minshengbao.functionActivity.Invoice;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.InvoiceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
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
 * @date 2017/11/2  
 */
public class InvoiceHistory extends BaseActivity {
    private XListView  mListView;
    private View       layoutNoData;
    private String     userId,password;
    private InvoiceAdapter mAdapter;
    private JSONArray jsonArray;
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<InvoiceHistory>mWeakReference;
        public RequestHandler(InvoiceHistory activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceHistory activity=mWeakReference.get();
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
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.refreshType==0){
                                activity.mListView.stopRefresh(true);
                            }else if (activity.refreshType==1){
                                activity.mListView.stopLoadMore();
                            }
                            if(activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.invoiceList .clear();
                                }
                            }
                            activity.onReceiveHistoryData();
                        }else {
                            activity.mListView.stopRefresh(false);
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
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
        new PromptDialog.Builder(this)
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
    private void onReceiveHistoryData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getString("status");
                String waybillNum= jsonObject.getString("waybill_num");
                String name= jsonObject.getString("name");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("status", status);
                map.put("name",name);
                map.put("waybill_num", waybillNum);
                map.put("amount",amount);
                map.put("time",time);
                invoiceList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (invoiceList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        setCommonHeader("发票历史");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        intView();
        mListView.setPullLoadEnable(true);
        mAdapter = new InvoiceAdapter(this,invoiceList);
        mListView.setAdapter(mAdapter);
        initData();
    }
    private void intView() {
        mListView=(XListView)findViewById(R.id.id_view_invoice);
        layoutNoData =findViewById(R.id.id_nodata_view);
    }
    private void initData() {
        customDialog.show();
        loadData(1);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
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
        });
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.INSURANCE_HISTORY_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
