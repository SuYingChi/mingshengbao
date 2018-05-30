package com.msht.minshengbao.FunctionActivity.fragment;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.Adapter.HomeFunctionAdapter;
import com.msht.minshengbao.Adapter.HotRepairAdapter;
import com.msht.minshengbao.Adapter.TopmoduleAdapter;
import com.msht.minshengbao.Bean.ADInfo;
import com.msht.minshengbao.Bean.ActivityInfo;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.Electricvehicle.ElectricHome;
import com.msht.minshengbao.FunctionActivity.GasService.GasIccard;
import com.msht.minshengbao.FunctionActivity.GasService.GasPayfee;
import com.msht.minshengbao.FunctionActivity.GasService.GasService;
import com.msht.minshengbao.FunctionActivity.GasService.GasWriteTable;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.IcbcHtml;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.IntelligentFarmHml;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.VegetableGentlemen;
import com.msht.minshengbao.FunctionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.FunctionActivity.Public.AllService;
import com.msht.minshengbao.FunctionActivity.Public.SelectCity;
import com.msht.minshengbao.FunctionActivity.WaterApp.WaterHome;
import com.msht.minshengbao.FunctionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.FunctionActivity.repairService.HomeAppliancescClean;
import com.msht.minshengbao.FunctionActivity.repairService.HouseApplianceFix;
import com.msht.minshengbao.FunctionActivity.repairService.LampCircuit;
import com.msht.minshengbao.FunctionActivity.repairService.OtherRepair;
import com.msht.minshengbao.FunctionActivity.repairService.PublishOrder;
import com.msht.minshengbao.FunctionActivity.repairService.SanitaryWare;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.LocationUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, AMapLocationListener ,MyScrollview.ScrollViewListener{
    private View  layout_notopen;
    private LinearLayout Lnavigation;
    private RelativeLayout Rselectcity;
    private MyScrollview myScrollview;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private CardView  cardView;
    private View layout_havedata;
    private ImageCycleView mAdView;
    private MyNoScrollGridView mGridView,mHotGrid;
    private MyNoScrollGridView mTopmodule;
    private HomeFunctionAdapter homeFunctionAdapter;
    private HotRepairAdapter    hotRepairAdapter;
    private TopmoduleAdapter topmoduleAdapter;
    private TextView  tv_City,tv_naviga;
    private TextView  tv_notopen;
    private String mCity="海口";
    private String cityId="";
    private String Id;
    private String flag;
    private int times=0;
    private int bgHeight;// 上半身的高度
    private JSONArray jsonArray,topmoduleArray;
    private static final int SUCCESS=1;
    private static final int FAILURE = 0;
    private static final int REQUESTCOODE=1;//城市标志
    private Context mContext;
    private final String mPageName = "首页_民生";
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    private ArrayList<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> hotList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> topmoduleList = new ArrayList<HashMap<String, String>>();
    private MZBannerView mMZBanner;

    public HomeFragment() {}
    Handler geturlhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            mSwipeRefresh.setRefreshing(false);
                            infos.clear();//再次刷新清除原数据
                            getimageUrls();
                        }else {
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    mSwipeRefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler getfunctionhandler= new Handler() {
        public void handleMessage(Message msg) {
            getHotFix();
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String Error = object.optString("error");
                        JSONObject json =object.optJSONObject("data");
                        cityId=json.optString("city_id");
                        int online_flag=json.getInt("online_flag");
                        if (online_flag==1){
                            layout_havedata.setVisibility(View.VISIBLE);
                            layout_notopen.setVisibility(View.GONE);
                        }else {
                            layout_havedata.setVisibility(View.GONE);
                            layout_notopen.setVisibility(View.VISIBLE);
                            tv_notopen.setText("您定位到的城市#"+mCity+"#未开通服务，请切换城市");

                        }
                        JSONObject server=json.getJSONObject("serve");
                        topmoduleArray=server.optJSONArray("top_module");
                        jsonArray=server.optJSONArray("first_module");
                        if(result.equals("success")) {
                            mSwipeRefresh.setRefreshing(false);
                            topmoduleList.clear();
                            intiTopmodule();
                            functionList.clear();
                            initFunction();
                        }else {
                            Toast.makeText(getActivity(), Error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    mSwipeRefresh.setRefreshing(false);
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    Handler SpecialTopichandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        JSONArray Array =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            activityInfos.clear();//再次刷新清除原数据
                            getSpecialUrls(Array);
                        }else {
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    Handler getHothandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONArray Array =object.optJSONArray("data");
                        if(results.equals("success")) {
                            hotList.clear();
                            SavaHotRepair(Array);
                        }else {
                            Toast.makeText(getActivity(),error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void getimageUrls() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ADInfo info = new ADInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                infos.add(info);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdView.setImageResources(infos, mAdCycleViewListener);
    }
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            String myurl=info.getUrl();
            if (getDomain(myurl).equals("shop.msbapp.cn")){
                Intent intent=new Intent(getActivity(), ShopActivity.class);
                intent.putExtra("url",myurl);
                intent.putExtra("first",1);
                startActivity(intent);
            }else if (getDomain(myurl).equals("jsxss.net")){
                if (VariableUtil.loginStatus){
                    DegetableScxs();
                }else {
                    gologins();
                }
            }else if(!myurl.equals("null")){
                Intent intent=new Intent(getActivity(), HtmlPage.class);
                intent.putExtra("url",myurl);
                startActivity(intent);
            }
        }
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
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
        urls=null;
        return p;
    }
    private void getSpecialUrls(JSONArray array) {
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
        VariableUtil.citypos=-1;
        VariableUtil.loginStatus= SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
        initView(view);
        initRefresh();
        topmoduleAdapter=new TopmoduleAdapter(mContext,topmoduleList);
        mTopmodule.setAdapter(topmoduleAdapter);
        homeFunctionAdapter=new HomeFunctionAdapter(mContext,functionList);
        mGridView.setAdapter(homeFunctionAdapter);
        hotRepairAdapter=new HotRepairAdapter(mContext,hotList);
        mHotGrid.setAdapter(hotRepairAdapter);
        StartActivity();
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
                FunctionData();
                initCardData();
            }
        });
    }
    private void StartActivity() {
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
                        ElectricVehicle();
                        break;
                    case"all_service":
                        if (VariableUtil.loginStatus){
                            allserve();
                        }else {
                            gologins();
                        }
                        break;
                    case "intelligent_farm":
                        if (VariableUtil.loginStatus){
                            IntelligentFarm();
                        }else {
                            gologins();
                        }
                        break;
                    case "drinking_water":
                        if (VariableUtil.loginStatus){
                            Drinkingwater();
                        }else {
                            gologins();
                        }
                        break;
                   case "vegetables_scxs":
                        if (VariableUtil.loginStatus){
                            DegetableScxs();
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
                    String Id=hotList.get(position).get("id");
                    String Name=hotList.get(position).get("name");
                    Intent intent=new Intent(mContext,PublishOrder.class);
                    intent.putExtra("id",Id);
                    intent.putExtra("name",Name);
                    intent.putExtra("maintype","家电维修");
                    startActivity(intent);
                }else {
                    Intent login=new Intent(mContext, LoginActivity.class);
                    startActivity(login);
                }
            }
        });
        mTopmodule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codes=topmoduleList.get(position).get("code");
                Id=topmoduleList.get(position).get("id");
                switch (codes){
                    case "shop":
                        shopmall();
                        break;
                    case "gas_pay":
                        if (VariableUtil.loginStatus){
                            gaspay();
                        }else {
                            gologins();
                        }
                        break;
                    case "gas_meter":
                        if (VariableUtil.loginStatus){
                            gasmeter();
                        }else {
                            gologins();
                        }
                        break;
                    case "gas_iccard":
                        if (VariableUtil.loginStatus){
                            iccard();
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
    private void DegetableScxs() {
        Intent intent=new Intent(mContext, VegetableGentlemen.class);
        startActivity(intent);
    }
    private void Drinkingwater() {
        Intent serve=new Intent(mContext,WaterHome.class);
        serve.putExtra("mCity",mCity);
        serve.putExtra("cityId",cityId);
        startActivity(serve);
    }
    private void ElectricVehicle() {
        Intent intent=new Intent(mContext, ElectricHome.class);
        startActivity(intent);
    }
    private void allserve() {
        Intent serve=new Intent(mContext,AllService.class);
        serve.putExtra("mCity",mCity);
        serve.putExtra("cityId",cityId);
        startActivity(serve);
    }
    private void shopmall() {
        Intent intent=new Intent(mContext, ShopActivity.class);
        startActivity(intent);
    }
    private void gaspay() {
        Intent selete=new Intent(mContext,GasPayfee.class);
        startActivity(selete);
    }
    private void gasmeter() {
        Intent selete=new Intent(mContext,GasWriteTable.class);
        startActivity(selete);
    }
    private void iccard() {
        Intent card=new Intent(mContext,GasIccard.class);
        startActivity(card);
    }
    private void gologins() {
        Intent login=new Intent(mContext, LoginActivity.class);
        startActivity(login);
    }
    private void householdclean() {
        Intent intent=new Intent(mContext, HomeAppliancescClean.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void householdrepair() {
        Intent intent=new Intent(mContext, HouseApplianceFix.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void sanitaryware() {
        Intent intent=new Intent(mContext, SanitaryWare.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void lampcircuit() {
        Intent intent=new Intent(mContext,LampCircuit.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void otherrepair() {
        Intent intent=new Intent(mContext,OtherRepair.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void gasserve() {
        Intent intent=new Intent(mContext,GasService.class);
        intent.putExtra("pid",Id);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void insurance() {
        Intent intent=new Intent(mContext, InsuranceHome.class);
        startActivity(intent);
    }
    private void IntelligentFarm() {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(mContext, IntelligentFarmHml.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUESTCOODE://获取昵称设置返回数据
                if(data!=null){
                    if (resultCode==2){
                        mCity=data.getStringExtra("mCity");
                        VariableUtil.City=mCity;
                        flag=data.getStringExtra("flag");
                        cityId=data.getStringExtra("Id");
                        tv_City.setText(mCity);
                        functionList.clear();
                        hotList.clear();
                        FunctionData();
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
        SendrequestUtil.executeGet(validateURL, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                geturlhandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                geturlhandler.sendMessage(msg);
            }
        });

    }
    private void initLocation() {
        LocationUtils.setmLocation(mContext);
        LocationUtils.mlocationClient.setLocationListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                FunctionData();   //没权限定位默认海口
            }else {
                LocationUtils.mlocationClient.startLocation();
            }
        }else {
            LocationUtils.mlocationClient.startLocation();
        }
    }
    private void initView(View view) {
        view.findViewById(R.id.id_layout_air).setOnClickListener(this);
        view.findViewById(R.id.id_layout_over).setOnClickListener(this);
        cardView=(CardView)view.findViewById(R.id.id_card_view);
        mSwipeRefresh=(VerticalSwipeRefreshLayout)view.findViewById(R.id.id_swipe_refresh);
        mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
        layout_notopen=view.findViewById(R.id.id_not_open);
        layout_havedata=view.findViewById(R.id.id_layout_havedata);
        Rselectcity=(RelativeLayout)view.findViewById(R.id.id_re_city);
        Lnavigation=(LinearLayout) view.findViewById(R.id.id_li_navigation);
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mAdView = (ImageCycleView)view.findViewById(R.id.ad_view);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        mHotGrid=(MyNoScrollGridView) view.findViewById(R.id.id_hot_view);
        mTopmodule=(MyNoScrollGridView)view.findViewById(R.id.id_topmodule_view);
        tv_naviga=(TextView)view.findViewById(R.id.id_tv_naviga);
        tv_City=(TextView)view.findViewById(R.id.id_tv_city);
        tv_notopen=(TextView)view.findViewById(R.id.id_tv_nodata);
    }
    private void initCardBanner() {
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                ActivityInfo imageInfo=activityInfos.get(position);
                String title=imageInfo.getTitle();
                String right_Url=imageInfo.getUrl();
                String Domain=getDomain(right_Url);
                if (!right_Url.equals("null")){
                    if (Domain.equals("shop.msbapp.cn")){
                        Intent intent=new Intent(getActivity(), ShopActivity.class);
                        intent.putExtra("url",right_Url);
                        intent.putExtra("first",1);
                        startActivity(intent);
                    }else if(Domain.equals("mims.icbc.com.cn")){
                        Intent intent=new Intent(getActivity(), IcbcHtml.class);
                        intent.putExtra("url",right_Url);
                        intent.putExtra("navigate",title);
                        startActivity(intent);
                    }else if (Domain.equals("ccclub.cmbchina.com")){
                        Intent intent=new Intent(getActivity(), HtmlPage.class);
                        intent.putExtra("url",right_Url);
                        intent.putExtra("navigate",title);
                        startActivity(intent);
                    }else if (Domain.equals("jsxss.net")){
                        if (VariableUtil.loginStatus){
                            DegetableScxs();
                        }else {
                            gologins();
                        }
                    }else if (!Domain.equals("")){
                        Intent intent=new Intent(getActivity(), HtmlPage.class);
                        intent.putExtra("url",right_Url);
                        intent.putExtra("navigate",title);
                        startActivity(intent);
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
        Rselectcity.setOnClickListener(this);
    }
    private void initListeners() {
        ViewTreeObserver vto = mAdView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAdView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = mAdView.getHeight();
                inisetListaner();
            }
        });
    }
    private void inisetListaner() {
        myScrollview.setScrollViewListener(this);
    }
    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {   //设置标题的背景颜色
            Lnavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));//0;0;255;0
            tv_naviga.setTextColor(Color.argb(0, 0, 255, 0));
        } else if (t > 0 && t <= bgHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tv_naviga.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            Lnavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {    //滑动到banner下面设置普通颜色
           // Lnavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            Lnavigation.setBackgroundResource(R.drawable.shape_change_background);
            tv_naviga.setTextColor(Color.argb(255, 255, 255, 255));
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
           // mImageView.setImageResource(data);
            String imgurl=data.getImages();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.icon_stub);
            requestOptions.placeholder(R.drawable.icon_stub);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);//deactivate the disk cache for a request.
            requestOptions.skipMemoryCache(false);//glide will not put image in the memory cache
            Glide.with(context).load(imgurl).apply(requestOptions)
                    .thumbnail(0.5f)
                    .into(mImageView);
           /* Glide
                    .with(context)
                    .load(imgurl)
                    .error(R.drawable.icon_error)
                    .placeholder(R.drawable.icon_stub)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//deactivate the disk cache for a request.
                    .skipMemoryCache(false)//glide will not put image in the memory cache
                    .thumbnail(0.5f)
                    .into(mImageView);*/
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_city:
                Intent city=new Intent(mContext, SelectCity.class);
                startActivityForResult(city,REQUESTCOODE);
                break;
            case R.id.id_layout_air:
                if (VariableUtil.loginStatus){
                    AirConditioner();
                }else {
                    gologins();
                }
                break;
            case R.id.id_layout_over:
                if (VariableUtil.loginStatus){
                    HoodsClean();
                }else {
                    gologins();
                }
                break;
            default:

                break;
        }
    }
    private void AirConditioner() {
        Intent intent=new Intent(mContext,PublishOrder.class);
        intent.putExtra("id","33");
        intent.putExtra("name","空调清洗");
        intent.putExtra("maintype","家电清洗");
        startActivity(intent);
    }
    private void HoodsClean() {
        Intent intent=new Intent(mContext,PublishOrder.class);
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
                tv_City.setText(mCity);
                LocationUtils.mlocationClient.stopLocation();
            }else {
                VariableUtil.City=mCity;
                tv_City.setText(mCity);
                String text="ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo();
                Toast.makeText(mContext,"获取位置信息失败",Toast.LENGTH_SHORT).show();
                if (times==2){
                    LocationUtils.mlocationClient.stopLocation();
                    times=0;
                }
                times++;
            }
        }
        FunctionData();
        initCardData();
    }

    private void initCardData() {
        String functionUrl=UrlUtil.SpecialTopic_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendrequestUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                SpecialTopichandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                SpecialTopichandler.sendMessage(msg);
            }
        });
    }

    private void FunctionData() {
        String functionUrl=UrlUtil.HomeFunction_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendrequestUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                getfunctionhandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                getfunctionhandler.sendMessage(msg);
            }
        });
    }
    private void intiTopmodule() {
        try {
            for (int i = 0; i < topmoduleArray.length(); i++) {
                JSONObject jsonObject = topmoduleArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                topmoduleList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (topmoduleList.size()!=0){
            topmoduleAdapter.notifyDataSetChanged();
            cardView.setVisibility(View.VISIBLE);
        }else {
            cardView.setVisibility(View.GONE);
        }
    }
    private void initFunction() {
        VariableUtil.BoolCode=true;  //记录是否提供燃气服务模块
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
                if (code.equals("gas_serve")){
                    VariableUtil.BoolCode=false;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (functionList.size()!=0){
            homeFunctionAdapter.notifyDataSetChanged();
        }else {
        }
    }
    private void getHotFix() {
        String functionUrl=UrlUtil.HotRepair_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendrequestUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                getHothandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                getHothandler.sendMessage(msg);
            }
        });
    }
    private void SavaHotRepair(JSONArray array) {
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
        }else {
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
