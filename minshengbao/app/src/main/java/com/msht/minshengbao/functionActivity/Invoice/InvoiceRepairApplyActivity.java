package com.msht.minshengbao.functionActivity.Invoice;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.AgreeTreatyActivity;
import com.msht.minshengbao.functionActivity.Public.SelectAddressActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.Public.SelectPickUpSiteActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceRepairApplyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView licenseImage;
    private LinearLayout layoutView;
    private LinearLayout layoutTaxpayer;
    private LinearLayout layoutCompany;
    private LinearLayout layoutCamera;
    private Button btnPersonal, btnCompany;
    private Button btnCommon, btnZengzhi, btnSend;
    private TextView  etAddress;
    private EditText etName;
    private EditText etRecipients, etPhone;
    private EditText etTaxpayerNum, etBank, etBankcard;
    private EditText etCompanyTel, etCompanyAddress;
    private TextView tvTitle;
    private TextView tvRightText;
    private TextView tvAddressTitle;
    private View    layoutDistrict;
    private String  userId,password,type="1";
    private String  name,content,amount;
    private String  recipients, phoneNum;
    private String  address,identyfyNo,bank;
    private String  bankcard,companyTel, companyAddress;
    private String  relateOrder;
    private String  deliveryType="1";
    private File    certeFile =null;
    private boolean booleanType =false;
    private boolean booleanInvoice =false;
    private static  final int REQUEST_CALL_CAMERE=1;
    private static final int OVER_AMOUNT=400;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<InvoiceRepairApplyActivity> mWeakReference;
        public RequestHandler(InvoiceRepairApplyActivity activity) {
            mWeakReference=new WeakReference<InvoiceRepairApplyActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceRepairApplyActivity activity=mWeakReference.get();
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
                        String resultCode=object.optString("result_code");
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.setResult(1);
                            if (activity.deliveryType.equals(ConstantUtil.VALUE_ONE)){
                                activity.onSuccess("您的发票已申请成功！");
                            }else {
                                activity.onSuccess("您的发票申请已成功提交，工作人员将在五个工作日内致电您领取发票！");
                            }
                        } else {
                            activity.noticeDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onSuccess(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                }).show();
    }
    private void noticeDialog(String s) {
        new PromptDialog.Builder(this)
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_repair_apply);
        context=this;
        mPageName="发票申请";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        relateOrder=data.getStringExtra("idinvoice");
        amount=data.getStringExtra("total_amount");
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("说明");
    }
    private void initView() {
        licenseImage =(ImageView)findViewById(R.id.id_license_img);
        btnCommon =(Button)findViewById(R.id.id_btn_commen);
        btnZengzhi =(Button)findViewById(R.id.id_btn_zengzhi);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        btnPersonal =(Button)findViewById(R.id.id_btn_personal);
        btnCompany =(Button)findViewById(R.id.id_btn_company);
        layoutDistrict =findViewById(R.id.id_re_district);
        layoutView =(LinearLayout)findViewById(R.id.id_li_view);
        layoutTaxpayer =(LinearLayout)findViewById(R.id.id_taxpayer_layout);
        layoutCompany =(LinearLayout)findViewById(R.id.id_company_layout);;
        layoutCamera =(LinearLayout)findViewById(R.id.id_camara_layout);;
        etAddress=(TextView) findViewById(R.id.id_tv_address);
        tvTitle =(TextView) findViewById(R.id.id_tv_title);
        tvAddressTitle=(TextView)findViewById(R.id.id_address_title) ;
        etName =(EditText)findViewById(R.id.id_et_name);
        etRecipients =(EditText)findViewById(R.id.id_et_recipient);
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        etTaxpayerNum =(EditText)findViewById(R.id.id_et_taxpayer_num);
        etBank =(EditText)findViewById(R.id.id_et_bank);
        etBankcard =(EditText)findViewById(R.id.id_et_bankcard);
        etCompanyTel =(EditText)findViewById(R.id.id_et_company_tel);
        etCompanyAddress =(EditText)findViewById(R.id.id_et_company_addr);
        TextView tvAmount =(TextView)findViewById(R.id.id_amount);
        tvAmount.setText(amount);
        btnSend.setEnabled(false);
    }
    private void initEvent() {
        RadioGroup radioGroupMain = (RadioGroup) findViewById(R.id.id_group);
        tvRightText.setOnClickListener(this);
        btnCommon.setOnClickListener(this);
        btnZengzhi.setOnClickListener(this);
        btnCompany.setOnClickListener(this);
        btnPersonal.setOnClickListener(this);
        licenseImage.setOnClickListener(this);
        layoutDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryType.equals(ConstantUtil.VALUE_ONE)){
                    Intent intent=new Intent(context, SelectAddressActivity.class);
                    startActivityForResult(intent,1);
                }else {
                    Intent intent=new Intent(context, SelectPickUpSiteActivity.class);
                    startActivityForResult(intent,1);
                }

            }
        });
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etName.addTextChangedListener(myTextWatcher);
        etRecipients.addTextChangedListener(myTextWatcher);
        etPhone.addTextChangedListener(myTextWatcher);
        etAddress.addTextChangedListener(myTextWatcher);
        btnSend.setOnClickListener(this);
        radioGroupMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_radio_mail_post:
                        deliveryType="1";
                        tvAddressTitle.setText("选择地址：");
                        etAddress.setText("");
                        break;
                    case R.id.id_radio_pick:
                        etAddress.setText("");
                        deliveryType="2";
                        tvAddressTitle.setText("选择自提点：");
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etName.getText().toString())||TextUtils.isEmpty(etRecipients.getText().toString())
                    ||TextUtils.isEmpty(etPhone.getText().toString())||TextUtils.isEmpty(etAddress.getText().toString())){
                btnSend.setEnabled(false);
            }else {
                btnSend.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_commen:
                booleanInvoice =false;
                btnCommon.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnZengzhi.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                if (booleanType){
                    type="3";
                    layoutView.setVisibility(View.VISIBLE);
                    layoutTaxpayer.setVisibility(View.VISIBLE);
                    layoutCompany.setVisibility(View.GONE);
                    layoutCamera.setVisibility(View.GONE);
                    etBank.setHint("请输入您的开户银行（选填）");
                    etBankcard.setHint("请输入您的开户账号（选填）");
                }else {
                    type="1";
                    layoutView.setVisibility(View.GONE);
                    layoutCamera.setVisibility(View.GONE);
                    layoutTaxpayer.setVisibility(View.GONE);
                }
                break;
            case R.id.id_btn_zengzhi:
                booleanInvoice =true;
                type="2";
                btnCommon.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                btnZengzhi.setBackgroundResource(R.drawable.shape_orange_corner_button);
                layoutView.setVisibility(View.VISIBLE);
                layoutCamera.setVisibility(View.VISIBLE);
                layoutTaxpayer.setVisibility(View.VISIBLE);
                layoutCompany.setVisibility(View.VISIBLE);
                layoutView.setVisibility(View.VISIBLE);
                etBank.setHint("请输入您的开户银行（必填）");
                etBankcard.setHint("请输入您的开户账号（必填）");
                break;
            case R.id.id_btn_personal:
                booleanType =false;
                booleanInvoice =false;
                type="1";
                btnCommon.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnPersonal.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnCompany.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                btnZengzhi.setVisibility(View.GONE);
                layoutView.setVisibility(View.GONE);
                layoutCamera.setVisibility(View.GONE);
                layoutTaxpayer.setVisibility(View.GONE);
                break;
            case R.id.id_btn_company:
                booleanType =true;
                if (booleanInvoice){
                    type="2";
                    layoutView.setVisibility(View.VISIBLE);
                    layoutCamera.setVisibility(View.VISIBLE);
                    layoutTaxpayer.setVisibility(View.VISIBLE);
                    layoutCompany.setVisibility(View.VISIBLE);
                    layoutView.setVisibility(View.VISIBLE);
                    btnZengzhi.setBackgroundResource(R.drawable.shape_orange_corner_button);
                    etBank.setHint("请输入您的开户银行（必填）");
                    etBankcard.setHint("请输入您的开户账号（必填）");
                }else {
                    type="3";
                    layoutView.setVisibility(View.VISIBLE);
                    layoutTaxpayer.setVisibility(View.VISIBLE);
                    layoutCompany.setVisibility(View.GONE);
                    layoutCamera.setVisibility(View.GONE);
                    btnZengzhi.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                    etBank.setHint("请输入您的开户银行（选填）");
                    etBankcard.setHint("请输入您的开户账号（选填）");
                }
                btnZengzhi.setVisibility(View.VISIBLE);
                btnCompany.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btnPersonal.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                break;
            case R.id.id_license_img:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(InvoiceRepairApplyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(InvoiceRepairApplyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CALL_CAMERE);
                    }else {
                        receivePhoto();
                    }
                } else {
                    receivePhoto();
                }
                break;
            case R.id.id_btn_send:
                applyData();
                break;
            case R.id.id_tv_rightText:
                String url=UrlUtil.INVOICE_EXPLAIN;
                Intent intent=new Intent(this, AgreeTreatyActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("navigation","发票说明");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CALL_CAMERE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                receivePhoto();
            }else {
                Toast.makeText(InvoiceRepairApplyActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void receivePhoto() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(InvoiceRepairApplyActivity.this, PhotoPicker.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE)){
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String filepath = photos.get(0);
                certeFile = new File(filepath);
                if (certeFile.exists()&& certeFile.canRead()) {
                    licenseImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(filepath, 500, 500));
                }
                compressImg(certeFile);
            }
        }else if (requestCode==1){
            if (resultCode==1){
                String mAddress=data.getStringExtra("mAddress");
                etAddress.setText(mAddress);
            }
        }
    }
    private void compressImg(File files) {
        Luban.with(this)
                //传人要压缩的图片
                .load(files)
                //设置回调
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        certeFile =file;
                    }
                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        Toast.makeText(InvoiceRepairApplyActivity.this,"图片压缩失败!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).launch();    //启动压缩
    }

    private void applyData() {
        name      = etName.getText().toString().trim();
        content   = tvTitle.getText().toString().trim();
        recipients= etRecipients.getText().toString().trim();
        phoneNum = etPhone.getText().toString().trim();
        address=etAddress.getText().toString().trim();
        identyfyNo= etTaxpayerNum.getText().toString().trim();
        bank      = etBank.getText().toString().trim();
        bankcard  = etBankcard.getText().toString().trim();
        companyTel= etCompanyTel.getText().toString().trim();
        companyAddress = etCompanyAddress.getText().toString().trim();
        double doubleAmount=Double.parseDouble(amount);
        switch (type){
            case VariableUtil.VALUE_ONE:
                requestServer(doubleAmount);
                break;
            case VariableUtil.VALUE_TWO:
                if (TextUtils.isEmpty(etTaxpayerNum.getText().toString())){
                    noticeDialog("请输入您的纳税人识别号");
                }else if (TextUtils.isEmpty(etBank.getText().toString())){
                    noticeDialog("请输入您的开户银行");
                }else if (TextUtils.isEmpty(etBankcard.getText().toString())){
                    noticeDialog("请输入您的开户账号");
                }else if (TextUtils.isEmpty(etCompanyTel.getText().toString())){
                    noticeDialog("请输入您的企业电话");
                }else if (TextUtils.isEmpty(etCompanyAddress.getText().toString())){
                    noticeDialog("请输入您的企业地址");
                }else {
                    requestServer(doubleAmount);
                }
                break;
            case VariableUtil.VALUE_THREE:
                if (TextUtils.isEmpty(etTaxpayerNum.getText().toString())){
                    noticeDialog("请输入您的纳税人识别号");
                }else {
                    requestServer(doubleAmount);
                }
                break;
            default:
                break;
        }
    }

    private void requestServer(double doubleAmount) {
        if (deliveryType.equals(ConstantUtil.VALUE_ONE)){
            if (doubleAmount<OVER_AMOUNT){
                new PromptDialog.Builder(this)
                        .setTitle("民生宝")
                        .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                        .setMessage("由于您的发票金额还未满400元，我们" +
                                "会通过顺丰到付的方式给您邮寄发票，" +
                                "签收时请将邮费会给快递小哥哦")
                        .setButton1("我再想想", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setButton2("我很确定", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                customDialog.show();
                                sendService();
                            }
                        })
                        .show();
            }else {
                customDialog.show();
                sendService();
            }
        }else {
            customDialog.show();
            sendService();
        }
    }
    private void sendService() {
        String validateURL= UrlUtil.INVOICE_APPLY_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String,File> fileParams=new HashMap<String,File>();
        textParams.put("userId", userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("name",name);
        textParams.put("title",content);
        textParams.put("amount",amount);
        textParams.put("recipient",recipients);
        textParams.put("phone", phoneNum);
        textParams.put("address",address);
        textParams.put("taxpayer_num",identyfyNo);
        textParams.put("bank",bank);
        textParams.put("bankcard",bankcard);
        textParams.put("company_tel",companyTel);
        textParams.put("company_address", companyAddress);
        textParams.put("relate_order",relateOrder);
        textParams.put("delivery_type",deliveryType);
        if (certeFile !=null){
            fileParams.put("business_license_img", certeFile);
        }
        SendRequestUtil.postFileToServer(textParams,fileParams,validateURL,requestHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }

    }
}
