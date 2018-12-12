package com.msht.minshengbao.functionActivity.Electricvehicle;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.VehicleAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/24  
 */
public class SearchStoreActivity extends BaseActivity {
    private EditText etSearch;
    private MultiLineChooseLayout chooseLayout;
    private LoadMoreListView moreListView;
    private View layoutHistory;
    private VehicleAdapter mStoreAdapter;
    private ArrayList<String> mDataList=new ArrayList<>();
    private ArrayList<HashMap<String, String>> mStoreList = new ArrayList<HashMap<String, String>>();
    private String keyword="";
    private String cityCode="",cityName="";
    private String areaCode="",areaName="";
    private String address="";
    private String userPhone ="";
    private String mLongitude ="0.0";
    private String mLatitude ="0.0";
    private JSONArray jsonArray;
    private int requestType=0;
    private final RequestHandler requestHandler =new RequestHandler(this);
    private final SendMeterHandler sendMeterHandler=new SendMeterHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SearchStoreActivity> mWeakReference;
        public RequestHandler(SearchStoreActivity activity) {
            mWeakReference=new WeakReference<SearchStoreActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SearchStoreActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("message");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONArray  dataArray=object.optJSONArray("data");
                                activity.onQuestionData(dataArray);
                            }else {
                                activity.layoutHistory.setVisibility(View.GONE);
                            }
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
    private static class SendMeterHandler extends Handler{
        private WeakReference<SearchStoreActivity> mWeakReference;
        private SendMeterHandler(SearchStoreActivity activity) {
            mWeakReference=new WeakReference<SearchStoreActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SearchStoreActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("message");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            activity.jsonArray=jsonObject.getJSONArray("list");
                            if(activity.jsonArray.length()==0){
                                activity.moreListView.loadComplete(false);
                            }else {
                                activity.moreListView.loadComplete(true);
                            }
                            activity.onReceiveData();
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
    private void onQuestionData(JSONArray array) {
        try{
            for (int i=0;i<array.length();i++){
                JSONObject json = array.getJSONObject(i);
                String content = json.getString("keyword");
                mDataList.add(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        layoutHistory.setVisibility(View.VISIBLE);
        chooseLayout.setList(mDataList);
    }
    private void onReceiveData() {
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
                mStoreList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mStoreList.size()!=0&& mStoreList !=null){
            mStoreAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_search_store);
        context=this;
        mPageName="搜索店铺";
        userPhone = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent data=getIntent();
        mLatitude =data.getStringExtra("lat");
        mLongitude =data.getStringExtra("lon");
        areaCode=data.getStringExtra("areaCode");
        areaName=data.getStringExtra("areaName");
        cityCode=data.getStringExtra("cityCode");
        cityName=data.getStringExtra("cityName");
        address=data.getStringExtra("address");
        initView();
        initData();
        mStoreAdapter=new VehicleAdapter(context, mStoreList);
        moreListView.loadComplete(false);
        moreListView.setAdapter(mStoreAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String storeId= mStoreList.get(position).get("id");
                String distance= mStoreList.get(position).get("distance");
                Intent intent=new Intent(context,ElectricsStoreDetailActivity.class);
                intent.putExtra("store_id",storeId);
                intent.putExtra("distance",distance);
                startActivity(intent);
            }
        });
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
               // requestSever(pageIndex + 1);
                moreListView.loadComplete(false);
            }
        });

    }
    private void initData() {
        String dataUrl="";
        if (requestType==0){
            dataUrl= UrlUtil.SEARCH_HISTORY;
        }else if (requestType==1){
            dataUrl= UrlUtil.CLEAR_HISTORY;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        textParams.put("searchBy", userPhone);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initView() {
        etSearch =(EditText)findViewById(R.id.et_search);
        layoutHistory =findViewById(R.id.id_re_history);
        chooseLayout=(MultiLineChooseLayout)findViewById(R.id.id_multiChoose);
        moreListView=(LoadMoreListView)findViewById(R.id.id_view_data);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etSearch.addTextChangedListener(myTextWatcher);
        findViewById(R.id.id_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHistory.setVisibility(View.GONE);
                mStoreList.clear();
                mStoreAdapter.notifyDataSetChanged();
                keyword= etSearch.getText().toString().trim();
                requestSever(1);
            }
        });
        findViewById(R.id.id_delete_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionDialog();
            }
        });
        chooseLayout.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                keyword=text;
                etSearch.setText(text);
                layoutHistory.setVisibility(View.GONE);
                mStoreList.clear();
                mStoreAdapter.notifyDataSetChanged();
                requestSever(1);
            }
        });
        findViewById(R.id.lay_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void onActionDialog() {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确认是否删除记录")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        requestType=1;
                        initData();
                    }
                })
                .show();

    }
    private void requestSever(int i) {
        String dataUrl = UrlUtil.ELECTRIC_LIST_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(i);
        textParams.put("type","1");
        textParams.put("keyword",keyword);
        textParams.put("latitude", mLatitude);
        textParams.put("longitude", mLongitude);
        textParams.put("areaCode",areaCode);
        textParams.put("areaName",areaName);
        textParams.put("cityCode",cityCode);
        textParams.put("cityName",cityName);
        textParams.put("address",address);
        textParams.put("searchBy", userPhone);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","100");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(dataUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,sendMeterHandler);
    }
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count>=1){
                requestType=0;
                mDataList.clear();
                initData();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
