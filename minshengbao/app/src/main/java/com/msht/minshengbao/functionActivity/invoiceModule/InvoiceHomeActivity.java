package com.msht.minshengbao.functionActivity.invoiceModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/4 
 */
public class InvoiceHomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String PAGE_NAME="发票申请";
    private View   layoutGasInvoice;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceHomeActivity> mWeakReference;
        private RequestHandler(InvoiceHomeActivity reference) {
            mWeakReference = new WeakReference<InvoiceHomeActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceHomeActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            reference.onReceiveData(jsonObject);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                   // ToastUtil.ToastText(reference.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveData(JSONObject jsonObject) {
        int type=jsonObject.optInt("type");
        if (type==1){
            layoutGasInvoice.setVisibility(View.VISIBLE);
        }else {
           layoutGasInvoice.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_home);
        context=this;
        mPageName=PAGE_NAME;
        setCommonHeader(PAGE_NAME);
        customDialog=new CustomDialog(this, "正在加载");
        findViewById(R.id.repair_invoice_layout).setOnClickListener(this);
        layoutGasInvoice=findViewById(R.id.gas_invoice_layout);
        layoutGasInvoice.setOnClickListener(this);
        onGetControlTypeData();
    }
    private void onGetControlTypeData() {
        customDialog.show();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(UrlUtil.INVOICE_CONTROL_URL,OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gas_invoice_layout:
                onStartGasInvoice();
                break;
            case R.id.repair_invoice_layout:
                onStartRepairInvoice();
                break;
                default:
                    break;
        }
    }
    private void onStartGasInvoice() {
        Intent intent=new Intent(context,InvoiceCustomerNoActivity.class);
        startActivity(intent);
    }
    private void onStartRepairInvoice() {
        Intent intent=new Intent(context,InvoiceOpenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
