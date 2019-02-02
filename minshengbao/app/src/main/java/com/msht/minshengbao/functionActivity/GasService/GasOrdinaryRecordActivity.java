package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.GetAddressAdapter;
import com.msht.minshengbao.functionActivity.fragment.PayRecord;

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
public class GasOrdinaryRecordActivity extends BaseActivity {
    private String    userId;
    private String    password;
    private XRecyclerView mRecyclerView;
    private int pos=-1;
    private int refreshType;
    private GetAddressAdapter adapter;
    private View     noDataLayout;
    private int pageIndex=0;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();

    private final PayRecordHandler payRecordHandler =new PayRecordHandler(this);
    private static class PayRecordHandler extends Handler {

        private WeakReference<GasOrdinaryRecordActivity> mWeakReference;
        public PayRecordHandler(GasOrdinaryRecordActivity reference) {
            mWeakReference=new WeakReference<GasOrdinaryRecordActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasOrdinaryRecordActivity reference=mWeakReference.get();
            if (reference==null||reference.isFinishing()){
                return;
            }
            if (reference.refreshType==0){
                reference.mRecyclerView.refreshComplete();
            }else if (reference.refreshType==1){
                reference.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            reference.onReceiveRecordData(jsonArray);
                        }else {
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case SendRequestUtil.FAILURE:
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
        if (jsonArray!=null&&jsonArray.length()>0){
            if (pageIndex==1){
                recordList.clear();
            }
            try {
                for (int i = 0; i <jsonArray.length(); i++) {
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
                noDataLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }else {
                noDataLayout.setVisibility(View.VISIBLE);
            }
        }else {
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_ordinary_record);
        context=this;
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        setCommonHeader("缴费记录");
        TextView tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有客户号");
        noDataLayout=findViewById(R.id.id_no_data_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView )findViewById(R.id.id_pay_record_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        adapter=new GetAddressAdapter(context,recordList,pos);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        adapter.setClickCallBack(new GetAddressAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                pos=position;
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(pos).get("customerNo");
                String address=recordList.get(pos).get("name");
                Intent name=new Intent(context, GasPayRecordActivity.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","0");
                startActivity(name);
            }
        });
        adapter.setRadioButtonClickListener(new GetAddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(position).get("customerNo");
                String address=recordList.get(position).get("name");
                Intent name=new Intent(context, GasPayRecordActivity.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","0");
                startActivity(name);
            }
        });
        requestData();
    }
    private void requestData() {
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
        pageIndex=i;
        String validateURL = UrlUtil.PayCustomerNo_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,payRecordHandler);
    }
}
