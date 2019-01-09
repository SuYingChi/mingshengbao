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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.DownloadVersion.DownloadService;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.androidShop.Fragment.ShopCarParentFragment;
import com.msht.minshengbao.androidShop.Fragment.ShopMainFragment;
import com.msht.minshengbao.androidShop.activity.TotalMessageListActivity;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetMsgCountView;
import com.msht.minshengbao.androidShop.viewInterface.IMessagePreView;
import com.msht.minshengbao.events.CarNumEvent;
import com.msht.minshengbao.events.NetWorkEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.msht.minshengbao.functionActivity.fragment.HomeFragment;
import com.msht.minshengbao.functionActivity.fragment.LoginMyFrag;
import com.msht.minshengbao.functionActivity.fragment.MyFragment;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.PopupMenu;
import com.msht.minshengbao.receiver.NetBroadcastReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 首页
 *
 * @author hong
 * @date 2016/4/10
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RadioButton radioButton;
    private TextView tvNavigation, tvMassageNum;
    private Fragment homeFrag, myFrag, myNewFrag;
    private Fragment orderFrag, currentFragment;
    private View networkLayout;
    private View hearLayout;
    private String userId;
    private String password;
    private String messageCount = "0";
    private String urls;
    private boolean versionState;
    private JSONObject objectJson;
    private JSONObject jsonObject;
    /**
     * 最大显示消息数
     **/
    private static final int MAX_MASSAGE = 99;
    private static final String MY_ACTION = "ui";
    private static final int MY_LOCATION_REQUEST = 0;
    private static final int REQUEST_CODE = 2;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int MY_CAMERA_REQUEST = 3;
    private int clickCode = 0x001;
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
    private final MessageNumHandler messageNumHandler = new MessageNumHandler(this);
    private final PushHandler pushHandler = new PushHandler(this);
    private final VersionHandler versionHandler = new VersionHandler(this);
    private ImmersionBar mImmersionBar;
    private ShopMainFragment shopMainFrag;
    private ShopCarParentFragment shopCarParentFragment;
    private TextView tvCarNum;
    private Integer msgCount;


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

    private static class MessageNumHandler extends Handler {
        private WeakReference<MainActivity> mWeakReference;

        public MessageNumHandler(MainActivity mainActivity) {
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
                        JSONObject json = object.optJSONObject("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onUnreadMassage(json);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    }

    private static class PushHandler extends Handler {
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
            versionCode = pi.versionCode;
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
        VariableUtil.messageNum = json.optInt("num")+msgCount;
        if (VariableUtil.messageNum != 0) {
            if (VariableUtil.messageNum > MAX_MASSAGE) {
                messageCount = String.valueOf(MAX_MASSAGE);
            } else {
                messageCount = String.valueOf(VariableUtil.messageNum);
            }
            tvMassageNum.setText(messageCount);
            tvMassageNum.setVisibility(View.VISIBLE);

        } else {
            tvMassageNum.setVisibility(View.GONE);
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
        initImmersionBar();
        context = this;
        //推送统计
        PushAgent.getInstance(context).onAppStart();
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        StatusBarCompat.compat(this, 0x00ffffff);
        //StatusBarCompat.setStatusBar(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        VariableUtil.loginStatus = SharedPreferencesUtil.getLstate(this, SharedPreferencesUtil.Lstate, false);
        versionState = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.VersionState, false);
        initView();
        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "Myfragment");
        } else {
            initTab();
        }
        initRequestPermission();
        if (VariableUtil.loginStatus) {
            if (NetWorkUtil.isNetWorkEnable(this)) {
                //  onGetMessage();
                getTotalMessage();
            }
            initGetInformation();
            initPush();
        }
        checkVerSion();
        initBroadcast();
    }

    private void getTotalMessage() {
        ShopPresenter.getMsgCount(new IGetMsgCountView() {
            @Override
            public void onGetMsgCountSuccess(String s) {
                try {
                    msgCount = Integer.valueOf(new JSONObject(s).optString("datas"));
                    onGetMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void showLoading() {

            }

            @Override
            public void dismissLoading() {

            }

            @Override
            public void onError(String s) {

            }

            @Override
            public String getKey() {
                return ShopSharePreferenceUtil.getInstance().getKey();
            }

            @Override
            public String getUserId() {
                return null;
            }

            @Override
            public String getLoginPassword() {
                return null;
            }

            @Override
            public void onLogout() {

            }

            @Override
            public void onNetError() {

            }
        });
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
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

    private void checkVerSion() {
        int device = 2;
        String validateURL = UrlUtil.APP_VERSION_URL + "?device=" + device;
        SendRequestUtil.getDataFromService(validateURL, versionHandler);
    }

    private void onGetMessage() {
        String validateURL = UrlUtil.MESSAGE_UNREAD_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password", password);
        SendRequestUtil.postDataFromServiceTwo(validateURL, textParams, messageNumHandler);
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
        hearLayout = findViewById(R.id.id_head_view);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        radioButton = (RadioButton) findViewById(R.id.radio_me);
        ImageView messageImg = (ImageView) findViewById(R.id.id_massage_img);
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
                        /*if (VariableUtil.loginStatus){
                            clickTab3Layout();
                        }else {
                            gologinActivity();
                        }*/
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
    }

    private void initTab() {
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
                ShopPresenter.getCarList(new ICarListView() {
                    @Override
                    public void onGetCarListSuccess(String s) {
                        try {
                            JSONObject jsonObj = new JSONObject(s);
                            JSONObject dataObj = jsonObj.getJSONObject("datas");
                            JSONArray jsonArray = dataObj.optJSONArray("cart_list");
                            int carnum = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                                /*for (int ii = 0; ii < good.length(); ii++) {
                                    carnum += Integer.valueOf(good.optJSONObject(ii).optString("goods_num"));
                                }*/
                                carnum +=good.length();
                            }
                            if (carnum > 0) {
                                tvCarNum.setVisibility(View.VISIBLE);
                                tvCarNum.setText(carnum + "");
                                bundle.putInt("index", 1);
                            } else {
                                tvCarNum.setVisibility(View.GONE);
                                bundle.putInt("index", 2);
                            }
                            EventBus.getDefault().postSticky(new CarNumEvent(carnum));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void showLoading() {

                    }

                    @Override
                    public void dismissLoading() {

                    }

                    @Override
                    public void onError(String s) {

                    }

                    @Override
                    public String getKey() {
                        return ShopSharePreferenceUtil.getInstance().getKey();
                    }

                    @Override
                    public String getUserId() {
                        return null;
                    }

                    @Override
                    public String getLoginPassword() {
                        return null;
                    }

                    @Override
                    public void onLogout() {

                    }

                    @Override
                    public void onNetError() {

                    }
                }, false);
            } else {
                tvCarNum.setVisibility(View.GONE);
                EventBus.getDefault().postSticky(new CarNumEvent(-1));
                bundle.putInt("index", 0);
            }
            shopCarParentFragment.setArguments(bundle);
        }
      /*  if (getIntent().getBooleanExtra("shophome", false)) {
            ((RadioButton) findViewById(R.id.radio_order)).setChecked(true);
            hearLayout.setVisibility(View.INVISIBLE);
            tvNavigation.setText("商城");
            if (!shopMainFrag.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_layout, shopMainFrag).commitAllowingStateLoss();
                currentFragment = shopMainFrag;
                clickCode = 0x004;
            } else {
                clickCode = 0x004;
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), shopMainFrag);
            }
        } else {*/
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
        /* }*/
    }

    private void addOrShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }
        // 如果当前fragment未被添加，则添加到Fragment管理器中
        if (!fragment.isAdded()) {
            /*fragmentTransaction.hide(currentFragment)
                    .add(R.id.content_layout, minshengFrag).commit();*/
            fragmentTransaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commitAllowingStateLoss();
        } else {
            //fragmentTransaction.hide(currentFragment).show(minshengFrag).commit();
            fragmentTransaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_massage_img:
                if (VariableUtil.loginStatus) {
                    messageCenter();
                } else {
                    gologinActivity();
                }
                break;
            case R.id.id_right_massage:
                if (VariableUtil.loginStatus) {
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
        String[] abs = new String[]{"扫一扫", "消息"};
        PopupMenu mPopupMenu = new PopupMenu(this, abs);
        // 设置弹出菜单弹出的位置
        mPopupMenu.showLocation(R.id.id_massage_img, -90, 10);
        // 设置回调监听，获取点击事件
        mPopupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, int position) {
                if (position == 0) {
                    goScanCode();
                } else if (position == 1) {
                    goMessage();
                }
            }
        });
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
        //  Intent intent = new Intent(context, MessageCenterActivity.class);
        startActivity(new Intent(this, TotalMessageListActivity.class));
        //startActivityForResult(intent, 0);
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
       /* if (VariableUtil.networkStatus){
            Intent intent=new Intent(context, ShopHomeActivity.class);
            startActivityForResult(intent, clickCode);
        }*/
        hearLayout.setVisibility(View.INVISIBLE);
        clickCode = 0x004;
        tvNavigation.setText("商城");
        if (shopMainFrag == null) {
            shopMainFrag = new ShopMainFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shopMainFrag);
    }

    private void clickTab3Layout() {
       /* hearLayout.setVisibility(View.INVISIBLE);
        clickCode =0x002;
        tvNavigation.setText("订单");
        *//*if (orderFrag== null) {
            orderFrag = new OrderFragment();
        }*//*
         *//**每次进入重新加载 **//*
        orderFrag = new OrderFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), orderFrag);*/
        hearLayout.setVisibility(View.INVISIBLE);
        clickCode = 0x002;
        tvNavigation.setText("购物车");
        if (shopCarParentFragment == null) {
            shopCarParentFragment = new ShopCarParentFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), shopCarParentFragment);
        //在add hide show结构中，重复show的话不会再次回调onvisible ,所以需要外放接口手动触发onvisible时的操作
        shopCarParentFragment.refreshCarFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GoShopMainEvent messageEvent) {
        ((RadioButton) findViewById(R.id.radio_mall)).setChecked(true);
        clickTab2Layout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CarNumEvent carNumEvent) {
        if (carNumEvent.getCarNum() > 0) {
            tvCarNum.setVisibility(View.VISIBLE);
            tvCarNum.setText(String.format("%d", carNumEvent.getCarNum()));
        } else {
            tvCarNum.setVisibility(View.GONE);
        }
    }

    private void clickTab4Layout() {
        clickCode = 0x003;
        tvNavigation.setText("我的");
        hearLayout.setVisibility(View.INVISIBLE);
        if (!VariableUtil.loginStatus) {
            /*if (myFrag == null) {
                myFrag = new MyFragment();
            }*/
            myFrag = new MyFragment();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myFrag);
        } else {
            /*if (myNewFrag==null){
                myNewFrag=new LoginMyFrag();
            }*/
            myNewFrag = new LoginMyFrag();
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), myNewFrag);

        }
    }

    private void initPush() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .requestCode(REQUEST_CODE)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
            } else {
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
                        .requestCode(MY_PERMISSIONS_REQUEST)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
            } else {
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
        } else {
            AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
        }
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                downLoadApk(urls);
            } else if (requestCode == REQUEST_CODE) {
                pushMessage();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            ToastUtil.ToastText(context, "获取权限失败！");
        }
    };

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
                        //  System.exit(0);
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
    }

    //再次回到首页时手动刷新购物车fragment
    @Override
    protected void onRestart() {
        super.onRestart();
        refreshCarNum();
        if (shopCarParentFragment != null) {
            shopCarParentFragment.refreshCarFragment();
        }
        if (currentFragment instanceof LoginMyFrag) {
            ((LoginMyFrag) currentFragment).getOrdersNum();
        } else if (currentFragment instanceof ShopMainFragment) {
            ((ShopMainFragment) currentFragment).getMessageCount();
        }
        if (VariableUtil.loginStatus && NetWorkUtil.isNetWorkEnable(this)) {
            getTotalMessage();
        }
    }

    private void refreshCarNum() {
        if (!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
            ShopPresenter.getCarList(new ICarListView() {
                @Override
                public void onGetCarListSuccess(String s) {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONObject dataObj = jsonObj.getJSONObject("datas");
                        JSONArray jsonArray = dataObj.optJSONArray("cart_list");
                        // String totalAddedCarNum = dataObj.optString("cart_count");
                        int carnum = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                          /*  for (int ii = 0; ii < good.length(); ii++) {
                                carnum += Integer.valueOf(good.optJSONObject(ii).optString("goods_num"));
                            }*/
                            carnum +=good.length();
                        }
                        EventBus.getDefault().postSticky(new CarNumEvent(carnum));
                        if (carnum > 0) {
                            tvCarNum.setVisibility(View.VISIBLE);
                            tvCarNum.setText(String.format("%d", carnum));
                        } else {
                            tvCarNum.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void showLoading() {

                }

                @Override
                public void dismissLoading() {

                }

                @Override
                public void onError(String s) {

                }

                @Override
                public String getKey() {
                    return ShopSharePreferenceUtil.getInstance().getKey();
                }

                @Override
                public String getUserId() {
                    return null;
                }

                @Override
                public String getLoginPassword() {
                    return null;
                }

                @Override
                public void onLogout() {

                }

                @Override
                public void onNetError() {

                }
            }, false);
        } else {
            tvCarNum.setVisibility(View.GONE);
            EventBus.getDefault().postSticky(new CarNumEvent(-1));
        }
    }

}
