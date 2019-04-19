package com.msht.minshengbao.functionActivity.myActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.androidShop.event.LoginShopEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.LoginShopBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.ILoginShopView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/4/11  
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btnRegister, btnFindPassword, btnLogin;
    private EditText etUserName, etPassword;
    private TextView tvResult;
    private ImageView backImage;
    private String username;
    private String mPassword;
    private String pushUrl;
    private CustomDialog customDialog;
    private Context context;
    public static final String MY_ACTION = "ui";
    private final LogonHandler logonHandler=new LogonHandler(this);
    private static class LogonHandler extends Handler{
        private WeakReference<LoginActivity> mWeakReference;
        public LogonHandler(LoginActivity activity) {
            mWeakReference=new WeakReference<LoginActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LoginActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        JSONObject objectInfo = object.optJSONObject("data");
                        if (result.equals(SendRequestUtil.SUCCESS_VALUE)){
                            activity.onReceivePersionelData(objectInfo);
                        }else {
                            activity.tvResult.setText(error);
                        }
                    } catch (JSONException e) {
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
    private void onReceivePersionelData(JSONObject objectInfo) {
        String id = objectInfo.optString("id");
        String password = objectInfo.optString("password");
        String level = objectInfo.optString("level");
        String nickname = objectInfo.optString("nickname");
        String sex  =objectInfo.optString("sex");
        String avatar = objectInfo.optString("avatar");
        String shopCookie=objectInfo.optString("shopCookie");
        String shop=objectInfo.optString("shop");
        SharedPreferencesUtil.putUserId(this,SharedPreferencesUtil.UserId,id);
        SharedPreferencesUtil.putAvatarUrl(this,SharedPreferencesUtil.AvatarUrl,avatar);
        SharedPreferencesUtil.putPassword(this,SharedPreferencesUtil.Password,password);
        SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.NickName,nickname);
        SharedPreferencesUtil.putUserName(this,SharedPreferencesUtil.UserName,username);
        SharedPreferencesUtil.putpassw(this,SharedPreferencesUtil.passw,mPassword);
        SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
        SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.shopCookie,shopCookie);
        ShopPresenter.loginShop(username,mPassword, new ILoginShopView() {

            @Override
            public String getKey() {
                return null;
            }

            @Override
            public void onLoginShopSuccess(String s) {
                LoginShopBean bean = JsonUtil.toBean(s, LoginShopBean.class);
                if(bean!=null){
                    ShopSharePreferenceUtil.setShopSpStringValue("key",bean.getDatas().getKey());
                    ShopSharePreferenceUtil.setShopSpStringValue("username", bean.getDatas().getUsername());
                    ShopSharePreferenceUtil.setShopSpStringValue("userId",bean.getDatas().getUserid());
                    ShopSharePreferenceUtil.setShopSpStringValue("password",etPassword.getText().toString());
                    String searchhis = ShopSharePreferenceUtil.getShopSpStringValue(ShopSharePreferenceUtil.getInstance().getUserId());
                    ArrayList<String> list;
                    if (TextUtils.isEmpty(searchhis) || searchhis.equals("null")) {
                        list = new ArrayList<String>();
                    } else {
                        list = JsonUtil.stringsToList(searchhis);
                    }
                    MyApplication.getInstance().setList(list);
                    EventBus.getDefault().postSticky(new LoginShopEvent(bean));
                    Intent broadcast=new Intent();
                    broadcast.setAction(MY_ACTION);
                    broadcast.putExtra("broadcast", "1");
                    sendBroadcast(broadcast);
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("index",0);
                    intent.putExtra("pushUrl",pushUrl);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        /*if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        StatusBarCompat.setStatusBar(this);*/
        context=this;
        mPageName="登录页面";
        PushAgent.getInstance(context).onAppStart();
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
            pushUrl=data.getStringExtra("pushUrl");
        }
        initFindView();
        initEvent();
    }
    private void initFindView() {
        btnRegister =(Button)findViewById(R.id.id_button_register);
        btnFindPassword =(Button)findViewById(R.id.id_button_findpassword);
        btnLogin =(Button)findViewById(R.id.id_button_login);
        etUserName =(EditText)findViewById(R.id.id_et_usename);
        etPassword =(EditText)findViewById(R.id.id_et_password);
        tvResult =(TextView)findViewById(R.id.id_result);
        backImage =(ImageView)findViewById(R.id.id_back);
        findViewById(R.id.id_weiChat_login).setOnClickListener(this);
        btnLogin.setEnabled(false);
    }
    private void initEvent() {
        btnRegister.setOnClickListener(this);
        btnFindPassword.setOnClickListener(this);
        backImage.setOnClickListener(this);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etUserName.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etUserName.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_button_register:
                Intent reg=new Intent(context,RegisterActivity.class);
                startActivity(reg);
                break;
            case R.id.id_button_findpassword:
                Intent findPassword=new Intent(context,FindPasswordActivity.class);
                startActivity(findPassword);
                break;
            case R.id.id_button_login:
                loginSystem();
                break;
            case R.id.id_back:
                finish();
                break;
            case R.id.id_weiChat_login:
                onWeiChatLogin();
                break;
            default:
                break;
        }
    }
    private void onWeiChatLogin() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }
    private void loginSystem() {
        username = etUserName.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();
        if(matchLoginMsg(username,mPassword)) {
            customDialog.show();
            requestService();
        }
    }
    private void requestService() {
        String validateURL= UrlUtil.Login_Url;
        String versionName=AppPackageUtil.getPackageVersionName(getApplicationContext());
        versionName =versionName.replace("v","");
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("username",username);
        textParams.put("password", mPassword);
        textParams.put("client","android");
        textParams.put("version",versionName);
        SendRequestUtil.postDataFromService(validateURL,textParams,logonHandler);
    }

    private void onGetWeiChatData(final  Map<String, String> weiChatData) {
        String unionId=weiChatData.get("unionid");
        String versionCode=AppPackageUtil.getPackageVersionName(getApplicationContext());
        String requestUrl=UrlUtil.VERIFICATION_WEI_CHAT_LOGIN;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("unionid",unionId);
        textParams.put("client","android");
        textParams.put("version", versionCode);
        customDialog.show();
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_FORM, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onAnalysisData(weiChatData,data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showErrorLong(data.toString());
            }
        });
    }
    private void onAnalysisData(Map<String, String> data, String s) {
        try {
            JSONObject object = new JSONObject(s);
            String result=object.optString("result");
            String error = object.optString("error");
            JSONObject objectInfo = object.optJSONObject("data");
            if (result.equals(SendRequestUtil.SUCCESS_VALUE)){
                String isExist=objectInfo.optString("isExist");
                if (isExist.equals(ConstantUtil.VALUE_ONE)){

                    String userId=objectInfo.optString("id");
                    String password=objectInfo.optString("password");
                    String phone=objectInfo.optString("phone");
                    String nickname=objectInfo.optString("nickname");
                    String avatar=objectInfo.optString("avatar");
                    String shop=objectInfo.optString("shop");
                    String shopCookie=objectInfo.optString("shopCookie");
                    String isWeChatBind=objectInfo.optString("isWeChatBind");
                    SharedPreferencesUtil.putUserId(this,SharedPreferencesUtil.UserId,userId);
                    SharedPreferencesUtil.putAvatarUrl(this,SharedPreferencesUtil.AvatarUrl,avatar);
                    SharedPreferencesUtil.putPassword(this,SharedPreferencesUtil.Password,password);
                    SharedPreferencesUtil.putNickName(this,SharedPreferencesUtil.NickName,nickname);
                    SharedPreferencesUtil.putUserName(this,SharedPreferencesUtil.UserName,phone);
                    SharedPreferencesUtil.putpassw(this,SharedPreferencesUtil.passw,password);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
                    SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
                    SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.shopCookie,shopCookie);
                    SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,isWeChatBind);
                    Intent broadcast=new Intent();
                    broadcast.setAction(MY_ACTION);
                    broadcast.putExtra("broadcast", "1");
                    sendBroadcast(broadcast);
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("index",0);
                    intent.putExtra("pushUrl",pushUrl);
                    startActivity(intent);
                    MyApplication.removeAllActivity();
                    finish();
                }else{
                    onStartBindPhone(data);
                }
            }else {
               CustomToast.showErrorLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void onStartBindPhone(Map<String, String> data) {
        String unionId=data.get("unionid");
        String name= data.get("name");
        String gender=data.get("gender");
        String iconUrl=data.get("iconurl");
        Intent intent=new Intent(context,BindWeiChatActivity.class);
        intent.putExtra("unionId",unionId);
        intent.putExtra("name",name);
        intent.putExtra("gender",gender);
        intent.putExtra("iconUrl",iconUrl);
        intent.putExtra("pushUrl",pushUrl);
        startActivity(intent);
        MyApplication.addActivity(this);
    }
    private boolean matchLoginMsg(String name, String word) {
        if(TextUtils.isEmpty(name))
        {
            CustomToast.showWarningLong("手机号不能为空");
            return false;
        }
        if(TextUtils.isEmpty(word)) {
            CustomToast.showWarningLong("密码不能为空");
            return false;
        }
        return true;
    }
    UMAuthListener umAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            onGetWeiChatData(data);
        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            CustomToast.showSuccessLong("失败：" + t.getMessage());
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            CustomToast.showSuccessLong("取消了");
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        UMShareAPI.get(this).release();
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
