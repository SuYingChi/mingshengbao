package com.msht.minshengbao.functionActivity.electricVehicle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.MapAddressAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.base.RecordSQLiteOpenHelper;
import com.msht.minshengbao.MoveSelectAddress.ALocationClientFactory;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.widget.ListViewForScrollView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/16  
 */
public class ReplaceAddressActivity extends BaseActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener {
    private String mCity="";
    private String mArea;
    private String addressName;
    private String shortAddress;
    private String mLat,mLon;
    private View layoutHistory;
    private View layoutClear;
    private View layoutAddress;
    private View layoutSearch;
    private TextView tvLocation;
    private ImageView imgLocation;
    private EditText etSearch;
    private ListViewForScrollView mListView;
    private ListViewForScrollView historyListView;
    private MapAddressAdapter addressAdapter;
    private AMapLocationClient locationClient;
    /** 用于存放历史搜索记录 **/
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    private static  final int MY_LOCATION_REQUEST=0;
    private ArrayList<HashMap<String, String>>  mList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_address);
        context=this;
        int mActivityMode=getIntent().getIntExtra("mode",0);
        if (mActivityMode!=1){
            mPageName="切换地址";
        }else {
            mPageName="填写地址";
        }
        setCommonHeader(mPageName);
        initView();
        addressAdapter=new MapAddressAdapter(context,mList);
        mListView.setAdapter(addressAdapter);
        initEvent();
        initHistory();
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
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                etSearch.setText(name);
                layoutHistory.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mAddress=mList.get(position).get("mContent");
                String lat=mList.get(position).get("latitude");
                String lon=mList.get(position).get("longitude");
                String title=mList.get(position).get("title");
                String city=mList.get(position).get("city");;
                String area=mList.get(position).get("area");;
                // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
                boolean booleanHasData = hasData(mAddress.trim());
                // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                if (!booleanHasData) {
                    insertData(mAddress.trim());
                    queryData("");
                }
                Intent name=new Intent();
                name.putExtra("mAddress",mAddress);
                name.putExtra("lat",lat);
                name.putExtra("lon",lon);
                name.putExtra("title",title);
                name.putExtra("city",city);
                name.putExtra("area",area);
                setResult(1, name);
                finish();
            }
        });
        findViewById(R.id.id_layout_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent name=new Intent();
                name.putExtra("mAddress",addressName);
                name.putExtra("lat",mLat);
                name.putExtra("lon",mLon);
                name.putExtra("title",shortAddress);
                name.putExtra("city",mCity);
                name.putExtra("area",mArea);
                setResult(1, name);
                finish();
            }
        });
    }
    private void insertData(String mAddress) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + mAddress + "')");
        db.close();
    }
    private void initHistory() {
        // 2. 实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(context);
        // 3. 第1次进入时查询所有的历史搜索记录
        queryData("");
    }

    private void initEvent() {
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHistory.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.VISIBLE);
            }
        });
        layoutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空数据库
                deleteData();
                // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
                queryData("");
            }
        });
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationClient.startLocation();
            }
        });
    }
    private void initView() {
        layoutSearch =findViewById(R.id.id_search_view);
        layoutAddress =findViewById(R.id.id_layout_address);
        layoutHistory =findViewById(R.id.id_layout_history);
        layoutClear =findViewById(R.id.id_layout_clear);
        tvLocation =(TextView)findViewById(R.id.id_location) ;
        imgLocation =(ImageView)findViewById(R.id.id_img_location) ;
        historyListView =(ListViewForScrollView)findViewById(R.id.id_history_view);
        mListView =(ListViewForScrollView)findViewById(R.id.id_address_view);
        historyListView =(ListViewForScrollView)findViewById(R.id.id_history_view);
        etSearch =(EditText)findViewById(R.id.id_et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            // 输入文本后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                // 每次输入后，模糊查询数据库 & 显示
                // 注：若搜索框为空,则模糊搜索空字符 = 显示所有的搜索历史
                String tempName = etSearch.getText().toString().trim();
                if (tempName.length()>0) {
                    doSearchQuery(tempName);
                }

            }
        });
    }

    private void doSearchQuery(String keyWord) {
        int currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", mCity);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(16);
        // 设置查第一页
        query.setPageNum(currentPage);
        query.setCityLimit(true);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }
    private void queryData(String tempName) {
        // 1. 模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 2. 创建adapter适配器对象 & 装入模糊搜索的结果
        BaseAdapter adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 3. 设置适配器
        historyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        System.out.println(cursor.getCount());
        // 当输入框为空 & 数据库中有搜索记录时，显示 "删除搜索记录"按钮
        if (tempName.equals("") && cursor.getCount() != 0){
            layoutClear.setVisibility(View.VISIBLE);
        }
        else {
            layoutClear.setVisibility(View.GONE);
        }
    }
    /**
     * 清空数据库
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
    /**
     * 检查数据库中是否已经有该搜索记录
     */
    private boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //  判断是否有下一个
        return cursor.moveToNext();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            //移动地图中心到当前的定位位置
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            mCity=aMapLocation.getCity();
            mArea=aMapLocation.getDistrict();
            mLon=Double.toString(latitude);
            mLat=Double.toString(longitude);
            addressName=aMapLocation.getAddress();
            shortAddress  ="";
            if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                shortAddress = aMapLocation.getAoiName();
            }else {
                shortAddress = shortAddress + aMapLocation.getAoiName() ;
            }
            tvLocation.setText(shortAddress);
            locationClient.stopLocation();
        }
    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        mList.clear();
        if(rCode == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {
                ArrayList<PoiItem> items = poiResult.getPois();
                for (PoiItem item : items) {
                    //获取经纬度对象
                    LatLonPoint llp = item.getLatLonPoint();
                    double lon = llp.getLongitude();
                    double lat = llp.getLatitude();
                    String longitude=Double.toString(lon);
                    String latitude=Double.toString(lat);
                    String city=item.getCityName();
                    String area=item.getBusinessArea();
                    String title = item.getTitle();
                    String mContent=item.getAdName()+item.getSnippet();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("longitude",longitude);
                    map.put("latitude",latitude);
                    map.put("city",city);
                    map.put("area",area);
                    map.put("title", title);
                    map.put("mContent", mContent);
                    mList.add(map);
                }
                addressAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {}
    @Override
    protected void onDestroy() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        super.onDestroy();
    }

}
