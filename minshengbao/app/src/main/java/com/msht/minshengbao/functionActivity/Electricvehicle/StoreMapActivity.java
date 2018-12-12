package com.msht.minshengbao.functionActivity.Electricvehicle;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/25 
 */
public class StoreMapActivity extends BaseActivity implements  AMap.OnMyLocationChangeListener {
    private double latitude,longitude;
    private double lat,lon;
    private String storeName, storeAddress;
    private String initDistance;
    private boolean buttonStatus =true;
    private TextView ttvDistance;
    private CardView mCardView;
    private ImageView buttonImg;
    private MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private MarkerOptions markerOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        context=this;
        mPageName="门店地址";
        Intent data=getIntent();
        latitude=data.getDoubleExtra("latitude",0.0);
        longitude=data.getDoubleExtra("longitude",0.0);
        storeName =data.getStringExtra("storeName");
        storeAddress =data.getStringExtra("storeAddress");
        initDistance =data.getStringExtra("distance");
        setCommonHeader(mPageName);
        initView(savedInstanceState);
        initEvent();
    }
    private void initEvent() {
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonStatus){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 40));
                    buttonImg.setImageResource(R.drawable.shoplocation_xh);
                    buttonStatus =false;
                }else {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 40));
                    buttonImg.setImageResource(R.drawable.setlocation_xh);
                    buttonStatus =true;
                }
            }
        });
    }
    private void initView(Bundle savedInstanceState) {
        TextView tvStoreAddress =(TextView)findViewById(R.id.id_store_address) ;
        TextView tvStoreName =(TextView)findViewById(R.id.id_store_name) ;
        ttvDistance =(TextView)findViewById(R.id.id_tv_distance);
        mCardView=(CardView)findViewById(R.id.id_card_view) ;
        buttonImg=(ImageView)findViewById(R.id.id_button_img) ;
        mMapView = (MapView) findViewById(R.id.id_mapView);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        setLocationStyle();
        onAddMarkerOption();
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 100));
        aMap.setOnMyLocationChangeListener(this);
        aMap.setMyLocationEnabled(true);
        ttvDistance.setText(initDistance);
        tvStoreName.setText(storeName);
        tvStoreAddress.setText(storeAddress);
    }

    private void onAddMarkerOption() {
        LatLng latLng=new LatLng(latitude,longitude);
        markerOption = new MarkerOptions();
        markerOption.position(latLng);
       /*markerOption.title(storeAddress)*/
        markerOption.draggable(false);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.store_setaddr_xh)));
    }

    private void setLocationStyle() {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) ;
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
            double mDistance= VariableUtil.twoDecinmal2(distance);
            String distanceText=String.valueOf(mDistance)+"m";
            ttvDistance.setText(distanceText);
        } else {
            ToastUtil.ToastText(context,"定位失败,请您检查定位设置权限");
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
