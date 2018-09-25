package com.msht.minshengbao.functionActivity.WaterApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.MoveSelectAddress.PoiSearchAdapter;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

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
 * @date 2018/8/16  
 */
public class WaterEquipmentMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, AMapLocationListener, View.OnClickListener, PoiSearch.OnPoiSearchListener, AMap.InfoWindowAdapter {
    private View layoutMap;
    private View layoutSearch;
    private ImageView locationImg;
    private boolean mFirst =true;
    private EditText autoText;
    private MapView mMapView = null;
    private AMap aMap;
    private double lat,lon;
    private AMapLocationClient locationClient;
    private MyLocationStyle myLocationStyle;
    private PoiSearchAdapter searchAdapter;
    private Context context;
    private TextView tvCancel;
    private CustomDialog customDialog;
    private String mCity;
    private ArrayList<MarkerOptions> markerOptionsArrayList=new ArrayList<MarkerOptions>();
    private ArrayList<HashMap<String, String>>  addressList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<WaterEquipmentMapActivity> mWeakReference;
        public RequestHandler(WaterEquipmentMapActivity activity) {
            mWeakReference = new WeakReference<WaterEquipmentMapActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterEquipmentMapActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what){
                case  SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        JSONArray dataArray=object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveData(dataArray);
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
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
    private void onFailure(String error) {
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
    private void onReceiveData(JSONArray dataArray) {
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);
                String equipmentNo=obj.optString("equipmentNo");
                String address=obj.optString("address");
                String communityName=obj.optString("communityName");
                String latitude=obj.optString("latitude");
                String longitude=obj.optString("longitude");
                double lat =obj.optDouble("latitude");
                double lon = obj.optDouble("longitude");
                //保证经纬度没有问题的时候可以填false
                LatLng latLng = new LatLng(lat, lon, true);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_water_xh));
                markerOptions.icon(bitmapDescriptor);
                markerOptions.position(latLng);
                markerOptions.title(address);
                markerOptionsArrayList.add(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (markerOptionsArrayList!=null&&markerOptionsArrayList.size()!=0){
            aMap.addMarkers(markerOptionsArrayList,false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_equipment_map);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setLocationLimit();
        initView(savedInstanceState);
    }

    private void setLocationLimit() {
        locationClient = ALocationClientFactory.createLocationClient(this, ALocationClientFactory.createDefaultOption(),this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .requestCode(ConstantUtil.MY_LOCATION_REQUEST)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .send();
            }else {
                locationClient.startLocation();
            }
        }else {
            locationClient.startLocation();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.id_mapView);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        autoText =(EditText) findViewById(R.id.et_search);
        tvCancel =(TextView)findViewById(R.id.id_cancel);
        layoutMap =findViewById(R.id.id_map_location);
        layoutSearch =findViewById(R.id.id_search_location);
        locationImg=(ImageView)findViewById(R.id.id_location_img);
        ListViewForScrollView searchData =(ListViewForScrollView)findViewById(R.id.id_search_data);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        //隐藏缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        setLocationStyle();
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置为true表示启动显示定位蓝点。
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
        aMap.setOnMyLocationChangeListener(this);
        aMap.setInfoWindowAdapter(this);
        tvCancel.setEnabled(false);
        tvCancel.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        findViewById(R.id.id_scan_view).setOnClickListener(this);
        findViewById(R.id.id_card_view).setOnClickListener(this);
        findViewById(R.id.id_edit_layout).setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        autoText.addTextChangedListener(myTextWatcher);
        autoText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvCancel.setEnabled(true);
                tvCancel.setVisibility(View.VISIBLE);
                layoutMap.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchAdapter=new PoiSearchAdapter(this,addressList);
        searchData.setAdapter(searchAdapter);
        searchData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lon=addressList.get(position).get("longitude");
                String lat =addressList.get(position).get("latitude");
                double latitude=Double.valueOf(lat);
                double longitude=Double.valueOf(lon);
                layoutMap.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                autoText.setText("");
                autoText.setCursorVisible(false);
                setSoftInputManager();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 40));
                locationImg.setVisibility(View.VISIBLE);
            }
        });
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
        if (location != null ) {
            lat=location.getLatitude();
            lon=location.getLongitude();
            if (mFirst){
                customDialog.show();
                mFirst =false;
            }
            initEquipmentData();
        } else {
            ToastUtil.ToastText(context,"定位失败");
        }
    }

    private void initEquipmentData() {
        String dataUrl = UrlUtil.WATER_EQUIPMENT_SEARCH;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.id_cancel:
                tvCancel.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.GONE);
                layoutMap.setVisibility(View.VISIBLE);
                setSoftInputManager();
                break;
            case R.id.id_edit_layout:
                tvCancel.setEnabled(true);
                tvCancel.setVisibility(View.VISIBLE);
                layoutMap.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.id_card_view:
                locationImg.setVisibility(View.GONE);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 40));
                break;
            case R.id.id_scan_view:
                onStartScanActivity();
                break;
            default:
                break;
        }
    }

    private void onStartScanActivity() {
        Intent intent =new Intent(context, QrCodeScanActivity.class);
        startActivity(intent);
        finish();
    }

    private void setSoftInputManager() {
        InputMethodManager inputMethodManager=(InputMethodManager)autoText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager!=null){
            inputMethodManager.hideSoftInputFromWindow(autoText.getWindowToken(),0);
        }
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0) {
                doSearchQuery(s.toString().trim());
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    private void doSearchQuery(String keyWord) {
        int currentPage = 0;
        /*
         *  Poi查询条件类
         *  第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
         */
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", mCity);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(16);
        // 设置查第一页
        query.setPageNum(currentPage);
        query.setCityLimit(true);
        // POI搜索
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if(rCode == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {
                addressList.clear();
                ArrayList<PoiItem> items = poiResult.getPois();
                for (PoiItem item : items) {
                    //获取经纬度对象
                    LatLonPoint llp = item.getLatLonPoint();
                    double lon = llp.getLongitude();
                    double lat = llp.getLatitude();
                    String longitude=Double.toString(lon);
                    String latitude=Double.toString(lat);
                    String title = item.getTitle();
                    String mContent=item.getProvinceName()+item.getCityName()
                            +item.getAdName()
                            +item.getSnippet();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("longitude",longitude);
                    map.put("latitude",latitude);
                    map.put("title", title);
                    map.put("mContent", mContent);
                    addressList.add(map);
                }
                searchAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if(requestCode==ConstantUtil.MY_LOCATION_REQUEST) {
                locationClient.startLocation();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==ConstantUtil.MY_LOCATION_REQUEST) {
                ToastUtil.ToastText(context,"获取位置授权失败");
            }
        }
    };
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            if (!TextUtils.isEmpty(aMapLocation.getCity())){
                mCity=aMapLocation.getCity();
            }else {
                mCity=aMapLocation.getDistrict();
            }
            locationClient.stopLocation();
        }
    }
    View infoWindow=null;
    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(this).inflate(
                    R.layout.custom_info_window, null);
        }
        render(marker, infoWindow);
    return infoWindow;
    }
    private void render(Marker marker, View infoWindow) {
        TextView name=(TextView)infoWindow.findViewById(R.id.title);
        name.setText(marker.getTitle());
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
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
    }
}
