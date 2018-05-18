package com.msht.minshengbao.FunctionView;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.DownloadVersion.DownloadService;
import com.msht.minshengbao.FunctionView.HtmlWeb.ShopActivity;
import com.msht.minshengbao.FunctionView.Myview.LoginView;
import com.msht.minshengbao.FunctionView.Public.QRCodeScan;
import com.msht.minshengbao.FunctionView.fragmeht.HomeFragment;
import com.msht.minshengbao.FunctionView.fragmeht.LoginMyFrag;
import com.msht.minshengbao.FunctionView.fragmeht.MyFragment;
import com.msht.minshengbao.FunctionView.fragmeht.OrderFragment;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.PopupMenu;
import com.msht.minshengbao.receiver.NetBroadcastReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;


import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private PopupMenu   popupMenu;
    private ImageView   messageimg;
    private RadioGroup  radiogroup_main;
    private RadioButton myradiobutton;
    private TextView    tv_naviga,tv_massnum;
    private Fragment    minshengFrag, myFrag,mynewFrag;
    private Fragment    orderFrag,currentFragment;
    private View        network_layout;
    private int         click_code=0x001;
    private String      userId;
    private String      password;
    private String      messnum="0";
    private String      urls;
    private boolean     VersionState;
    private JSONObject objectJson;
    private JSONObject jsonObject;
    private Context   mContext;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private static final String MY_ACTION = "ui";
    public  boolean lstate=false;
    private static  final int MY_LOCATION_REQUEST=0;
    private static  final int REQUEST_CODE=2;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private static  final int MY_CAMERA_REQUEST=3;
    private static final String ERROR_NETWORK = "网络连接失败，请稍后再试";
    private static final String ERROR_SERVICE = "服务器异常，请稍后再试";
    private NetBroadcastReceiver receiver;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        objectJson=object.getJSONObject("data");
                        if(Results.equals("success")) {
                            initinfoShow();
                        }else {
                            Toast.makeText(mContext,error ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(mContext,msg.obj.toString() ,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler  MessageNumHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        JSONObject json =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            showunread(json);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(mContext, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    };
    Handler VersionHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            initVersion();
                        }else {
                            Toast.makeText(mContext,error ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(mContext, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler  pushHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                        }else {
                            Toast.makeText(mContext, Error, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(mContext, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
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
            if (VersionState&&NetWorkUtil.isWifiAvailable(mContext)){
                LoadApk(url);
            }else {
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
            }
        }

    }
    private void showunread(JSONObject json) {
        int unreadNum=json.optInt("num");
        if (unreadNum!=0){
            messnum=String.valueOf(unreadNum);
            tv_massnum.setText(messnum);
            tv_massnum.setVisibility(View.VISIBLE);
        }else {
            tv_massnum.setVisibility(View.GONE);
        }
    }
    private void initinfoShow() {
        String sex     =objectJson.optString("sex");
        String phoneNo=objectJson.optString("phone");
        SharedPreferencesUtil.putSex(this,SharedPreferencesUtil.Sex,sex);
        SharedPreferencesUtil.putPhoneNumber(this,SharedPreferencesUtil.PhoneNumber,phoneNo);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentFragment!=null){
            getSupportFragmentManager().putFragment(outState,"Myfragment",currentFragment);
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        PushAgent.getInstance(mContext).onAppStart();   //推送统计
        userId=SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        StatusBarCompat.compat(this,0x00ffffff);
        //StatusBarCompat.setStatusBar(this);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        lstate=SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        VersionState=SharedPreferencesUtil.getBoolean(this,SharedPreferencesUtil.VersionState,false);
        initView();
        if (savedInstanceState!=null){
            currentFragment=getSupportFragmentManager().getFragment(savedInstanceState,"Myfragment");
        }else {
            initTab();
        }
        intrequestPermission();
        if (lstate){
            if (NetWorkUtil.IsNetWorkEnable(this)){
                unmessage();
            }
            initGetinfomation();
            initPush();
        }
        CheckVerSion();
        initBroadcast();//广播接收
    }
    private void intrequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_LOCATION_REQUEST, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        if (Code==MY_LOCATION_REQUEST){
                            LocationUtils.mlocationClient.startLocation();
                        }
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        Toast.makeText(mContext,"没有权限您将无法进行相关操作！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x001:
                ((RadioButton)findViewById(R.id.radio_home)).setChecked(true);
                break;
            case 0x002:
                ((RadioButton)findViewById(R.id.radio_order)).setChecked(true);
                break;
            case 0x003:
                ((RadioButton)findViewById(R.id.radio_me)).setChecked(true);
                break;
            default:
                break;
        }
        if (resultCode==0x004){    //用于点击使用优惠券返回首页
            ((RadioButton)findViewById(R.id.radio_home)).setChecked(true);
        }
    }
    private void CheckVerSion() {
        int device=2;
        String validateURL = UrlUtil.App_versionUrl+"?device="+device;
        HttpUrlconnectionUtil.executeGet(validateURL, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                VersionHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                VersionHandler.sendMessage(msg);

            }
        });
    }
    private void unmessage() {
        Thread messageThread=new Thread(new unreadRunnable());
        messageThread.start();
    }
    private void initBroadcast() {
        IntentFilter filter=new IntentFilter();
        filter.addAction(MY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }
    /*接受广播*/
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String info=intent.getExtras().getString("broadcast");
            if(info.equals("1")){
                finish();  //接受到广播后把上一级的Mainpage 消除
            }else if (info.equals("2")){
                tv_massnum.setVisibility(View.GONE);
            }
        }
    };
    private void initView() {
        network_layout=findViewById(R.id.id_network_layout);
        radiogroup_main = (RadioGroup) findViewById(R.id.radiogroup_main);
        tv_naviga=(TextView)findViewById(R.id.id_tv_navigation);
        tv_massnum=(TextView)findViewById(R.id.id_main_messnum);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        myradiobutton=(RadioButton)findViewById(R.id.radio_me);
        messageimg=(ImageView)findViewById(R.id.id_massage_img);
        messageimg.setOnClickListener(this);
        findViewById(R.id.id_right_massage).setOnClickListener(this);
        radiogroup_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_home:
                        clickTab1Layout();
                        break;
                    case R.id.radio_mall:
                        clickTab2Layout();
                        break;
                    case R.id.radio_order:
                        if (lstate){
                            clickTab3Layout();
                        }else {
                            gologinActivity();
                        }
                        break;
                    case R.id.radio_me:
                        clickTab4Layout();
                        break;
                }
            }
        });
    }
    private void initMynew() {
        if (mynewFrag==null) {
            mynewFrag = new LoginMyFrag();
            Bundle bundle = new Bundle();
            String phone= SharedPreferencesUtil.getPhoneNumber(this,SharedPreferencesUtil.PhoneNumber,"");
            bundle.putString("phonenumber", phone);
            bundle.putString("messnum",messnum);
            mynewFrag.setArguments(bundle);
        }
        if (!mynewFrag.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, mynewFrag).commit();
            currentFragment = mynewFrag;
            myradiobutton.setChecked(true);
           // Head_layout.setVisibility(View.VISIBLE);
            click_code=0x003;
            tv_naviga.setText("我的");
            addOrShowFragment(getSupportFragmentManager().beginTransaction(),mynewFrag);
        }
    }
    private void initMy() {
        if (myFrag ==null) {
            myFrag  = new MyFragment();
        }
        if (!myFrag .isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, myFrag ).commit();
            // 记录当前Fragment
            currentFragment = myFrag ;
            click_code=0x003;
            myradiobutton.setChecked(true);
          //  Head_layout.setVisibility(View.VISIBLE);
            tv_naviga.setText("我的");
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myFrag);
        }
    }
    private void initTab() {
        if (minshengFrag == null) {
            minshengFrag = new HomeFragment();
        }
        if (!minshengFrag.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, minshengFrag).commit();
            currentFragment = minshengFrag;
            click_code=0x001;
           // Head_layout.setVisibility(View.VISIBLE);
        }
    }
    private void addOrShowFragment(FragmentTransaction fragmentTransaction, Fragment minshengFrag) {
        if (currentFragment == minshengFrag)
            return;

        if (!minshengFrag.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            fragmentTransaction.hide(currentFragment)
                    .add(R.id.content_layout, minshengFrag).commit();
        } else {
            fragmentTransaction.hide(currentFragment).show(minshengFrag).commit();
        }
        currentFragment = minshengFrag;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_massage_img:
                if (lstate){
                    messageCenter();
                }else {
                    gologinActivity();
                }
                break;
            case R.id.id_right_massage:
                if (lstate){
                    messageCenter();
                }else {
                    gologinActivity();
                }
                break;
            default:
                break;
        }
    }
    private void messageCenter() {
        String[] abs = new String[]{"扫一扫", "消息"};
        popupMenu = new PopupMenu(this,abs);
        popupMenu.showLocation(R.id.id_massage_img,-90,10);// 设置弹出菜单弹出的位置
        // 设置回调监听，获取点击事件
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, int position) {
                if (position==0){
                    GoScancode();
                }else if (position==1){
                    GoMessage();
                }
            }
        });
    }
    private void GoScancode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        if (Code==MY_CAMERA_REQUEST){
                            GoScanActivity();
                        }
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        Toast.makeText(mContext,"没有权限您将无法进行扫描操作！",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                GoScanActivity();
            }
        }else {
            GoScanActivity();
        }
    }
    private void GoScanActivity() {
        Intent intent=new Intent(mContext, QRCodeScan.class);
        startActivity(intent);
    }
    private void GoMessage() {
         Intent intent=new Intent(mContext,MessageCenter.class);
        startActivityForResult(intent,0);
    }

    private void gologinActivity(){
        Intent intent=new Intent(mContext,LoginView.class);
        startActivityForResult(intent,click_code);
    }
    private void clickTab1Layout() {
       // Head_layout.setVisibility(View.VISIBLE);
        click_code=0x001;
        tv_naviga.setText("民生宝");
        if (minshengFrag == null) {
            minshengFrag = new HomeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), minshengFrag);
    }
    private void clickTab2Layout() {
        if (networkStatus){
            Intent intent=new Intent(mContext, ShopActivity.class);
            startActivityForResult(intent,click_code);
        }
    }
    private void clickTab3Layout() {
       // Head_layout.setVisibility(View.VISIBLE);
        click_code=0x002;
        tv_naviga.setText("订单");
        /*if (orderFrag== null) {
            orderFrag = new OrderFragment();
        }*/
        orderFrag = new OrderFragment();   //每次进入重新加载
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), orderFrag);
    }
    private void clickTab4Layout() {
       // Head_layout.setVisibility(View.VISIBLE);
        click_code=0x003;
        tv_naviga.setText("我的");
        if(!lstate) {
            /*if (myFrag == null) {
                myFrag = new MyFragment();
            }*/
            myFrag  = new MyFragment();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myFrag);
        }else {
            /*if (mynewFrag==null){
                mynewFrag=new LoginMyFrag();
            }*/
            mynewFrag=new LoginMyFrag();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), mynewFrag);
        }
    }
    private void initPush() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .requestCode(REQUEST_CODE)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
            }else {
                pushMessage();
            }
        }else {
            pushMessage();
        }
    }
    private void pushMessage() {
        String DeviceToken=SharedPreferencesUtil.getDeviceData(this,SharedPreferencesUtil.DeviceToken,"");
        String validateURL = UrlUtil.pushDeviceToken;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("deviceType","2");
        textParams.put("token",DeviceToken);
        HttpUrlconnectionUtil.executepostTwo(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                pushHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = SUCCESS;
                pushHandler.sendMessage(msg);
            }
        });
    }
    private void initGetinfomation() {
        String validateURL = UrlUtil.Userinfo_GasUrl;
        HttpURLConnection conn = null;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
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
    private class unreadRunnable implements Runnable {
        @Override
        public void run() {
            String validateURL = UrlUtil.Message_unreadUrl;
            HttpURLConnection conn = null;
            DataInputStream dis = null;
            Map<String, String> textParams = new HashMap<String, String>();
            try {
                textParams = new HashMap<String, String>();
                URL url = new URL(validateURL);
                textParams.put("userId",userId);
                textParams.put("password",password);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setDoInput(true); // 发送POST请求必须设置允许输入
                conn.setDoOutput(true); // 发送POST请求必须设置允许输出
                conn.setRequestProperty("Charset", "UTF-8");//设置编码
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                NetUtil.writeStringParams(textParams, ds);
                NetUtil.paramsEnd(ds);
                os.flush();
                os.close();
                conn.connect();
                dis = new DataInputStream(conn.getInputStream());
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    String resultStr= NetUtil.readString(is);
                    Message msg = new Message();
                    msg.obj = resultStr;
                    msg.what = SUCCESS;
                    MessageNumHandler.sendMessage(msg);
                }else {
                    Message msg = new Message();
                    msg.obj=ERROR_SERVICE;
                    msg.what = FAILURE;
                    MessageNumHandler.sendMessage(msg);
                }
            }catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.obj=ERROR_NETWORK;
                msg.what = FAILURE;
                MessageNumHandler.sendMessage(msg);
                Log.d(this.toString(), e.getMessage() + "  127 line");
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
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
        Intent intent = new Intent(mContext,DownloadService.class);
        intent.putExtra("url", url);
        startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==MY_LOCATION_REQUEST){
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }else if (requestCode==MY_CAMERA_REQUEST){
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }else {
            AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode==MY_PERMISSIONS_REQUEST){
                DownLoadApk(urls);
            }else if (requestCode==REQUEST_CODE){
                pushMessage();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            Toast.makeText(mContext,"获取权限失败！",Toast.LENGTH_SHORT).show();
        }
    };
    public void initNetBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver =new  NetBroadcastReceiver();
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        initNetBroadcast();
    }
    @Override
    public void onNetChange(boolean netMobile) {
        super.onNetChange(netMobile);
        if (netMobile){
            network_layout.setVisibility(View.GONE);
        }else {
            network_layout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK&& event.getRepeatCount()==0){
            showTips();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showTips() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("是否要退出民生宝")
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        MobclickAgent.onKillProcess(mContext);
                       // ZhugeSDK.getInstance().flush(getApplicationContext());//诸葛数据
                        finish();
                        System.exit(0);
                    }
                })
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
       // ZhugeSDK.getInstance().init(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesUtil.putBoolean(this, SharedPreferencesUtil.FIRST_OPEN, false);
        MobclickAgent.onPause(mContext);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
