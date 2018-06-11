package com.msht.minshengbao.FunctionActivity.Invoice;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.InvoiceAdapter;
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

public class InvoiceHistory extends BaseActivity {
    private TextView tv_naviga;
    private XListView mListView;
    private View Rnodata;
    private ImageView backimg;
    private String     userId,password;
    private InvoiceAdapter mAdapter;
    private final int SUCCESS   = 1;
    private final int FAILURE   = 0;
    private JSONArray jsonArray;   //数据解析
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler = new Handler() {
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
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    invoiceList .clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(InvoiceHistory.this,msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
    private void faifure(String error) {
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
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getString("status");
                String waybill_num= jsonObject.getString("waybill_num");
                String name= jsonObject.getString("name");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("status", status);
                map.put("name",name);
                map.put("waybill_num", waybill_num);
                map.put("amount",amount);
                map.put("time",time);
                invoiceList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (invoiceList.size()==0){
            Rnodata.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            Rnodata.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        setCommonHeader("发票历史");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        intView();
        mListView.setPullLoadEnable(true);
        mAdapter = new InvoiceAdapter(this,invoiceList);
        mListView.setAdapter(mAdapter);
        initData();
    }
    private void intView() {
        mListView=(XListView)findViewById(R.id.id_view_invoice);
        Rnodata=findViewById(R.id.id_nodata_view);

    }
    private void initData() {
        customDialog.show();
        loadData(1);
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
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.Insurance_history_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
}
