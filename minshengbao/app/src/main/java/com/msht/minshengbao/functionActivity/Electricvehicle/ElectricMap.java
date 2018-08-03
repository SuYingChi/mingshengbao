package com.msht.minshengbao.functionActivity.Electricvehicle;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ElectricMap extends BaseActivity implements AMap.OnMyLocationChangeListener, AMapLocationListener {
    private View layout_near;
    private ImageView right_img;
    private String lat;
    private String lon;
    private String cityCode="",cityName="";
    private String areaCode="",areaName="";
    private String address="";
    private int pageNo=1;
    private String pageSize="100";
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private MapView mMapView = null;
    private AMap aMap;
    private boolean First=true;
    private AMapLocationClient locationClient;
    private MyLocationStyle myLocationStyle;
    private MultiPointOverlayOptions overlayOptions;
    private MultiPointOverlay multiPointOverlay;
    private static  final int MY_LOCATION_REQUEST=0;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    private CustomDialog customDialog;
    Handler sendmeterHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("code");
                        if(Results.equals("success")) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            jsonArray=jsonObject.getJSONArray("list");
                            initshowdata();
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private void initshowdata() {
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
                LatLng latLng = new LatLng(lat, lon, false);//保证经纬度没有问题的时候可以填false
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
    private void showfaiture(String error) {
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
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("");
        setlocation();
        initView(savedInstanceState);
        initEvent();
    }
    private void setlocation() {
        locationClient = ALocationClientFactory.createLocationClient(this, ALocationClientFactory.createDefaultOption(),this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .requestCode(MY_LOCATION_REQUEST)
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

    private void initData() {
        String dataurl = UrlUtil.ELECTRIC_LIST_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("type","0");
        textParams.put("latitude",lat);
        textParams.put("longitude",lon);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize",pageSize);
        SendrequestUtil.executepost(dataurl,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                sendmeterHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                sendmeterHandler.sendMessage(msg);
            }
        });
    }
    private void initEvent() {
        aMap.setOnMultiPointClickListener(new AMap.OnMultiPointClickListener() {
            @Override
            public boolean onPointClick(MultiPointItem multiPointItem) {
                String pos=multiPointItem.getTitle();
                if ((!pos.equals(""))&&pos!=null){
                    int position=Integer.valueOf(pos).intValue();
                    String store_id=mList.get(position).get("id");
                    String distance=mList.get(position).get("distance");
                    Intent intent=new Intent(context,ElectricsStoreDetail.class);
                    intent.putExtra("store_id",store_id);
                    intent.putExtra("distance",distance);
                    startActivity(intent);
                }
                return false;
            }
        });
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SearchStore.class);
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
        layout_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView(Bundle savedInstanceState) {
        layout_near=findViewById(R.id.id_layout_near);
        right_img=(ImageView)findViewById(R.id.id_right_img);
        mMapView = (MapView) findViewById(R.id.id_mapView);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);  //隐藏缩放按钮
        LocationStyle();
        MultiPoint();
        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
        aMap.setOnMyLocationChangeListener(this);
    }
    private void MultiPoint() {
        overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.store_setaddr_xh));//设置图标
        overlayOptions.anchor(0.5f,0.5f); //设置锚点
    }
    private void LocationStyle() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
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
            if (First){
                customDialog.show();
                First=false;
            }
            initData();
        } else {
            Toast.makeText(context,"定位失败",Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if(requestCode==MY_LOCATION_REQUEST) {
                locationClient.startLocation();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_LOCATION_REQUEST) {
                Toast.makeText(context,"获取位置授权失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

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
    }
}
