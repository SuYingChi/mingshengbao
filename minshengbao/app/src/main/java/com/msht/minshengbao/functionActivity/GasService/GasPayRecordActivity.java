package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.PayRecordAdapter;
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
 * @date 2016/7/2  
 */
public class GasPayRecordActivity extends BaseActivity {
    private String    userId;
    private String    password;
    private String    customerNo;
    private String    address;
    private String    urlType;
    private String    validateURL = UrlUtil.PayRecors_HistoryUrl;
    private View      layoutNoData;
    private XRecyclerView mRecyclerView;
    private TextView  tvNoData;
    private int refreshType;
    private PayRecordAdapter adapter;
    private int pageIndex=0;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    private final PayRecordHandler payRecordHandler=new PayRecordHandler(this);
    private static class PayRecordHandler  extends Handler{
        private WeakReference<GasPayRecordActivity> mWeakReference;
        public PayRecordHandler(GasPayRecordActivity gasPayRecord) {
            mWeakReference = new WeakReference<GasPayRecordActivity>(gasPayRecord);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasPayRecordActivity activity=mWeakReference.get();
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
                        JSONArray jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (jsonArray.length()>0){
                                if (activity.pageIndex<=1){
                                    activity.recordList.clear();
                                }
                                if (activity.urlType.equals(ConstantUtil.VALUE_TWO)){
                                    activity.onNBRecordData(jsonArray);
                                }else{
                                    activity.onRecordData(jsonArray);
                                }
                            }
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
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

    private void onNBRecordData(JSONArray jsonArray) {
        try {
            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                String customerNo = jsonObject.optString("customerNo");
                String amount = jsonObject.optString("amount");
                String address = jsonObject.optString("address");
                String state = jsonObject.optString("meterState");
                String payMethod=jsonObject.getString("payState");
                String payTime=jsonObject.getString("payTime");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("customerNo", customerNo);
                map.put("amount", amount);
                map.put("address", address);
                map.put("state", state);
                map.put("payMethod",payMethod);
                map.put("payTime",payTime);
                map.put("writeCardState",state);
                map.put("tableType","2");
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
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
    private void onRecordData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String customerNo = jsonObject.getString("customerNo");
                String amount = jsonObject.getString("amount");
                String address = jsonObject.getString("address");
                String state = jsonObject.getString("state");
                String payMethod=jsonObject.getString("pay_method");
                String payTime=jsonObject.getString("pay_time");
                String writeCardState="0";
                String tableType;
                switch (urlType){
                    case ConstantUtil.VALUE_ZERO:
                        tableType="0";
                        break;
                    case ConstantUtil.VALUE_ONE:
                        tableType="1";
                        break;
                    case ConstantUtil.VALUE_TWO:
                        tableType="2";
                        break;
                    default:
                        tableType="0";
                        break;
                }
                if (jsonObject.has("writecard_state")){
                    writeCardState=jsonObject.getString("writecard_state");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("customerNo", customerNo);
                map.put("amount", amount);
                map.put("address", address);
                map.put("state", state);
                map.put("payMethod",payMethod);
                map.put("payTime",payTime);
                map.put("writeCardState",writeCardState);
                map.put("tableType",tableType);
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            layoutNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_pay_record);
        context=this;
        mPageName="充值缴费记录";
        customerNo=getIntent().getStringExtra("customerNo");
        address=getIntent().getStringExtra("address");
        urlType=getIntent().getStringExtra("urlType");
        if (urlType.equals(VariableUtil.VALUE_ZERO)){
            mPageName="缴费记录";
        }else {
            mPageName="充值记录";
        }
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initHeader();
        initView();
        initData();
    }
    private void initHeader() {
        ((TextView)findViewById(R.id.id_customerText)).setText(customerNo);
        ((TextView)findViewById(R.id.id_address)).setText(address);
    }
    private void initData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        if (urlType.equals(VariableUtil.VALUE_ZERO)){
            validateURL = UrlUtil.PayRecors_HistoryUrl;
        }else if (urlType.equals(VariableUtil.VALUE_ONE)){
            validateURL = UrlUtil.IC_RECHARGE_HISTORY_URL;
        }else if (urlType.equals(VariableUtil.VALUE_TWO)){
            validateURL = UrlUtil.INTERNET_TABLE_RECORD;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(i);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        OkHttpRequestUtil.getInstance(context.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,payRecordHandler);
    }
    private void initView() {
        TextView tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有记录");
        layoutNoData=findViewById(R.id.id_no_data_view);
        mRecyclerView=(XRecyclerView) findViewById(R.id.id_record_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        adapter = new PayRecordAdapter(context,recordList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
           customDialog.dismiss();
        }
    }
}
