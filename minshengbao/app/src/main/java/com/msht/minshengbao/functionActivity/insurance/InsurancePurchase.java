package com.msht.minshengbao.functionActivity.insurance;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.AgreeTreaty;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.EnsureBuy;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;


import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Demo class
 * @author hong
 * @date 2016/10/31
 */
public class InsurancePurchase extends BaseActivity implements View.OnClickListener {
    private View layoutBuy, layoutCallPhone;
    private CheckBox checkBox;
    private TextView tvConsult;
    private TextView tvAgree1, tvAgree2, tvAgree3;
    private TextView tvRealAmount;
    private EditText etName, etIdCard, etCustomer;
    private EditText etPhone, etEmail;
    private EditText etAddress, etRecommend;
    private MaterialSpinner spinner;
    private String   name, idCard,customer,phone,email;
    private String   address,recommend;
    private String   insuranceAmount ="942000.00";
    private String   id="1537981";
    private int      deadline=5;
    private String   amount="300.00";
    private String   startTime, stopTime;
    private String   url,navigation;
    private String   card_type="0";
    private int requestType=0;
    private static final String[] CERTIFICATE_TYPE = {"居民身份证","港澳通行证","台湾通行证"};
    private CustomDialog customDialog;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private final GetinsuranceHandler getinsuranceHandler=new GetinsuranceHandler(this);
    private static class GetinsuranceHandler extends Handler{

        private WeakReference<InsurancePurchase> mWeakReference;
        public GetinsuranceHandler(InsurancePurchase reference) {
            mWeakReference = new WeakReference<InsurancePurchase>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final InsurancePurchase reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestType==0){
                                reference.initShow(jsonObject);
                            }else if (reference.requestType==1){
                                reference.getHouse(jsonObject);
                            }
                        }else {
                            reference.faifure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void getHouse(JSONObject jsonObject) {
        String customerName= jsonObject.optString("customerName");
        String customerType= jsonObject.optString("customerType");
        String address= jsonObject.optString("address");
        String room= jsonObject.optString("room");
        etAddress.setText(address);
    }
    private void initShow(JSONObject jsonObject) {
        String url= jsonObject.optString("url");
        String orderNumber= jsonObject.optString("orderNumber");
        String params= jsonObject.optString("params");
        Intent intent=new Intent(this,InsurancePay.class);
        intent.putExtra("url",url);
        intent.putExtra("params",params);
        startActivity(intent);
    }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_purchase);
        context=this;
        setCommonHeader("购买保险");
        customDialog=new CustomDialog(this, "正在加载");
        amount=getIntent().getStringExtra("Amount");
        id=getIntent().getStringExtra("insurance_Id");
        deadline=getIntent().getIntExtra("vDeadLines",5);
        insuranceAmount =getIntent().getStringExtra("vSecuritys");
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        findViewById(R.id.id_status_view).setVisibility(View.GONE);
        tvConsult =(TextView)findViewById(R.id.id_tv_rightText);
        tvConsult.setVisibility(View.GONE);
        tvConsult.setText("咨询");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    if (data!=null){
                        id=data.getStringExtra("Id");
                        amount=data.getStringExtra("amount");
                        insuranceAmount =data.getStringExtra("insurance");
                        tvRealAmount.setText(amount);
                       // int time=Integer.valueOf(deadline).intValue();
                        int time=deadline;
                        SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar=Calendar.getInstance();
                        Calendar start=Calendar.getInstance();
                        start.add(Calendar.DAY_OF_MONTH,1);
                        startTime =formats.format(start.getTime());
                        calendar.add(Calendar.YEAR,time);
                        stopTime =formats.format(calendar.getTime());
                    }
                }
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void initEvent() {
        layoutBuy.setOnClickListener(this);
        tvConsult.setOnClickListener(this);
        tvAgree1.setOnClickListener(this);
        tvAgree2.setOnClickListener(this);
        tvAgree3.setOnClickListener(this);
        layoutBuy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.color.touch_backgroud);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.color.colorOrange);
                }
                return false;
            }
        });
        layoutCallPhone.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position==0){
                    card_type="0";
                }else if (position==1){
                    card_type="4";
                }else if (position==2){
                    card_type="5";
                }else {
                    card_type="0";
                }

            }
        });
    }
    private void initView() {
        layoutBuy =findViewById(R.id.id_re_buy);
        layoutCallPhone =findViewById(R.id.id_re_call);
        checkBox=(CheckBox)findViewById(R.id.id_box_read);
        tvAgree1 =(TextView)findViewById(R.id.id_text1);
        tvAgree2 =(TextView)findViewById(R.id.id_text2);
        tvAgree3 =(TextView)findViewById(R.id.id_text3);
        findViewById(R.id.id_text4).setOnClickListener(this);
        tvRealAmount =(TextView)findViewById(R.id.id_buy_amount);
        etName =(EditText)findViewById(R.id.id_et_name);
        etIdCard =(EditText) findViewById(R.id.id_et_idcard);
        etCustomer =(EditText)findViewById(R.id.id_et_customerNo);
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        etEmail =(EditText)findViewById(R.id.id_et_email);
        etAddress =(EditText)findViewById(R.id.id_et_address);
        etRecommend =(EditText)findViewById(R.id.id_et_recommend);
        tvRealAmount.setText(amount);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems(CERTIFICATE_TYPE);
        SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Calendar start=Calendar.getInstance();
        start.add(Calendar.DAY_OF_MONTH,1);
        startTime =formats.format(start.getTime());
        calendar.add(Calendar.YEAR,deadline);
        stopTime =formats.format(calendar.getTime());
        etCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==8||s.length()==10){
                    requestServer();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    private void requestServer() {
        requestType=1;
        customer= etCustomer.getText().toString().trim();
        String validateURL = UrlUtil.GET_HOUSE_ADDRESS_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("customerNo",customer);
        SendrequestUtil.postDataFromService(validateURL,textParams,getinsuranceHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_re_call:
                callHotLine();
                break;
            case R.id.id_re_buy:
                buyInsurance();
                break;
            case R.id.id_tv_rightText:
                callHotLine();
                break;
            case R.id.id_text1:
                navigation="投保须知";
                url="http://msbapp.cn/insurance/toubaoxuzhi.html";
                gotoAgree();
                break;
            case R.id.id_text2:
                navigation="投保声明与授权";
                url="http://msbapp.cn/insurance/toubaoshengming.html";
                gotoAgree();
                break;
            case R.id.id_text3:
                navigation="人身意外伤害保险条例";
                url="http://msbapp.cn/insurance/baoxiantiaokuan.html";
                gotoAgree();
                break;
            case R.id.id_text4:
                navigation="保险说明";
                url=UrlUtil.INSURANCE_EXPLAIN_URL;;
                gotoAgree();
                break;
            default:
                break;
        }
    }
    private void gotoAgree() {
        Intent intent=new Intent(this, AgreeTreaty.class);
        intent.putExtra("url",url);
        intent.putExtra("navigation",navigation);
        startActivity(intent);
    }
    private void buyInsurance() {
        name= etName.getText().toString().trim();
        idCard = etIdCard.getText().toString().trim();
        customer= etCustomer.getText().toString().trim();
        phone= etPhone.getText().toString().trim();
        email= etEmail.getText().toString().trim();
        address= etAddress.getText().toString().trim();
        recommend= etRecommend.getText().toString().trim();
        if (match(name, idCard,customer,address)){
            if (RegularExpressionUtil.isPhone(phone)&&isEmailEmpty(email)){
                if (checkBox.isChecked()){
                    showDialogs();
                }else {
                    new PromptDialog.Builder(this)
                            .setTitle("民生宝")
                            .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                            .setMessage("请您先勾选保险协议")
                            .setButton1("确定", new PromptDialog.OnClickListener() {

                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                new PromptDialog.Builder(this)
                        .setTitle("民生宝")
                        .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                        .setMessage("手机或邮箱格式不正确")
                        .setButton1("确定", new PromptDialog.OnClickListener() {

                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();

                            }
                        }).show();
            }
        }
    }
    private boolean isEmailEmpty(String email) {
        boolean result=true;
        if (TextUtils.isEmpty(email)){
            result=true;
        }else {
            result= RegularExpressionUtil.isEmail(email);
        }
        return result;
    }
    private void showDialogs() {
        final EnsureBuy insurance=new EnsureBuy(this);
        insurance.setCustomerText(customer);
        insurance.setNameText(name);
        insurance.setIdcardText(idCard);
        insurance.setAmount(insuranceAmount);
        insurance.setTypeText(amount+"套餐");
        insurance.setEffictive(startTime);
        insurance.setCutoffDate(stopTime);
        insurance.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insurance.dismiss();
            }
        });
        insurance.setOnpositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insurance.dismiss();
                customDialog.show();
                requestService();
            }
        });
        insurance.show();
    }
    private void requestService() {
        requestType=0;
        String validateURL = UrlUtil.INSURANCE_BUY_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("insurance_id",id);
        textParams.put("customer_no",customer);
        textParams.put("amount",amount);
        textParams.put("name",name);
        textParams.put("card_type",card_type);
        textParams.put("id_card", idCard);
        textParams.put("phone",phone);
        textParams.put("email",email);
        textParams.put("address",address);
        textParams.put("start_date", startTime);
        textParams.put("end_date", stopTime);
        textParams.put("recommend",recommend);
        SendrequestUtil.postDataFromService(validateURL,textParams,getinsuranceHandler);
    }
    private boolean match(String name, String idcard, String customer, String address) {
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(idcard)||TextUtils.isEmpty(customer)||TextUtils.isEmpty(address)){
            ToastUtil.ToastText(context,"请您填写投保人信息完整");
            return false;
        }else {
            return true;
        }
    }
    private void onDetail() {
        Intent detail=new Intent(this,InsuranceDetail.class);
        detail.putExtra("id",id);
        startActivity(detail);
    }
    private void callHotLine() {
        final String phone = "963666";
        new PromptDialog.Builder(this)
                .setTitle("客服电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(InsurancePurchase.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "963666"));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }else {
                ToastUtil.ToastText(context,"授权失败");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
