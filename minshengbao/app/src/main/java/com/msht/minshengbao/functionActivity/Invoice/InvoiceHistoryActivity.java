package com.msht.minshengbao.functionActivity.Invoice;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;

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
public class InvoiceHistoryActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private InvoiceHistoryAdapter mAdapter;
    private View       layoutNoData;
    private String     userId,password;
    private JSONArray jsonArray;
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<InvoiceHistoryActivity>mWeakReference;
        public RequestHandler(InvoiceHistoryActivity activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceHistoryActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            if (activity.refreshType==0){
                activity.mRecyclerView.refreshComplete();
            }else if (activity.refreshType==1){
                activity.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if(activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.invoiceList .clear();
                                }
                            }
                            activity.onReceiveHistoryData();
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
                String invoiceType=jsonObject.optString("invoiceType");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                String invoiceId=jsonObject.optString("invoiceId");
                String statusDes="";
                String invoiceTypeName="纸质发票";
                switch (status){
                    case ConstantUtil.VALUE_ONE:
                        statusDes="待寄出";
                        break;
                    case ConstantUtil.VALUE_TWO:
                        statusDes="已寄出";
                        break;
                    default:
                        statusDes="未知状态";
                        break;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("statusDes",statusDes);
                map.put("name",name);
                map.put("content",name);
                map.put("waybill_num", waybillNum);
                map.put("amount",amount);
                map.put("time",time);
                map.put("invoiceId",invoiceId);
                map.put("invoiceType",invoiceType);
                map.put("invoiceTypeName",invoiceTypeName);
                invoiceList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (invoiceList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_gas_history);
        context=this;
        mPageName="发票历史";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        intView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new InvoiceHistoryAdapter(invoiceList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        initData();
        mAdapter.setClickCallBack(new InvoiceHistoryAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                String invoiceId=invoiceList.get(pos).get("invoiceId");
                Intent intent=new Intent(context,InvoiceRepairDetailActivity.class);
                intent.putExtra("invoiceId",invoiceId);
                startActivity(intent);
            }
        });
    }
    private void intView() {
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_view_invoice);
        layoutNoData =findViewById(R.id.id_nodata_view);
    }
    private void initData() {
        customDialog.show();
        loadData(1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
