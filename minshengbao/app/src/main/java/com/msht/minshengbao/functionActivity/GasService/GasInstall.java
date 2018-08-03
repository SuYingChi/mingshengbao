package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.adapter.repairAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectTable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GasInstall extends BaseActivity implements View.OnClickListener {
    private Button btnselectaddr,btnverify;
    private TextView Einstalltype;
    private RelativeLayout typelayout;
    private EditText Einstalltopic;
    private EditText Edetail,EAddress;
    private EditText et_phone;
    private static final int REQUESTCOODE=2;//地址标志
    private repairAdapter adapter;
    private ArrayList<HashMap<String, String>> typeList = new ArrayList<HashMap<String, String>>();
    private String TypeNum;//安装类型
    private String customerNo;//客户号
    private String houseId;//房屋ID
    private String userId;
    private String password;
    private String phone;
    private int requesttype=0;
    private int pos=-1;
    private JSONArray jsonArray;   //数据解析
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    Handler installHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (requesttype==0){
                            jsonArray =object.optJSONArray("data");
                        }
                        if(Results.equals("success")) {
                            if (requesttype==0){
                                savadata();
                            }else {
                                showsuccess();
                            }
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void savadata() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name     =jsonObject.getString("name");
                String installType = jsonObject.getString("installType");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("installType", installType);
                typeList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        TypeNum=typeList.get(0).get("installType");    //默认报装类型
        Einstalltype.setText(typeList.get(0).get("name"));
    }
    private void showsuccess() {
        String navigation="报装通气";
        Intent success=new Intent(context,ServerSuccess.class);
        success.putExtra("navigation",navigation);
        success.putExtra("boolean",true);
        startActivity(success);
        finish();
    }
    private void showfaiture(String s) {
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
        setCommonHeader("报装通气");
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
            case REQUESTCOODE://获取昵称设置返回数据
                if(data!=null){
                    if (resultCode==2){
                        String addressname=data.getStringExtra("addressname");
                        customerNo=data.getStringExtra("customerNo");
                        houseId=data.getStringExtra("houseId");
                        EAddress.setText(addressname);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView() {
        et_phone=(EditText)findViewById(R.id.id_et_phone);
        btnselectaddr=(Button)findViewById(R.id.id_btn_selectaddress);
        btnverify=(Button)findViewById(R.id.id_btn_verify);//确认按钮
        typelayout=(RelativeLayout)findViewById(R.id.id_install_layout);
        EAddress=(EditText)findViewById(R.id.id_select_address);
        Einstalltype=(TextView) findViewById(R.id.id_install_type);
        Einstalltopic=(EditText)findViewById(R.id.id_install_topic);
        Edetail=(EditText)findViewById(R.id.id_et_install_detail);
        btnverify.setEnabled(false);
    }
    private void intData() {
        customDialog.show();
        requesttype=0;
        String validateURL = UrlUtil.InstallType_Url;
        SendrequestUtil.executeGet(validateURL, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                installHandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message message = new Message();
                message.what = FAILURE;
                message.obj =fail;
                installHandler.sendMessage(message);
            }
        });
    }
    private void iniEvent() {
        EAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( EAddress.getText().toString()) || TextUtils.isEmpty(Einstalltopic.getText().toString())||
                        TextUtils.isEmpty(Edetail.getText().toString())) {
                    btnverify.setEnabled(false);
                } else {
                    btnverify.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        Einstalltopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( EAddress.getText().toString()) || TextUtils.isEmpty(Einstalltopic.getText().toString())||
                        TextUtils.isEmpty(Edetail.getText().toString())) {
                    btnverify.setEnabled(false);
                } else {
                    btnverify.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        Edetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( EAddress.getText().toString()) || TextUtils.isEmpty(Einstalltopic.getText().toString())||
                        TextUtils.isEmpty(Edetail.getText().toString())) {
                    btnverify.setEnabled(false);
                } else {
                    btnverify.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnselectaddr.setOnClickListener(this);
        typelayout.setOnClickListener(this);
        Einstalltype.setOnClickListener(this);
        btnverify.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_selectaddress:
                selectaddr();
                break;
            case R.id.id_install_layout:
                installpicker();
                break;
            case R.id.id_install_type:
                installpicker();
                break;
            case R.id.id_btn_verify:
                if (isphone(et_phone.getText().toString())){
                    verifyinstall();
                }else {
                    showfaiture("手机号格式不确正");
                }
                break;
            default:
                break;
        }
    }
    private void selectaddr() {
        Intent selete=new Intent(context,SelectCustomerno.class);
        startActivityForResult(selete,REQUESTCOODE);
    }
    private void installpicker() {
        final SelectTable selectTable=new SelectTable(context);
        final TextView tv_title=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tv_title.setText("选择报装类型");
        adapter=new repairAdapter(this,typeList,pos);
        mListView.setAdapter(adapter);
        selectTable.show();
        selectTable.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTable.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                TypeNum=typeList.get(position).get("installType");
                String Opentype=typeList.get(position).get("name");
                Einstalltype.setText(Opentype);
                selectTable.dismiss();
            }
        });

    }
    private void verifyinstall() {
        String mType=Einstalltype.getText().toString().trim();
        String mTitle= Einstalltopic.getText().toString().trim();
        String description= Edetail.getText().toString().trim();
        String mAddress=EAddress.getText().toString().trim();
        String phone=et_phone.getText().toString().trim();
        customDialog.show();
        requesttype=1;
        String type="3";
        String validateURL = UrlUtil.InstallServer_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title",mTitle);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("houseId",houseId);
        textParams.put("customerNo",customerNo);
        textParams.put("phone",phone);
        textParams.put("address",mAddress);
        textParams.put("installType",TypeNum);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                installHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                installHandler.sendMessage(msg);
            }
        });
    }
    private boolean isphone(String phone) {
        Pattern pattern=Pattern.compile("1[0-9]{10}");
        Matcher matcher=pattern.matcher(phone);
        return matcher.matches();
    }
}
