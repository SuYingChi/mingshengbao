package com.msht.minshengbao.functionActivity.myActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.EnsureDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.functionActivity.gasService.AddCustomerNoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class CorrelationGasCustomerNoActivity extends BaseActivity {
    private Button btnCorrelation;
    private EditText etCustomerNo;
    private String addressId;
    private String userId,password;
    private String customerNo;
    private int    requestCode=0;
    private CustomDialog customDialog;
    private CorrelationGasCustomerNoActivity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correlation_gas_customer_no);
        context=this;
        mActivity=this;
        setCommonHeader("关联燃气号");
        Intent data=getIntent();
        if (data!=null){
            addressId=data.getStringExtra("addressId");
        }
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
    }
    private void initFindViewId() {
        btnCorrelation =(Button)findViewById(R.id.id_btn_add_address);
        etCustomerNo =(EditText)findViewById(R.id.id_customerNo);
        etCustomerNo.addTextChangedListener(new MyTextWatcher());
        btnCorrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerNo=etCustomerNo.getText().toString().trim();
                onCorrelation();
            }
        });
    }
    private void onCorrelation() {
        customDialog.show();
        String validateURL="";
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        if (requestCode==0){
            validateURL=UrlUtil.NEW_HOUSE_SEARCH_URL;
        }else if (requestCode==1){
            validateURL=UrlUtil.ADDRESS_BIND_GAS_CUSTOMER_NO_URL;
            textParams.put("id",addressId);
        }
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
                if (requestCode==0){
                    JSONObject jsonObject = object.optJSONObject("data");
                    if (jsonObject!=null){
                        String address = jsonObject.optString("address");
                        onEnsureDialog(address);
                    }
                }else if (requestCode==1){
                    setResult(1);
                    CustomToast.showSuccessDialog("绑定成功");
                    finish();
                }
            }else {
                CustomToast.showWarningLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void onEnsureDialog(String address) {
        if (mActivity!=null&&!mActivity.isFinishing()) {
            new EnsureDialog(context).builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setTitleText("请确认燃气用户")
                    .setContentOneText(customerNo)
                    .setContentTwoText(address)
                    .setOnPositiveClickListener(new EnsureDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCode = 1;
                            onCorrelation();
                        }
                    }).show();
        }
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(etCustomerNo.getText().toString())){
                btnCorrelation.setEnabled(true);
            }else {
                btnCorrelation.setEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
