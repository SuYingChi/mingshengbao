package com.msht.minshengbao.FunctionView.Electricvehicle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.VechicAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.repairService.PublishOrder;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchStore extends BaseActivity {
    private EditText et_search;
    private MultiLineChooseLayout chooseLayout;
    private LoadMoreListView moreListView;
    private View layout_history;
    private VechicAdapter mStoreAdapter;
    private ArrayList<String> mDataList=new ArrayList<>();
    private ArrayList<HashMap<String, String>> StoreList = new ArrayList<HashMap<String, String>>();
    private int pageNo=1;//当前页数
    private int pageIndex=0;
    private String size = "100";//每页加载的大小
    private String keyword="";
    private String cityCode="",cityName="";
    private String areaCode="",areaName="";
    private String address="";
    private String userphone="";
    private double longitude=0.0,latitude=0.0;
    private String Longitude="0.0";
    private String Latitude="0.0";
    private final int  SUCCESS = 1;
    private final int  FAILURE = 0;
    private JSONArray jsonArray;
    private int requestType=0;

    Handler sendmeterHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("message");
                        if(Results.equals("success")) {
                            JSONObject jsonObject=object.optJSONObject("data");
                            jsonArray=jsonObject.getJSONArray("list");
                            if(jsonArray.length()==0){
                                moreListView.loadComplete(false);
                            }else {
                                moreListView.loadComplete(true);
                            }
                            initshowdata();
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
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("message");
                        if(Results.equals("success")) {
                            if (requestType==0){
                                JSONArray  Array=object.optJSONArray("data");
                                questionData(Array);
                            }else {
                                layout_history.setVisibility(View.GONE);
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

    private void questionData(JSONArray array) {
        try{
            for (int i=0;i<array.length();i++){
                JSONObject json = array.getJSONObject(i);
                String content = json.getString("keyword");
                mDataList.add(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        layout_history.setVisibility(View.VISIBLE);
        chooseLayout.setList(mDataList);      //显示数据
    }
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
                StoreList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (StoreList.size()!=0&&StoreList!=null){
            mStoreAdapter.notifyDataSetChanged();
            //mNodataText.setVisibility(View.GONE);
        }else {
           // mNodataText.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_search_store);
        context=this;
        userphone= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        Intent data=getIntent();
        Latitude=data.getStringExtra("lat");
        Longitude=data.getStringExtra("lon");
        areaCode=data.getStringExtra("areaCode");
        areaName=data.getStringExtra("areaName");
        cityCode=data.getStringExtra("cityCode");
        cityName=data.getStringExtra("cityName");
        address=data.getStringExtra("address");
        initView();
        initData();
        mStoreAdapter=new VechicAdapter(context,StoreList);
        moreListView.loadComplete(false);
        moreListView.setAdapter(mStoreAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String store_id=StoreList.get(position).get("id");
                String distance=StoreList.get(position).get("distance");
                Intent intent=new Intent(context,ElectricsStoreDetail.class);
                intent.putExtra("store_id",store_id);
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
        String dataurl="";
        if (requestType==0){
            dataurl= UrlUtil.Search_History;
        }else if (requestType==1){
            dataurl= UrlUtil.Clear_History;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        textParams.put("searchBy",userphone);
        HttpUrlconnectionUtil.executepost(dataurl,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });


    }
    private void initView() {
        et_search=(EditText)findViewById(R.id.et_search);
        layout_history=findViewById(R.id.id_re_history);
        chooseLayout=(MultiLineChooseLayout)findViewById(R.id.id_multiChoose);
        moreListView=(LoadMoreListView)findViewById(R.id.id_view_data);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        et_search.addTextChangedListener(myTextWatcher);
        findViewById(R.id.id_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_history.setVisibility(View.GONE);
                StoreList.clear();
                mStoreAdapter.notifyDataSetChanged();
                keyword=et_search.getText().toString().trim();
                requestSever(1);
            }
        });
        findViewById(R.id.id_delete_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionDialog();
            }
        });
        chooseLayout.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                keyword=text;
                et_search.setText(text);
                layout_history.setVisibility(View.GONE);
                StoreList.clear();
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
    private void ActionDialog() {
        new PromptDialog.Builder(this)
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
        pageIndex =i;
        pageNo=i;
        String dataurl = UrlUtil.ElectricList_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("type","1");
        textParams.put("keyword",keyword);
        textParams.put("latitude",Latitude);
        textParams.put("longitude",Longitude);
        textParams.put("areaCode",areaCode);
        textParams.put("areaName",areaName);
        textParams.put("cityCode",cityCode);
        textParams.put("cityName",cityName);
        textParams.put("address",address);
        textParams.put("searchBy",userphone);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize",size);
        HttpUrlconnectionUtil.executepost(dataurl,textParams, new ResultListener() {
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
