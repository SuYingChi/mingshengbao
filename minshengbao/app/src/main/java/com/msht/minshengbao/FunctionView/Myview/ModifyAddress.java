package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.Public.MoveSelectAddress;
import com.msht.minshengbao.FunctionView.Public.SelectCity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModifyAddress extends BaseActivity implements View.OnClickListener {
    private View Rcity,Raddress;
    private ImageView right_img;
    private TextView  tv_city;
    private TextView  tv_address;
    private EditText  et_doorplate;
    private Button    btn_ensure;
    private String    userId,id;
    private String    password;
    private String    mCity,cityId;
    private String    mAddress;
    private String    longitude;
    private String    latitude;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private int          requestCode=0;
    private CustomDialog customDialog;
    private Context context;
    private static final int REQUESTCOODE=1;//城市标志
    private static final int AddressCode=2;
    Handler requestHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requestCode==0) {
                                setResult(0x001);
                                finish();
                            }else if (requestCode==1){
                                setResult(0x002);
                                finish();
                            }
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
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
    private void displayDialog(String string) {
        new PromptDialog.Builder(this)
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
        setCommonHeader("修改地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        String detailAddr=data.getStringExtra("address");
        id=data.getStringExtra("id");
        latitude=data.getStringExtra("latitude");
        longitude=data.getStringExtra("longitude");
        cityId=data.getStringExtra("city_id");
        initView();
        initEvent();
        tv_address.setText(detailAddr);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUESTCOODE:
                if (data!=null){
                    if (resultCode==2){
                        mCity=data.getStringExtra("mCity");
                        cityId=data.getStringExtra("Id");
                        tv_city.setText(mCity);
                    }
                }
                break;
            case AddressCode:
                if (data!=null){
                    if (resultCode==1){
                        String Address=data.getStringExtra("addressInfo");
                        longitude=data.getStringExtra("longitude");
                        latitude=data.getStringExtra("latitude");
                        tv_address.setText(Address);
                    }
                }
                break;
        }
    }
    private void initView() {
        right_img=(ImageView)findViewById(R.id.id_right_img);
        right_img.setVisibility(View.VISIBLE);
        right_img.setImageResource(R.drawable.delete2x);
        Rcity=findViewById(R.id.id_re_city);
        Raddress=findViewById(R.id.id_re_address);
        tv_city=(TextView)findViewById(R.id.id_tv_city);
        tv_address=(TextView)findViewById(R.id.id_address);
        et_doorplate=(EditText)findViewById(R.id.id_et_doorplate);
        btn_ensure=(Button)findViewById(R.id.id_btn_ensure);
    }
    private void initEvent() {
        right_img.setOnClickListener(this);
        Rcity.setOnClickListener(this);
        Raddress.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_ensure:
                String address=tv_address.getText().toString().trim();
                String doorplate=et_doorplate.getText().toString().trim();
                mAddress=address+doorplate;
                if (matchcity(tv_city.getText().toString())&&matchaddress(tv_address.getText().toString())){
                    requestCode=0;
                    requestService();
                }
                break;
            case R.id.id_re_city:
                Intent city=new Intent(context, SelectCity.class);
                startActivityForResult(city,REQUESTCOODE);
                break;
            case R.id.id_re_address:
                if (TextUtils.isEmpty(tv_city.getText().toString())){
                    Toast.makeText(context, "请选择城市", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    Intent intent=new Intent(context, MoveSelectAddress.class);
                    intent.putExtra("city_name",mCity);
                    startActivityForResult(intent,AddressCode);
                }
                break;
            case R.id.id_right_img:
                delectAddres();
                break;
            default:
                break;
        }
    }
    private void delectAddres() {
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
                        requestCode=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private boolean matchaddress(String s) {
        if (TextUtils.isEmpty(s)){
            Toast.makeText(context, "请填写您的地址", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }else {
            return true;
        }
    }
    private boolean matchcity(String s) {
        if (TextUtils.isEmpty(s)){
            Toast.makeText(context, "请选择城市", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }else {
            return true;
        }
    }
    private void requestService() {
        customDialog.show();
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        if (requestCode==0){
            validateURL = UrlUtil.ModifyAddress_Url;
            textParams.put("city_id",cityId);
            textParams.put("address",mAddress);
            textParams.put("longitude",longitude);
            textParams.put("latitude",latitude);
        }else if (requestCode==1){
            validateURL = UrlUtil.DelectAddress_Url;
        }
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
}
