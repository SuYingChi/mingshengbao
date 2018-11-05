package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    private View      layoutCity;
    private TextView  tvCity;
    private EditText  etAddress;
    private EditText  etName;
    private EditText  etPhone;
    private String    userId;
    private String    password;
    private String    mCity,cityId;
    private String    mAddress;
    private String    mName;
    private String    mPhone;
    private String    longitude;
    private String    latitude;
    private int       requestCode=0;
    private CustomDialog customDialog;
    /**
     * 城市标志
     */
    private static final int REQUEST_CODE =1;
    private static final int ADDRESS_CODE =2;
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<AddAddressActivity> mWeakReference;
        private   RequestHandler(AddAddressActivity activity) {
            mWeakReference = new WeakReference<AddAddressActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final AddAddressActivity activity=mWeakReference.get();
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
                            activity.onFailure(error);
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
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
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
        setContentView(R.layout.activity_add_address);
        context=this;
        setCommonHeader("添加地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
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
            case ADDRESS_CODE:
                if (data!=null){
                    if (resultCode==1){
                        String addressInfo=data.getStringExtra("addressInfo");
                        String title=data.getStringExtra("title");
                        longitude=data.getStringExtra("longitude");
                        latitude=data.getStringExtra("latitude");
                    }
                }
                break;
            default:
                    break;
        }
    }
    private void initView() {
        findViewById(R.id.id_re_city).setOnClickListener(this);
        tvCity =(TextView)findViewById(R.id.id_tv_city);
        etName =(EditText)findViewById(R.id.id_et_name);
        etPhone =(EditText)findViewById(R.id.id_et_phone);
        etAddress =(EditText)findViewById(R.id.id_et_address);
        findViewById(R.id.id_re_newaddress).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_newaddress:
                mAddress= etAddress.getText().toString().trim();
                mName= etName.getText().toString().trim();
                mPhone= etPhone.getText().toString().trim();
                if (matchCity(tvCity.getText().toString())&& matchAddress(etAddress.getText().toString())
                        &&matchName(mName)&& matchPhone(mPhone)&&isPhone(mPhone)){
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
                    startActivityForResult(intent, ADDRESS_CODE);
                }
                break;
            default:
                break;
        }
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
    private boolean matchCity(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtil.ToastText(context,"请选择城市");
            return false;
        }else {
            return true;
        }
    }

    /**
     * 验证是否为11位电话号码
     * @param phoneNo  电话号码
     * @return true or false
     */
    private boolean isPhone(String phoneNo) {
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
        requestCode=0;
        String validateURL = UrlUtil.NewAddAddress_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("city_id",cityId);
        textParams.put("address",mAddress);
        textParams.put("name",mName);
        textParams.put("phone",mPhone);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }


    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
