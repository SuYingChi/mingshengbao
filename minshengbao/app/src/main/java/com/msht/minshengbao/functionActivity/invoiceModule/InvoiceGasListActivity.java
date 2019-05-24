package com.msht.minshengbao.functionActivity.invoiceModule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.InvoiceGasAdapter;

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
 * @date 2018/9/6 
 */
public class InvoiceGasListActivity extends BaseActivity {
    private int refreshType=0;
    private View layoutNoData;
    private XRecyclerView mRecyclerView;
    private InvoiceGasAdapter mInvoiceGasAdapter;
    private String  userId;
    private String  password;
    private String  customerNo;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceGasListActivity> mWeakReference;
        public RequestHandler(InvoiceGasListActivity reference) {
            mWeakReference=new WeakReference<InvoiceGasListActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceGasListActivity reference=mWeakReference.get();
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
                                reference.mInvoiceGasAdapter.notifyDataSetChanged();
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
                String fpId = jsonObject.getString("fpId");
                String customerNo = jsonObject.getString("customerNo");
                String rechargeGas = jsonObject.getString("rechargeGas");
                String amount = jsonObject.getString("amount");
                String paymentNo = jsonObject.getString("paymentNo");
                String paymentDate = jsonObject.getString("paymentDate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("fpId", fpId);
                map.put("customerNo", customerNo);
                map.put("rechargeGas",rechargeGas);
                map.put("amount",amount);
                map.put("paymentNo",paymentNo);
                map.put("paymentDate",paymentDate);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList!=null&&mList.size()!=0){
            layoutNoData.setVisibility(View.GONE);
            mInvoiceGasAdapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_gas_list);
        mPageName="燃气缴费发票订单";
        context=this;
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        customerNo=getIntent().getStringExtra("customerNo");
        setCommonHeader(mPageName);
        layoutNoData=findViewById(R.id.id_nodata_view);
        initRightText();
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_invoice_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mInvoiceGasAdapter=new InvoiceGasAdapter(mList);
        mRecyclerView.setAdapter(mInvoiceGasAdapter);
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
        mInvoiceGasAdapter.setClickCallBack(new InvoiceGasAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                onStartApply(pos);
            }
        });
        mInvoiceGasAdapter.setItemRadioClickBack(new InvoiceGasAdapter.ItemRadioClickBack() {
            @Override
            public void onItemRadioBack(int pos) {
                onStartApply(pos);
            }
        });
    }

    private void initRightText() {
        TextView tvHistory =(TextView)findViewById(R.id.id_tv_rightText);
        tvHistory.setVisibility(View.VISIBLE);
        tvHistory.setText("发票历史");
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InvoiceGasHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onStartApply(int pos) {
       // String customerNo=mList.get(pos).get("customerNo");
        String paymentNo=mList.get(pos).get("paymentNo");
        String fpId=mList.get(pos).get("fpId");
        String amount=mList.get(pos).get("amount");
        Intent intent=new Intent(context,InvoiceGasApplyActivity.class);
        intent.putExtra("customerNo",customerNo);
        intent.putExtra("paymentNo",paymentNo);
        intent.putExtra("fpId",fpId);
        intent.putExtra("amount",amount);
        startActivityForResult(intent,ConstantUtil.REQUEST_CODE_ONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ConstantUtil.REQUEST_CODE_ONE:
                refreshType=0;
                initData();
                break;
            default:
                mInvoiceGasAdapter.notifyDataSetChanged();
                break;
        }
    }
    private void initData() {
        String validateURL = UrlUtil.INVOICE_GAS_NOT_OPEN_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>(3);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
