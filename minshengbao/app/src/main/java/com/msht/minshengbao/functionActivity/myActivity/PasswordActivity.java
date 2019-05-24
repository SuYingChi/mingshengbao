package com.msht.minshengbao.functionActivity.myActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends BaseActivity {
    private EditText et_origin;
    private EditText et_newword;
    private EditText et_ensureWord;
    private Button   btn_ensure;
    private String   userId,password;
    private final int RESET_CODE=1;
    private ACache mCache;
    private MethodHandler methodHandler=new MethodHandler(this);
    private static class MethodHandler extends Handler{
        private WeakReference<PasswordActivity> mWeakReference;
        public MethodHandler(PasswordActivity activity) {
            mWeakReference = new WeakReference<PasswordActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final PasswordActivity reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONObject object1 =object.getJSONObject("data");
                            String Password=object1.getString("password");
                            reference.NotifySuccess("提示","密码重置成功，请重新登录");
                        }else {
                            reference.showNotify("提示",Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.showNotify("提示",msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void NotifySuccess(String title, String s) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setSuccess();
                    }
                }).show();
    }

    private void setSuccess() {
        //清除原有数据
        SharedPreferencesUtil.clearPreference(this,"AppData");
        mCache.remove("AVATARIMG");
        mCache.clear();
        clearCookie();
        setResult(RESET_CODE);
        Intent intent=new Intent(context,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearCookie() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieSyncManager.startSync();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }

    private void showNotify(String title, String s) {
        new PromptDialog.Builder(this)
                .setTitle(title)
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
        setContentView(R.layout.activity_setpassword);
        context=this;
        mPageName="修改密码";
        //获取缓存数据
        mCache = ACache.get(this);
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        setCommonHeader(mPageName);
        initView();


    }
    private void initView() {
        et_origin=(EditText)findViewById(R.id.id_origin_password);
        et_newword=(EditText)findViewById(R.id.id_new_password);
        et_ensureWord=(EditText)findViewById(R.id.id_ensure_password);
        btn_ensure=(Button)findViewById(R.id.id_btn_ensure);
        btn_ensure.setEnabled(false);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        et_origin.addTextChangedListener(myTextWatcher);
        et_newword.addTextChangedListener(myTextWatcher);
        et_ensureWord.addTextChangedListener(myTextWatcher);
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword=et_newword.getText().toString();
                if (newPassword.equals(et_newword.getText().toString())){
                    requestService();
                }else {
                    ToastUtil.ToastText(context,"两次输入密码不一致");
                }
            }
        });
    }
    private class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(et_origin.getText().toString())||TextUtils.isEmpty(et_ensureWord.getText().toString())||
                    TextUtils.isEmpty(et_newword.getText().toString())) {
                btn_ensure.setEnabled(false);
            } else {
                btn_ensure.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void requestService() {
        String oldPassword=et_origin.getText().toString().trim();
        String newPassword=et_newword.getText().toString().trim();
        String ensurePwd=et_ensureWord.getText().toString().trim();
        String Url= UrlUtil.SetPassword_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("oldPassword",oldPassword);
        textParams.put("newPassword",newPassword);
        SendRequestUtil.postDataFromService(Url, textParams,methodHandler);
    }

}
