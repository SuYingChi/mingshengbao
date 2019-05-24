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

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.EnsureAddress;
import com.msht.minshengbao.custom.Dialog.EnsureDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.CustomToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/7/2  
 */
public class AddCustomerNoActivity extends BaseActivity implements View.OnClickListener {
    private String validateURL = UrlUtil.HouseSearch_Url;
    private Button addAddress;
    private EditText etCustomerNo;
    private String userId,password;
    private String customerNo;
    private int    requestCode=0;
    private int    enterType=0;
    private CustomDialog customDialog;
    private AddCustomerNoActivity mActivity;
    private void showDialogs(String address) {
        if (mActivity!=null&&!mActivity.isFinishing()){
            new EnsureDialog(context).builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setTitleText("请确认燃气用户")
                    .setContentOneText(customerNo)
                    .setContentTwoText(address)
                    .setOnPositiveClickListener(new EnsureDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCode=1;
                            addHouseAddress();
                        }
                    }).show();
            /*final EnsureAddress ensureAddress=new EnsureAddress(context);
            ensureAddress.setAddressText(address);
            ensureAddress.setCustomerText(customerNo);
            ensureAddress.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ensureAddress.dismiss();
                }
            });
            ensureAddress.setOnpositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ensureAddress.dismiss();
                    requestCode=1;
                    addHouseAddress();
                }
            });
            ensureAddress.show();*/
        }
    }

    private void onErrorTipDialog(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1TextColor(0xfff96331)
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
        setContentView(R.layout.activity_add_customer_no);
        context=this;
        mActivity=this;
        setCommonHeader("用户号快速添加");
        Intent data=getIntent();
        if (data!=null){
            enterType=data.getIntExtra("enterType",0);
        }
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initEvent();
    }
    private void initFindViewId() {
        addAddress =(Button)findViewById(R.id.id_btn_add_address);
        etCustomerNo =(EditText)findViewById(R.id.id_customerNo);
        addAddress.setEnabled(false);
        addAddress.setBackgroundResource(R.drawable.shape_gray_corner_button);
    }
    private void initEvent() {
        etCustomerNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etCustomerNo.getText().toString())){
                    addAddress.setEnabled(false);
                    addAddress.setBackgroundResource(R.drawable.shape_gray_corner_button);
                }else {
                    addAddress.setEnabled(true);
                    addAddress.setBackgroundResource(R.drawable.selector_touch_button_change);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        addAddress.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_add_address:
                String txt= etCustomerNo.getText().toString().trim();
                requestCode=0;
                customerNo= txt;
                if (enterType==1||enterType==2){
                    onCheckTable();
                }else {
                    addHouseAddress();
                }
                break;
            default:
                break;
        }
    }

    private void onCheckTable() {
        String customerNo=etCustomerNo.getText().toString().trim();
        customDialog.show();
        String validateURL=UrlUtil.GET_TABLE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onReceiveTableData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onReceiveTableData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONArray array=object.optJSONArray("data");
                if (array!=null&&array.length()!=0){
                    JSONObject json = array.optJSONObject(0);
                    int  tableType = json.optInt("lx");
                    switch (tableType){
                        case 11:
                            onOrdinaryTableExpense();
                            break;
                        case 17:
                            onInternetTable();
                            break;
                        case 12:
                            onOtherTable();
                            break;
                        default:
                            break;
                    }
                }else {
                    onErrorTipDialog("该用户号无表具");
                }
            }else {
                onErrorTipDialog( error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void onOtherTable() {
        addHouseAddress();
    }
    private void onInternetTable() {
        if (enterType==1){
            new PromptDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("该用户号所属的表类型为物联网表，添加后将不会在普表里出现")
                    .setButton1("取消", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("继续添加", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            addHouseAddress();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            addHouseAddress();
        }
    }
    private void onOrdinaryTableExpense() {
        if (enterType==2){
            new PromptDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("该用户号所属的表类型为普表，添加后将不会在物联网表里出现")
                    .setButton1("取消", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("继续添加", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            addHouseAddress();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            addHouseAddress();
        }
    }
    private void addHouseAddress(){
        customDialog.show();
        if (requestCode==0){
            //validateURL = UrlUtil.HouseSearch_Url;
            validateURL=UrlUtil.NEW_HOUSE_SEARCH_URL;
        }else if (requestCode==1){
            //validateURL =UrlUtil.ADD_ADDRESS_URL;
            validateURL=UrlUtil.NEW_ADD_CUSTOMER_NO_URL;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
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
                        showDialogs(address);
                    }
                }else if (requestCode==1){
                    setResult(1);
                    CustomToast.showSuccessDialog("添加地址成功");
                    finish();
                }
            }else {
                CustomToast.showWarningLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
