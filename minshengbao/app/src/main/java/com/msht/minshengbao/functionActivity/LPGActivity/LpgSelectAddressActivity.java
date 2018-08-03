package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.AddressAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 选择地址
 * @author hong
 * @date 2018/7/6  
 */
public class LpgSelectAddressActivity extends BaseActivity {

    private LoadMoreListView mListView;
    private AddressAdapter mAdapter;
    private View layoutView;
    private View layoutBtnNew;
    private TextView tvRightText;
    private String userId;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private int pageNo=1;
    private int pageIndex=0;
    private ArrayList<HashMap<String, String>> addressList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgSelectAddressActivity> mWeakReference;
        public RequestHandler(LpgSelectAddressActivity activity) {
            mWeakReference=new WeakReference<LpgSelectAddressActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgSelectAddressActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        Log.d("msg.obj=",msg.obj.toString());
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String msgError = object.optString("msg");
                        JSONObject jsonObject=object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            boolean isEndPage=jsonObject.optBoolean("isEndPage");
                            activity.jsonArray =jsonObject.optJSONArray("addressLists");
                            if (activity.jsonArray.length() == 0) {
                                if (activity.pageNo==1){
                                    activity.layoutView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                activity.onReceiveAddressData();
                                activity.layoutView.setVisibility(View.GONE);
                            }
                            if (isEndPage){
                                activity.mListView.loadComplete(false);
                            }else {
                                activity.mListView.loadComplete(true);
                            }

                        }else {
                            activity.displayDialog(msgError);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void displayDialog(String string) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveAddressData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String userId=jsonObject.getString("userId");
                String siteId=jsonObject.optString("siteId");
                String addressShort=jsonObject.optString("addressShort");
                String floor=jsonObject.optString("floor");
                String isElevator=jsonObject.optString("isElevator");
                String unit=jsonObject.optString("unit");
                String roomNum=jsonObject.optString("roomNum");
                String city=jsonObject.optString("city");
                String area=jsonObject.optString("area");
                String addressName=jsonObject.getString("addressName");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("userId", userId);
                map.put("siteId",siteId);
                map.put("address", addressName);
                map.put("name",addressShort);
                map.put("longitude", longitude);
                map.put("latitude", latitude);
                map.put("floor",floor);
                map.put("isElevator",isElevator);
                map.put("unit",unit);
                map.put("roomNum",roomNum);
                map.put("city",city);
                map.put("area",area);
                addressList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (addressList.size()!=0){
            mAdapter.notifyDataSetChanged();
            findViewById(R.id.id_re_nodata).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.id_re_nodata).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.GONE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_select_address);
        context=this;
        setCommonHeader("选择地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_ID,"");
        initHeaderView();
        initFindViewId();
        mAdapter=new AddressAdapter(this,addressList);
        mListView.setAdapter(mAdapter);
        initAddressData(1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGetData(position);
            }
        });
        mListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                initAddressData(pageIndex + 1);
            }
        });
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutBtnNew.setVisibility(View.VISIBLE);
            }
        });
        layoutBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEditAddress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                addressList.clear();
                mAdapter.notifyDataSetChanged();
                initAddressData(1);
                break;
                default:
                    break;
        }
    }

    private void onGetData(int position) {
        String siteId=addressList.get(position).get("siteId");
        String addressName=addressList.get(position).get("address");
        String addressShort=addressList.get(position).get("name");
        String longitude=addressList.get(position).get("longitude");
        String latitude=addressList.get(position).get("latitude");
        String floor=addressList.get(position).get("floor");
        String isElevator=addressList.get(position).get("isElevator");
        String unit=addressList.get(position).get("unit");
        String roomNum=addressList.get(position).get("roomNum");
        String city=addressList.get(position).get("city");
        String area=addressList.get(position).get("area");
        Intent intent=new Intent();
        intent.putExtra("siteId",siteId);
        intent.putExtra("addressName",addressName);
        intent.putExtra("addressShort",addressShort);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);
        intent.putExtra("isElevator",isElevator);
        intent.putExtra("floor",floor);
        intent.putExtra("unit",unit);
        intent.putExtra("roomNum",roomNum);
        intent.putExtra("city",city);
        intent.putExtra("area",area);
        setResult(1,intent);
        finish();
    }
    private void goEditAddress() {
        Intent intent=new Intent(context,LpgEditAddressActivity.class);
        startActivityForResult(intent,1);
    }
    private void initAddressData(int i) {
        pageIndex=i;
        pageNo=i;
        String pageNum=String.valueOf(pageNo);
        customDialog.show();
        String requestUrl = UrlUtil.LPG_QUERY_ALL_ADDRESS;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("pageNum",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(context).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initHeaderView() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("管理");
    }
    private void initFindViewId() {
        layoutView =findViewById(R.id.id_re_nodata);
        layoutBtnNew =findViewById(R.id.id_re_new_address);
        mListView=(LoadMoreListView)findViewById(R.id.id_address_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
