package com.msht.minshengbao.functionActivity.electricVehicle;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/17  
 */
public class ElectricMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, AMapLocationListener {
    private View layoutNear;
    private ImageView rightImage;
    private String lat;
    private String lon;
    private String cityCode="",cityName="";
    private String areaCode="",areaName="";
    private String address="";
    private MapView mMapView = null;
    private AMap aMap;
    private boolean mFirst =true;
    private AMapLocationClient locationClient;
    private MyLocationStyle myLocationStyle;
    private MultiPointOverlayOptions overlayOptions;
    private MultiPointOverlay multiPointOverlay;
    private static  final int MY_LOCATION_REQUEST=0;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ElectricMapActivity> mWeakReference;
        public RequestHandler(ElectricMapActivity activity) {
            mWeakReference=new WeakReference<ElectricMapActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ElectricMapActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("code");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            activity.jsonArray=jsonObject.getJSONArray("list");
                            activity.initShowData();
                        }else {
                            activity.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void initShowData() {
        List<MultiPointItem> list = new ArrayList<MultiPointItem>();
        String pos="0";
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id=obj.optString("id");
                String name=obj.optString("name");
                String telephone=obj.optString("telephone");
                String address=obj.optString("address");
                String latitude=obj.optString("latitude");
                String longitude=obj.optString("longitude");
                String cityCode=obj.optString("cityCode");
                String cityName=obj.optString("cityName");
                String areaCode=obj.optString("areaCode");
                String areaName=obj.optString("areaName");
                String imgUrl=obj.optString("imgUrl");
                String distance=obj.optString("distance");
                String lastModifyTime=obj.optString("lastModifyTime");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("telephone", telephone);
                map.put("address",address);
                map.put("latitude",latitude);
                map.put("longitude", longitude);
                map.put("cityCode",cityCode);
                map.put("cityName",cityName);
                map.put("areaCode", areaCode);
                map.put("areaName",areaName);
                map.put("imgUrl",imgUrl);
                map.put("distance",distance);
                map.put("lastModifyTime",lastModifyTime);
                mList.add(map);
                pos=String.valueOf(i);
                double lat =obj.getDouble("latitude");
                double lon = obj.getDouble("longitude");
                LatLng latLng = new LatLng(lat, lon, false);
                MultiPointItem multiPointItem = new MultiPointItem(latLng);
                multiPointItem.setTitle(pos);
                list.add(multiPointItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mList.size()!=0&&mList!=null){
            if(multiPointOverlay != null) {
                multiPointOverlay.setItems(list);
                multiPointOverlay.setEnable(true);
            }
        }
    }
    private void onShowFailure(String error) {
        new PromptDialog.Builder(context)
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_map);
        context=this;
        mPageName="电动车地图";
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader(mPageName);
        onSetLocation();
        initView(savedInstanceState);
        initEvent();
    }
    private void onSetLocation() {
        locationClient = ALocationClientFactory.createLocationClient(this, ALocationClientFactory.createDefaultOption(),this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                locationClient.startLocation();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许定位权限，地图定位无法使用！");
                            }
                        }).start();


            }else {
                locationClient.startLocation();
            }
        }else {
            locationClient.startLocation();
        }
    }

    private void initData() {
        String dataUrl = UrlUtil.ELECTRIC_LIST_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        int pageNo=1;
        String pageNum=String.valueOf(pageNo);
        textParams.put("type","0");
        textParams.put("latitude",lat);
        textParams.put("longitude",lon);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","100");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initEvent() {
        aMap.setOnMultiPointClickListener(new AMap.OnMultiPointClickListener() {
            @Override
            public boolean onPointClick(MultiPointItem multiPointItem) {
                String pos=multiPointItem.getTitle();
                if (!TextUtils.isEmpty(pos)){
                    int position=Integer.parseInt(pos);
                    String storeId=mList.get(position).get("id");
                    String distance=mList.get(position).get("distance");
                    Intent intent=new Intent(context,ElectricsStoreDetailActivity.class);
                    intent.putExtra("store_id",storeId);
                    intent.putExtra("distance",distance);
                    startActivity(intent);
                }
                return false;
            }
        });
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SearchStoreActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                intent.putExtra("areaCode",areaCode);
                intent.putExtra("areaName",areaName);
                intent.putExtra("cityCode",cityCode);
                intent.putExtra("cityName",cityName);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
        layoutNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView(Bundle savedInstanceState) {
        layoutNear =findViewById(R.id.id_layout_near);
        rightImage =(ImageView)findViewById(R.id.id_right_img);
        mMapView = (MapView) findViewById(R.id.id_mapView);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        setLocationStyle();
        initMultiPoint();
        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置为true表示启动显示定位蓝点。
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
        aMap.setOnMyLocationChangeListener(this);
    }
    private void initMultiPoint() {
        overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.store_setaddr_xh));
        overlayOptions.anchor(0.5f,0.5f);
    }
    private void setLocationStyle() {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_xh));
        myLocationStyle.strokeColor(0xfff96331);
        myLocationStyle.radiusFillColor(0x40f96331);
    }
    @Override
    public void onMyLocationChange(Location location) {
        if ( location != null ) {
            lat=String.valueOf(location.getLatitude());
            lon=String.valueOf(location.getLongitude());
            if (mFirst){
                customDialog.show();
                mFirst =false;
            }
            initData();
        } else {
            ToastUtil.ToastText(context,"定位失败");
        }
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            cityCode=aMapLocation.getCityCode();
            cityName=aMapLocation.getCity();
            areaCode=aMapLocation.getAdCode();
            areaName=aMapLocation.getDistrict();
            address=aMapLocation.getAddress();
            lat=String.valueOf(latitude);
            lon=String.valueOf(longitude);
            locationClient.stopLocation();
        }
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.out_from_top);;
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
