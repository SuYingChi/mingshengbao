package com.msht.minshengbao.functionActivity.invoiceModule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.ContentUtil;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/11  
 */
public class InvoiceRepairDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvStatusDes;
    private TextView tvTime;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvRecipient;
    private TextView tvName;
    private TextView tvTaxpayer;
    private TextView tvContent;
    private TextView tvAmount;
    private TextView tvBank;
    private TextView tvAccount;
    private TextView tvEnterpriseTel;
    private TextView tvEnterpriseAddress;
    private ImageView imageView;
    private View     layoutEnterprise;
    private View     enterpriseConnection;
    private View     layoutLicense;
    private View     layoutTaxpayer;
    private View     layoutDelivery;
    private String   userId,password;
    private String   invoiceId;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceRepairDetailActivity> mWeakReference;
        public RequestHandler(InvoiceRepairDetailActivity activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceRepairDetailActivity activity=mWeakReference.get();
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

        String companyAddress=jsonObject.optString("companyAddress");
        String phone=jsonObject.optString("phone");
        String businessLicenseImg=jsonObject.optString("businessLicenseImg");
        invoiceId=jsonObject.optString("invoiceId");
        int status=jsonObject.optInt("status");
        int type=jsonObject.optInt("type");
        String companyTel=jsonObject.optString("companyTel");
        String recipient=jsonObject.optString("recipient");
        String bankcard=jsonObject.optString("bankcard");
        String amount=jsonObject.optString("amount")+"元";
        String time=jsonObject.optString("time");
        String title=jsonObject.optString("title");
        String taxpayerNum=jsonObject.optString("taxpayerNum");
        String address=jsonObject.optString("address");
        String name=jsonObject.optString("name");
        String bank=jsonObject.optString("bank");
        onSetVisibleView(type,bank);
        onSetStatus(status);
        tvTime.setText(time);
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvRecipient.setText(recipient);
        tvName.setText(name);
        tvAmount.setText(amount);
        tvContent.setText(title);
        tvBank.setText(bank);
        tvTaxpayer.setText(taxpayerNum);
        tvAccount.setText(bankcard);
        tvEnterpriseTel.setText(companyTel);
        tvEnterpriseAddress.setText(companyAddress);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.icon_error);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        Glide.with(context)
                .load(businessLicenseImg)
                .apply(requestOptions).into(imageView);

    }

    private void onSetStatus(int status) {
        switch (status){
            case 1:
                layoutDelivery.setVisibility(View.GONE);
                tvStatusDes.setText("纸质发票待寄出");
                break;
            case 2:
                layoutDelivery.setVisibility(View.VISIBLE);
                tvStatusDes.setText("纸质发票已寄出");
                break;
            case 7:
                layoutDelivery.setVisibility(View.GONE);
                tvStatusDes.setText("纸质发票待自提");
                break;
            case 8:
                layoutDelivery.setVisibility(View.GONE);
                tvStatusDes.setText("纸质发票已自提");
                break;
            default:
                layoutDelivery.setVisibility(View.GONE);
                tvStatusDes.setText("纸质发票");
                break;
        }
    }
    private void onSetVisibleView(int type, String bank) {
        switch (type){
            case 1:
                layoutEnterprise.setVisibility(View.GONE);
                layoutLicense.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.GONE);
                break;
            case 2:
                layoutEnterprise.setVisibility(View.VISIBLE);
                layoutLicense.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                enterpriseConnection.setVisibility(View.VISIBLE);
                break;
            case 3:
                layoutLicense.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                enterpriseConnection.setVisibility(View.GONE);
                Log.d("VisibleView=",bank);
                if (!TextUtils.isEmpty(bank)&&!bank.equals(ConstantUtil.NULL_VALUE)){
                    layoutEnterprise.setVisibility(View.VISIBLE);
                }else {
                    layoutEnterprise.setVisibility(View.GONE);
                }
                break;
            default:
                layoutEnterprise.setVisibility(View.GONE);
                layoutLicense.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_repair_detail);
        context=this;
        mPageName="维修发票详情";
        setCommonHeader(mPageName);
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        invoiceId=getIntent().getStringExtra("invoiceId");
        initFindViewId();
        initData();

    }

    private void initData() {
        String validateURL = UrlUtil.INVOICE_REPAIR_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("invoiceId",invoiceId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    private void initFindViewId() {
        imageView=(ImageView) findViewById(R.id.id_business_license);
        layoutEnterprise=findViewById(R.id.id_enterprise_layout);
        tvStatusDes=(TextView)findViewById(R.id.id_invoice_status);
        tvTime=(TextView)findViewById(R.id.id_date_time);
        tvPhone=(TextView)findViewById(R.id.id_tv_phone);
        tvAddress=(TextView)findViewById(R.id.id_tv_address) ;
        tvRecipient=(TextView)findViewById(R.id.id_tv_recipient) ;
        tvName=(TextView)findViewById(R.id.id_tv_invoiceName);
        tvContent=(TextView)findViewById(R.id.id_tv_content);
        tvTaxpayer=(TextView)findViewById(R.id.id_taxpayerNo) ;
        tvAmount=(TextView)findViewById(R.id.id_invoice_amount) ;
        tvBank=(TextView)findViewById(R.id.id_open_bank);
        tvAccount=(TextView)findViewById(R.id.id_open_account);
        tvBank=(TextView)findViewById(R.id.id_open_bank);
        tvEnterpriseTel=(TextView)findViewById(R.id.id_enterprise_tel);
        tvEnterpriseAddress=(TextView)findViewById(R.id.id_enterprise_address);
        layoutEnterprise=findViewById(R.id.id_enterprise_layout);
        enterpriseConnection=findViewById(R.id.id_enterprise_connection);
        layoutLicense=findViewById(R.id.id_license_layout);
        layoutTaxpayer=findViewById(R.id.id_taxpayer_layout);
        layoutDelivery=findViewById(R.id.id_delivery_layout);
        layoutDelivery.setOnClickListener(this);
        findViewById(R.id.id_order_layout).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_order_layout:
                onStartOrder();
                break;
            case R.id.id_delivery_layout:
                onStartExpress();
                break;
            default:
                break;
        }
    }
    private void onStartOrder() {
        Intent intent=new Intent(context,InvoiceRepairOrderActivity.class);
        intent.putExtra("invoiceId",invoiceId);
        startActivity(intent);
    }
    private void onStartExpress() {
        Intent intent=new Intent(context,InvoiceLogisticsInformationActivity.class);
        intent.putExtra("invoiceId",invoiceId);
        startActivity(intent);
    }
}
