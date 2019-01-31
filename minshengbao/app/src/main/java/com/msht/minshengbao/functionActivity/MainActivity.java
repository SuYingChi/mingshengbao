package com.msht.minshengbao.functionActivity;

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
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.MenuItem;
import com.msht.minshengbao.DownloadVersion.DownloadService;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.androidShop.Fragment.ShopCarParentFragment;
import com.msht.minshengbao.androidShop.Fragment.ShopMainFragment;
import com.msht.minshengbao.androidShop.activity.TotalMessageListActivity;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.ISimpleCarListView;
import com.msht.minshengbao.events.CarNumEvent;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.AppShortCutUtil;
import com.msht.minshengbao.ViewUI.widget.TopRightMenu;
import com.msht.minshengbao.events.LocationEvent;
import com.msht.minshengbao.events.NetWorkEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.msht.minshengbao.functionActivity.Public.SelectCityActivity;
import com.msht.minshengbao.functionActivity.fragment.HomeFragment;
import com.msht.minshengbao.functionActivity.fragment.LoginMyFrag;
import com.msht.minshengbao.functionActivity.fragment.MyFragment;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.permissionManager.RuntimeRationale;
import com.msht.minshengbao.receiver.NetBroadcastReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
//import com.yanzhenjie.permission.PermissionListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Demo class
 * 首页
 *
 * @author hong
 * @date 2016/4/10
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView    tvNavigation, tvMassageNum;
    private Fragment    homeFrag, myFrag,myNewFrag;
    private Fragment    orderFrag,currentFragment;
    private View        networkLayout;
    private Toolbar        hearLayout;
    private String      userId;
    private String      password;
    private String      urls;
    private boolean     versionState;
    private JSONObject  objectJson;
    private JSONObject  jsonObject;
    /**最大显示消息数 **/
    private static final int MAX_MASSAGE=99;
    private static  final String MY_ACTION = "ui";
    private static  final int MY_LOCATION_REQUEST=0;
    private static  final int MY_CAMERA_REQUEST=3;
    private int     clickCode =0x001;
    /**
     * USE_COUPON_CODE 优惠券使用返回
     */
    private static final int USE_COUPON_CODE = 0x004;
    /**
     * EXIT_CODE 退出登录返回
     */
    private static final int EXIT_CODE = 0x005;
    private NetBroadcastReceiver receiver;
    private final RequestHandler requestHandler = new RequestHandler(this);
    private final PushHandler pushHandler = new PushHandler(this);
    private final VersionHandler versionHandler = new VersionHandler(this);
    private ImmersionBar mImmersionBar;
    private ShopMainFragment shopMainFrag;
    private ShopCarParentFragment shopCarParentFragment;
    private TextView tvCarNum;
    private View layoutSelectCity;
    private TextView tvCity;
    private final int REQUEST_CODE=100;


    private static class RequestHandler extends Handler {
        private WeakReference<MainActivity> mWeakReference;

        public RequestHandler(MainActivity mainActivity) {
            mWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = mWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        activity.objectJson = object.getJSONObject("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onPersonalInformation();
                        } else {
                            ToastUtil.ToastText(activity.context, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }




    private static class PushHandler extends Handler{
        private WeakReference<MainActivity> mWeakReference;

        public PushHandler(MainActivity mainActivity) {
            mWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = mWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        if (!results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            ToastUtil.ToastText(activity.context, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private static class VersionHandler extends Handler {

        private WeakReference<MainActivity> mWeakReference;

        public VersionHandler(MainActivity mainActivity) {
            mWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = mWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        activity.jsonObject = object.optJSONObject("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveVersion();
                        } else {
                            ToastUtil.ToastText(activity.context, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveVersion() {
        String version = jsonObject.optString("version");
        int versions = Integer.parseInt(version);
        String title = jsonObject.optString("title");
        String desc = jsonObject.optString("desc");
        final String url = jsonObject.optString("url");
        int versionCode = 0;
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                versionCode = pi.baseRevisionCode;
            }else {
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionCode < versions) {
            if (versionState && NetWorkUtil.isWifiAvailable(context)) {
                loadMsbApk(url);
            } else {
                new PromptDialog.Builder(this)
                        .setTitle(title)
                        .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                        .setMessage(desc.replace("\\n", "\n"))
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
                                loadMsbApk(url);
                            }
                        })
                        .show();
            }
        }
    }

    private void onUnreadMassage(JSONObject json) {
        VariableUtil.messageNum=json.optInt("num");
        String messageCount;
        if ( VariableUtil.messageNum!=0){
            if (VariableUtil.messageNum>MAX_MASSAGE){
                messageCount=String.valueOf(MAX_MASSAGE);
            }else {
                messageCount=String.valueOf(VariableUtil.messageNum);
            }
            tvMassageNum.setText(messageCount);
            tvMassageNum.setVisibility(View.VISIBLE);

        } else {
            tvMassageNum.setVisibility(View.GONE);
        }
    }
    private void onUnBadgeMassage(JSONObject json) {
        int badgeCount=json.optInt("num");
        String messageCount="0";
        if (badgeCount!=0){
            if (badgeCount>MAX_MASSAGE){
                messageCount=String.valueOf(MAX_MASSAGE);

            }else {
                messageCount=String.valueOf(badgeCount);
            }
            AppShortCutUtil.addNumShortCut(context,MainActivity.class,true,messageCount,true);
        }else {
            AppShortCutUtil.addNumShortCut(context,MainActivity.class,false,messageCount,true);
        }

    }

    private void onPersonalInformation() {
        String sex = objectJson.optString("sex");
        String phoneNo = objectJson.optString("phone");
        SharedPreferencesUtil.putSex(this, SharedPreferencesUtil.Sex, sex);
        SharedPreferencesUtil.putPhoneNumber(this, SharedPreferencesUtil.PhoneNumber, phoneNo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentFragment != null) {
            getSupportFragmentManager().putFragment(outState, "Myfragment", currentFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //推送统计
        mPageName="首页";
        PushAgent.getInstance(context).onAppStart();
        userId=SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
      //  StatusBarCompat.compat(this,0x00ffffff);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        versionState =SharedPreferencesUtil.getBoolean(this,SharedPreferencesUtil.VersionState,false);
        initView();
        initImmersionBar();
        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "Myfragment");
        } else {
            int index = getIntent().getIntExtra("index", 0);
            initTab(index);
        }
        refreshCarNum();
        initRequestPermission();
        if (isLoginState(context)){
           /* if (NetWorkUtil.isNetWorkEnable(this)){
                onGetMessage();
            }*/
            onGetMessage();
            initGetInformation();
            initPush();
            onGetBadgeCountMessage();
        }
        checkVersion();
        initBroadcast();
        /*首次进入检测通知权限*/
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            if (!AppPackageUtil.isNotificationManagerEnabled(getApplicationContext())){
                AppPackageUtil.openNotificationManager(context);
            }
            SharedPreferencesUtil.putBoolean(this, SharedPreferencesUtil.FIRST_OPEN, false);
        }
        Intent data=getIntent();
        if (data!=null){
            String pushUrl=data.getStringExtra("pushUrl");
            if (!TextUtils.isEmpty(pushUrl)){
                AppActivityUtil.onPushActivity(context,pushUrl);
            }
        }
    }


    protected void initImmersionBar() {
        mImmersionBar  = ImmersionBar.with(this);
        //白色状态栏处理
        mImmersionBar .statusBarDarkFont(true, 0.2f);
        if (ImmersionBar.hasNavigationBar(this)) {
            BarParams barParams = ImmersionBar.with(this).getBarParams();
            if (barParams.fullScreen) {
                mImmersionBar.fullScreen(false).navigationBarColor(R.color.black).init();
            }else {
                mImmersionBar.init();
                ImmersionBar.setTitleBar(this, hearLayout);
            }
        } else {
            mImmersionBar.init();
            ImmersionBar.setTitleBar(this, hearLayout);
        }
    }

    private void initRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_LOCATION_REQUEST, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code == MY_LOCATION_REQUEST) {
                            LocationUtils.mLocationClient.startLocation();
                        }
                    }

                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(context, "没有权限您将无法进行相关操作！");
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x001:
                ((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
                break;
            case 0x002:
                ((RadioButton) findViewById(R.id.radio_order)).setChecked(true);
                break;
            case 0x003:
                ((RadioButton) findViewById(R.id.radio_me)).setChecked(true);
                break;
            case 0x004:
                ((RadioButton) findViewById(R.id.radio_mall)).setChecked(true);
                break;
            //获取昵称设置返回数据
            case REQUEST_CODE:
                if(data!=null){
                    if (resultCode==2){
                        String mCity = data.getStringExtra("mCity");
                        //   flag=data.getStringExtra("flag");
                        String cityId = data.getStringExtra("Id");
                        tvCity.setText(mCity);
                        if(homeFrag instanceof HomeFragment){
                            ((HomeFragment) homeFrag).onSelectedCity(mCity,cityId);
                        }
                    }
                }
                break;
            default:
                break;
        }
        //用于点击使用优惠券返回首页
        if (resultCode == USE_COUPON_CODE) {
            ((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
        } else if (resultCode == EXIT_CODE) {
            clickTab4Layout();
        }
    }
    private void checkVersion() {
        int device=2;
        String validateURL = UrlUtil.APP_VERSION_URL +"?device="+device;
        SendRequestUtil.getDataFromService(validateURL,versionHandler);
    }

    private void onGetMessage() {
        String validateURL = UrlUtil.MESSAGE_UNREAD_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("key",ShopSharePreferenceUtil.getInstance().getKey());
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onAnalysisMessage(data.toString(),0);
            }
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onGetBadgeCountMessage() {
        String validateURL = UrlUtil.BADGE_COUNT_URL;
        HashMap<String, String> textParams = new HashMap<String, String>(3);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("key",ShopSharePreferenceUtil.getInstance().getKey());
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onAnalysisMessage(data.toString(),1);
            }
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context,data.toString());
                AppShortCutUtil.addNumShortCut(context,MainActivity.class,false,"0",true);
            }
        });
       // SendRequestUtil.postDataFromServiceTwo(validateURL,textParams,messageNumHandler);
    }
    private void onAnalysisMessage(String result,int requestType){
        try {
            JSONObject object = new JSONObject(result);
            String results=object.optString("result");
            String error = object.optString("error");
            JSONObject json =object.optJSONObject("data");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (requestType==0){
                    onUnreadMassage(json);
                }else {
                    onUnBadgeMassage(json);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }
    /**
     * 接受广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("broadcast");
            if (data.equals(VariableUtil.VALUE_ONE)) {
                finish();  //接受到广播后把上一级的MainActivity 消除
            } else if (data.equals(VariableUtil.VALUE_TWO)) {
                tvMassageNum.setVisibility(View.GONE);
                VariableUtil.messageNum = 0;
            }
        }
    };

    private void initView() {
        networkLayout = findViewById(R.id.id_network_layout);
        RadioGroup radioGroupMain = (RadioGroup) findViewById(R.id.radiogroup_main);
        tvNavigation = (TextView) findViewById(R.id.id_tv_navigation);
        tvMassageNum = (TextView) findViewById(R.id.id_main_messnum);
        hearLayout = (Toolbar)findViewById(R.id.id_head_view);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        ImageView messageImg =(ImageView)findViewById(R.id.id_massage_img);
        messageImg.setOnClickListener(this);
        findViewById(R.id.id_right_massage).setOnClickListener(this);
        radioGroupMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_home:
                        clickTab1Layout();
                        break;
                    case R.id.radio_mall:
                        clickTab2Layout();
                        break;
                    case R.id.radio_order:
                        clickTab3Layout();
                        break;
                    case R.id.radio_me:
                        clickTab4Layout();
                        break;
                    default:
                        break;
                }
            }
        });
        tvCarNum = (TextView) findViewById(R.id.shop_car_num);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvCarNum.getLayoutParams();
        int marginleft = (int) (DimenUtil.getScreenWidth() * 0.64);
        layoutParams.leftMargin = marginleft;
        layoutParams.topMargin = (int) (getResources().getDimension(R.dimen.margin_5));
        tvCarNum.setLayoutParams(layoutParams);
        layoutSelectCity =findViewById(R.id.id_city_layout);
        layoutSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent city = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivityForResult(city, REQUEST_CODE);
            }
        });
        tvCity = (TextView) findViewById(R.id.id_tv_city);
    }

    private void initTab(int index) {
        if (homeFrag == null) {
            homeFrag = new HomeFragment();
        }
        if (shopMainFrag == null) {
            shopMainFrag = new ShopMainFragment();
        }
        if (shopCarParentFragment == null) {
            shopCarParentFragment = new ShopCarParentFragment();
            final Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
                ShopPresenter.getCarList(new ISimpleCarListView() {
                    @Override
                    public void onGetCarListSuccess(String s) {
                        try {
                            JSONObject jsonObj = new JSONObject(s);
                            JSONObject dataObj = jsonObj.getJSONObject("datas");
                            JSONArray jsonArray = dataObj.optJSONArray("cart_list");
                            int carNum = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                                carNum +=good.length();
                            }
                            if (carNum > 0) {
                                tvCarNum.setVisibility(View.VISIBLE);
                                tvCarNum.setText(carNum+"");
                                bundle.putInt("index", 1);
                            } else {
                                tvCarNum.setVisibility(View.GONE);
                                bundle.putInt("index", 0);
                            }
                            EventBus.getDefault().postSticky(new CarNumEvent(carNum));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public String getKey() {
                        return ShopSharePreferenceUtil.getInstance().getKey();
                    }
                });
            } else {
                tvCarNum.setVisibility(View.GONE);
                EventBus.getDefault().postSticky(new CarNumEvent(-1));
                bundle.putInt("index", 2);
            }
            shopCarParentFragment.setArguments(bundle);
        }
      switch (index){
          case 0:
              if (!homeFrag.isAdded()) {
                  FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                  trans.remove(homeFrag).commit();
                  getSupportFragmentManager().beginTransaction()
                          .add(R.id.content_layout, homeFrag).commit();
                  currentFragment = homeFrag;
                  clickCode = 0x001;
              } else {
                  clickCode = 0x001;
                  addOrShowFragment(getSupportFragmentManager().beginTransaction(), homeFrag);
              }
              break;
          case 1:
              ((RadioButton) findViewById(R.id.radio_mall)).setChecked(true);
              clickTab2Layout();
              break;
          case 2:
              ((RadioButton) findViewById(R.id.radio_order)).setChecked(true);
              clickTab3Layout();
              break;
          case 3:
              ((RadioButton) findViewById(R.id.radio_me)).setChecked(true);
              clickTab4Layout();
              break;
              default:break;
      }

        /* }*/
    }

    private void addOrShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if(currentFragment == null) {
            fragmentTransaction .add(R.id.content_layout, fragment).commitAllowingStateLoss();
        }else if(!currentFragment.equals(fragment)&&fragment!=null){
            if (!fragment.isAdded()) {
                fragmentTransaction.hide(currentFragment)
                        .add(R.id.content_layout, fragment).commitAllowingStateLoss();
            } else {
                fragmentTransaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        currentFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_massage_img:
                if (isLoginState(context)){
                    messageCenter();
                } else {
                    gologinActivity();
                }
                break;
            case R.id.id_right_massage:
                if (isLoginState(context)){
                    messageCenter();
                } else {
                    gologinActivity();
                }
                break;
            default:
                break;
        }
    }

    private void messageCenter() {
        TopRightMenu mTopRightMenu = new TopRightMenu(this);
        //添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.scan_pic, "扫一扫"));
        menuItems.add(new MenuItem(R.mipmap.bell, "消息"));
        mTopRightMenu
                //显示菜单图标，默认为true
                .showIcon(true)
                //背景变暗，默认为true
                .dimBackground(true)
                //显示动画，默认为true
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        if (position==0){
                            goScanCode();
                        }else if (position==1){
                            goMessage();
                        }else {
                            goScanCode();
                        }
                    }

                })
                //带偏移量
                .setShowAsDropDown(findViewById(R.id.id_massage_img), getResources().getDimensionPixelOffset(R.dimen.margin_width_70), getResources().getDimensionPixelOffset(R.dimen.margin_width10));
    }

    private void goScanCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code == MY_CAMERA_REQUEST) {
                            goScanActivity();
                        }
                    }

                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(context, "没有权限您将无法进行扫描操作！");
                    }
                });

            } else {
                goScanActivity();
            }
        } else {
            goScanActivity();
        }
    }

    private void goScanActivity() {
        Intent intent = new Intent(context, QrCodeScanActivity.class);
        startActivity(intent);
    }

    private void goMessage() {
        if(!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())){
            startActivity(new Intent(this, TotalMessageListActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void gologinActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivityForResult(intent, clickCode);
    }

    private void clickTab1Layout() {
        hearLayout.setVisibility(View.VISIBLE);
        clickCode = 0x001;
        tvNavigation.setText("民生宝");
        if (homeFrag == null) {
            homeFrag = new HomeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), homeFrag);
    }

    private void clickTab2Layout() {
        hearLayout.setVisibility(View.INVISIBLE);
        clickCode = 0x004;
        tvNavigation.setText("商城");
        if (shopMainFrag == null) {
            shopMainFrag = new ShopMainFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shopMainFrag);
    }

    private void clickTab3Layout() {
        hearLayout.setVisibility(View.INVISIBLE);
        clickCode = 0x002;
        tvNavigation.setText("购物车");
        if (shopCarParentFragment == null) {
            shopCarParentFragment = new ShopCarParentFragment();
        }
        //在add hide show结构中，重复show的话不会再次回调onvisible ,所以需要外放接口手动触发onvisible时的操作
        shopCarParentFragment.refreshCarFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shopCarParentFragment);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GoShopMainEvent messageEvent) {
        ((RadioButton) findViewById(R.id.radio_mall)).setChecked(true);
        clickTab2Layout();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent locationEvent) {
        tvCity.setText(locationEvent.city);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CarNumEvent carNumEvent) {
        if (carNumEvent.getCarNum() > 0) {
            tvCarNum.setVisibility(View.VISIBLE);
            tvCarNum.setText(String.format(Locale.CHINA,"%d", carNumEvent.getCarNum()));
        } else {
            tvCarNum.setVisibility(View.GONE);
        }
    }

    private void clickTab4Layout() {
        clickCode = 0x003;
        tvNavigation.setText("我的");
        hearLayout.setVisibility(View.INVISIBLE);
        if(!isLoginState(context)) {
            myFrag = new MyFragment();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myFrag);
        } else {
            myNewFrag = new LoginMyFrag();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myNewFrag);
        }
    }

    private void initPush() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                pushMessage();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许权限通过，部分功能将无法使用");
                            }
                        }).start();
            }else {
                pushMessage();
            }
        } else {
            pushMessage();
        }
    }

    private void pushMessage() {
        String deviceData = SharedPreferencesUtil.getDeviceData(this, SharedPreferencesUtil.DeviceToken, "");
        String validateURL = UrlUtil.PUSH_DEVICE_TOKEN;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password", password);
        textParams.put("deviceType", "2");
        textParams.put("token", deviceData);
        SendRequestUtil.postDataFromServiceThree(validateURL, textParams, pushHandler);
    }

    private void initGetInformation() {
        String validateURL = UrlUtil.USER_INFO_GAS_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password", password);
        SendRequestUtil.postDataFromService(validateURL, textParams, requestHandler);
    }

    private void loadMsbApk(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                urls = url;
                AndPermission.with(this)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .rationale(new RuntimeRationale())
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许权限通过，无法更新");
                            }
                        })
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                downLoadApk(urls);
                            }
                        }).start();;
            }else {
                downLoadApk(url);
            }
        } else {
            downLoadApk(url);
        }
    }

    private void downLoadApk(String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("url", url);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST) {
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (requestCode == MY_CAMERA_REQUEST) {
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void initNetBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetBroadcastReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNetBroadcast();
        SharedPreferencesUtil.putAppAliveState(this, SharedPreferencesUtil.IS_App_ALIVE, true);
    }

    @Override
    public void onNetWorkEventBus(NetWorkEvent netMobile) {
        super.onNetWorkEventBus(netMobile);
        if (netMobile.getMessage()) {
            networkLayout.setVisibility(View.GONE);
        } else {
            networkLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showTips();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTips() {
        new PromptDialog.Builder(context)
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
                        MobclickAgent.onKillProcess(context);
                        // ZhugeSDK.getInstance().flush(getApplicationContext());//诸葛数据
                        Gson gson = new Gson();
                        String data = gson.toJson(MyApplication.getInstance().getList());
                        if (!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
                            ShopSharePreferenceUtil.setShopSpStringValue(ShopSharePreferenceUtil.getInstance().getUserId(), data);
                        } else {
                            ShopSharePreferenceUtil.setShopSpStringValue("noLoginSearch", data);
                        }
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
        // ZhugeSDK.getInstance().init(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesUtil.putBoolean(this, SharedPreferencesUtil.FIRST_OPEN, false);
        MobclickAgent.onPause(context);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        if (context!=null){
            SharedPreferencesUtil.putAppAliveState(context, SharedPreferencesUtil.IS_App_ALIVE, false);
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshCarNum();
        if (currentFragment instanceof ShopCarParentFragment) {
            ((ShopCarParentFragment)currentFragment).refreshCarFragment();
        }
        if (currentFragment instanceof LoginMyFrag) {
            ((LoginMyFrag) currentFragment).getOrdersNum();
        } else if (currentFragment instanceof ShopMainFragment) {
            ((ShopMainFragment) currentFragment).getMessageCount();
        }
        if (isLoginState(context) && NetWorkUtil.isNetWorkEnable(this)) {
            onGetMessage();
        }else {
            tvMassageNum.setVisibility(View.GONE);
            VariableUtil.messageNum=0;
        }
    }

    private void refreshCarNum() {
        if (!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
            ShopPresenter.getCarList(new ISimpleCarListView() {
                @Override
                public void onGetCarListSuccess(String s) {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONObject dataObj = jsonObj.getJSONObject("datas");
                        JSONArray jsonArray = dataObj.optJSONArray("cart_list");
                        int carNum = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                            carNum += good.length();
                        }
                        EventBus.getDefault().postSticky(new CarNumEvent(carNum));
                        if (carNum > 0) {
                            tvCarNum.setVisibility(View.VISIBLE);
                            tvCarNum.setText(String.format(Locale.CHINA,"%d", carNum));
                        } else {
                            tvCarNum.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public String getKey() {
                    return ShopSharePreferenceUtil.getInstance().getKey();
                }
            }
        );
        } else {
            tvCarNum.setVisibility(View.GONE);
            EventBus.getDefault().postSticky(new CarNumEvent(-1));
        }
    }
}
