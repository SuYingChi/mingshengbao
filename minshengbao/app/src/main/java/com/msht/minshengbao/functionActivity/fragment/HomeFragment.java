package com.msht.minshengbao.functionActivity.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.msht.minshengbao.Base.BaseHomeFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.ViewUI.Dialog.HomeAdvertisingDialog;
import com.msht.minshengbao.ViewUI.widget.DragImageView;
import com.msht.minshengbao.ViewUI.widget.MarqueeView;
import com.msht.minshengbao.adapter.HomeFunctionAdapter;
import com.msht.minshengbao.adapter.HotRepairAdapter;
import com.msht.minshengbao.adapter.TopModuleAdapter;
import com.msht.minshengbao.Bean.AdvertisingInfo;
import com.msht.minshengbao.Bean.ActivityInfo;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.ShopUrlActivity;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.functionActivity.Electricvehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIcCardActivity;
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
import com.msht.minshengbao.functionActivity.HtmlWeb.RichTextActivity;
import com.msht.minshengbao.functionActivity.Public.MsbHeadLineActivity;
import com.msht.minshengbao.functionActivity.Public.SelectCityActivity;
import com.msht.minshengbao.functionActivity.repairService.PublishOrderActivity;
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author hong
 * @date 2016/04/10
 */
public class HomeFragment extends BaseHomeFragment implements View.OnClickListener, AMapLocationListener ,MyScrollview.ScrollViewListener{
    private View layoutNotOpen;
    private LinearLayout layoutNavigation;
    private RelativeLayout layoutSelectCity;
    private MyScrollview myScrollview;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private CardView cardView;
    private View layoutHaveData;
    private View layoutHeadLine;
    private ImageCycleView mAdView;
    private MarqueeView marqueeView;
    private DragImageView dragImageView;
    private MyNoScrollGridView  mGridView,mHotGrid;
    private MyNoScrollGridView  mTopModule;
    private HomeFunctionAdapter homeFunctionAdapter;
    private HotRepairAdapter hotRepairAdapter;
    private TopModuleAdapter topmoduleAdapter;
    private TextView tvCity, tvNavigation;
    private TextView tvNotOpen;
    private String mCity = "海口";
    private String cityId = "1";
    private String Id = "null";
    private int times = 0;

    private int requestType=0;
    private String url;
    private String share;
    private String title;
    private String desc;
    private String backUrl;
    /**
     * bgHeight;上半身的高度
     */
    private int bgHeight;
    private JSONArray jsonArray, rightTopArray;
    /**
     * REQUEST_CODE 城市标志
     */
    private static final int REQUEST_CODE = 1;
    private Context mContext;
    private final String mPageName = "首页_民生";
    private ArrayList<AdvertisingInfo> adInformation = new ArrayList<AdvertisingInfo>();
    private ArrayList<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> hotList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> rightTopList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> headLineList = new ArrayList<HashMap<String, String>>();
    private List<CharSequence> headLineInfo = new ArrayList<>();
    private MZBannerView mMZBanner;
    private final GetAdvertisingHandler getAdvertisingHandler=new GetAdvertisingHandler(this);
    private final GetUrlHandler geturlHandler=new GetUrlHandler(this);
    private final GetFunctionHandler getFunctionHandler=new GetFunctionHandler(this);
    private final SpecialTopicHandler specialTopicHandler =new SpecialTopicHandler(this);
    private final GetHotHandler getHotHandler=new GetHotHandler(this);
    private final GetMsbHeadLineHandler headLineHandler=new GetMsbHeadLineHandler(this);
    private Toolbar hearLayout;


    public HomeFragment() {}
    private static class GetAdvertisingHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        public GetAdvertisingHandler(HomeFragment homeFragment) {
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
                        String error=object.optString("error");
                        JSONArray array =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestType==0){
                                reference.onHomeAdvertisingDialog(array);
                                reference.requestType=1;
                                reference.initAdvertisingData();
                            }else {
                                reference.onFloatingAdvertisingData(array);
                            }
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

    private static class GetMsbHeadLineHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;
        public GetMsbHeadLineHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            reference.initAdvertisingData();
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String code=object.optString("code");
                        JSONArray jsonArray=object.optJSONArray("data");
                        if (code.equals(ConstantUtil.VALUE_ZERO)){
                            reference.onReceiveHeadLineData(jsonArray);
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
    private static class GetUrlHandler extends Handler{
        private WeakReference<HomeFragment> mWeakReference;

        private GetUrlHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference = mWeakReference.get();
            if (reference == null || reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray = object.optJSONArray("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.mSwipeRefresh.setRefreshing(false);
                            reference.adInformation.clear();//再次刷新清除原数据
                            reference.onGetImageUrls();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private static class GetFunctionHandler extends Handler {
        private WeakReference<HomeFragment> mWeakReference;

        private GetFunctionHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference = mWeakReference.get();
            if (reference == null || reference.isDetached()) {
                return;
            }
            reference.getHotFix();
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result = object.optString("result");
                        String error = object.optString("error");
                        JSONObject json = object.optJSONObject("data");
                        reference.cityId = json.optString("city_id");
                        VariableUtil.cityId=reference.cityId;
                        int onlineFlag=json.getInt("online_flag");
                        if (onlineFlag==1){
                            reference.layoutHaveData.setVisibility(View.VISIBLE);
                            reference.layoutNotOpen.setVisibility(View.GONE);
                        } else {
                            reference.layoutHaveData.setVisibility(View.GONE);
                            reference.layoutNotOpen.setVisibility(View.VISIBLE);
                            reference.tvNotOpen.setText("您定位到的城市#" + reference.mCity + "#未开通服务，请切换城市");

                        }
                        JSONObject server = json.getJSONObject("serve");
                        reference.rightTopArray = server.optJSONArray("top_module");
                        reference.jsonArray = server.optJSONArray("first_module");
                        if (result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.mSwipeRefresh.setRefreshing(false);
                            reference.rightTopList.clear();
                            reference.onTipTopModule();
                            reference.functionList.clear();
                            reference.onFunction();
                        } else {
                            ToastUtil.ToastText(reference.mContext, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private static class SpecialTopicHandler extends Handler {
        private WeakReference<HomeFragment> mWeakReference;

        private SpecialTopicHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference = mWeakReference.get();
            if (reference == null || reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        JSONArray array = object.optJSONArray("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.activityInfos.clear();//再次刷新清除原数据
                            reference.onGetSpecialUrls(array);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private static class GetHotHandler extends Handler {
        private WeakReference<HomeFragment> mWeakReference;

        private GetHotHandler(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final HomeFragment reference = mWeakReference.get();
            if (reference == null || reference.isDetached()) {
                return;
            }
            reference.initMarqueeViewData();
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        JSONArray array = object.optJSONArray("data");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.hotList.clear();
                            reference.onSaveHotRepair(array);
                        } else {
                            ToastUtil.ToastText(reference.mContext, error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
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
                AdvertisingInfo info = new AdvertisingInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                info.setDesc(jsonObject.optString("desc"));
                info.setTitle(jsonObject.optString("title"));
                info.setShare(jsonObject.optString("share"));
                adInformation.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdView.setImageResources(adInformation, mAdCycleViewListener);
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void onImageClick(AdvertisingInfo info, int position, View imageView) {
            String myUrl = info.getUrl();
            if (!TextUtils.isEmpty(myUrl)&&!myUrl.equals(VariableUtil.NULL_VALUE)) {
                    String title=info.getTitle();
                    String share=info.getShare();
                    String desc=info.getDesc();
                    String backUrl=info.getBackUrl();
                    AppActivityUtil.onAppActivityType(mContext,myUrl,title,share,desc,"header_img",backUrl);
            }
        }
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            // 使用ImageLoader对图片进行加装！
            ImageLoader.getInstance().displayImage(imageURL, imageView);
        }
    };
    private void onGetSpecialUrls(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ActivityInfo info = new ActivityInfo();
                info.setImages(jsonObject.getString("image"));
                info.setUrl(jsonObject.getString("url"));
                info.setTitle(jsonObject.getString("title"));
                info.setDesc(jsonObject.optString("desc"));
                info.setShare(jsonObject.optString("share"));
                activityInfos.add(info);
            }
        } catch (JSONException e) {
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
    private void onReceiveHeadLineData(JSONArray jsonArray) {
        headLineInfo.clear();
        headLineList.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String title="  热点    "+jsonObject.optString("title");
                String url=jsonObject.optString("url");
                String pic=jsonObject.optString("pic");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("title",title);
                map.put("url",url);
                map.put("pic",pic);
                headLineList.add(map);
                SpannableString style = new SpannableString(title);
                style.setSpan(new BackgroundColorSpan(Color.RED),0,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                headLineInfo.add(style);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (headLineInfo.size()>0){
            layoutHeadLine.setVisibility(View.VISIBLE);
            marqueeView.startWithList(headLineInfo, R.anim.anim_bottom_in, R.anim.anim_top_out);
        }else {
            layoutHeadLine.setVisibility(View.GONE);
        }

    }
    private void onFloatingAdvertisingData(JSONArray array) {
        if (array!=null&&array.length()>0){
            dragImageView.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = array.getJSONObject(0);
            String image = jsonObject.getString("image");
            share=jsonObject.getString("share");
            title=jsonObject.getString("title");
            desc=jsonObject.optString("desc");
            url=jsonObject.optString("url");
            backUrl=jsonObject.optString("back_url");
            Uri uri = Uri.parse(image);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    //. 其他设置（如果有的话）
                    .build();
            dragImageView.setController(controller);
        }catch (JSONException e){
            e.printStackTrace();
        }
        }else {
            dragImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public View initFindView() {
        mContext=getActivity();
        if(mRootView==null){
            mRootView= LayoutInflater.from(mContext).inflate(R.layout.fragment_home,null,false);
            //view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        }
        VariableUtil.cityPos =-1;
        initView(mRootView);

        VariableUtil.cityPos = -1;
        initRefresh();
        topmoduleAdapter = new TopModuleAdapter(mContext, rightTopList);
        mTopModule.setAdapter(topmoduleAdapter);
        homeFunctionAdapter = new HomeFunctionAdapter(mContext, functionList);
        mGridView.setAdapter(homeFunctionAdapter);
        hotRepairAdapter = new HotRepairAdapter(mContext, hotList);
        mHotGrid.setAdapter(hotRepairAdapter);
        onStartActivity();
        initCardBanner();
        initLocation();
        initData();
        initEvent();
        initListeners();
        return mRootView;
    }

    private void initMarqueeViewData() {

        String requestUrl=UrlUtil.HOME_MSB_APP_HEADLINE;
        try {
            requestUrl =requestUrl +"?num="+ URLEncoder.encode("4", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,headLineHandler);
    }

    private void initRefresh() {
        mSwipeRefresh.setProgressViewEndTarget(false, 100);
        mSwipeRefresh.setProgressViewOffset(false, 2, 45);
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
                String hasNext=functionList.get(position).get("hasNext");
                String name=functionList.get(position).get("name");
                String url=functionList.get(position).get("url");
                if (!TextUtils.isEmpty(url)){
                    AppActivityUtil.onStartUrl(mContext,url);
                }else {
                    AppActivityUtil.startActivityCode(mContext,codes,Id,name,hasNext);
                }
            }
        });
        mHotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLoginState(mContext)){
                    String mId=hotList.get(position).get("id");
                    String name=hotList.get(position).get("name");
                    String code=hotList.get(position).get("code");
                    Intent intent=new Intent(mContext,PublishOrderActivity.class);
                    intent.putExtra("id",mId);
                    intent.putExtra("name",name);
                    intent.putExtra("mMainType","家电维修");
                    intent.putExtra("code",code);
                    startActivity(intent);
                } else {
                    AppActivityUtil.onStartLoginActivity(mContext,"");
                }
            }
        });
        mTopModule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codes= rightTopList.get(position).get("code");
                Id= rightTopList.get(position).get("id");
                String name=rightTopList.get(position).get("name");
                String url=rightTopList.get(position).get("url");
                if (!TextUtils.isEmpty(url)){
                    AppActivityUtil.onStartUrl(mContext,url);
                }else {
                    AppActivityUtil.startActivityTopCode(mContext,codes,Id,name);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取昵称设置返回数据
            case REQUEST_CODE:
                if(data!=null){
                    if (resultCode==2){
                        mCity=data.getStringExtra("mCity");
                     //   flag=data.getStringExtra("flag");
                        cityId=data.getStringExtra("Id");
                        VariableUtil.City=mCity;
                        VariableUtil.cityId=cityId;
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
        String validateURL = UrlUtil.HOME_ADVERTISEMENT_URL;
       // OkHttpRequestManager.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL,OkHttpRequestManager.TYPE_GET,null,geturlHandler);
        SendRequestUtil.getDataFromServiceTwo(validateURL,geturlHandler);
    }

    private void initLocation() {
        LocationUtils.setLocation(mContext);
        LocationUtils.mLocationClient.setLocationListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                functionData();   //没权限定位默认海口
            } else {
                LocationUtils.mLocationClient.startLocation();
            }
        } else {
            LocationUtils.mLocationClient.startLocation();
        }
    }

    private void initView(View view) {
        marqueeView = (MarqueeView)view.findViewById(R.id.id_marqueeView);
        layoutHeadLine=view.findViewById(R.id.id_marquee_layout);
        view.findViewById(R.id.id_layout_air).setOnClickListener(this);
        view.findViewById(R.id.id_layout_over).setOnClickListener(this);
        view.findViewById(R.id.id_headLine_more).setOnClickListener(this);
        cardView=(CardView)view.findViewById(R.id.id_card_view);
        mSwipeRefresh=(VerticalSwipeRefreshLayout)view.findViewById(R.id.id_swipe_refresh);
        mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
        layoutNotOpen =view.findViewById(R.id.id_not_open);
        layoutHaveData =view.findViewById(R.id.id_layout_havedata);
        layoutSelectCity =view.findViewById(R.id.id_city_layout);
        layoutNavigation=(LinearLayout) view.findViewById(R.id.id_li_navigation);
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mAdView = (ImageCycleView)view.findViewById(R.id.ad_view);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        mHotGrid=(MyNoScrollGridView) view.findViewById(R.id.id_hot_view);
        mTopModule =(MyNoScrollGridView)view.findViewById(R.id.id_topmodule_view);
        tvNavigation =(TextView)view.findViewById(R.id.id_tv_naviga);
        tvCity =(TextView)view.findViewById(R.id.id_tv_city);
        tvNotOpen =(TextView)view.findViewById(R.id.id_tv_nodata);
        dragImageView=(DragImageView) view.findViewById(R.id.id_floating_view);
        hearLayout = (Toolbar)view.findViewById(R.id.toolbar);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), hearLayout);
    }
    private void initCardBanner() {
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                ActivityInfo imageInfo=activityInfos.get(position);
                String title=imageInfo.getTitle();
                String rightUrl=imageInfo.getUrl();
                String share=imageInfo.getShare();
                String desc=imageInfo.getDesc();
                String backUrl=imageInfo.getBackUrl();
                AppActivityUtil.onAppActivityType(mContext,rightUrl,title,share,desc,"homepage_special_topic_activity",backUrl);
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
        dragImageView.setOnClickListener(this);
        layoutSelectCity.setOnClickListener(this);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                String id=headLineList.get(position).get("id");
                String title=headLineList.get(position).get("title");
                String pic=headLineList.get(position).get("pic");
                Intent intent=new Intent(mContext,RichTextActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title",title);
                intent.putExtra("pic",pic);
                startActivity(intent);
            }
        });
    }

    private void initListeners() {
        ViewTreeObserver vto = mAdView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAdView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = mAdView.getHeight();
                onSetListeners();
            }
        });
    }

    private void onSetListeners() {
        myScrollview.setScrollViewListener(this);
    }

    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            hearLayout.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tvNavigation.setTextColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if (t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tvNavigation.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            hearLayout.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {
            //滑动到banner下面设置普通颜色  Lnavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            hearLayout.setBackgroundResource(R.drawable.shape_change_background);
            tvNavigation.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }

    public static class BannerViewHolder implements MZViewHolder<ActivityInfo> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.mzbanner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }
        @Override
        public void onBind(Context context, int position, ActivityInfo data) {
            // 数据绑定
            String imgUrl=data.getImages();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.icon_stub);
            requestOptions.placeholder(R.drawable.icon_stub);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(false);
            Glide.with(context).load(imgUrl).apply(requestOptions)
                    .thumbnail(0.5f)
                    .into(mImageView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_city_layout:
                Intent city = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(city, REQUEST_CODE);
                break;
            case R.id.id_layout_air:
                if (isLoginState(mContext)){
                    airConditioner();
                }else {
                    AppActivityUtil.onStartLoginActivity(mContext,"");
                }
                break;
            case R.id.id_layout_over:
                if (isLoginState(mContext)){
                    hoodsClean();
                }else {
                    AppActivityUtil.onStartLoginActivity(mContext,"");
                }
                break;
            case R.id.id_headLine_more:
                onMoreHeadLine();
                break;
            case R.id.id_floating_view:
                AppActivityUtil.onAppActivityType(mContext,url,title,share,desc,"float_activity",backUrl);
                break;
            default:
                break;
        }
    }

    private void onMoreHeadLine() {
        Intent intent=new Intent(mContext,MsbHeadLineActivity.class);
        startActivity(intent);
    }
    private void airConditioner() {
        Intent intent=new Intent(mContext,PublishOrderActivity.class);
        intent.putExtra("id","33");
        intent.putExtra("name","空调清洗");
        intent.putExtra("mMainType","家电清洗");
        intent.putExtra("code",ConstantUtil.AIR_CONDITIONER_CLEAN);
        startActivity(intent);
    }

    private void hoodsClean() {
        Intent intent=new Intent(mContext,PublishOrderActivity.class);
        intent.putExtra("id","10");
        intent.putExtra("name","燃气灶维修");
        intent.putExtra("mMainType","家电维修");
        intent.putExtra("code",ConstantUtil.GAS_STOVE_REPAIR);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                String city = aMapLocation.getCity();
                String district = aMapLocation.getDistrict();
                aMapLocation.getCityCode();
                if (city.contains("省")) {
                    if (district.contains("陵水")) {
                        mCity = "陵水";
                    } else {
                        mCity = district;
                    }
                } else {
                    mCity = city.replace("市", "");
                }
                VariableUtil.City = mCity;
                tvCity.setText(mCity);
                LocationUtils.mLocationClient.stopLocation();
            } else {
                VariableUtil.City = mCity;
                tvCity.setText(mCity);
                String text = "ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo();
                ToastUtil.ToastText(mContext, "获取位置信息失败");
                if (times == 2) {
                    LocationUtils.mLocationClient.stopLocation();
                    times = 0;
                }
                times++;
            }
        }
        functionData();
        initCardData();
    }

    private void initCardData() {
        String functionUrl=UrlUtil.SPECIAL_TOPIC_URL;
        try {
            functionUrl=functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendRequestUtil.getDataFromService(functionUrl,specialTopicHandler);
    }

    private void functionData() {
        String functionUrl=UrlUtil.HOME_FUNCTION_URL;
        try {
            functionUrl =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8")+"&version="+URLEncoder.encode("201811", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(functionUrl, OkHttpRequestUtil.TYPE_GET,null,getFunctionHandler);
        //SendRequestUtil.getDataFromService(function,getFunctionHandler);
    }

    private void onTipTopModule() {
        try {
            for (int i = 0; i < rightTopArray.length(); i++) {
                JSONObject jsonObject = rightTopArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                String url=jsonObject.optString("url");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                map.put("url",url);
                rightTopList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (rightTopList!=null&&rightTopList.size()!= 0) {
            topmoduleAdapter.notifyDataSetChanged();
            cardView.setVisibility(View.VISIBLE);
            mTopModule.setNumColumns(rightTopList.size());
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    private void onFunction() {
        //记录是否提供燃气服务模块
        VariableUtil.BoolCode = true;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String code=jsonObject.getString("code");
                String url=jsonObject.optString("url");
                String hasNext=jsonObject.optString("has_next");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("code",code);
                map.put("url",url);
                map.put("hasNext",hasNext);
                functionList.add(map);
                if (code.equals(ConstantUtil.GAS_SERVE)) {
                    VariableUtil.BoolCode = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (functionList.size() != 0) {
            homeFunctionAdapter.notifyDataSetChanged();
        }
    }

    private void getHotFix() {
        String functionUrl = UrlUtil.HOT_REPAIR_URL;
        String function = "";
        try {
            function = functionUrl + "?city_id=" + URLEncoder.encode(cityId, "UTF-8") + "&city_name=" + URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(function, OkHttpRequestUtil.TYPE_GET, null, getHotHandler);
    }

    private void onSaveHotRepair(JSONArray array) {
        try {
            if(array!=null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String code = jsonObject.getString("code");
                    HashMap<String, String> maps = new HashMap<String, String>();
                    maps.put("id", id);
                    maps.put("name", name);
                    maps.put("code", code);
                    hotList.add(maps);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (hotList.size() != 0) {
            hotRepairAdapter.notifyDataSetChanged();
        }
    }

    private void initAdvertisingData() {
        String requestUrl=UrlUtil.ADVERTISING_URL;
        String code="pop_up_activity";
        if (requestType==1){
            code="float_activity";
        }
        try {
            requestUrl =requestUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&code="+URLEncoder.encode(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,getAdvertisingHandler);
    }

    /**
     * 限制每天只显示一次弹框
     * @param array 数据集
     */
    private void onHomeAdvertisingDialog(JSONArray array){
        long curDate=DateUtils.getCurDateLong("MM-dd");
        long date=SharedPreferencesUtil.getDateLong(mContext,SharedPreferencesUtil.SAVE_DATE,0);
        if (array.length()>0){
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                String image = jsonObject.getString("image");
                final String share=jsonObject.getString("share");
                final String title=jsonObject.getString("title");
                final String desc=jsonObject.optString("desc");
                String url=jsonObject.optString("url");
                final String backUrl=jsonObject.optString("back_url");
                if (curDate!=date){
                    new HomeAdvertisingDialog(mContext,image, url).builder()
                            .setOnAdvertisingClickListener(new HomeAdvertisingDialog.OnAdvertisingClickListener() {
                                @Override
                                public void onClick(String url) {
                                    AppActivityUtil.onAppActivityType(mContext,url,title,share,desc,"pop_up_activity",backUrl);
                                }
                            })
                            .show();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
            SharedPreferencesUtil.putDateLong(mContext,SharedPreferencesUtil.SAVE_DATE,curDate);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }
    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();
        MobclickAgent.onPageStart(mPageName);
    }

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
