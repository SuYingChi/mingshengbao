package com.msht.minshengbao.FunctionActivity.Electricvehicle;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.msht.minshengbao.Adapter.VechicAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.MoveSelectAddress.GeoCoderUtil;
import com.msht.minshengbao.MoveSelectAddress.LatLngEntity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.MySwipeRefreshLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElectricHome extends BaseActivity implements MySwipeRefreshLayout.OnRefreshListener, AMapLocationListener {
    private MySwipeRefreshLayout refreshView;
    private LoadMoreListView moreListView;
    private VechicAdapter mAdapter;
    private TextView mHint;
    private TextView tv_address;
    private View layout_addr;
    private ImageView right_img;
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient locationClient;
    private String lat;
    private String lon;
    private String cityCode="",cityName="";
    private String areaCode="",areaName="";
    private String address="";
    private int pageNo=1;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static  final int MY_LOCATION_REQUEST=0;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    Handler sendmeterHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("code");
                        if(Results.equals("success")) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            boolean lastPage=jsonObject.getBoolean("lastPage");
                            jsonArray=jsonObject.getJSONArray("list");
                            if(jsonArray.length()!=0&&jsonArray!=null){
                                initshowdata();
                            }
                            if (lastPage){
                                moreListView.loadComplete(false);  //最后一页
                            }else {
                                moreListView.loadComplete(true);
                            }
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
    private void initshowdata() {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mList.size()!=0&&mList!=null){
            mAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_electric_home);
        context=this;
        setCommonHeader("电动车");
        initView(savedInstanceState);
        mAdapter=new VechicAdapter(context,mList);
        moreListView.setAdapter(mAdapter);
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
        initEvent();
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                pageNo++;
                initData();
            }
        });
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String store_id=mList.get(position).get("id");
                String distance=mList.get(position).get("distance");
                Intent intent=new Intent(context,ElectricsStoreDetail.class);
                intent.putExtra("store_id",store_id);
                intent.putExtra("distance",distance);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    lat=data.getStringExtra("lat");
                    lon=data.getStringExtra("lon");
                    address=data.getStringExtra("mAddress");
                    tv_address.setText(address);
                    double Latitude=Double.valueOf(lat);
                    double Longitude=Double.valueOf(lon);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), 40));
                }
                break;
            default:
                break;
        }
    }
    private void initEvent() {
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
        layout_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ReplaceAddress.class);
                startActivityForResult(intent,1);
            }
        });
        findViewById(R.id.id_layout_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElectricHome.this.startActivity(new Intent(context,ElectricMap.class));
                overridePendingTransition(R.anim.in_from_top, 0);
            }
        });
    }
    private void initView(Bundle savedInstanceState) {
        MyOncameraChange myOncameraChange=new MyOncameraChange();
        right_img=(ImageView)findViewById(R.id.id_right_img);
        tv_address=(TextView)findViewById(R.id.id_tv_address);
        layout_addr=findViewById(R.id.id_layout_addr);
        moreListView=(LoadMoreListView)findViewById(R.id.id_more_info);
        mHint = (TextView) findViewById(R.id.hint);
        refreshView=(MySwipeRefreshLayout) findViewById(R.id.id_refresh_view);
        refreshView.setOnRefreshListener(this);
        mMapView = (MapView) findViewById(R.id.id_mapView);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写
        aMap = mMapView.getMap();
        aMap.setOnCameraChangeListener(myOncameraChange); // 添加移动地图事件监听器
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);  //隐藏缩放按钮
        //滑动冲突
        moreListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(moreListView != null && moreListView.getChildCount() > 0){
                    boolean firstItemVisible = moreListView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = moreListView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshView.setEnabled(enable);
            }
        });
    }
    @Override
    public void onNormal() {
        mHint.setText("下拉查看位置");
    }
    @Override
    public void onLoose() {
        mHint.setText("松手进入");
    }
    @Override
    public void onRefresh() {
        refreshView.setRefreshing(false);
        refreshView.stopRefresh();
        ElectricHome.this.startActivity(new Intent(context,ElectricMap.class));
        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_top);
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            //移动地图中心到当前的定位位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 40));
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            cityCode=aMapLocation.getCityCode();
            cityName=aMapLocation.getCity();
            areaCode=aMapLocation.getAdCode();
            areaName=aMapLocation.getDistrict();
            address=aMapLocation.getAddress();
            String M=aMapLocation.getPoiName();
            tv_address.setText(M);
            lat=String.valueOf(latitude);
            lon=String.valueOf(longitude);
            mList.clear();
            locationClient.stopLocation();
            initData();

        }
    }
    private class MyOncameraChange implements AMap.OnCameraChangeListener{
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {}
        @Override
        public void onCameraChangeFinish(final CameraPosition cameraPosition) {
            LatLngEntity latLngEntity = new LatLngEntity(cameraPosition.target.latitude, cameraPosition.target.longitude);
            //地理反编码工具类，代码在后面
            GeoCoderUtil.getInstance(ElectricHome.this).geoAddress(latLngEntity, new GeoCoderUtil.GeoCoderAddressListener() {
                @Override
                public void onAddressResult(String result) {
                    if (result.contains("区")&&result.length()>=2){
                        int len=result.indexOf("区");
                        String address=result.substring(len+1);
                        tv_address.setText(address);
                    }else {
                        tv_address.setText(result);
                    }
                }
            });
            double lats=cameraPosition.target.latitude;
            double lons=cameraPosition.target.longitude;
            lat=String.valueOf(lats);
            lon=String.valueOf(lons);
            mList.clear();
            mAdapter.notifyDataSetChanged();
            pageNo=1;
            initData();
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
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        super.onDestroy();
    }
}
