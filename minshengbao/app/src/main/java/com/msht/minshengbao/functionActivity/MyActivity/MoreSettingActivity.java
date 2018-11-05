package com.msht.minshengbao.functionActivity.MyActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.DownloadVersion.DownloadService;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.CacheUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.SwitchView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener {
    private Button btnExit;
    private TextView tvCacheSize;
    private String  urls;
    private boolean loginState, versionState;
    private JSONObject jsonObject;
    private ACache mCache;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private CustomDialog customDialog;
    public static final String MY_ACTION = "ui";
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static  class  RequestHandler extends  Handler{
        private WeakReference<MoreSettingActivity> mWeakReference;
        public RequestHandler(MoreSettingActivity activity) {
            mWeakReference=new WeakReference<MoreSettingActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MoreSettingActivity activity=mWeakReference.get();
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
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE) ){
                            activity.onReceiveVersionData();
                        }else {
                            activity.showDialog(error);
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
    private void showDialog(String error) {
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

    private void onReceiveVersionData() {
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
                            requestLoadLimit(url);
                        }
                    })
                    .show();
        }else {
            showDialog("当前已是最新版本");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        mCache = ACache.get(this);
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        setCommonHeader("更多设置");
        loginState =SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        versionState =SharedPreferencesUtil.getBoolean(this,SharedPreferencesUtil.VersionState,false);
        initView();
    }
    private void initView() {
        tvCacheSize =(TextView)findViewById(R.id.id_cache_size);
        btnExit =(Button)findViewById(R.id.id_btn_exit);
        SwitchView switchView=(SwitchView)findViewById(R.id.id_switch);
        findViewById(R.id.id_re_version).setOnClickListener(this);
        findViewById(R.id.id_re_aboutme).setOnClickListener(this);
        findViewById(R.id.id_re_clearCache).setOnClickListener(this);
        btnExit.setOnClickListener(this);
        if (loginState){
            btnExit.setVisibility(View.VISIBLE);
        }else {
            btnExit.setVisibility(View.GONE);
        }
        if (versionState){
            switchView.setOpened(true);
        }else {
            switchView.setOpened(false);
        }
        switchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                SharedPreferencesUtil.putBoolean(context,SharedPreferencesUtil.VersionState,true);
            }
            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                SharedPreferencesUtil.putBoolean(context,SharedPreferencesUtil.VersionState,false);
            }
        });
        setTextCacheSize();
    }

    private void setTextCacheSize() {
        try {
            tvCacheSize.setText(CacheUtil.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_aboutme:
                goAboutMine();
                break;
            case R.id.id_re_version:
                checkVersion();
                break;
            case R.id.id_btn_exit:
                exitLogin();
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
    private void exitLogin() {
        //清除原有数据
        SharedPreferencesUtil.clearPreference(this,"AppData");
        mCache.remove("avatarimg");
        mCache.clear();
        //清除网页Cookie
        clearCookie();
        /*水宝账户清除*/
        VariableUtil.waterAccount="";
        VariableUtil.loginStatus= SharedPreferencesUtil.getLstate(context, SharedPreferencesUtil.Lstate, false);
        setResult(0x005);
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
        String validateURL = UrlUtil.APP_VERSION_URL +"?device="+device;
        SendRequestUtil.getDataFromService(validateURL,requestHandler);
    }
    private void goAboutMine() {
        Intent intent=new Intent(context, AboutMineActivity.class);
        startActivity(intent);
    }
    private void requestLoadLimit(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                urls=url;
                AndPermission.with(this)
                        .requestCode(MY_PERMISSIONS_REQUEST)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
            }else {
                downLoadApk(url);
            }
        }else {
            downLoadApk(url);
        }
    }
    private void downLoadApk(String url) {
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
                downLoadApk(urls);
            }
        }
        @Override
        public void onFailed(int requestCode) {
            ToastUtil.ToastText(context,"获取权限失败");
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
