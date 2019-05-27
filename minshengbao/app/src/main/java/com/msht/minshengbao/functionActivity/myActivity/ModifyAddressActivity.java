package com.msht.minshengbao.functionActivity.myActivity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.Dialog.EnsureDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.functionActivity.publicModule.MoveSelectAddress;
import com.msht.minshengbao.functionActivity.publicModule.SelectCityActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class ModifyAddressActivity extends BaseActivity implements View.OnClickListener {
    private Button    btnEnsure;
    private TextView  tvCity;
    private TextView  tvAddress;
    private EditText  etDoorAddress;
    private EditText  etName;
    private EditText  etPhone;
    private EditText  etCustomerNo;
    private String    userId,id;
    private String    password;
    private String    mCity,cityId;
    private String    mAddress;
    private String    mName;
    private String    mPhone;
    private String    longitude ;
    private String    latitude ;
    private String    locAddress;
    private String    doorAddress;
    private String    customerNo;
    private String    mCustomerNo;
    private int       requestCode=0;
    private boolean   isChangeCustomer=false;
    private CustomDialog customDialog;
    private Context context;
    private ModifyAddressActivity mActivity;
    /**
     * 城市标志
     */
    private static final int REQUEST_CODE =1;
    private static final int ADDRESS_CODE =2;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ModifyAddressActivity>mWeakReference;
        public RequestHandler(ModifyAddressActivity activity) {
            mWeakReference=new WeakReference<ModifyAddressActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ModifyAddressActivity activity =mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0) {
                                activity.setResult(0x001);
                                activity.finish();
                            }else if (activity.requestCode==1){
                                activity.setResult(0x002);
                                activity.finish();
                            }
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void displayDialog(String string) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
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
        setContentView(R.layout.activity_modify_address);
        context=this;
        mActivity=this;
        setCommonHeader("修改地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        if (data!=null){
            mAddress=data.getStringExtra("address");
            id=data.getStringExtra("id");
            cityId=data.getStringExtra("city_id");
            mCity=data.getStringExtra("city_name");
            mName=data.getStringExtra("name");
            mPhone=data.getStringExtra("phone");
            longitude=data.getStringExtra("longitude");
            latitude =data.getStringExtra("latitude");
            locAddress=data.getStringExtra("locAddress");
            doorAddress=data.getStringExtra("doorAddress");
            customerNo=data.getStringExtra("customerNo");

        }
        initView();
        etName.setText(mName);
        etPhone.setText(mPhone);
        if (!TextUtils.isEmpty(locAddress)&&!latitude.equals(ConstantUtil.NULL_VALUE)){
            tvAddress.setText(locAddress);
        }else {
            tvAddress.setHint("请选择您的地址");
        }
        if (!TextUtils.isEmpty(doorAddress)&&!doorAddress.equals(ConstantUtil.NULL_VALUE)){
            etDoorAddress.setText(doorAddress);
        }else {
            etDoorAddress.setHint("例：8号楼808室");
        }
        if (!TextUtils.isEmpty(mCity)&&!latitude.equals(ConstantUtil.NULL_VALUE)){
            tvCity.setText(mCity);
        }else {
            tvCity.setHint("例：海口");
        }
        etCustomerNo.setText(customerNo);
        initEvent();
    }

    private void initEvent() {
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchGasCustomerNo();
            }
        });
    }
    private void onSearchGasCustomerNo() {
        customDialog.show();
        mCustomerNo=etCustomerNo.getText().toString().trim();
        String validateURL=UrlUtil.NEW_HOUSE_SEARCH_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",mCustomerNo);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)){
                JSONObject jsonObject = object.optJSONObject("data");
                if (jsonObject!=null){
                    String address = jsonObject.optString("address");
                    onGasAddressDialogs(address);
                }
            }else {
                CustomToast.showWarningLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void onGasAddressDialogs(String address) {
        if (mActivity!=null&&!mActivity.isFinishing()){
            new EnsureDialog(context).builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setTitleText("请确认燃气用户")
                    .setContentOneText(mCustomerNo)
                    .setContentTwoText(address)
                    .setOnPositiveClickListener(new EnsureDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(View v) {
                            isChangeCustomer = !TextUtils.isEmpty(mCustomerNo) && !mCustomerNo.equals(customerNo);
                        }
                    })
                    .setCancelClickListener(new EnsureDialog.OnCancelClickListener() {
                        @Override
                        public void onCancelClick(View v) {
                            isChangeCustomer=false;
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (data!=null){
                    if (resultCode==2){
                        mCity=data.getStringExtra("mCity");
                        cityId=data.getStringExtra("Id");
                        tvCity.setText(mCity);
                    }
                }
                break;
            case ADDRESS_CODE:
                if (data!=null){
                    if (resultCode==1){
                        String address=data.getStringExtra("addressInfo");
                        latitude=data.getStringExtra("latitude");
                        longitude=data.getStringExtra("longitude");
                        tvAddress.setText(address);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void initView() {
        ImageView rightImg =(ImageView)findViewById(R.id.id_right_img);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.delete2x);
        rightImg.setOnClickListener(this);
        btnEnsure=(Button)findViewById(R.id.id_ensure_btn) ;
        tvCity =(TextView)findViewById(R.id.id_tv_city);
        tvAddress=(TextView)findViewById(R.id.id_map_address);
        etDoorAddress =(EditText) findViewById(R.id.id_et_address);
        etCustomerNo=(EditText)findViewById(R.id.id_customerNo);
        etCustomerNo.addTextChangedListener(new MyTextWatcher());
        etName =(EditText)findViewById(R.id.id_et_name);
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        findViewById(R.id.id_re_newaddress).setOnClickListener(this);
        findViewById(R.id.id_re_city).setOnClickListener(this);
        findViewById(R.id.id_map_address_layout).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_newaddress:
                mName= etName.getText().toString().trim();
                mPhone= etPhone.getText().toString().trim();
                if (matchCity(tvCity.getText().toString())&&matchAddress(tvAddress.getText().toString())
                        &&matchName(mName)&&isPhone(mPhone)){
                    mAddress=tvAddress.getText().toString().trim()+etDoorAddress.getText().toString().trim();
                    requestCode=0;
                    requestService();
                }
                break;
            case R.id.id_re_city:
                Intent city=new Intent(context, SelectCityActivity.class);
                startActivityForResult(city, REQUEST_CODE);
                break;
            case R.id.id_right_img:
                onDeleteAddress();
                break;
            case R.id.id_map_address_layout:
                if (!TextUtils.isEmpty(mCity)&&!mCity.equals(ConstantUtil.NULL_VALUE)){
                    onMapAddress();
                }else {
                    CustomToast.showWarningLong("请选择城市");
                }
                break;
            default:
                break;
        }
    }
    private void onMapAddress() {
        Intent intent=new Intent(context, MoveSelectAddress.class);
        intent.putExtra("city_name",mCity);
        startActivityForResult(intent, ADDRESS_CODE);
    }
    private void onDeleteAddress() {
        new PromptDialog.Builder(this)
                .setTitle("删除地址")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确定删除该地址?")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestDelete();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void requestDelete() {
        customDialog.show();
        String validateURL = UrlUtil.DELETE_ADDRESS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onSendDataSuccess(1,data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }
    private boolean matchName(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtil.ToastText(context,"请填写您的收货人名");
            return false;
        }else {
            return true;
        }
    }
    private boolean matchAddress(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtil.ToastText(context,"请填写您的地址");
            return false;
        }else {
            return true;
        }
    }
    private boolean matchCity(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtil.ToastText(context,"请选择城市");
            return false;
        }else {
            return true;
        }
    }
    private boolean isPhone(String phoneNo) {     //判断电话号码个格式
        Matcher matcher=NUMBER_PATTERN.matcher(phoneNo);
        if (matcher.matches()){
            return true;
        }else {
            ToastUtil.ToastText(context,"电话号码格式不正确");
            return false;
        }
    }
    private void requestService() {
        customDialog.show();
        String validateURL = UrlUtil.NEW_MODIFY_ADDRESS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        textParams.put("city_id",cityId);
        textParams.put("address",mAddress);
        if (isChangeCustomer){
            textParams.put("customerNo",mCustomerNo);
        }
        textParams.put("name",mName);
        textParams.put("phone",mPhone);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        textParams.put("locAddress",locAddress);
        textParams.put("doorAddress",doorAddress);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onSendDataSuccess(0,data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }

    private void onSendDataSuccess(int i,String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (i==0) {
                    setResult(0x001);
                    finish();
                }else if (i==1){
                   setResult(0x002);
                   finish();
                }
            }else {
                CustomToast.showErrorLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(etCustomerNo.getText().toString())){
                btnEnsure.setEnabled(true);
            }else {
                btnEnsure.setEnabled(false);
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
