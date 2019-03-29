package com.msht.minshengbao.functionActivity.invoiceModule;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.InvoiceEnsureDialog;
import com.msht.minshengbao.ViewUI.Dialog.PublicNoticeDialog;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/6  
 */
public class InvoiceGasApplyActivity extends BaseActivity implements View.OnClickListener {
    private View layoutView;
    private View layoutTaxpayer;
    private View layoutAddress;
    private Button   btnSend;
    private TextView tvType;
    private TextView tvAddress;
    private TextView tvInvoiceName;
    private TextView tvTaxpayerNum,tvCompanyAddress;
    private TextView etEmail;
    private int     requestType=0;
    private String  userId,password,type="1";
    private String  invoiceType,amount;
    private String  invoiceName, email;
    private String  address, taxpayerNum;
    private String  customerNo;
    private String  paymentNo;
    private String  fpId;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<InvoiceGasApplyActivity> mWeakReference;
        public RequestHandler(InvoiceGasApplyActivity activity) {
            mWeakReference=new WeakReference<InvoiceGasApplyActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceGasApplyActivity activity=mWeakReference.get();
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
                        String error = object.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONObject jsonObject=object.optJSONObject("data");
                                activity.onReceiveInvoiceData(jsonObject);
                            }else {
                                activity.onSuccess();
                            }
                        } else {
                            activity.onNoticeDialog(error);
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
    private void onReceiveInvoiceData(JSONObject jsonObject) {
        String name=jsonObject.optString("name");
        String address=jsonObject.optString("address");
        taxpayerNum=jsonObject.optString("taxpayer_num");
        type=jsonObject.optString("type");
        tvTaxpayerNum.setText(taxpayerNum);
        tvAddress.setText(address);
        tvCompanyAddress.setText(address);
        tvInvoiceName.setText(name);
        onSetType(type);
    }
    private void onSetType(String type) {
        switch (type){
            case "1":
                tvType.setText("个人");
                layoutView.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.GONE);
                break;
            case "3":
                tvType.setText("单位");
                layoutTaxpayer.setVisibility(View.VISIBLE);
                layoutView.setVisibility(View.VISIBLE);
                layoutAddress.setVisibility(View.GONE);
                break;
            default:
                tvType.setText("个人");
                layoutView.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.GONE);
                break;
        }
    }

    private void onSuccess() {
        setResult(1);
        Intent intent=new Intent(context,InvoiceSendSuccessActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_gas_apply);
        context=this;
        mPageName="燃气缴费发票申请";
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader(mPageName);
        Intent data=getIntent();
        customerNo=data.getStringExtra("customerNo");
        fpId=data.getStringExtra("fpId");
        amount=data.getStringExtra("amount");
        paymentNo=data.getStringExtra("paymentNo");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initEvent();
        initInvoiceData();
    }
    private void initInvoiceData() {
        requestType=0;
        customDialog.show();
        String validateURL= UrlUtil.INVOICE_DATA_TYPE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler) ;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.REQUEST_CODE_ONE:
                if (resultCode==ConstantUtil.REQUEST_CODE_ONE){
                    address=data.getStringExtra("mAddress");
                    tvAddress.setText(address);
                    tvCompanyAddress.setText(address);
                }
                break;
                default:
                    break;
        }
    }
    private void initEvent() {
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etEmail.addTextChangedListener(myTextWatcher);
        tvAddress.addTextChangedListener(myTextWatcher);
        btnSend.setOnClickListener(this);
    }
    private void initFindViewId() {
        findViewById(R.id.id_status_view).setVisibility(View.GONE);
        tvType=(TextView)findViewById(R.id.id_type);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        layoutView =findViewById(R.id.id_company_layout);
        layoutTaxpayer =findViewById(R.id.id_taxpayer_layout);
        tvAddress=(TextView) findViewById(R.id.id_tv_address);
        tvInvoiceName =(TextView)findViewById(R.id.id_tv_name);
        etEmail =(EditText)findViewById(R.id.id_et_email);
        tvTaxpayerNum =(TextView)findViewById(R.id.id_tv_taxpayer_num);
        tvCompanyAddress =(TextView) findViewById(R.id.id_tv_company_address);
        TextView tvAmount =(TextView)findViewById(R.id.id_amount);
        layoutAddress=findViewById(R.id.id_district_layout);
        findViewById(R.id.id_correct).setOnClickListener(this);
        tvAmount.setText(amount);
        btnSend.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_send:
                onApplyData();
                break;
            case R.id.id_correct:
                onStartHtml();
                break;
            default:
                break;
        }
    }
    private void onStartHtml() {
        String url= UrlUtil.GAS_INVOICE_QUESTION_URL;
        Intent price=new Intent(context,HtmlPageActivity.class);
        price.putExtra("navigate","疑问解答");
        price.putExtra("url",url);
        startActivity(price);
    }
    private void onApplyData() {
        invoiceName=tvInvoiceName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        taxpayerNum = tvTaxpayerNum.getText().toString().trim();
        switch (type){
            case VariableUtil.VALUE_ONE:
                address=tvAddress.getText().toString().trim();
                invoiceType="个人普通发票";
                if (!RegularExpressionUtil.isEmail(email)){
                    onNoticeDialog("电子邮箱格式不正确!");
                }else if (TextUtils.isEmpty(tvAddress.getText().toString())){
                    onNoticeDialog("抱歉！由于您的联系地址缺失，不能提交申请，建议前去营业厅对其进行处理。");
                }else if (TextUtils.isEmpty(invoiceName)){
                    onNoticeDialog("抱歉！由于您的发票抬头缺失，不能提交申请，建议前去营业厅对其进行处理。");
                }else {
                    requestServer();
                }
                break;
            case VariableUtil.VALUE_THREE:
                invoiceType="企业普通发票";
                address=tvCompanyAddress.getText().toString().trim();
                if (TextUtils.isEmpty(tvTaxpayerNum.getText().toString())){
                    onNoticeDialog("抱歉！由于您的纳税识别号缺失，不能提交申请，建议前去营业厅对其进行处理。");
                }else if (TextUtils.isEmpty(tvCompanyAddress.getText().toString())){
                    onNoticeDialog("抱歉！由于您的企业地址缺失，不能提交申请，建议前去营业厅对其进行处理。");
                }else if(!RegularExpressionUtil.isEmail(email)){
                    onNoticeDialog("电子邮箱格式不正确!");
                }else {
                    requestServer();
                }
                break;
            default:
                break;
        }
    }
    private void onNoticeDialog(String result) {
        new PublicNoticeDialog(context).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setTitleText("温馨提示")
                .setButtonText("我知道了")
                .setMessageContentText(result)
                .setImageVisibility(false)
                .setLineViewVisibility(true)
                .show();
    }
    private void requestServer() {
        new InvoiceEnsureDialog(context).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setEmailText(email)
                .setInvoiceType(invoiceType)
                .setInvoiceName(invoiceName)
                .setTaxpayerNum(taxpayerNum)
                .setOnPositiveClickListener(new InvoiceEnsureDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSendService();
                    }
                }).show();
    }

    private void onSendService() {
        requestType=1;
        customDialog.show();
        String validateURL= UrlUtil.INVOICE_GAS_APPLY;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("fpId",fpId);
        textParams.put("customerNo",customerNo);
        textParams.put("paymentNo",paymentNo);
        textParams.put("amount",amount);
        textParams.put("email",email);
        textParams.put("name",invoiceName);
        textParams.put("company_address",address);
        textParams.put("taxpayer_num", taxpayerNum);
        textParams.put("version","201902");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler) ;

    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etEmail.getText().toString())){
                btnSend.setEnabled(false);
            }else {
                btnSend.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
