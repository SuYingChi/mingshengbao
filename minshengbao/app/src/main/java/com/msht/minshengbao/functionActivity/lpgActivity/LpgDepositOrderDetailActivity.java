package com.msht.minshengbao.functionActivity.lpgActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.adapter.LpgReturnBottleDetailAdapter;
import com.umeng.analytics.MobclickAgent;

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
 * @date 2018/7/24  
 */
public class LpgDepositOrderDetailActivity extends BaseActivity {
    private TextView tvAddress;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvDeliveryTime;
    private TextView tvTotalAmount;
    private TextView tvCreateDate;
    private TextView tvOrderNo;
    private String orderId;
    private LpgReturnBottleDetailAdapter mAdapter;
    private int requestCode=0;
    private static final int PAY_SUCCESS_CODE=0x001;
    private static final int PAY_CANCEL_CODE=0X002;
    private static final String PAGE_NAME="退瓶明细";
    private CustomDialog customDialog;

    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<LpgDepositOrderDetailActivity> mWeakReference;
        public RequestHandler(LpgDepositOrderDetailActivity activity) {
            mWeakReference = new WeakReference<LpgDepositOrderDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgDepositOrderDetailActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                JSONObject jsonObject =object.optJSONObject("data");
                                activity.onReceiveOrderData(jsonObject);
                            }else if (activity.requestCode==1){
                                activity.onCancelSuccess();
                            }
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveOrderData(JSONObject jsonObject) {

        String addressName=jsonObject.optString("addressName");
        String buyer=jsonObject.optString("buyer");
        String mobile=jsonObject.optString("mobile");
        String deliveryTime=jsonObject.optString("deliveryTime");
        String realAmount=jsonObject.optString("realAmount");
        String createDate=jsonObject.optString("createDate");
        JSONArray jsonArray=jsonObject.optJSONArray("voLists");
        String realAmountText="¥"+realAmount;
        String deliveryTimeText="预约时间："+deliveryTime;
        tvOrderNo.setText(orderId);
        tvAddress.setText(addressName);
        tvCreateDate.setText(createDate);
        tvDeliveryTime.setText(deliveryTimeText);
        tvMobile.setText(mobile);
        tvName.setText(buyer);
        tvTotalAmount.setText(realAmountText);
        onReturnBottleList(jsonArray);
    }

    private void onReturnBottleList(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String bottleWeight = json.getString("bottleWeight");
                String amount=json.getString("amount");
                String createDate=json.getString("createDate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("bottleWeight", bottleWeight);
                map.put("amount",amount);
                map.put("createDate",createDate);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onCancelSuccess() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("订单已取消")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(0x002);
                        finish();
                    }
                }).show();
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_deposit_order_detail);
        context=this;
        setCommonHeader(PAGE_NAME);
        Intent data=getIntent();
        if (data!=null){
            orderId=data.getStringExtra("orderId");
        }
        customDialog=new CustomDialog(this, "正在加载");
        initFindViewId();
        ListViewForScrollView mListView=(ListViewForScrollView)findViewById(R.id.id_return_bottle_view);
        mAdapter=new LpgReturnBottleDetailAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        initOrderData();


    }

    private void initOrderData() {
        requestCode=0;
        String orderType="0";
        customDialog.show();
        String validateURL= UrlUtil.LPG_QUERY_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        textParams.put("orderType",orderType);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        tvOrderNo=(TextView)findViewById(R.id.id_tv_orderNo);
        tvCreateDate=(TextView)findViewById(R.id.id_place_time);
        tvAddress = (TextView) findViewById(R.id.id_tv_address);
        tvName = (TextView) findViewById(R.id.id_tv_username);
        tvMobile = (TextView) findViewById(R.id.id_tv_mobile);
        findViewById(R.id.id_tv_elevator).setVisibility(View.GONE);
        tvDeliveryTime = (TextView) findViewById(R.id.id_delivery_time);
        tvTotalAmount= (TextView) findViewById(R.id.id_total_amount);
        ButtonM btnCancel=(ButtonM)findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrder();
            }
        });
    }
    private void onCancelOrder() {

        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否取消?")
                .setButton1("残忍取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestService();
                        dialog.dismiss();
                    }
                })
                .setButton2("算了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) { dialog.dismiss(); }
                }).show();
    }
    private void requestService() {
        customDialog.show();
        requestCode=1;
        String validateURL= UrlUtil.LPG_FAIL_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
