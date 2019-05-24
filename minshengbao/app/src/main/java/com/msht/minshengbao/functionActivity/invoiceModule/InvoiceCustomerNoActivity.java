package com.msht.minshengbao.functionActivity.invoiceModule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.CustomerNoAdapter;

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
 * @date 2018/9/4 
 */
public class InvoiceCustomerNoActivity extends BaseActivity {
    private static final String PAGE_NAME="燃气缴费用户";
    private int refreshType=0;
    private View  layoutNoData;
    private XRecyclerView mRecyclerView;
    private CustomerNoAdapter mCustomerNoAdapter;
    private String    userId;
    private String    password;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler payRecordHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceCustomerNoActivity> mWeakReference;
        public RequestHandler(InvoiceCustomerNoActivity reference) {
            mWeakReference=new WeakReference<InvoiceCustomerNoActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceCustomerNoActivity reference=mWeakReference.get();
            if (reference==null||reference.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            if (reference.refreshType==0){
                                reference.mRecyclerView.refreshComplete();
                                reference.mList.clear();
                                reference.mCustomerNoAdapter.notifyDataSetChanged();
                            }else if (reference.refreshType==1){
                                reference.mRecyclerView.loadMoreComplete();
                            }
                            if (jsonArray.length()>0){
                                reference.onReceiveRecordData(jsonArray);
                            }
                        }else {
                            reference.mRecyclerView.refreshComplete();
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mRecyclerView.refreshComplete();
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
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
    private void onReceiveRecordData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String address = jsonObject.getString("address");
                String customerNo = jsonObject.getString("customerNo");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", address);
                map.put("customerNo", customerNo);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList!=null&&mList.size()!=0){
            layoutNoData.setVisibility(View.GONE);
            mCustomerNoAdapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_customer_no);
        context=this;
        mPageName=PAGE_NAME;
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        setCommonHeader(PAGE_NAME);
        layoutNoData=findViewById(R.id.id_nodata_view);
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_customer_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mCustomerNoAdapter = new CustomerNoAdapter(mList);
        mRecyclerView.setAdapter(mCustomerNoAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        initData();
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                initData();
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                initData();
            }
        });
        mCustomerNoAdapter.setClickCallBack(new CustomerNoAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                String customerNo=mList.get(pos).get("customerNo");
                Intent intent=new Intent(context,InvoiceGasListActivity.class);
                intent.putExtra("customerNo",customerNo);
                startActivity(intent);
            }
        });
    }
    private void initData() {
        String validateURL = UrlUtil.INVOICE_GAS_CUSTOMER_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,payRecordHandler);
    }
}
