package com.msht.minshengbao.functionActivity.invoiceModule;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.adapter.LpgDeliveryInfoAdapter;

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
 * @date 2018/9/12  
 */
public class InvoiceLogisticsInformationActivity extends BaseActivity {
    private TextView tvLogisticsCompany;
    private TextView tvExpressOrderNo;
    private TextView tvExpressStatus;
    private ImageView mImageView;
    private ListViewForScrollView mExpressInformation;
    private LpgDeliveryInfoAdapter mAdapter;
    private String   userId,password;
    private String   invoiceId;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceLogisticsInformationActivity> mWeakReference;
        public RequestHandler(InvoiceLogisticsInformationActivity activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceLogisticsInformationActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject =object.optJSONObject("data");
                            activity.onReceiveDetailData(jsonObject);
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

    private void onReceiveDetailData(JSONObject jsonObject) {
        String expressOrderNo="快递单号："+jsonObject.optString("no");
        String expressCompany="快递公司："+jsonObject.optString("delivery_name");
        int status=jsonObject.optInt("status");
        onSetStatus(status);
        tvExpressOrderNo.setText(expressOrderNo);
        tvLogisticsCompany.setText(expressCompany);
        JSONArray deliveryList=jsonObject.optJSONArray("deliveryList");
        onDeliveryListData(deliveryList);

    }

    private void onDeliveryListData(JSONArray deliveryList) {
        try {
            for (int i = 0; i < deliveryList.length(); i++) {
                JSONObject jsonObject = deliveryList.getJSONObject(i);
                String context = jsonObject.getString("context");
                String time = jsonObject.getString("time");
                String fTime = jsonObject.getString("ftime");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("describe", context);
                map.put("time", time);
                map.put("createDate",fTime);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    private void onSetStatus(int status) {
        switch (status){
            case 0:
                tvExpressStatus.setText("快递状态：运输中");
                mImageView.setImageResource(R.drawable.logistics_transport_xh);
                break;
            case 1:
                tvExpressStatus.setText("快递状态：揽件");
                mImageView.setImageResource(R.drawable.logistics_agitate_good_xh);
                break;
            case 2:
                tvExpressStatus.setText("快递状态：疑难(快递出现问题)");
                mImageView.setImageResource(R.drawable.logistics_question_xh);
                break;
            case 3:
                tvExpressStatus.setText("快递状态：已签收");
                mImageView.setImageResource(R.drawable.logistics_signature_xh);
                break;
            case 4:
                tvExpressStatus.setText("快递状态：退签");
                mImageView.setImageResource(R.drawable.logistics_outsign_xh);
                break;
            case 5:
                tvExpressStatus.setText("快递状态：派件中");
                mImageView.setImageResource(R.drawable.logistics_send_xh);
                break;
            case 6:
                tvExpressStatus.setText("快递状态：货物退回发货人");
                mImageView.setImageResource(R.drawable.logistics_return_xh);
                break;
            default:
                tvExpressStatus.setText("未知状态");
                mImageView.setImageResource(R.drawable.logistics_transport_xh);
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_logistics_information);
        context=this;
        mPageName="发票物流信息";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        invoiceId=getIntent().getStringExtra("invoiceId");
        setCommonHeader(mPageName);
        initFindViewId();
        mAdapter=new LpgDeliveryInfoAdapter(context,mList);
        mExpressInformation.setAdapter(mAdapter);
        initData();

    }
    private void initData() {
        String validateURL = UrlUtil.INVOICE_EXPRESS_INFORMATION;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("invoiceId",invoiceId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        tvLogisticsCompany=(TextView)findViewById(R.id.id_logistics_company);
        tvExpressOrderNo=(TextView)findViewById(R.id.id_express_orderNo);
        tvExpressStatus=(TextView)findViewById(R.id.id_express_status);
        mExpressInformation=(ListViewForScrollView)findViewById(R.id.id_logistics_information);
        mImageView=(ImageView)findViewById(R.id.id_portrait_view);
    }
}
