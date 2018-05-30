package com.msht.minshengbao.FunctionActivity.Electricvehicle;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

public class StoreMap extends BaseActivity implements  AMap.OnMyLocationChangeListener {
    private double latitude,longitude;
    private double lat,lon;
    private String store_name,store_address;
    private String  initdistance;
    private boolean buttonstatus=true;
    private boolean Locationstatus=true;
    private TextView tv_storeName,tv_storeAddress;
    private TextView tv_distance;
    private CardView mCardView;
    private ImageView buttonImg;
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;
    private MarkerOptions markerOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        context=this;
        Intent data=getIntent();
        latitude=data.getDoubleExtra("latitude",0.0);
        longitude=data.getDoubleExtra("longitude",0.0);
        store_name=data.getStringExtra("store_name");
        store_address=data.getStringExtra("store_address");
        initdistance=data.getStringExtra("distance");
        setCommonHeader("门店地址");
        initView(savedInstanceState);
        initEvent();
    }
    private void initEvent() {
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonstatus){
                    if (Locationstatus){
                        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点。
                        Locationstatus=false;
                    }else {
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 40));
                    }
                    buttonImg.setImageResource(R.drawable.shoplocation_xh);
                    buttonstatus=false;
                }else {
                    buttonImg.setImageResource(R.drawable.setlocation_xh);
                    buttonstatus=true;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 40));
                }
            }
        });
    }
    private void initView(Bundle savedInstanceState) {
        tv_storeAddress=(TextView)findViewById(R.id.id_store_address) ;
        tv_storeName=(TextView)findViewById(R.id.id_store_name) ;
        tv_distance=(TextView)findViewById(R.id.id_tv_distance);
        mCardView=(CardView)findViewById(R.id.id_card_view) ;
        buttonImg=(ImageView)findViewById(R.id.id_button_img) ;
        mMapView = (MapView) findViewById(R.id.id_mapView);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);  //隐藏缩放按钮
        LocationStyle();
        AddMarkerOption();
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 100));
        aMap.setOnMyLocationChangeListener(this);
        tv_distance.setText(initdistance);
        tv_storeName.setText(store_name);
        tv_storeAddress.setText(store_address);
    }

    private void AddMarkerOption() {
        LatLng latLng=new LatLng(latitude,longitude);
        markerOption = new MarkerOptions();
        markerOption.position(latLng);
       // markerOption.title(store_address);
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.store_setaddr_xh)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
       // markerOption.setFlat(true);//设置marker平贴地图效果
    }

    private void LocationStyle() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_xh));
        myLocationStyle.strokeColor(0xfff96331);
        myLocationStyle.radiusFillColor(0x40f96331);
    }
    @Override
    public void onMyLocationChange(Location location) {
        if ( location != null ) {
            lat=location.getLatitude();
            lon=location.getLongitude();
            LatLng latLng=new LatLng(latitude,longitude);
            LatLng latLng1=new LatLng(lat,lon);
            float distance = AMapUtils.calculateLineDistance(latLng,latLng1);
            double mDistance= VariableUtil.TwoDecinmal2(distance);
            tv_distance.setText(String.valueOf(mDistance)+"m");
        } else {
            Toast.makeText(context,"定位失败",Toast.LENGTH_SHORT).show();
        }
    }
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
        mMapView.onDestroy();
        super.onDestroy();
    }
}
