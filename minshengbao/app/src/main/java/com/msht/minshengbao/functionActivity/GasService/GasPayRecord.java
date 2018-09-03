package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.PayRecordAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
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
public class GasPayRecord extends BaseActivity {
    private String    userId;
    private String    password;
    private String    customerNo;
    private String    address;
    private String    urlType;
    private String    validateURL = UrlUtil.PayRecors_HistoryUrl;
    private XListView mListView;
    private TextView  tvNoData;
    private int refreshType;
    private JSONArray jsonArray;
    private PayRecordAdapter adapter;
    private int pageNo=1;
    private int pageIndex=0;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    private final PayRecordHandler payRecordHandler=new PayRecordHandler(this);
    private static class PayRecordHandler  extends Handler{
        private WeakReference<GasPayRecord> mWeakReference;
        public PayRecordHandler(GasPayRecord gasPayRecord) {
            mWeakReference = new WeakReference<GasPayRecord>(gasPayRecord);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasPayRecord activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.refreshType==0){
                                activity.mListView.stopRefresh(true);
                            }else if (activity.refreshType==1){
                                activity.mListView.stopLoadMore();
                            }
                            if (activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.recordList.clear();
                                }
                                activity.onRecordData();
                            }
                        }else {
                            activity.mListView.stopLoadMore();
                            activity.mListView.stopRefresh(false);
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case SendrequestUtil.FAILURE:
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
    private void onRecordData() {
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
                if (jsonObject.has("writecard_state")){
                    writeCardState=jsonObject.getString("writecard_state");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("customerNo", customerNo);
                map.put("amount", amount);
                map.put("address", address);
                map.put("state", state);
                map.put("pay_method",payMethod);
                map.put("pay_time",payTime);
                map.put("writecard_state",writeCardState);
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            tvNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_pay_record);
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        customerNo=getIntent().getStringExtra("customerNo");
        address=getIntent().getStringExtra("address");
        urlType=getIntent().getStringExtra("urlType");
        if (urlType.equals(VariableUtil.VALUE_ONE)){
            setCommonHeader("充值记录");

        }else {
            setCommonHeader("缴费记录");
        }
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initHeader();
        initView();
        initdata();
    }
    private void initHeader() {
        ((TextView)findViewById(R.id.id_customerNo)).setText(customerNo);
        ((TextView)findViewById(R.id.id_address)).setText(address);
    }
    private void initdata() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String size="16";
        if (urlType.equals(VariableUtil.VALUE_ZERO)){
            validateURL = UrlUtil.PayRecors_HistoryUrl;
        }else if (urlType.equals(VariableUtil.VALUE_ONE)){
            validateURL = UrlUtil.IC_RECHARGE_HISTORY_URL;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        textParams.put("page",pageNum);
        textParams.put("size",size);
        OkHttpRequestManager.getInstance(context).requestAsyn(validateURL,OkHttpRequestManager.TYPE_POST_MULTIPART,textParams,payRecordHandler);
    }
    private void initView() {
        tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有交费记录");
        mListView=(XListView)findViewById(R.id.id_payrecord_listview);
        mListView.setPullLoadEnable(true);
        adapter = new PayRecordAdapter(context,recordList);
        mListView.setAdapter(adapter);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
           customDialog.dismiss();
        }
    }
}
