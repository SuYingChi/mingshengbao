package com.msht.minshengbao.functionActivity.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.adapter.CustomerNoAdapter;
import com.msht.minshengbao.functionActivity.invoiceModule.InvoiceGasListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class GasInvoiceFragment extends Fragment {

    private int   refreshType=0;
    private View  layoutNoData;
    private XRecyclerView     mRecyclerView;
    private CustomerNoAdapter mCustomerNoAdapter;
    private String   userId;
    private String   password;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private Context  context;
    public GasInvoiceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invoice, container, false);
        context=getActivity();
        userId = SharedPreferencesUtil.getUserId(MyApplication.getMsbApplicationContext(), SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(MyApplication.getMsbApplicationContext(), SharedPreferencesUtil.Password, "");
        initView(view);
        return view;
    }
    private void initView(View view) {
        layoutNoData=view.findViewById(R.id.id_nodata_view);
        mRecyclerView=(XRecyclerView)view.findViewById(R.id.id_customer_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
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
        OkHttpRequestManager.getInstance(MyApplication.getMsbApplicationContext()).postRequestAsync(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                CustomToast.showErrorLong(data.toString());
            }
        });
    }
    private void onAnalysisData(String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONArray jsonArray =object.optJSONArray("data");
                onReceiveRecordData(jsonArray);
            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();}
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
        mCustomerNoAdapter.notifyDataSetChanged();
        if (mList!=null&&mList.size()!=0){
            layoutNoData.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance(MyApplication.getMsbApplicationContext()).requestCancel(this);
    }
}
