package com.msht.minshengbao.FunctionActivity.GasService;

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
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.EnsureAddress;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCustomerNo extends BaseActivity implements View.OnClickListener {
    private String validateURL = UrlUtil.HouseSearch_Url;
    private Button Addaddress;
    private EditText EcustomerNo;
    private String userId,password;
    private String customerNo;
    private String Results;
    private String  Error;
    private String address;
    private int   requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        Results=object.optString("result");
                        Error = object.optString("error");
                        if(Results.equals("success")){
                            if (requestCode==0){
                                JSONObject ObjectInfo = object.optJSONObject("data");
                                String addr = ObjectInfo.optString("address");
                                showDialogs(addr);
                            }else if (requestCode==1){
                                success();
                            }
                        }else {
                            showlistview(Error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void showDialogs(String addr) {
        final EnsureAddress ensureAddress=new EnsureAddress(this);
        ensureAddress.setAddressText(addr);
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
                AddHouseaddress();
            }
        });
        ensureAddress.show();
    }
    private void success() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("添加地址成功")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        Intent ARRIVE=new Intent();
                        setResult(1);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void showlistview(String error) {
        new PromptDialog.Builder(this)
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
        setCommonHeader("添加客户号");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initfindViewByid();
        initEvent();
    }
    private void initfindViewByid() {
        Addaddress=(Button)findViewById(R.id.id_btn_add_address);
        EcustomerNo=(EditText)findViewById(R.id.id_customerNo);
        Addaddress.setEnabled(false);
        Addaddress.setBackgroundResource(R.drawable.shape_gray_corner_button);
    }
    private void initEvent() {
        EcustomerNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(EcustomerNo.getText().toString())){
                    Addaddress.setEnabled(false);
                    Addaddress.setBackgroundResource(R.drawable.shape_gray_corner_button);
                }else {
                    Addaddress.setEnabled(true);
                    Addaddress.setBackgroundResource(R.drawable.selector_touch_button_change);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Addaddress.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_add_address:
                String txt= EcustomerNo.getText().toString().trim();
                if (matchertype(txt)){
                    requestCode=0;
                    customerNo= txt;
                    address="";
                    AddHouseaddress();
                }
                break;
            default:
                break;
        }
    }
    private void AddHouseaddress(){
        customDialog.show();
        if (requestCode==0){
            validateURL = UrlUtil.HouseSearch_Url;
        }else if (requestCode==1){
            validateURL =UrlUtil.ADD_ADDRESS_URL;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("customerNo",customerNo);
        textParams.put("address",address);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
    private boolean matchertype(String txt) {
        Pattern p= Pattern.compile("[0-9]*");
        Matcher matcher=p.matcher(txt);
        return matcher.matches();
    }
}
