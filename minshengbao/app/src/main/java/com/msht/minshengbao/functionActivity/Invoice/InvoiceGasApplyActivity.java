package com.msht.minshengbao.functionActivity.Invoice;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
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
import com.msht.minshengbao.ViewUI.Dialog.DeliveryInfoDialog;
import com.msht.minshengbao.ViewUI.Dialog.InvoiceEnsureDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.AgreeTreatyActivity;
import com.msht.minshengbao.functionActivity.Public.SelectAddressActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

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
    private Button btnPersonal, btnCompany;
    private Button   btnSend;
    private EditText etAddress;
    private EditText etInvoiceName;
    private EditText etTaxpayerNum, etBank, etBankcard;
    private EditText etCompanyTel, etCompanyAddress;
    private TextView etEmail;
    private String  userId,password,type="1";
    private String  invoiceType,amount;
    private String  invoiceName, email;
    private String  address,identifyNo,bank;
    private String  bankcard,companyTel;
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
                            activity.onSuccess();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.REQUEST_CODE_ONE:
                if (resultCode==ConstantUtil.REQUEST_CODE_ONE){
                    address=data.getStringExtra("mAddress");
                    etAddress.setText(address);
                    etCompanyAddress.setText(address);
                }
                break;
                default:
                    break;
        }
    }
    private void initEvent() {
        btnCompany.setOnClickListener(this);
        btnPersonal.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etInvoiceName.addTextChangedListener(myTextWatcher);
        etEmail.addTextChangedListener(myTextWatcher);
        etAddress.addTextChangedListener(myTextWatcher);
        btnSend.setOnClickListener(this);
    }
    private void initFindViewId() {
        findViewById(R.id.id_status_view).setVisibility(View.GONE);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        btnPersonal =(Button)findViewById(R.id.id_btn_personal);
        btnCompany =(Button)findViewById(R.id.id_btn_company);
        layoutView =findViewById(R.id.id_li_view);
        layoutTaxpayer =findViewById(R.id.id_taxpayer_layout);
        etAddress=(EditText) findViewById(R.id.id_et_address);
        etInvoiceName =(EditText)findViewById(R.id.id_et_name);
        etEmail =(EditText)findViewById(R.id.id_et_email);
        etTaxpayerNum =(EditText)findViewById(R.id.id_et_taxpayer_num);
        etBank =(EditText)findViewById(R.id.id_et_bank);
        etBankcard =(EditText)findViewById(R.id.id_et_bankcard);
        etCompanyTel =(EditText)findViewById(R.id.id_et_company_tel);
        etCompanyAddress =(EditText)findViewById(R.id.id_et_company_addr);
        TextView tvAmount =(TextView)findViewById(R.id.id_amount);
        layoutAddress=findViewById(R.id.id_district_layout);
        tvAmount.setText(amount);
        btnSend.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_personal:
                type="1";
                btnPersonal.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnCompany.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                layoutView.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
                break;
            case R.id.id_btn_company:
                type="3";
                layoutView.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                layoutAddress.setVisibility(View.GONE);
                etBank.setHint("请输入您的开户银行（必填）");
                etBankcard.setHint("请输入您的开户账号（必填）");
                btnCompany.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnPersonal.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                break;
            case R.id.id_btn_send:
                onApplyData();
                break;
            default:
                break;
        }
    }
    private void onApplyData() {
        invoiceName=etInvoiceName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        identifyNo= etTaxpayerNum.getText().toString().trim();
        bank      = etBank.getText().toString().trim();
        bankcard  = etBankcard.getText().toString().trim();
        companyTel= etCompanyTel.getText().toString().trim();
        switch (type){
            case VariableUtil.VALUE_ONE:
                address=etAddress.getText().toString().trim();
                invoiceType="个人普通发票";
                if (!RegularExpressionUtil.isEmail(email)){
                    onNoticeDialog("电子邮箱格式不正确!");
                }else if (TextUtils.isEmpty(etAddress.getText().toString())){
                    onNoticeDialog("请输入您的联系地址");
                }else {
                    requestServer();
                }
                break;
            case VariableUtil.VALUE_THREE:
                invoiceType="企业普通发票";
                address=etCompanyAddress.getText().toString().trim();
                if (TextUtils.isEmpty(etTaxpayerNum.getText().toString())){
                    onNoticeDialog("请输入您的纳税人识别号");
                }else if (TextUtils.isEmpty(etBank.getText().toString())){
                    onNoticeDialog("请输入您的开户银行");
                }else if (TextUtils.isEmpty(etBankcard.getText().toString())){
                    onNoticeDialog("请输入您的开户账号");
                }else if (TextUtils.isEmpty(etCompanyTel.getText().toString())){
                    onNoticeDialog("请输入您的企业电话");
                }else if (TextUtils.isEmpty(etCompanyAddress.getText().toString())){
                    onNoticeDialog("请输入您的企业地址");
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
    private void onNoticeDialog(String s) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void requestServer() {
        new InvoiceEnsureDialog(context).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setEmailText(email)
                .setInvoiceType(invoiceType)
                .setInvoiceName(invoiceName)
                .setTaxpayerNum(identifyNo)
                .setOnPositiveClickListener(new InvoiceEnsureDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSendService();
                    }
                }).show();
    }

    private void onSendService() {
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
        textParams.put("taxpayer_num",identifyNo);
        textParams.put("bank",bank);
        textParams.put("bankcard",bankcard);
        textParams.put("company_tel",companyTel);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler) ;

    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etInvoiceName.getText().toString())||TextUtils.isEmpty(etEmail.getText().toString())){
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
