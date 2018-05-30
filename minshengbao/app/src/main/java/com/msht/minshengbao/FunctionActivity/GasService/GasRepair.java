package com.msht.minshengbao.FunctionActivity.GasService;

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
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GasRepair extends BaseActivity implements View.OnClickListener {
    private EditText Eselectaddr,Erepairtopic,Edetail;
    private EditText et_phone;
    private Button btnselectAddr;
    private Button comfirm;//确认
    private String customerNo,houseId;
    private String userId;
    private String password;
    private String phone;
    private String Title,description,Addressname;
    private CustomDialog customDialog;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static final int REQUESTCOODE=7;//地址标志;
    Handler repairHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            showsuccess();
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    displayDialog(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void showsuccess() {
        String navigation="报修服务";
        Intent success=new Intent(context,ServerSuccess.class);
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
            case REQUESTCOODE:
                if(data!=null){
                    if (resultCode==2){
                        String addressname=data.getStringExtra("addressname");
                        houseId=data.getStringExtra("houseId");
                        customerNo=data.getStringExtra("customerNo");
                        Eselectaddr.setText(addressname);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView() {
        et_phone=(EditText)findViewById(R.id.id_et_phone);
        btnselectAddr=(Button)findViewById(R.id.id_btn_selectaddress);
        comfirm=(Button)findViewById(R.id.id_btn_comfirm);
        Eselectaddr=(EditText)findViewById(R.id.id_select_address);
        Erepairtopic=(EditText)findViewById(R.id.id_repair_topic);
        Edetail=(EditText)findViewById(R.id.id_repair_detail);
        Eselectaddr.setInputType(InputType.TYPE_NULL);
        comfirm.setEnabled(false);
    }
    private void iniEvent() {
        btnselectAddr.setOnClickListener(this);
        Eselectaddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eselectaddr.getText().toString()) || TextUtils.isEmpty(Erepairtopic.getText().toString())||
                        TextUtils.isEmpty(Edetail.getText().toString())
                        ) {
                    comfirm.setEnabled(false);
                } else {
                    comfirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Erepairtopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eselectaddr.getText().toString()) || TextUtils.isEmpty(Erepairtopic.getText().toString()) ||
                        TextUtils.isEmpty(Edetail.getText().toString())) {
                    comfirm.setEnabled(false);
                } else {
                    comfirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Edetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eselectaddr.getText().toString()) || TextUtils.isEmpty(Erepairtopic.getText().toString()) ||
                        TextUtils.isEmpty(Edetail.getText().toString())) {
                    comfirm.setEnabled(false);
                } else {
                    comfirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        comfirm.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_selectaddress:
                selectaddr();
                break;
            case R.id.id_btn_comfirm:
                Verifysend();
            default:
                break;
        }
    }
    private void selectaddr() {
        Intent selete=new Intent(context,SelectCustomerno.class);
        startActivityForResult(selete,REQUESTCOODE);
    }
    private void Verifysend() {
        Addressname=Eselectaddr.getText().toString().trim();
        Title = Erepairtopic.getText().toString().trim();
        description = Edetail.getText().toString().trim();
        phone=et_phone.getText().toString().trim();
        if (matchtitleMsg(Title,Addressname,phone)) {
            customDialog.show();
            requestSevice();
        }
    }
    private boolean matchtitleMsg(String title, String addressname,String phones) {
        if(title.equals("")) {
            displayDialog("报修主题不能为空");
            return false;
        }else if(addressname.equals("")) {
            displayDialog("地址不能为空");
            return false;
        }else if (!isphone(phones)){
            displayDialog("手机号格式不正确");
            return false;
        }
        return true;
    }
    private boolean isphone(String phone) {
        Pattern pattern=Pattern.compile("1[0-9]{10}");
        Matcher matcher=pattern.matcher(phone);
        return matcher.matches();
    }
    private void displayDialog(String s) {
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
    private void requestSevice() {
        String type="5";
        String validateURL = UrlUtil.InstallServer_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title",Title);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("houseId",houseId);
        textParams.put("customerNo",customerNo);
        textParams.put("phone",phone);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                repairHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                repairHandler.sendMessage(msg);
            }
        });
    }
}
