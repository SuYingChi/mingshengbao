package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/9/15  
 */
public class GasRepairActivity extends BaseActivity implements View.OnClickListener {
    private EditText etSelectAddress, etRepairTopic, etDetail;
    private EditText etPhone;
    private Button btnSelectAddress;
    private Button confirm;
    private String customerNo,houseId;
    private String userId;
    private String password;
    private String phone;
    private String mTitle,description;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasRepairActivity> mWeakReference;
        public RequestHandler(GasRepairActivity activity) {
            mWeakReference = new WeakReference<GasRepairActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasRepairActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onSendSuccess();
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.displayDialog(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onSendSuccess() {
        String navigation="报修服务";
        Intent success=new Intent(context,ServerSuccessActivity.class);
        success.putExtra("navigation",navigation);
        success.putExtra("boolean",true);
        startActivity(success);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_repair);
        context=this;
        setCommonHeader("燃气报修");
        customDialog=new CustomDialog(this, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
        iniEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantUtil.REQUEST_CODE_SEVEN:
                if(data!=null){
                    if (resultCode==2){
                        String addressName=data.getStringExtra("addressname");
                        houseId=data.getStringExtra("houseId");
                        customerNo=data.getStringExtra("customerNo");
                        etSelectAddress.setText(addressName);
                    }
                }
                break;
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView() {
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        btnSelectAddress =(Button)findViewById(R.id.id_btn_selectaddress);
        confirm =(Button)findViewById(R.id.id_btn_comfirm);
        etSelectAddress =(EditText)findViewById(R.id.id_select_address);
        etRepairTopic =(EditText)findViewById(R.id.id_repair_topic);
        etDetail =(EditText)findViewById(R.id.id_repair_detail);
        etSelectAddress.setInputType(InputType.TYPE_NULL);
        confirm.setEnabled(false);
    }
    private void iniEvent() {
        btnSelectAddress.setOnClickListener(this);
        confirm.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etSelectAddress.addTextChangedListener(myTextWatcher);
        etRepairTopic.addTextChangedListener(myTextWatcher);
        etDetail.addTextChangedListener(myTextWatcher);
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etSelectAddress.getText().toString()) || TextUtils.isEmpty(etRepairTopic.getText().toString()) ||
                    TextUtils.isEmpty(etDetail.getText().toString())) {
                confirm.setEnabled(false);
            } else {
                confirm.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_selectaddress:
                onStartSelectAddress();
                break;
            case R.id.id_btn_comfirm:
                onVerifySend();
            default:
                break;
        }
    }
    private void onStartSelectAddress() {
        Intent select=new Intent(context,SelectCustomerNo.class);
        startActivityForResult(select,ConstantUtil.REQUEST_CODE_SEVEN);
    }
    private void onVerifySend() {
        String mAddressName = etSelectAddress.getText().toString().trim();
        mTitle = etRepairTopic.getText().toString().trim();
        description = etDetail.getText().toString().trim();
        phone= etPhone.getText().toString().trim();
        if (isMatchTitleMsg(mTitle, mAddressName,phone)) {
            customDialog.show();
            requestService();
        }
    }
    private boolean isMatchTitleMsg(String title, String addressName, String phones) {
        if(TextUtils.isEmpty(title)) {
            displayDialog("报修主题不能为空");
            return false;
        }else if(TextUtils.isEmpty(addressName)) {
            displayDialog("地址不能为空");
            return false;
        }else if (!RegularExpressionUtil.isPhone(phones)){
            displayDialog("手机号格式不正确");
            return false;
        }
        return true;
    }
    private void displayDialog(String s) {
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
    private void requestService() {
        String type="5";
        String validateURL = UrlUtil.INSTALL_SERVER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title", mTitle);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("houseId",houseId);
        textParams.put("customerNo",customerNo);
        textParams.put("phone",phone);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
