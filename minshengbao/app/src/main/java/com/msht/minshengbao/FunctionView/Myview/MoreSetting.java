package com.msht.minshengbao.FunctionView.Myview;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.DownloadVersion.DownloadService;
import com.msht.minshengbao.FunctionView.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.CacheUtil;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.SwitchView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONObject;

public class MoreSetting extends BaseActivity implements View.OnClickListener {
    private Button     btn_exit;
    private TextView   tv_CacheSize;
    private SwitchView switchView;
    private String  urls;
    private boolean lstate,VersionState;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONObject jsonObject;
    private ACache mCache;//缓存
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private CustomDialog customDialog;
    public static final String MY_ACTION = "ui";   //广播跳转意图
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            initVersion();
                        }else {
                            ShowDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void ShowDialog(String error) {
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

    private void initVersion() {
        String version=jsonObject.optString("version");
        int versions=Integer.parseInt(version);
        String title=jsonObject.optString("title");
        String desc=jsonObject.optString("desc");
        final String url=jsonObject.optString("url");
        int versionCode=0;
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionCode=pi.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        if (versionCode<versions){
            new PromptDialog.Builder(this)
                    .setTitle(title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(desc.replace("\\n","\n"))
                    .setButton1("以后再说", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("更新", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            LoadApk(url);
                        }
                    })
                    .show();
        }else {
            ShowDialog("当前已是最新版本");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        mCache = ACache.get(this);//获取缓存数据
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        setCommonHeader("更多设置");
        lstate=SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        VersionState=SharedPreferencesUtil.getBoolean(this,SharedPreferencesUtil.VersionState,false);
        initView();
    }
    private void initView() {
        tv_CacheSize=(TextView)findViewById(R.id.id_cache_size);
        btn_exit=(Button)findViewById(R.id.id_btn_exit);
        switchView=(SwitchView)findViewById(R.id.id_switch);
        findViewById(R.id.id_re_version).setOnClickListener(this);
        findViewById(R.id.id_re_aboutme).setOnClickListener(this);
        findViewById(R.id.id_re_clearCache).setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        if (lstate){
            btn_exit.setVisibility(View.VISIBLE);
        }else {
            btn_exit.setVisibility(View.GONE);
        }
        if (VersionState){
            switchView.setOpened(true);
        }else {
            switchView.setOpened(false);
        }
        switchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true); // or false
                SharedPreferencesUtil.putBoolean(context,SharedPreferencesUtil.VersionState,true);
            }
            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false); // or false
                SharedPreferencesUtil.putBoolean(context,SharedPreferencesUtil.VersionState,false);
            }
        });
        setTextCacheSize();
    }

    private void setTextCacheSize() {
        try {
            tv_CacheSize.setText(CacheUtil.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_aboutme:
                Goaboutmine();
                break;
            case R.id.id_re_version:
                checkVersion();
                break;
            case R.id.id_btn_exit:
                ExitLogin();
                break;
            case R.id.id_re_clearCache:
                if (CacheUtil.clearAllCache(context)){
                    setTextCacheSize();
                }
                break;
            default:
                break;
        }
    }
    private void ExitLogin() {
        SharedPreferencesUtil.Clear(this,"AppData");//清除原有数据
        mCache.remove("AVATARIMG");//清除原有数据
        mCache.clear();
        Intent broadcast=new Intent();
        broadcast.setAction(MY_ACTION);
        broadcast.putExtra("broadcast", "1");
        sendBroadcast(broadcast);
        clearCookie();
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(2);
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
    private void checkVersion() {
        customDialog.show();
        int device=2;
        String validateURL = UrlUtil.App_versionUrl+"?device="+device;
        HttpUrlconnectionUtil.executeGet(validateURL, new ResultListener() {
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
    private void Goaboutmine() {
        Intent intent=new Intent(context, AboutMine.class);
        startActivity(intent);
    }
    private void LoadApk(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                urls=url;
                AndPermission.with(this)
                        .requestCode(MY_PERMISSIONS_REQUEST)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
            }else {
                DownLoadApk(url);
            }
        }else {
            DownLoadApk(url);
        }
    }
    private void DownLoadApk(String url) {
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra("url", url);
        startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode==MY_PERMISSIONS_REQUEST){
                DownLoadApk(urls);
            }
        }
        @Override
        public void onFailed(int requestCode) {
            Toast.makeText(context,"获取权限失败",Toast.LENGTH_SHORT).show();
        }
    };
}
