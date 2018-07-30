package com.msht.minshengbao.FunctionActivity.Public;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.MoveSelectAddress.GeoCoderUtil;
import com.msht.minshengbao.MoveSelectAddress.LatLngEntity;
import com.msht.minshengbao.MoveSelectAddress.LocationBean;
import com.msht.minshengbao.MoveSelectAddress.PoiSearchAdapter;
import com.msht.minshengbao.MoveSelectAddress.PoiSearchTask;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/3/8  
 */
public class MoveSelectAddress extends BaseActivity implements AMap.OnCameraChangeListener, View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener, AMapLocationListener, PoiSearch.OnPoiSearchListener {
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient locationClient;
    private View layoutMap;
    private View layoutSearch;
    private TextView tvAddressDesc;
    private TextView tvCancel;
    private LinearLayout layoutPoi;
    private String mCity;
    private PoiAdapter poiAdapter;
    private PoiSearchAdapter searchAdapter;
    private LocationBean currentLoc;
    private static  final int MY_LOCATION_REQUEST=0;
    private static  final String EXTRA_DATA="addressInfo";
    private ArrayList<HashMap<String, String>>  mList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_select_address);
        initViews(savedInstanceState);
        context=this;
        mPageName ="定位地址";
        Intent data=getIntent();
        mCity=data.getStringExtra("city_name");
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
    private void initViews(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.id_mapView);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        //隐藏缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        layoutPoi = (LinearLayout) findViewById(R.id.ll_poi);
        layoutMap =(View)findViewById(R.id.id_map_location);
        layoutSearch =(View)findViewById(R.id.id_search_location);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        EditText autoText =(EditText) findViewById(R.id.et_search);
        tvAddressDesc = (TextView) findViewById(R.id.addressDesc);
        ListView lvData = (ListView) findViewById(R.id.lv_data);
        ListViewForScrollView searchData =(ListViewForScrollView)findViewById(R.id.id_search_data);
        tvCancel =(TextView)findViewById(R.id.id_cancel);
        // 添加移动地图事件监听器
        aMap.setOnCameraChangeListener(this);
        tvCancel.setEnabled(false);
        tvCancel.setOnClickListener(this);
        autoText.addTextChangedListener(this);
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
        ivBack.setOnClickListener(this);
        poiAdapter = new PoiAdapter(this);
        searchAdapter=new PoiSearchAdapter(this,mList);
        searchData.setAdapter(searchAdapter);
        searchData.setOnItemClickListener(this);
        lvData.setOnItemClickListener(this);
        lvData.setAdapter(poiAdapter);
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {}
    @Override
    public void onCameraChangeFinish(final CameraPosition cameraPosition) {


        LatLngEntity latLngEntity = new LatLngEntity(cameraPosition.target.latitude, cameraPosition.target.longitude);
        //地理反编码工具类，代码在后面
        GeoCoderUtil.getInstance(MoveSelectAddress.this).geoAddress(latLngEntity, new GeoCoderUtil.GeoCoderAddressListener() {
            @Override
            public void onAddressResult(String result) {
                tvAddressDesc.setText(result);
                currentLoc = new LocationBean(cameraPosition.target.longitude,cameraPosition.target.latitude,result,"");
                //地图的中心点位置改变后都开始poi的附近搜索
                PoiSearchTask.getInstance(MoveSelectAddress.this).setAdapter(poiAdapter).onSearch("", "",cameraPosition.target.latitude,cameraPosition.target.longitude);
            }
        });
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
                break;
            default:
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>0) {
            doSearchQuery(s.toString().trim());
        }
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
    public void afterTextChanged(Editable s) {}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.lv_data){
            //POI的地址的listview的item的点击
            LocationBean bean = (LocationBean) poiAdapter.getItem(position);
            String addressInfo=bean.getContent();
            String title=bean.getTitle();
            String longitude=Double.toString(bean.getLon());
            String latitude=Double.toString(bean.getLat());
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, addressInfo);
            intent.putExtra("title",title);
            intent.putExtra("longitude",longitude);
            intent.putExtra("latitude",latitude);
            setResult(1, intent);
            finish();
        }else if (parent.getId() == R.id.id_search_data){
            String addressInfo=mList.get(position).get("mContent");
            String title=mList.get(position).get("title");
            String longitude= mList.get(position).get("longitude");
            String latitude = mList.get(position).get("latitude");
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, addressInfo);
            intent.putExtra("title",title);
            intent.putExtra("longitude",longitude);
            intent.putExtra("latitude",latitude);
            setResult(1, intent);
            finish();
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
                ToastUtil.ToastText(context,"获取位置授权失败");
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            //移动地图中心到当前的定位位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 40));
            //获取定位信息
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            String addressStr =aMapLocation.getProvince()+ aMapLocation.getCity()
                    + aMapLocation.getDistrict()
                    +aMapLocation.getStreet()+aMapLocation.getStreetNum();
            if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                addressStr += aMapLocation.getAoiName()+ "附近";
            }else {
                addressStr = addressStr + aMapLocation.getAoiName() ;
            }
            tvAddressDesc.setText(addressStr);
            //这里是定位完成之后开始poi的附近搜索，代码在后面
            PoiSearchTask.getInstance(this).setAdapter(poiAdapter).onSearch("", "",latitude,longitude);
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
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        super.onDestroy();
    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if(rCode == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {
                mList.clear();
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
                    mList.add(map);
                }
                searchAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
    public class PoiAdapter extends BaseAdapter {
        private List<LocationBean> datas = new ArrayList<>();
        private final int RESOURCE = R.layout.item_app_list_poi;
        public PoiAdapter(Context context) {}
        @Override
        public int getCount() {
            return datas.size();
        }
        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView == null){
                vh = new ViewHolder();
                convertView = getLayoutInflater().inflate(RESOURCE, null);
                vh.tvTitle = (TextView) convertView.findViewById(R.id.address);
                vh.tvText = (TextView) convertView.findViewById(R.id.addressDesc);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            LocationBean bean = (LocationBean) getItem(position);
            vh.tvTitle.setText(bean.getTitle());
            vh.tvText.setText(bean.getContent());
            return convertView;
        }
        private class ViewHolder{
            TextView tvTitle;
            TextView tvText;
        }
        public void setData(List<LocationBean> datas){
            this.datas = datas;
            if(datas.size()>0){
                layoutPoi.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(MoveSelectAddress.this,"没有结果",Toast.LENGTH_SHORT).show();
                layoutPoi.setVisibility(View.GONE);
            }
        }
    }
}
