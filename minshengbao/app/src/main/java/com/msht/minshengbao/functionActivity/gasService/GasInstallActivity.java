package com.msht.minshengbao.functionActivity.gasService;

import android.app.Dialog;
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

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.ViewUI.Dialog.SelectDialog;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.publicModule.PaySuccessActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/18 
 */
public class GasInstallActivity extends BaseActivity implements View.OnClickListener {
    private Button   btnSelectAddress, btnVerify;
    private TextView tvInstallType;
    private View layoutType;
    private TextView tvAddress;
    private EditText etInstallTopic;
    private EditText etDetail;
    private EditText etPhone;
    private ArrayList<HashMap<String, String>> typeList = new ArrayList<HashMap<String, String>>();
    /**安装类型 **/
    private String typeNum;
    /**客户号**/
    private String customerNo="";
    /**房屋ID **/
    private String houseId="";
    private String userId;
    private String password;
    private int requestType =0;
    private int mPosition=-1;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private final  RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasInstallActivity> mWeakReference;
        public RequestHandler(GasInstallActivity activity ) {
            mWeakReference=new WeakReference<GasInstallActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final GasInstallActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog.isShowing()&&activity.customDialog!=null){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType ==0){
                                activity.jsonArray =object.optJSONArray("data");
                                activity.onSaveData();
                            }else {
                                JSONObject jsonObject=object.optJSONObject("data");
                                String orderId=jsonObject.optString("id");
                                activity.onPublishSuccess(orderId);
                            }
                        }else {
                            activity.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onShowFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }
    private void onPublishSuccess(String orderId) {
        String pageUrl=UrlUtil.APP_PAY_SUCCESS_PAGE +"userId="+userId+"&event_code=gas_order_activity"
                +"&event_relate_id="+orderId;
        Intent success=new Intent(context,PaySuccessActivity.class);
        success.putExtra("url","");
        success.putExtra("pageUrl",pageUrl);
        success.putExtra("type","1");
        success.putExtra("navigation","报装通气");
        startActivity(success);
        finish();
    }
    private void onSaveData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name     =jsonObject.getString("name");
                String installType = jsonObject.getString("installType");
                String moduleType="1";
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("moduleType",moduleType);
                map.put("name", name);
                map.put("installType", installType);
                typeList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (typeList!=null&&typeList.size()>0){
            typeNum =typeList.get(0).get("installType");
            tvInstallType.setText(typeList.get(0).get("name"));
        }
    }
    private void onShowSuccess() {
        String navigation="报装通气";
        Intent success=new Intent(context,ServerSuccessActivity.class);
        success.putExtra("navigation",navigation);
        success.putExtra("boolean",true);
        startActivity(success);
        finish();
    }
    private void onShowFailure(String s) {
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
        setContentView(R.layout.activity_gas_install);
        context=this;
        if (getIntent()!=null){
            mPageName=getIntent().getStringExtra("name");
            if (TextUtils.isEmpty(mPageName)){
                mPageName="燃气通气";
            }
        }else {
            mPageName="燃气通气";
        }
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
        intData();
        iniEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantUtil.REQUEST_CODE_TWO:
                if(data!=null){
                    if (resultCode==2){
                        String addressName=data.getStringExtra("addressname");
                        customerNo=data.getStringExtra("customerNo");
                        houseId=data.getStringExtra("houseId");
                        tvAddress.setText(addressName);
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
        btnVerify =(Button)findViewById(R.id.id_btn_verify);
        layoutType =findViewById(R.id.id_install_layout);
        tvAddress = findViewById(R.id.id_select_address);
        tvInstallType =(TextView) findViewById(R.id.id_install_type);
        etInstallTopic =(EditText)findViewById(R.id.id_install_topic);
        etDetail =(EditText)findViewById(R.id.id_et_install_detail);
        btnVerify.setEnabled(false);
    }
    private void intData() {
        customDialog.show();
        requestType =0;
        String validateURL = UrlUtil.INSTALL_TYPE_URL;
        OkHttpRequestUtil.getInstance(context).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private void iniEvent() {
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        tvAddress.addTextChangedListener(myTextWatcher);
        etInstallTopic.addTextChangedListener(myTextWatcher);
        etDetail.addTextChangedListener(myTextWatcher);
        btnSelectAddress.setOnClickListener(this);
        layoutType.setOnClickListener(this);
        tvInstallType.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(tvAddress.getText().toString()) || TextUtils.isEmpty(etInstallTopic.getText().toString())||
                    TextUtils.isEmpty(etDetail.getText().toString())) {
                btnVerify.setEnabled(false);
            } else {
                btnVerify.setEnabled(true);
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
            case R.id.id_btn_selectaddress:
                selectAddress();
                break;
            case R.id.id_install_layout:
                installPicker();
                break;
            case R.id.id_install_type:
                installPicker();
                break;
            case R.id.id_btn_verify:
                if (RegularExpressionUtil.isPhone(etPhone.getText().toString())){
                    verifyInstall();
                }else {
                    onShowFailure("手机号格式不确正");
                }
                break;
            default:
                break;
        }
    }
    private void selectAddress() {
        Intent select=new Intent(context,SelectCustomerNo.class);
        startActivityForResult(select,ConstantUtil.REQUEST_CODE_TWO);
    }
    private void installPicker() {
        new SelectDialog(this,typeList,mPosition).builder()
                .setTitleText("选择报装类型")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetItemClickListener(new SelectDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        mPosition=position;
                        typeNum =typeList.get(position).get("installType");
                        String openType=typeList.get(position).get("name");
                        tvInstallType.setText(openType);
                    }
                })
                .show();

    }
    private void verifyInstall() {
        String mType= tvInstallType.getText().toString().trim();
        String mTitle= etInstallTopic.getText().toString().trim();
        String description= etDetail.getText().toString().trim();
        String mAddress=tvAddress.getText().toString().trim();
        String phone= etPhone.getText().toString().trim();
        customDialog.show();
        requestType =1;
        String type="3";
        String validateURL = UrlUtil.INSTALL_SERVER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title",mTitle);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("houseId",houseId);
        textParams.put("customerNo",customerNo);
        textParams.put("phone",phone);
        textParams.put("address",mAddress);
        textParams.put("installType", typeNum);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);

        //OkHttpRequestUtil.getInstance(context).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog.isShowing()&&customDialog!=null){
            customDialog.dismiss();
        }
    }
}
