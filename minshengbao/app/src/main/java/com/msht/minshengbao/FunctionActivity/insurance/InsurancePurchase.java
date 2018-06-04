package com.msht.minshengbao.FunctionActivity.insurance;

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
import com.msht.minshengbao.FunctionActivity.HtmlWeb.AgreeTreayt;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demo class
 * @author hong
 * @date 2016/10/31
 */
public class InsurancePurchase extends BaseActivity implements View.OnClickListener {
    private View Rbuys,Rcallphone;
    private CheckBox checkBox;
    private TextView tv_consult;
    private TextView tv_agree1,tv_agree2,tv_agree3;
    private TextView tv_realamount;
    private EditText et_name,et_idcard,et_customer;
    private EditText et_phone,et_email;
    private EditText et_address,et_recommend;
    private MaterialSpinner spinner;
    private String   name,idcard,customer,phone,email;
    private String   address,recommend;
    private String   insurance_amount="942000.00";
    private String   id="1537981";
    private int      deadline=5;
    private String   amount="300.00";
    private String   start_time,stop_time;
    private String   idNo;
    private String   card_type="0";
    private int requestType=0;
    private static final String[] certificateType = {"居民身份证","港澳通行证","台湾通行证"};
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
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals("success")) {
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
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
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
        et_address.setText(address);
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
        insurance_amount=getIntent().getStringExtra("vSecuritys");
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        findViewById(R.id.id_status_view).setVisibility(View.GONE);
        tv_consult=(TextView)findViewById(R.id.id_tv_rightText);
        tv_consult.setVisibility(View.GONE);
        tv_consult.setText("咨询");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    if (data!=null){
                        id=data.getStringExtra("Id");
                        amount=data.getStringExtra("amount");
                        insurance_amount=data.getStringExtra("insurance");
                        tv_realamount.setText(amount);
                       // int time=Integer.valueOf(deadline).intValue();
                        int time=deadline;
                        SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar=Calendar.getInstance();
                        Calendar start=Calendar.getInstance();
                        start.add(Calendar.DAY_OF_MONTH,1);
                        start_time=formats.format(start.getTime());
                        calendar.add(Calendar.YEAR,time);
                        stop_time=formats.format(calendar.getTime());
                    }
                }
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void initEvent() {
        Rbuys.setOnClickListener(this);
        tv_consult.setOnClickListener(this);
        tv_agree1.setOnClickListener(this);
        tv_agree2.setOnClickListener(this);
        tv_agree3.setOnClickListener(this);
        Rbuys.setOnTouchListener(new View.OnTouchListener() {
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
        Rcallphone.setOnClickListener(this);
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
        Rbuys=findViewById(R.id.id_re_buy);
        Rcallphone=findViewById(R.id.id_re_call);
        checkBox=(CheckBox)findViewById(R.id.id_box_read);
        tv_agree1=(TextView)findViewById(R.id.id_text1);
        tv_agree2=(TextView)findViewById(R.id.id_text2);
        tv_agree3=(TextView)findViewById(R.id.id_text3);
        findViewById(R.id.id_text4).setOnClickListener(this);
        tv_realamount=(TextView)findViewById(R.id.id_buy_amount);
        et_name=(EditText)findViewById(R.id.id_et_name);
        et_idcard=(EditText) findViewById(R.id.id_et_idcard);
        et_customer=(EditText)findViewById(R.id.id_et_customerNo);
        et_phone=(EditText)findViewById(R.id.id_et_phone);
        et_email=(EditText)findViewById(R.id.id_et_email);
        et_address=(EditText)findViewById(R.id.id_et_address);
        et_recommend=(EditText)findViewById(R.id.id_et_recommend);
        tv_realamount.setText(amount);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems(certificateType);
        SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Calendar start=Calendar.getInstance();
        start.add(Calendar.DAY_OF_MONTH,1);
        start_time=formats.format(start.getTime());
        calendar.add(Calendar.YEAR,deadline);
        stop_time=formats.format(calendar.getTime());
        et_customer.addTextChangedListener(new TextWatcher() {
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
        customer=et_customer.getText().toString().trim();
        String validateURL = UrlUtil.GethouseAddress_Url;
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
                callHotline();
                break;
            case R.id.id_re_buy:
                buyinsurance();
                break;
            case R.id.id_tv_rightText:
                callHotline();
                break;
            case R.id.id_text1:
                idNo="1";
                gotoAgree();
                break;
            case R.id.id_text2:
                idNo="2";
                gotoAgree();
                break;
            case R.id.id_text3:
                idNo="3";
                gotoAgree();
                break;
            case R.id.id_text4:
                idNo="6";
                gotoAgree();
                break;
            default:
                break;
        }
    }
    private void gotoAgree() {
        Intent intent=new Intent(this, AgreeTreayt.class);
        intent.putExtra("idNo",idNo);
        startActivity(intent);
    }
    private void buyinsurance() {
        name=et_name.getText().toString().trim();
        idcard=et_idcard.getText().toString().trim();
        customer=et_customer.getText().toString().trim();
        phone=et_phone.getText().toString().trim();
        email=et_email.getText().toString().trim();
        address=et_address.getText().toString().trim();
        recommend=et_recommend.getText().toString().trim();
        if (match(name,idcard,customer,address)){
            if (isphone(phone)&&isemailEmpty(email)){
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
    private boolean isemailEmpty(String email) {
        if (TextUtils.isEmpty(email)){
            return true;
        }else {
            if (isemail(email)){
                return true;
            }else {
                return false;
            }
        }
    }
    private void showDialogs() {
        final EnsureBuy insurance=new EnsureBuy(this);
        insurance.setCustomerText(customer);
        insurance.setNameText(name);
        insurance.setIdcardText(idcard);
        insurance.setAmount(insurance_amount+"元");
        insurance.setTypeText(amount+"套餐");
        insurance.setEffictive(start_time);
        insurance.setCutoffDate(stop_time);
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
                requestSevice();
            }
        });
        insurance.show();
    }
    private void requestSevice() {
        requestType=0;
        String validateURL = UrlUtil.Insurance_buy_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("insurance_id",id);
        textParams.put("customer_no",customer);
        textParams.put("amount",amount);
        textParams.put("name",name);
        textParams.put("card_type",card_type);
        textParams.put("id_card",idcard);
        textParams.put("phone",phone);
        textParams.put("email",email);
        textParams.put("address",address);
        textParams.put("start_date",start_time);
        textParams.put("end_date",stop_time);
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
    private boolean isemail(String email) {
        String str="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|((["+
                "a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p=Pattern.compile(str);
        Matcher m=p.matcher(email);
        return m.matches();
    }
    private boolean isphone(String phone) {
        Matcher matcher=NUMBER_PATTERN.matcher(phone);
        return matcher.matches();
    }
    private void Detail() {
        Intent detail=new Intent(this,InsuranceDetail.class);
        detail.putExtra("id",id);
        startActivity(detail);
    }
    private void callHotline() {
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
}
