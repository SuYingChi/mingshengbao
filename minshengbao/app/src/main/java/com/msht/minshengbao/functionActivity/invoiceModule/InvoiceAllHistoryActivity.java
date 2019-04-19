package com.msht.minshengbao.functionActivity.invoiceModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/7/2  
 */
public class InvoiceAllHistoryActivity extends BaseActivity {

    private XRecyclerView mRecyclerView;
    private InvoiceHistoryAdapter mAdapter;
    private View layoutNoData;
    private String  userId,password;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_all_history);
        context=this;
        setCommonHeader("发票历史");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_view_invoice);
        layoutNoData =findViewById(R.id.id_noData_view);
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
                String invoiceType=invoiceList.get(pos).get("invoiceType");
                if (invoiceType.equals(ConstantUtil.VALUE_ZERO)){
                    String invoiceId=invoiceList.get(pos).get("invoiceId");
                    Intent intent=new Intent(context,InvoiceRepairDetailActivity.class);
                    intent.putExtra("invoiceId",invoiceId);
                    startActivity(intent);
                }else {
                    String invoiceId=invoiceList.get(pos).get("invoiceId");
                    Intent intent=new Intent(context,InvoiceGasDetailActivity.class);
                    intent.putExtra("invoiceId",invoiceId);
                    startActivity(intent);
                }

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
        String validateURL = UrlUtil.INVOICE_ALL_HISTORY;
        HashMap<String, String> textParams = new HashMap<String, String>(4);
        String pageNum=String.valueOf(pageIndex);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        textParams.put("size",String.valueOf(ConstantUtil.VALUE16));
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                Log.d("RequestSuccess=",data.toString());
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }

    private void onAnalysisData(String s) {

        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            JSONObject jsonObject =object.optJSONObject("data");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                int total=jsonObject.optInt("total");
                JSONArray jsonArray=jsonObject.optJSONArray("list");
                if (pageIndex==1){
                    invoiceList .clear();
                }
                if (pageIndex*ConstantUtil.VALUE16>=total){
                    mRecyclerView.setLoadingMoreEnabled(false);
                }else {
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
                onReceiveHistoryData(jsonArray);
            }else {
                CustomToast.showErrorLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onReceiveHistoryData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
               // String status = jsonObject.getString("status");
                String content=jsonObject.optString("content");
               // String waybillNum= jsonObject.getString("waybill_num");
               // String name= jsonObject.getString("name");
                String invoiceType=jsonObject.optString("invoiceType");
                String title=jsonObject.optString("title");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                String invoiceId=jsonObject.optString("invoiceId");
                String statusDesc=jsonObject.optString("statusDesc");
                String invoiceTypeName="纸质发票";
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("statusDesc",statusDesc);
               // map.put("name",content);
                map.put("content",content);
                //map.put("waybill_num", waybillNum);
                map.put("amount",amount);
                map.put("time",time);
                map.put("invoiceId",invoiceId);
                map.put("invoiceType",invoiceType);
                map.put("title",title);
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
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
