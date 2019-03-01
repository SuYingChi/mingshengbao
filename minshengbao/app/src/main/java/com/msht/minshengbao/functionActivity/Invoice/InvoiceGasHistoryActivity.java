package com.msht.minshengbao.functionActivity.Invoice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
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
 * @date 2018/7/2  
 */
public class InvoiceGasHistoryActivity extends BaseActivity {

    private XRecyclerView mRecyclerView;
    private View layoutNoData;
    private String     userId,password;
    private InvoiceHistoryAdapter mAdapter;
    private JSONArray jsonArray;
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType=0;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceGasHistoryActivity> mWeakReference;
        public RequestHandler(InvoiceGasHistoryActivity activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceGasHistoryActivity activity=mWeakReference.get();
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
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.jsonArray =object.optJSONArray("data");
                            if(activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.mList.clear();
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
                String title = jsonObject.getString("title");
                String content= jsonObject.getString("content");
                String invoiceId=jsonObject.optString("invoiceId");
                String name= jsonObject.getString("name");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                String invoiceType=jsonObject.optString("invoiceType");
                String status  =jsonObject.optString("status");
                String statusDesc="";
                String invoiceTypeName="";
                switch (status){
                    case ConstantUtil.VALUE_THREE:
                        statusDesc="待开票";
                        break;
                    case ConstantUtil.VALUE_FOUR:
                        statusDesc="开票中";
                        break;
                    case ConstantUtil.VALUE_FIVE:
                        statusDesc="开票失败";
                        break;
                    case ConstantUtil.VALUE_SIX:
                        statusDesc="开票成功";
                        break;
                    default:
                        statusDesc="未知状态";
                        break;
                }
                if (invoiceType.equals(ConstantUtil.VALUE_ONE)){
                    invoiceTypeName="电子发票";
                }else {
                    invoiceTypeName="纸质发票";
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("statusDesc", statusDesc);
                map.put("name",name);
                map.put("title", title);
                map.put("amount",amount);
                map.put("invoiceId",invoiceId);
                map.put("time",time);
                map.put("content",content);
                map.put("invoiceTypeName",invoiceTypeName);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList!=null&&mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
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
        mAdapter=new InvoiceHistoryAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        initData();
        mAdapter.setClickCallBack(new InvoiceHistoryAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                String invoiceId=mList.get(pos).get("invoiceId");
                Intent intent=new Intent(context,InvoiceGasDetailActivity.class);
                intent.putExtra("invoiceId",invoiceId);
                startActivity(intent);
            }
        });
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
        String validateURL = UrlUtil.INVOICE_GAS_HISTORY;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void intView() {
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_view_invoice);
        layoutNoData =findViewById(R.id.id_nodata_view);
    }
}
