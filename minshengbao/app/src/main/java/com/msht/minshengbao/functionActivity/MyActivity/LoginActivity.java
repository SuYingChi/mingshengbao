package com.msht.minshengbao.functionActivity.MyActivity;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.event.LoginShopEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.LoginShopBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.ILoginShopView;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister, btnFindPassword, btnLogin;
    private EditText etUserName, etPassword;
    private TextView tvResult;
    private ImageView backImage;
    private String username;
    private String mPassword;
    private String pushUrl;
    private CustomDialog customDialog;
    private Context context;
    private static final String mPageName="登录页面";
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
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        StatusBarCompat.setStatusBar(this);
        context=this;
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
        backImage =(ImageView)findViewById(R.id.id_gobackimg);
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
            case R.id.id_gobackimg:
                finish();
                break;
            default:
                break;
        }
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
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("username",username);
        textParams.put("password", mPassword);
        SendRequestUtil.postDataFromService(validateURL,textParams,logonHandler);
    }

    private boolean matchLoginMsg(String name, String word) {
        if(TextUtils.isEmpty(name))
        {
            ToastUtil.ToastText(context,"账号不能为空");
            return false;
        }
        if(TextUtils.isEmpty(word))
        {
            ToastUtil.ToastText(context,"密码不能为空");
            return false;
        }
        return true;
    }
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
    }
}
