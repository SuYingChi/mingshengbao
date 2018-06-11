package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button Btnregister,Btnfindpws,Btnlogin;
    private EditText Eusername,Epassword;
    private TextView Tresult;
    private ImageView backimg;
    private String username;
    private String passwordes;
    private CustomDialog customDialog;
    public static final String MY_ACTION = "ui";
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    Handler loginHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        JSONObject ObjectInfo = object.optJSONObject("data");
                        if (result.equals("success")){
                            Judgedata(ObjectInfo);
                        }else {
                            Tresult.setText(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context,msg.obj.toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void Judgedata(JSONObject objectInfo) {
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
        SharedPreferencesUtil.putpassw(this,SharedPreferencesUtil.passw,passwordes);
        SharedPreferencesUtil.putLstate(this,SharedPreferencesUtil.Lstate,true);
        SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.shopCookie,shopCookie);
        VariableUtil.loginStatus=SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        Intent broadcast=new Intent();
        broadcast.setAction(MY_ACTION);
        broadcast.putExtra("broadcast", "1");
        sendBroadcast(broadcast);
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        context=this;
        mPageName ="登录界面";
        PushAgent.getInstance(context).onAppStart();
        customDialog=new CustomDialog(this, "正在加载");
        initfindview();
        initEvent();
    }
    private void initfindview() {
        Btnregister=(Button)findViewById(R.id.id_button_register);
        Btnfindpws=(Button)findViewById(R.id.id_button_findpassword);
        Btnlogin=(Button)findViewById(R.id.id_button_login);
        Eusername=(EditText)findViewById(R.id.id_et_usename);
        Epassword=(EditText)findViewById(R.id.id_et_password);
        Tresult=(TextView)findViewById(R.id.id_result);
        backimg=(ImageView)findViewById(R.id.id_gobackimg);
        Btnlogin.setEnabled(false);
    }
    private void initEvent() {
        Btnregister.setOnClickListener(this);
        Btnfindpws.setOnClickListener(this);
        backimg.setOnClickListener(this);
        Eusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eusername.getText().toString()) || TextUtils.isEmpty(Epassword.getText().toString())) {
                    Btnlogin.setEnabled(false);
                } else {
                    Btnlogin.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        Epassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eusername.getText().toString()) || TextUtils.isEmpty(Epassword.getText().toString())) {
                    Btnlogin.setEnabled(false);
                } else {
                    Btnlogin.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        Btnlogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_button_register:
                Intent reg=new Intent(context,RegisterActivity.class);
                startActivity(reg);
                break;
            case R.id.id_button_findpassword:
                Intent findpaw=new Intent(context,FindPasswordActivity.class);
                startActivity(findpaw);
                break;
            case R.id.id_button_login:
                Loginsystem();
                break;
            case R.id.id_gobackimg:
                finish();
                break;
        }
    }

    private void Loginsystem() {
        username = Eusername.getText().toString().trim();
        passwordes = Epassword.getText().toString().trim();
        if(matchLoginMsg(username,passwordes)) {
            customDialog.show();
            requestSevice();
        }
    }

    private void requestSevice() {
        String validateURL= UrlUtil.Login_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("username",username);
        textParams.put("password", passwordes);
        SendrequestUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj =success;
                msg.what = SUCCESS;
                loginHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj =fail;
                msg.what = FAILURE;
                loginHandler.sendMessage(msg);
            }
        });
    }

    private boolean matchLoginMsg(String name, String wordes) {
        if(name.equals(""))
        {
            Toast.makeText(context, "账号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(wordes.equals(""))
        {
            Toast.makeText(context, "密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
