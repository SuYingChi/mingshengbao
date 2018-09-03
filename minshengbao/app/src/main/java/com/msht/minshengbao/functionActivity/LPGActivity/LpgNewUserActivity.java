package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
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

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.functionActivity.Electricvehicle.ReplaceAddressActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.MySheetDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.AgreeTreatyActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 *  新用户
 * @author hong
 * @date 2018/6/27
 */
public class LpgNewUserActivity extends BaseActivity implements View.OnClickListener {
    private Button   btnSend;
    private Button   btnCode;
    private EditText etUserName;
    private EditText etPhoneNo;
    private EditText etCode;
    private TextView tvSex;
    private TextView tvAddress;
    private TextView tvAddressShort;
    private TextView tvElevator;
    private EditText etRidgepole;
    private EditText etFloor;
    private EditText etRoom;
    private String   isSex;
    private String   isElevator;
    private String   mAddressShort;
    private String   mAddress;
    private String   mLon,mLat;
    private String   mArea;
    private String   mCity;
    private TimeCount time;
    private int      requestCode=0;
    private static final String PAGE_NAME="新用户申请";
    private CustomDialog customDialog;
    private Context context;
    /**选择地址标志 **/
    private static final int SELECT_ADDRESS_CODE=1;
    /**选择性别标志 **/
    private static final int SELECT_SEX_CODE=2;
    /**选择电梯标志 **/
    private static final int SELECT_ELEVATOR_CODE=3;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<LpgNewUserActivity> mWeakReference;
        public RequestHandler(LpgNewUserActivity activity) {
            mWeakReference = new WeakReference<LpgNewUserActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgNewUserActivity activity=mWeakReference.get();
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
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                ToastUtil.ToastText(activity.context,"验证码已发送");
                            }else if (activity.requestCode==1){
                                activity.onBindingAccount();
                            }else if (activity.requestCode==2){
                                activity.displayDialog(error);
                            }else {
                                activity.displayDialog(error);
                            }
                        }else {
                            activity.requestCode=0;
                            activity.displayDialog(error);
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
    private void displayDialog(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (requestCode==2){
                            setResult(ConstantUtil.BIND_SUCCESS);
                            finish();
                        }
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_new_user);
        context=this;
        setCommonHeader(PAGE_NAME);
        customDialog=new CustomDialog(this, "正在加载");
        initFindViewId();
        time=new TimeCount(60000,1000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_ADDRESS_CODE:
                if (resultCode==SELECT_ADDRESS_CODE){
                    mLat=data.getStringExtra("lat");
                    mLon=data.getStringExtra("lon");
                    mArea=data.getStringExtra("area");
                    mCity=data.getStringExtra("city");
                    mAddress=data.getStringExtra("mAddress");
                    mAddressShort=data.getStringExtra("title");
                    tvAddress.setText(mAddress);
                }
                break;
            case SELECT_SEX_CODE:
                if (resultCode==SELECT_SEX_CODE){
                    isSex=data.getStringExtra("isSex");
                    String mSex=data.getStringExtra("mSex");
                    tvSex.setText(mSex);
                }
                break;
            case SELECT_ELEVATOR_CODE:
                if (resultCode==SELECT_ELEVATOR_CODE){
                    isElevator=data.getStringExtra("isElevator");
                    String mElevator=data.getStringExtra("mElevator");
                    tvElevator.setText(mElevator);
                }
                break;
            default:
                break;
        }
    }
    private void initFindViewId() {
        findViewById(R.id.id_select_layout).setOnClickListener(this);
        findViewById(R.id.id_select_sex).setOnClickListener(this);
        findViewById(R.id.id_select_elevator).setOnClickListener(this);
        findViewById(R.id.id_know_text).setOnClickListener(this);
        etPhoneNo=(EditText)findViewById(R.id.id_et_phone);
        etUserName=(EditText)findViewById(R.id.id_et_name);
        etFloor=(EditText)findViewById(R.id.id_et_floor);
        etRidgepole=(EditText)findViewById(R.id.id_et_ridgepole);
        etRoom=(EditText)findViewById(R.id.id_et_room);
        tvAddress=(TextView)findViewById(R.id.id_tv_address);
        tvSex=(TextView)findViewById(R.id.id_tv_sex);
        tvElevator=(TextView)findViewById(R.id.id_tv_elevator);
        btnSend=(Button)findViewById(R.id.id_btn_send);
        etCode=(EditText)findViewById(R.id.id_et_code);
        btnCode=(Button)findViewById(R.id.id_btn_code);
        btnSend.setEnabled(false);
        btnCode.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etUserName.addTextChangedListener(myTextWatcher);
        etPhoneNo.addTextChangedListener(myTextWatcher);
        etFloor.addTextChangedListener(myTextWatcher);
        tvAddress.addTextChangedListener(myTextWatcher);
        tvSex.addTextChangedListener(myTextWatcher);
        tvElevator.addTextChangedListener(myTextWatcher);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_select_layout:
                selectAddress();
                break;
            case R.id.id_select_sex:
                selectSex();
                break;
            case R.id.id_select_elevator:
                selectElevator();
                break;
            case R.id.id_btn_send:
                requestService();
                break;
            case R.id.id_know_text:
                startNewUserTreaty();
                break;
            case R.id.id_btn_code:
                onGetVerification();
                break;
            default:
                break;
        }
    }

    private void onGetVerification() {
        String mobile=etPhoneNo.getText().toString().trim();
        if (RegularExpressionUtil.isPhone(mobile)) {
            btnCode.setText("正在发送...");
            time.start();
            onCaptchaCode(mobile);
        }else {
            displayDialog("您输入电话号码不正确");
        }
    }
    private void onCaptchaCode(String mobile) {
        requestCode=0;
        String requestUrl= UrlUtil.LPG_GET_CAPTCHA_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("mobile",mobile);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    private void startNewUserTreaty() {
        String url=UrlUtil.LPG_OPEN_TREATY;
        Intent intent=new Intent(context, AgreeTreatyActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigation","居民用户办理须知");
        startActivity(intent);
    }
    private void selectElevator() {
        String[] mList=new String[]{"无电梯","有电梯"};
        String mTitle="是否带电梯";
        new MySheetDialog(this,mTitle,mList).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetItemClickListener(new MySheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(String item, String string) {
                        tvElevator.setText(string);
                        isElevator=item;
                    }
                }).show();
    }
    private void selectSex() {
        String[] mList=new String[]{"男","女",};
        String mTitle="请选择性别";
        new MySheetDialog(this,mTitle,mList).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetItemClickListener(new MySheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(String item, String string) {
                        tvSex.setText(string);
                        isSex=item;
                    }
                }).show();
    }
    private void requestService() {
        requestCode=1;
        String requestUrl= UrlUtil.LPG_NEW_USER_URL;
        String mUserName=etUserName.getText().toString().trim();
        String mPhone=etPhoneNo.getText().toString().trim();
        String mFloor=etFloor.getText().toString().trim();
        String mRidgepole=etRidgepole.getText().toString().trim();
        String mRoom=etRoom.getText().toString().trim();
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userName",mUserName);
        textParams.put("mobile",mPhone);
        textParams.put("addressName",mAddress);
        textParams.put("addressShort",mAddressShort);
        textParams.put("longitude",mLon);
        textParams.put("latitude",mLat);
        textParams.put("floor",mFloor);
        textParams.put("isElevator",isElevator);
        textParams.put("sex",isSex);
        textParams.put("unit",mRidgepole);
        textParams.put("roomNum",mRoom);
        textParams.put("city", mCity);
        textParams.put("area",mArea);
        customDialog.show();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    private void onBindingAccount() {
        requestCode=2;
        String requestUrl= UrlUtil.LPG_BIND_MOBILE_URL;
        String mPhone=etPhoneNo.getText().toString().trim();
        String captchaCode=etCode.getText().toString().trim();
        String msbMobile= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
        String isApp="0";
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("mobile",mPhone);
        textParams.put("captchaCode",captchaCode);
        textParams.put("msbMobile",msbMobile);
        textParams.put("isApp",isApp);
        customDialog.show();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void selectAddress() {
        Intent intent=new Intent(context,ReplaceAddressActivity.class);
        intent.putExtra("mode",1);
        startActivityForResult(intent,SELECT_ADDRESS_CODE);
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etPhoneNo.getText().toString())) {
                btnCode.setEnabled(false);
                btnCode.setTextColor(Color.parseColor("#FF545454"));
                btnCode.setBackgroundResource(R.drawable.shape_white_border_rectangle);
            } else {
                //有效点击，
                btnCode.setEnabled(true);
                btnCode.setTextColor(Color.parseColor("#ffffffff"));
                btnCode.setBackgroundResource(R.drawable.shape_redorange_corners_button);
            }
            if(TextUtils.isEmpty(etPhoneNo.getText().toString())||TextUtils.isEmpty(etUserName.getText().toString())||
                    TextUtils.isEmpty(tvSex.getText().toString())||TextUtils.isEmpty(tvAddress.getText().toString())||
                    TextUtils.isEmpty(tvElevator.getText().toString())||TextUtils.isEmpty(etFloor.getText().toString())){
                btnSend.setEnabled(false);
            }else {
                btnSend.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {  //计时过程
            String UntilFinished=millisUntilFinished/1000+"秒";
            btnCode.setClickable(false);
            btnCode.setText(UntilFinished);
        }
        @Override
        public void onFinish() {
            btnCode.setText("获取验证码");
            btnCode.setClickable(true);
        }
    }
    private void removeTimeout() {
        if (time!=null){
            time.cancel();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
           customDialog.dismiss();
        }
        removeTimeout();

    }
}
