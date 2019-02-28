package com.msht.minshengbao.functionActivity.Invoice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceGasDetailActivity extends BaseActivity {
    private TextView tvStatusDes;
    private TextView tvTime;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvTaxpayer;
    private TextView tvContent;
    private TextView tvAmount;
    private TextView tvAddress;
    private TextView tvEnterpriseAddress;
    private View     layoutEnterprise;
    private View     layoutTaxpayer;
    private View     layoutLook;
    private View     layoutAddress;
    private String   userId,password;
    private String   invoiceId;
    private String   invoiceUrl;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceGasDetailActivity> mWeakReference;
        public RequestHandler(InvoiceGasDetailActivity activity) {
            mWeakReference=new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceGasDetailActivity activity=mWeakReference.get();
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
    private void onReceiveDetailData(JSONObject jsonObject) {
        String amount=jsonObject.optString("amount")+"元";
        String title=jsonObject.optString("title");
        String name=jsonObject.optString("name");
        String taxpayerNum=jsonObject.optString("taxpayerNum");
        invoiceUrl=jsonObject.optString("invoiceUrl");
        invoiceUrl=invoiceUrl.replace("","");
        String time=jsonObject.optString("time");
        String email=jsonObject.optString("email");
        int    status=jsonObject.optInt("status");
        String companyAddress=jsonObject.optString("companyAddress");
        String companyTel=jsonObject.optString("companyTel");
        String bankcard=jsonObject.optString("bankcard");
        String bank=jsonObject.optString("bank");
        int type=jsonObject.optInt("type");
        String statusDes="";
        switch (status){
            case 3:
                statusDes="电子发票待开票";
                layoutLook.setVisibility(View.GONE);
                break;
            case 4:
                statusDes="电子发票开票中";
                layoutLook.setVisibility(View.GONE);
                break;
            case 5:
                statusDes="电子发票开票失败";
                layoutLook.setVisibility(View.GONE);
                break;
            case 6:
                statusDes="电子发票开票成功";
                layoutLook.setVisibility(View.VISIBLE);
                break;
            default:
                statusDes="电子发票未知状态";
                layoutLook.setVisibility(View.GONE);
                break;
        }
        switch (type){
            case 1:
                layoutEnterprise.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
                break;
            case 2:
                layoutEnterprise.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                layoutAddress.setVisibility(View.GONE);
                break;
            case 3:
                layoutAddress.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                layoutEnterprise.setVisibility(View.VISIBLE);
                break;
                default:
                    break;

        }
        tvStatusDes.setText(statusDes);
        tvAmount.setText(amount);
        tvTime.setText(time);
        tvContent.setText(title);
        tvTaxpayer.setText(taxpayerNum);
        tvName.setText(name);
        tvEmail.setText(email);
        tvAddress.setText(companyAddress);
        tvEnterpriseAddress.setText(companyAddress);

    }
    private void onFailure(String error) {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_gas_detail);
        context=this;
        mPageName="电子发票详情";
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        invoiceId=getIntent().getStringExtra("invoiceId");
        setCommonHeader(mPageName);
        initFindViewId();
        initData();
    }
    private void initData() {
        String validateURL = UrlUtil.INVOICE_GAS_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("invoiceId",invoiceId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        layoutTaxpayer=findViewById(R.id.id_taxpayer_layout);
        layoutEnterprise=findViewById(R.id.id_enterprise_layout);
        layoutLook=findViewById(R.id.id_look_invoice);
        tvStatusDes=(TextView)findViewById(R.id.id_invoice_status);
        tvTime=(TextView)findViewById(R.id.id_date_time);
        tvEmail=(TextView)findViewById(R.id.id_tv_email);
        tvName=(TextView)findViewById(R.id.id_tv_invoiceName);
        tvContent=(TextView)findViewById(R.id.id_tv_content);
        tvTaxpayer=(TextView)findViewById(R.id.id_taxpayerNo) ;
        tvAmount=(TextView)findViewById(R.id.id_invoice_amount) ;

        tvAddress=(TextView)findViewById(R.id.id_tv_address);
        layoutAddress=findViewById(R.id.id_district_layout);
        tvEnterpriseAddress=(TextView)findViewById(R.id.id_enterprise_address);
        layoutLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AndroidPdfViewActivity.class);
                intent.putExtra("url",invoiceUrl);
                startActivity(intent);
            }
        });
    }
}
