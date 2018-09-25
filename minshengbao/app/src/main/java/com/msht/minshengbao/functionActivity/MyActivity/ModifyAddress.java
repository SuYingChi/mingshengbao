package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.Public.MoveSelectAddress;
import com.msht.minshengbao.functionActivity.Public.SelectCityActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyAddress extends BaseActivity implements View.OnClickListener {
    private View      layoutCity, layoutAddress;
    private ImageView rightImg;
    private TextView  tvCity;
    private TextView  tvAddress;
    private EditText  etDoorplate;
    private EditText  etName;
    private EditText  etPhone;
    private String    userId,id;
    private String    password;
    private String    mCity,cityId;
    private String    mAddress;
    private String    mName;
    private String    mPhone;
    private String    longitude;
    private String    latitude;
    private int          requestCode=0;
    private CustomDialog customDialog;
    private Context context;
    /**
     * 城市标志
     */
    private static final int REQUEST_CODE =1;
    private static final int AddressCode=2;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ModifyAddress>mWeakReference;
        public RequestHandler(ModifyAddress activity) {
            mWeakReference=new WeakReference<ModifyAddress>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ModifyAddress activity =mWeakReference.get();
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
        setCommonHeader("修改地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        mAddress=data.getStringExtra("address");
        id=data.getStringExtra("id");
        latitude=data.getStringExtra("latitude");
        longitude=data.getStringExtra("longitude");
        cityId=data.getStringExtra("city_id");
        mCity=data.getStringExtra("city_name");
        mName=data.getStringExtra("name");
        mPhone=data.getStringExtra("phone");
        initView();
        initEvent();
        tvCity.setText(mCity);
        tvAddress.setText(mAddress);
        etName.setText(mName);
        etPhone.setText(mPhone);
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
            case AddressCode:
                if (data!=null){
                    if (resultCode==1){
                        String address=data.getStringExtra("addressInfo");
                        longitude=data.getStringExtra("longitude");
                        latitude=data.getStringExtra("latitude");
                        tvAddress.setText(address);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void initView() {
        rightImg =(ImageView)findViewById(R.id.id_right_img);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.delete2x);
        layoutCity =findViewById(R.id.id_re_city);
        layoutAddress =findViewById(R.id.id_re_address);
        tvCity =(TextView)findViewById(R.id.id_tv_city);
        tvAddress =(TextView)findViewById(R.id.id_address);
        etDoorplate =(EditText)findViewById(R.id.id_et_doorplate);
        etName =(EditText)findViewById(R.id.id_et_name);
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        findViewById(R.id.id_re_newaddress).setOnClickListener(this);
    }
    private void initEvent() {
        rightImg.setOnClickListener(this);
        layoutCity.setOnClickListener(this);
        layoutAddress.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_newaddress:
                String address= tvAddress.getText().toString().trim();
                String doorplate= etDoorplate.getText().toString().trim();
                mAddress=address+doorplate;
                mName= etName.getText().toString().trim();
                mPhone= etPhone.getText().toString().trim();
                if (matchcity(tvCity.getText().toString())&&matchAddress(tvAddress.getText().toString())
                        &&matchName(mName)&&matchPhone(mPhone)&&isPhone(mPhone)){
                    requestCode=0;
                    requestService();
                }
                break;
            case R.id.id_re_city:
                Intent city=new Intent(context, SelectCityActivity.class);
                startActivityForResult(city, REQUEST_CODE);
                break;
            case R.id.id_re_address:
                if (TextUtils.isEmpty(tvCity.getText().toString())){
                    ToastUtil.ToastText(context,"请选择城市");
                }else {
                    Intent intent=new Intent(context, MoveSelectAddress.class);
                    intent.putExtra("city_name",mCity);
                    startActivityForResult(intent,AddressCode);
                }
                break;
            case R.id.id_right_img:
                deleteAddress();
                break;
            default:
                break;
        }
    }
    private void deleteAddress() {
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
    private boolean matchPhone(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtil.ToastText(context,"请填写您的联系电话");
            return false;
        }else {
            return true;
        }
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
    private boolean matchcity(String s) {
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
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        if (requestCode==0){
            validateURL = UrlUtil.NewModifyAddress_Url;
            textParams.put("city_id",cityId);
            textParams.put("address",mAddress);
            /*textParams.put("longitude",longitude);
            textParams.put("latitude",latitude);*/
            textParams.put("name",mName);
            textParams.put("phone",mPhone);
        }else if (requestCode==1){
            validateURL = UrlUtil.DelectAddress_Url;
        }
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
