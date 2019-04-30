package com.msht.minshengbao.functionActivity.waterApp;

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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.msht.minshengbao.MoveSelectAddress.GeoCoderUtil;
import com.msht.minshengbao.MoveSelectAddress.LatLngEntity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.adapter.WaterEquipmentListAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.MoveSelectAddress.PoiSearchAdapter;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.functionActivity.electricVehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.publicModule.QrCodeScanActivity;
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
 * @date 2018/8/16  
 */
public class WaterEquipmentMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, AMapLocationListener, View.OnClickListener, PoiSearch.OnPoiSearchListener, AMap.InfoWindowAdapter{
    private View layoutMap;
    private View layoutSearch;
    private View layoutNearEquipment;
    private View layoutEquipmentList;
    private TextView tvAddress;
    private ImageView locationImg;
    private boolean mFirst =true;
    private boolean requestFirst=false;
    private boolean moveFirst=true;
    private LoadMoreListView moreListView;
    private EditText autoText;
    private MapView mMapView = null;
    private AMap aMap;
    private double lat,lon;
    private int indexPage=0;
    private String latitude="";
    private String longitude="";
    private AMapLocationClient locationClient;
    private MyLocationStyle myLocationStyle;
    private PoiSearchAdapter searchAdapter;
    private WaterEquipmentListAdapter equipmentListAdapter;
    private Context context;
    private TextView tvCancel;
    private CustomDialog customDialog;
    private String mCity;
    private String defaultAddress,defaultName;
    private ArrayList<MarkerOptions> markerOptionsArrayList=new ArrayList<MarkerOptions>();
    private ArrayList<HashMap<String, String>>  addressList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>>  equipmentList = new ArrayList<HashMap<String, String>>();
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
                            CustomToast.showWarningLong(error);
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
                markerOptions.title(communityName);
                markerOptions.snippet(address);
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
        mPageName="售水机设备地图";
        customDialog=new CustomDialog(this, "正在加载");
        layoutEquipmentList=findViewById(R.id.id_equipment_list_layout);
        layoutNearEquipment=findViewById(R.id.id_layout_near);
        setLocationLimit();
        initView(savedInstanceState);
        initEvent();
        VariableUtil.mPos=-1;
        equipmentListAdapter=new WaterEquipmentListAdapter(context,equipmentList);
        moreListView.setAdapter(equipmentListAdapter);
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                initEquipmentNearData(indexPage+1,latitude,longitude);
            }
        });
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String lat=equipmentList.get(i).get("latitude");
                String lon=equipmentList.get(i).get("longitude");
                String communityName=equipmentList.get(i).get("communityName");
                if ((!TextUtils.isEmpty(lat))&&(!TextUtils.isEmpty(lon))&&!lat.equals(ConstantUtil.NULL_VALUE)){
                    VariableUtil.mPos=i;
                    equipmentListAdapter.notifyDataSetChanged();
                    requestFirst=false;
                    moveFirst=false;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TypeConvertUtil.convertToDouble(lat,20), TypeConvertUtil.convertToDouble(lon,110)), 80));
                    tvAddress.setText(communityName);
                    locationImg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void initEvent() {
        layoutNearEquipment.setTag(0);
        findViewById(R.id.id_right_img).setOnClickListener(this);
        findViewById(R.id.layout_back).setOnClickListener(this);
        layoutNearEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag=(Integer)view.getTag();
                switch (tag){
                    case 0:
                        layoutEquipmentList.setVisibility(View.VISIBLE);
                        view.setTag(1);
                        break;
                    case 1:
                        layoutEquipmentList.setVisibility(View.GONE);
                        view.setTag(0);
                        break;
                    default:
                        layoutEquipmentList.setVisibility(View.VISIBLE);
                        view.setTag(1);
                        break;
                }
            }
        });
    }
    private void setLocationLimit() {
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
                                CustomToast.showWarningLong("未允许定位权限，地图定位无法使用！");
                            }
                        }).start();
            }else {
                locationClient.startLocation();
            }
        }else {
            locationClient.startLocation();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initView(Bundle savedInstanceState) {
        View layoutHeader=findViewById(R.id.id_map_layout);
        layoutHeader.setBackgroundResource(R.drawable.shape_change_blue_bg);
        mMapView = (MapView) findViewById(R.id.id_mapView);
        ImageView ivBack = (ImageView) findViewById(R.id.id_back);
        tvAddress =(TextView)findViewById(R.id.id_tv_address);
        moreListView=(LoadMoreListView)findViewById(R.id.id_more_info) ;
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
        aMap.setOnCameraChangeListener(new MyOnCameraChange());
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
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 80));
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
            latitude=String.valueOf(lat);
            longitude=String.valueOf(lon);
            initEquipmentData();
            initEquipmentNearData(1,latitude,longitude);
        } else {
            ToastUtil.ToastText(context,"定位失败");
        }
    }
    private void initEquipmentNearData(int i, String latitude, String longitude) {
        String requestUrl=UrlUtil.WATER_EQUIPMENT_SEARCH_BY_LOCATE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        indexPage=i;
        textParams.put("type","2");
        textParams.put("latitude",latitude);
        textParams.put("longitude",longitude);
        textParams.put("pageNo",String.valueOf(indexPage));
        textParams.put("pageSize","16");
        Log.d("EquipmentNearData=",textParams.toString());
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                CustomToast.showWarningLong(data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("message");
            JSONObject jsonObject =object.optJSONObject("data");
            boolean firstPage=jsonObject.optBoolean("firstPage");
            boolean lastPage=jsonObject.optBoolean("lastPage");
            JSONArray jsonArray=jsonObject.optJSONArray("list");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (lastPage){
                    moreListView.loadComplete(false);
                }else {
                    moreListView.loadComplete(true);
                }
                if (indexPage==1){
                    equipmentList.clear();
                    VariableUtil.mPos=-1;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String equipmentNo = json.getString("equipmentNo");
                    String address=json.optString("address");
                    String communityName= json.getString("communityName");
                    String latitude = json.getString("latitude");
                    String longitude =json.optString("longitude");
                    String distance=json.optString("distance");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("equipmentNo",equipmentNo);
                    map.put("address", address);
                    map.put("communityName",communityName);
                    map.put("latitude",latitude);
                    map.put("longitude", longitude);
                    map.put("distance",distance);
                    equipmentList.add(map);
                }

            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        equipmentListAdapter.notifyDataSetChanged();

    }

    private void initEquipmentData() {
        String dataUrl = UrlUtil.WATER_EQUIPMENT_SEARCH;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
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
                onRequestLimit();
                break;
            case R.id.id_right_img:
                tvCancel.setEnabled(true);
                tvCancel.setVisibility(View.VISIBLE);
                layoutMap.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_back:
                layoutSearch.setVisibility(View.GONE);
                layoutMap.setVisibility(View.VISIBLE);
                setSoftInputManager();
                break;
            default:
                break;
        }
    }

    private void onRequestLimit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                onStartScanActivity();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CustomToast.showWarningLong("没有权限您将无法进行扫描操作！");
                            }
                        }).start();
            } else {
                onStartScanActivity();
            }
        } else {
            onStartScanActivity();
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
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            if (!TextUtils.isEmpty(aMapLocation.getCity())){
                mCity=aMapLocation.getCity();
            }else {
                mCity=aMapLocation.getDistrict();
            }
            defaultAddress=aMapLocation.getAddress();
            defaultName=aMapLocation.getPoiName();
            locationClient.stopLocation();

        }
    }
    View infoWindow=null;
    @Override
    public View getInfoWindow(Marker marker) {
        if ((!TextUtils.isEmpty(marker.getTitle()))&&(!marker.getTitle().equals(ConstantUtil.NULL_VALUE))){
            if (infoWindow == null) {
                infoWindow = LayoutInflater.from(this).inflate(
                        R.layout.custom_info_window, null);
            }
            render(marker, infoWindow);
            return infoWindow;
        }else {
            return null;
        }
    }
    private void render(Marker marker, View infoWindow) {
        TextView name=(TextView)infoWindow.findViewById(R.id.id_title);
        TextView content=(TextView)infoWindow.findViewById(R.id.id_content);
        content.setMaxEms(24);
        if (!TextUtils.isEmpty(marker.getTitle())){
            content.setText(marker.getSnippet());
            name.setText(marker.getTitle());
        }else {
            name.setText(defaultName);
            content.setText(defaultAddress);
        }
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    private class MyOnCameraChange implements AMap.OnCameraChangeListener{
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {}
        @Override
        public void onCameraChangeFinish(final CameraPosition cameraPosition) {
            LatLngEntity latLngEntity = new LatLngEntity(cameraPosition.target.latitude, cameraPosition.target.longitude);
            //地理反编码工具类，代码在后面
            GeoCoderUtil.getInstance(getApplicationContext()).geoAddress(latLngEntity, new GeoCoderUtil.GeoCoderAddressListener() {
                @Override
                public void onAddressResult(String result) {
                    if (moveFirst){
                        if (result.contains("街道")&&result.length()>=3){
                            int len=result.indexOf("道");
                            String address=result.substring(len+1);
                            tvAddress.setText(address);
                        }else {
                            tvAddress.setText(result);
                        }
                    }
                    moveFirst=true;
                }
            });
            if (requestFirst){
                initEquipmentNearData(1,String.valueOf(cameraPosition.target.latitude),String.valueOf(cameraPosition.target.longitude));
            }
            requestFirst=true;
        }
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
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}