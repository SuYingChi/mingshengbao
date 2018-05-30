package com.msht.minshengbao.FunctionActivity.GasService;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.PayRecordAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GasPayRecord extends BaseActivity {
    private String    userId;
    private String    password;
    private String    customerNo;
    private String    address;
    private String    urlType;
    private String    size="16";
    private String    validateURL = UrlUtil.PayRecors_HistoryUrl;
    private XListView mListView;
    private TextView tv_nodata;
    private int refreshType;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;//数据解析
    private PayRecordAdapter adapter;
    private int pageNo=1;
    private int pageIndex=0;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    Handler payrecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if (jsonArray.length()>0){
                                if (pageNo==1){
                                    recordList.clear();
                                }
                                initShow();
                            }
                        }else {
                            mListView.stopLoadMore();
                            mListView.stopRefresh(false);
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void failure(String error) {
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

    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String customerNo = jsonObject.getString("customerNo");
                String amount = jsonObject.getString("amount");
                String address = jsonObject.getString("address");
                String state = jsonObject.getString("state");
                String pay_method=jsonObject.getString("pay_method");
                String pay_time=jsonObject.getString("pay_time");
                String writecard_state="0";
                if (jsonObject.has("writecard_state")){
                    writecard_state=jsonObject.getString("writecard_state");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("customerNo", customerNo);
                map.put("amount", amount);
                map.put("address", address);
                map.put("state", state);
                map.put("pay_method",pay_method);
                map.put("pay_time",pay_time);
                map.put("writecard_state",writecard_state);
                recordList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (recordList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
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
        if (urlType.equals("1")){
            setCommonHeader("充值记录");

        }else {
            setCommonHeader("缴费记录");
        }
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initHeader();
        inittView();
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
        if (urlType.equals("0")){
            validateURL = UrlUtil.PayRecors_HistoryUrl;
        }else if (urlType.equals("1")){
            validateURL = UrlUtil.IcRechargeHistory_Url;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        textParams.put("page",pageNum);
        textParams.put("size",size);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                payrecordHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                payrecordHandler.sendMessage(msg);
            }
        });
    }
    private void inittView() {
        tv_nodata=(TextView)findViewById(R.id.id_tv_nodata);
        tv_nodata.setText("当前没有交费记录");
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
}
