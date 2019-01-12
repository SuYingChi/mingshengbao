package com.msht.minshengbao.functionActivity.GasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AndroidWorkaround;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/8  
 */
public class GasInternetTableRechargeActivity extends BaseActivity implements View.OnClickListener {
    private EditText etGasNum;
    private Button   btnRecharge;
    private String   mAddress;
    private String   mCustomerNo;
    private String   mLastGasNum;
    private String   mLastTime;
    private String   meterNo;
    private String   mGasNum;
    private String   mAmount="0";
    private String   userId;
    private String   password;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<GasInternetTableRechargeActivity> mWeakReference;
        public RequestHandler(GasInternetTableRechargeActivity activity) {
            mWeakReference = new WeakReference<GasInternetTableRechargeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasInternetTableRechargeActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error=object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveExpanseData(jsonObject);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
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
    private void onReceiveExpanseData(JSONObject jsonObject) {
        String totalAmount=jsonObject.optString("totalAmount");
        String gasCapacity=jsonObject.optString("gasCapacity");
        String discountAmount=jsonObject.optString("discountAmount");
        String payId=jsonObject.optString("payId");
        String customerNo=jsonObject.optString("customerNo");
        String payableAmount=jsonObject.optString("payableAmount");
        Intent intent=new Intent(context,GasInternetTablePayFeeActivity.class);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("gasCapacity",gasCapacity);
        intent.putExtra("discountAmount",discountAmount);
        intent.putExtra("payId",payId);
        intent.putExtra("customerNo",customerNo);
        intent.putExtra("payableAmount",payableAmount);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_table_recharge);
        //适配华为手机虚拟键遮挡tab的问题
        context=this;
        setCommonHeader("物联网表充值");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        if (data!=null){
            mAddress=data.getStringExtra("address");
            mCustomerNo=data.getStringExtra("customerNo");
            mLastGasNum=data.getStringExtra("lastRechargeAmount");
            mLastTime=data.getStringExtra("lastRechargeTime");
            meterNo=data.getStringExtra("meterNo");
        }
        initFindViewId();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE1:
                if (resultCode==2){
                    finish();
                }
                break;
                default:
                    break;
        }
    }
    private void initFindViewId() {
        etGasNum=(EditText)findViewById(R.id.id_et_gasNum);
        TextView tvLastTime=(TextView)findViewById(R.id.id_last_time);
        TextView tvLastGasNum=(TextView)findViewById(R.id.id_last_gasNum);
        TextView tvCustomerNo=(TextView)findViewById(R.id.id_customerNo);
        TextView tvAddress=(TextView)findViewById(R.id.id_tv_address);
        btnRecharge=(Button)findViewById(R.id.id_btn_recharge);
        tvCustomerNo.setText(mCustomerNo);
        tvAddress.setText(mAddress);
        tvLastGasNum.setText(mLastGasNum);
        tvLastTime.setText(mLastTime);
        etGasNum.addTextChangedListener(new MyTextWatcher());
        findViewById(R.id.id_recharge_record).setOnClickListener(this);
        findViewById(R.id.id_price_explain).setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        btnRecharge.setEnabled(false);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_recharge_record:
                onRechargeRecord();
                break;
            case R.id.id_price_explain:
                onGasPrice();
                break;
            case R.id.id_btn_recharge:
                onGasFeeRecharge();
                break;
                default:
                    break;
        }
    }
    private void onRechargeRecord() {
        Intent name=new Intent(context, GasPayRecordActivity.class);
        name.putExtra("customerNo",mCustomerNo);
        name.putExtra("address",mAddress);
        name.putExtra("urlType","2");
        startActivity(name);
    }
    private void onGasFeeRecharge() {
        String gasCapacity=etGasNum.getText().toString().trim();
        customDialog.show();
        String validateURL=UrlUtil.INTERNET_TABLE_PAYMENT;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("gasCapacity",gasCapacity);
        textParams.put("meterNo",meterNo);
        String M=validateURL+"?"+textParams.toString();
        Log.d("textParams=",validateURL+"?"+textParams.toString());
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onGasPrice() {
        String url= UrlUtil.Gasprice_Url;
        Intent price=new Intent(context,HtmlPageActivity.class);
        price.putExtra("navigate","气价说明");
        price.putExtra("url",url);
        startActivity(price);
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etGasNum.getText().toString())){
                btnRecharge.setEnabled(false);
            }else {
                btnRecharge.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
