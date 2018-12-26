package com.msht.minshengbao.functionActivity.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.adapter.HomeFunctionAdapter;
import com.msht.minshengbao.adapter.HotRepairAdapter;
import com.msht.minshengbao.adapter.TopModuleAdapter;
import com.msht.minshengbao.Bean.ADInfo;
import com.msht.minshengbao.Bean.ActivityInfo;
import com.msht.minshengbao.functionActivity.Electricvehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIccardActivity;
import com.msht.minshengbao.functionActivity.GasService.GasPayFeeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasServiceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasWriteTableActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.IcbcHtml;
import com.msht.minshengbao.functionActivity.HtmlWeb.IntelligentFarmHmlActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.VegetableGentlemenActivity;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgMyAccountActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.Public.AllServiceActivity;
import com.msht.minshengbao.functionActivity.Public.SelectCityActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterHomeActivity;
import com.msht.minshengbao.functionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.functionActivity.repairService.HomeApplianceCleanActivity;
import com.msht.minshengbao.functionActivity.repairService.HouseApplianceFixActivity;
import com.msht.minshengbao.functionActivity.repairService.LampCircuitActivity;
import com.msht.minshengbao.functionActivity.repairService.OtherRepairActivity;
import com.msht.minshengbao.functionActivity.repairService.PublishOrderActivity;
import com.msht.minshengbao.functionActivity.repairService.SanitaryWareActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.CardVMZBanner.MZBannerView;
import com.msht.minshengbao.ViewUI.CardVMZBanner.holder.MZHolderCreator;
import com.msht.minshengbao.ViewUI.CardVMZBanner.holder.MZViewHolder;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.ImageCycleView;
import com.msht.minshengbao.ViewUI.ImageCycleView.ImageCycleViewListener;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 * @date 2016/04/10
 */
public class HomeFragment extends Fragment implements View.OnClickListener, AMapLocationListener ,MyScrollview.ScrollViewListener{
    private View layoutNotOpen;
    private LinearLayout layoutNavigation;
    private RelativeLayout layoutSelectCity;
    private MyScrollview myScrollview;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private CardView  cardView;
    private View layoutHaveData;
    private ImageCycleView mAdView;
    private MyNoScrollGridView  mGridView,mHotGrid;
    private MyNoScrollGridView  mTopModule;
    private HomeFunctionAdapter homeFunctionAdapter;
    private HotRepairAdapter    hotRepairAdapter;
    private TopModuleAdapter topmoduleAdapter;
    private TextView tvCity, tvNavigation;
    private TextView tvNotOpen;
    private String mCity="海口";
    private String cityId="1";
    private String Id="null";
    private String flag;
    private int times=0;

    /**
     * bgHeight;上半身的高度
     */
    private int bgHeight;
    private JSONArray jsonArray, rightTopArray;
    /**
     * REQUEST_CODE 城市标志
     */
    private static final int REQUEST_CODE=1;
    private Context mContext;
    private final String mPageName = "首页_民生";
    private ArrayList<ADInfo> adInformation = new ArrayList<ADInfo>();
    private ArrayList<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> hotList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> rightTopList = new ArrayList<HashMap<String, String>>();
    private MZBannerView mMZBanner;
    private final GetUrlHandler geturlHandler=new GetUrlHandler(this);
    private final GetFunctionHandler getFunctionHandler=new GetFunctionHandler(this);
    private final SpecialTopicHandler specialTopicHandler =new SpecialTopicHandler(this);
    private final GetHotHandler getHotHandler=new GetHotHandler(this);
    public HomeFragment() {}
    private static class GetUrlHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        private GetUrlHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.mSwipeRefresh.setRefreshing(false);
                            reference.adInformation.clear();//再次刷新清除原数据
                            reference.onGetImageUrls();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class GetFunctionHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        private GetFunctionHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference =mWeakReference.get();
            if (reference == null||reference.isDetached()) {
                return;
            }
            reference.getHotFix();
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        JSONObject json =object.optJSONObject("data");
                        reference.cityId=json.optString("city_id");
                        int onlineFlag=json.getInt("online_flag");
                        if (onlineFlag==1){
                            reference.layoutHaveData.setVisibility(View.VISIBLE);
                            reference.layoutNotOpen.setVisibility(View.GONE);
                        }else {
                            reference.layoutHaveData.setVisibility(View.GONE);
                            reference.layoutNotOpen.setVisibility(View.VISIBLE);
                            reference.tvNotOpen.setText("您定位到的城市#"+reference.mCity+"#未开通服务，请切换城市");

                        }
                        JSONObject server=json.getJSONObject("serve");
                        reference.rightTopArray =server.optJSONArray("top_module");
                        reference.jsonArray=server.optJSONArray("first_module");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.mSwipeRefresh.setRefreshing(false);
                            reference.rightTopList.clear();
                            reference.onTipTopModule();
                            reference.functionList.clear();
                            reference.onFunction();
                        }else {
                            ToastUtil.ToastText(reference.mContext,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class SpecialTopicHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        private SpecialTopicHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference =mWeakReference.get();
            if (reference == null||reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONArray array =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.activityInfos.clear();//再次刷新清除原数据
                            reference.onGetSpecialUrls(array);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class GetHotHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        private GetHotHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference =mWeakReference.get();
            if (reference == null||reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONArray array =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.hotList.clear();
                            reference.onSaveHotRepair(array);
                        }else {
                            ToastUtil.ToastText(reference.mContext,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onGetImageUrls() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ADInfo info = new ADInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                adInformation.add(info);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdView.setImageResources(adInformation, mAdCycleViewListener);
    }
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            String myUrl=info.getUrl();
            if (!TextUtils.isEmpty(myUrl)){
                if (NetUtil.getDomain(myUrl).equals(ConstantUtil.SHOP_DOMAIN)){
                    Intent intent=new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("url",myUrl);
                    intent.putExtra("first",1);
                    startActivity(intent);
                }else if (NetUtil.getDomain(myUrl).equals(ConstantUtil.VEGETABLE_DOMAIN)){
                    if (VariableUtil.loginStatus){
                        vegetableScxs();
                    }else {
                        gologins();
                    }
                }else if(!myUrl.equals(VariableUtil.NULL_VALUE)){
                    Intent intent=new Intent(getActivity(), HtmlPageActivity.class);
                    intent.putExtra("url",myUrl);
                    startActivity(intent);
                }
            }
        }
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            // 使用ImageLoader对图片进行加装！
            ImageLoader.getInstance().displayImage(imageURL, imageView);
        }
    };
    public static String getDomain(String url){   //获取域名
        URL urls=null;
        String p="";
        try{
            urls=new URL(url);
            p=urls.getHost();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return p;
    }
    private void onGetSpecialUrls(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ActivityInfo info = new ActivityInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                info.setTitle(jsonObject.getString("title"));
                activityInfos.add(info);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mMZBanner.setPages(activityInfos, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBanner.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mContext=getActivity();
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        VariableUtil.cityPos =-1;
        VariableUtil.loginStatus= SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
        initView(view);
        initRefresh();
        topmoduleAdapter=new TopModuleAdapter(mContext, rightTopList);
        mTopModule.setAdapter(topmoduleAdapter);
        homeFunctionAdapter=new HomeFunctionAdapter(mContext,functionList);
        mGridView.setAdapter(homeFunctionAdapter);
        hotRepairAdapter=new HotRepairAdapter(mContext,hotList);
        mHotGrid.setAdapter(hotRepairAdapter);
        onStartActivity();
        initCardBanner();
        initLocation();
        initData();
        initEvent();
        initListeners();
        return view;
    }
    private void initRefresh() {
        mSwipeRefresh.setProgressViewEndTarget(false,100);
        mSwipeRefresh.setProgressViewOffset(false,2,45);
        mSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefresh.setEnabled(true);
        mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.transparent_color);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                functionData();
                initCardData();
            }
        });
    }
    private void onStartActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codes=functionList.get(position).get("code");
                Id=functionList.get(position).get("id");
                switch (codes){
                    case "household_clean":
                        householdclean();
                        break;
                    case "household_repair":
                        householdrepair();
                        break;
                    case "sanitary_ware":
                        sanitaryware();
                        break;
                    case "lamp_circuit":
                        lampcircuit();
                        break;
                    case "other_repair":
                        otherrepair();
                        break;
                    case "gas_serve":
                        gasserve();
                        break;
                    case "insurance":
                        if (VariableUtil.loginStatus){
                            insurance();
                        }else {
                            gologins();
                        }
                        break;
                    case "electric_vehicle_repair":
                        electricVehicle();
                        break;
                    case"all_service":
                        if (VariableUtil.loginStatus){
                            allServe();
                        }else {
                            gologins();
                        }
                        break;
                    case "intelligent_farm":
                        if (VariableUtil.loginStatus){
                            intelligentFarm();
                        }else {
                            gologins();
                        }
                        break;
                    case "drinking_water":
                        if (VariableUtil.loginStatus){
                            drinkingWater();
                        }else {
                            gologins();
                        }
                        break;
                   case "vegetables_scxs":
                        if (VariableUtil.loginStatus){
                            vegetableScxs();
                        }else {
                            gologins();
                        }
                        break;
                    default:
                        showNotify("民生宝" ,"已推出新版本，如果您想使用该服务，请点击更新！");
                        break;
                }
            }
        });
        mHotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (VariableUtil.loginStatus){
                    String mId=hotList.get(position).get("id");
                    String name=hotList.get(position).get("name");
                    Intent intent=new Intent(mContext,PublishOrderActivity.class);
                    intent.putExtra("id",mId);
                    intent.putExtra("name",name);
                    intent.putExtra("maintype","家电维修");
                    startActivity(intent);
                }else {
                    Intent login=new Intent(mContext, LoginActivity.class);
                    startActivity(login);
                }
            }
        });
        mTopModule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codes= rightTopList.get(position).get("code");
                Id= rightTopList.get(position).get("id");
                switch (codes){
                    case ConstantUtil.SHOP:
                        shopmall();
                        break;
                    case ConstantUtil.GAS_PAY:
                        if (VariableUtil.loginStatus){
                            gaspay();
                        }else {
                            gologins();
                        }
                        break;
                    case ConstantUtil.GAS_METER:
                        if (VariableUtil.loginStatus){
                            gasmeter();
                        }else {
                            gologins();
                        }
                        break;
                    case ConstantUtil.GAS_IC_CARD:
                        if (VariableUtil.loginStatus){
                            iccard();
                        }else {
                            gologins();
                        }
                        break;
                    case ConstantUtil.LPG_NAME:
                        if (VariableUtil.loginStatus){
                            lpgService();
                        }else {
                            gologins();
                        }
                        break;
                    default:
                        showNotify("民生宝" ,"已推出新版本，如果您想使用该服务，请点击更新！");
                        break;
                }
            }
        });
    }



    private void showNotify(String title ,String string) {
        new PromptDialog.Builder(mContext)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void lpgService() {
        Intent intent=new Intent(mContext, LpgMyAccountActivity.class);
        startActivity(intent);
    }
    private void vegetableScxs() {
        Intent intent=new Intent(mContext, VegetableGentlemenActivity.class);
        startActivity(intent);
    }
    private void drinkingWater() {
        Intent serve=new Intent(mContext,WaterHomeActivity.class);
        serve.putExtra("mCity",mCity);
        serve.putExtra("cityId",cityId);
        startActivity(serve);
    }
    private void electricVehicle() {
        Intent intent=new Intent(mContext, ElectricHomeActivity.class);
        startActivity(intent);
    }
    private void allServe() {
        Intent serve=new Intent(mContext,AllServiceActivity.class);
        serve.putExtra("mCity",mCity);
        serve.putExtra("cityId",cityId);
        startActivity(serve);
    }
    private void shopmall() {
        Intent intent=new Intent(mContext, ShopActivity.class);
        startActivity(intent);
    }
    private void gaspay() {
        Intent selete=new Intent(mContext,GasPayFeeActivity.class);
        startActivity(selete);
    }
    private void gasmeter() {
        Intent selete=new Intent(mContext,GasWriteTableActivity.class);
        startActivity(selete);
    }
    private void iccard() {
        Intent card=new Intent(mContext,GasIccardActivity.class);
        startActivity(card);
    }
    private void gologins() {
        Intent login=new Intent(mContext, LoginActivity.class);
        startActivity(login);
    }
    private void householdclean() {
        Intent intent=new Intent(mContext, HomeApplianceCleanActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void householdrepair() {
        Intent intent=new Intent(mContext, HouseApplianceFixActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void sanitaryware() {
        Intent intent=new Intent(mContext, SanitaryWareActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void lampcircuit() {
        Intent intent=new Intent(mContext,LampCircuitActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void otherrepair() {
        Intent intent=new Intent(mContext,OtherRepairActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void gasserve() {
        Intent intent=new Intent(mContext,GasServiceActivity.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void insurance() {
        Intent intent=new Intent(mContext, InsuranceHome.class);
        startActivity(intent);
    }
    private void intelligentFarm() {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(mContext, IntelligentFarmHmlActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //获取昵称设置返回数据
            case REQUEST_CODE:
                if(data!=null){
                    if (resultCode==2){
                        mCity=data.getStringExtra("mCity");
                        VariableUtil.City=mCity;
                        flag=data.getStringExtra("flag");
                        cityId=data.getStringExtra("Id");
                        tvCity.setText(mCity);
                        functionList.clear();
                        hotList.clear();
                        functionData();
                        initCardData();
                    }
                }
                break;
            default:
                break;
        }
    }
    private void initData() {
        String validateURL = UrlUtil.imgavatar_Url;
       // OkHttpRequestManager.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL,OkHttpRequestManager.TYPE_GET,null,geturlHandler);
        SendRequestUtil.getDataFromServiceTwo(validateURL,geturlHandler);
    }
    private void initLocation() {
        LocationUtils.setLocation(mContext);
        LocationUtils.mLocationClient.setLocationListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                functionData();   //没权限定位默认海口
            }else {
                LocationUtils.mLocationClient.startLocation();
            }
        }else {
            LocationUtils.mLocationClient.startLocation();
        }
    }
    private void initView(View view) {
        view.findViewById(R.id.id_layout_air).setOnClickListener(this);
        view.findViewById(R.id.id_layout_over).setOnClickListener(this);
        cardView=(CardView)view.findViewById(R.id.id_card_view);
        mSwipeRefresh=(VerticalSwipeRefreshLayout)view.findViewById(R.id.id_swipe_refresh);
        mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
        layoutNotOpen =view.findViewById(R.id.id_not_open);
        layoutHaveData =view.findViewById(R.id.id_layout_havedata);
        layoutSelectCity =(RelativeLayout)view.findViewById(R.id.id_re_city);
        layoutNavigation=(LinearLayout) view.findViewById(R.id.id_li_navigation);
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mAdView = (ImageCycleView)view.findViewById(R.id.ad_view);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        mHotGrid=(MyNoScrollGridView) view.findViewById(R.id.id_hot_view);
        mTopModule =(MyNoScrollGridView)view.findViewById(R.id.id_topmodule_view);
        tvNavigation =(TextView)view.findViewById(R.id.id_tv_naviga);
        tvCity =(TextView)view.findViewById(R.id.id_tv_city);
        tvNotOpen =(TextView)view.findViewById(R.id.id_tv_nodata);
    }
    private void initCardBanner() {
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                ActivityInfo imageInfo=activityInfos.get(position);
                String title=imageInfo.getTitle();
                String rightUrl=imageInfo.getUrl();
                String domain=getDomain(rightUrl);
                if ((!TextUtils.isEmpty(rightUrl))&&(!rightUrl.equals(VariableUtil.NULL_VALUE))){
                    switch (domain){
                        case ConstantUtil.SHOP_DOMAIN:
                            Intent intent=new Intent(mContext, ShopActivity.class);
                            intent.putExtra("url",rightUrl);
                            intent.putExtra("first",1);
                            startActivity(intent);
                            break;
                        case "mims.icbc.com.cn":
                            Intent bank1=new Intent(mContext, IcbcHtml.class);
                            bank1.putExtra("url",rightUrl);
                            bank1.putExtra("navigate",title);
                            startActivity(bank1);
                            break;
                        case "ccclub.cmbchina.com":
                            Intent bank2=new Intent(mContext, HtmlPageActivity.class);
                            bank2.putExtra("url",rightUrl);
                            bank2.putExtra("navigate",title);
                            startActivity(bank2);
                            break;
                        case ConstantUtil.VEGETABLE_DOMAIN:
                            if (VariableUtil.loginStatus){
                                vegetableScxs();
                            }else {
                                gologins();
                            }
                            break;
                        default:
                            Intent other=new Intent(mContext, HtmlPageActivity.class);
                            other.putExtra("url",rightUrl);
                            other.putExtra("navigate",title);
                            startActivity(other);
                            break;
                    }
                }
            }
        });
        mMZBanner.addPageChangeLisnter(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
    private void initEvent() {
        layoutSelectCity.setOnClickListener(this);
    }
    private void initListeners() {
        ViewTreeObserver vto = mAdView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAdView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = mAdView.getHeight();
                onSetListaner();
            }
        });
    }
    private void onSetListaner() {
        myScrollview.setScrollViewListener(this);
    }
    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            layoutNavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tvNavigation.setTextColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if (t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tvNavigation.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            layoutNavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {
            //滑动到banner下面设置普通颜色  Lnavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            layoutNavigation.setBackgroundResource(R.drawable.shape_change_background);
            tvNavigation.setTextColor(Color.argb(255, 255, 255, 255));
        }

    }
    public static class BannerViewHolder implements MZViewHolder<ActivityInfo> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.mzbanner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, ActivityInfo data) {
            // 数据绑定
            String imgurl=data.getImages();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.icon_stub);
            requestOptions.placeholder(R.drawable.icon_stub);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(false);
            Glide.with(context).load(imgurl).apply(requestOptions)
                    .thumbnail(0.5f)
                    .into(mImageView);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_city:
                Intent city=new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(city,REQUEST_CODE);
                break;
            case R.id.id_layout_air:
                if (VariableUtil.loginStatus){
                    airConditioner();
                }else {
                    gologins();
                }
                break;
            case R.id.id_layout_over:
                if (VariableUtil.loginStatus){
                    hoodsClean();
                }else {
                    gologins();
                }
                break;
            default:

                break;
        }
    }
    private void airConditioner() {
        Intent intent=new Intent(mContext,PublishOrderActivity.class);
        intent.putExtra("id","33");
        intent.putExtra("name","空调清洗");
        intent.putExtra("maintype","家电清洗");
        startActivity(intent);
    }
    private void hoodsClean() {
        Intent intent=new Intent(mContext,PublishOrderActivity.class);
        intent.putExtra("id","10");
        intent.putExtra("name","燃气灶维修");
        intent.putExtra("maintype","家电维修");
        startActivity(intent);
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                String city=aMapLocation.getCity();
                String district=aMapLocation.getDistrict();
                aMapLocation.getCityCode();
                if (city.contains("省")){
                    if (district.contains("陵水")){
                        mCity="陵水";
                    }else {
                        mCity=district;
                    }
                }else {
                    mCity=city.replace("市","");
                }
                VariableUtil.City=mCity;
                tvCity.setText(mCity);
                LocationUtils.mLocationClient.stopLocation();
            }else {
                VariableUtil.City=mCity;
                tvCity.setText(mCity);
                String text="ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo();
                ToastUtil.ToastText(mContext,"获取位置信息失败");
                if (times==2){
                    LocationUtils.mLocationClient.stopLocation();
                    times=0;
                }
                times++;
            }
        }
        functionData();
        initCardData();
    }

    private void initCardData() {
        String functionUrl=UrlUtil.SPECIAL_TOPIC_URL;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendRequestUtil.getDataFromService(function,specialTopicHandler);
    }
    private void functionData() {
        String functionUrl=UrlUtil.HOME_FUNCTION_URL;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(function, OkHttpRequestUtil.TYPE_GET,null,getFunctionHandler);
        //SendRequestUtil.getDataFromService(function,getFunctionHandler);
    }
    private void onTipTopModule() {
        try {
            for (int i = 0; i < rightTopArray.length(); i++) {
                JSONObject jsonObject = rightTopArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                rightTopList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (rightTopList.size()!=0){
            topmoduleAdapter.notifyDataSetChanged();
            cardView.setVisibility(View.VISIBLE);
        }else {
            cardView.setVisibility(View.GONE);
        }
    }
    private void onFunction() {
        //记录是否提供燃气服务模块
        VariableUtil.BoolCode=true;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                functionList.add(map);
                if (code.equals(ConstantUtil.GAS_SERVE)){
                    VariableUtil.BoolCode=false;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (functionList.size()!=0){
            homeFunctionAdapter.notifyDataSetChanged();
        }
    }
    private void getHotFix() {
        String functionUrl=UrlUtil.HOT_REPAIR_URL;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(function, OkHttpRequestUtil.TYPE_GET,null,getHotHandler);
    }
    private void onSaveHotRepair(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                HashMap<String, String> maps = new HashMap<String, String>();
                maps.put("id", id);
                maps.put("name",name);
                maps.put("code",code);
                hotList.add(maps);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (hotList.size()!=0){
            hotRepairAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();
        MobclickAgent.onPageStart(mPageName);

    };
    @Override
    public void onPause() {
        super.onPause();
        mMZBanner.pause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    public void onDestroy() {
        LocationUtils.setonDestroy();//销毁定位客户端，同时销毁本地定位服务
        super.onDestroy();
    }
}
