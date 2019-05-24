package com.msht.minshengbao.functionActivity.myActivity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.adapter.NewAddressManagerAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.functionActivity.gasService.AddCustomerNoActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class NewAddressManagerActivity extends BaseActivity {
    private String     userId,password;
    private String addressId;
    private int   requestCodes=0;
    private CustomDialog customDialog;
    private XRecyclerView mRecyclerView;
    private NewAddressManagerAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address_manager);
        context=this;
        setCommonHeader("地址管理");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_data_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new NewAddressManagerAdapter(context,mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        customDialog.show();
        initData();
        initEvent();
        onClickEvent();

    }

    private void onClickEvent() {
        mAdapter.setOnSupplementListener(new NewAddressManagerAdapter.OnItemSupplementListener() {
            @Override
            public void onItemSupplement(View view, int position) {
                String address=mList.get(position).get("address");
                String id = mList.get(position).get("id");
                String cityId=mList.get(position).get("cityId");
                String cityName=mList.get(position).get("cityName");
                String name=mList.get(position).get("name");
                String phone=mList.get(position).get("phone");
                String longitude = mList.get(position).get("longitude");
                String latitude =mList.get(position).get("latitude");
                String locAddress=mList.get(position).get("locAddress");
                String doorAddress=mList.get(position).get("doorAddress");
                String customerNo=mList.get(position).get("customerNo");
                Intent intent=new Intent(context,ModifyAddressActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("city_id",cityId);
                intent.putExtra("city_name",cityName);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("locAddress",locAddress);
                intent.putExtra("doorAddress",doorAddress);
                intent.putExtra("customerNo",customerNo);
                startActivityForResult(intent,1);
            }
        });
        mAdapter.setOnCorrelationListener(new NewAddressManagerAdapter.OnItemCorrelationListener() {
            @Override
            public void onItemCorrelation(View view, int position) {
                String id = mList.get(position).get("id");
                Intent intent=new Intent(context,CorrelationGasCustomerNoActivity.class);
                intent.putExtra("addressId",id);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001||resultCode==0x002){
                    setResult(1);
                    initData();
                }
                break;
            default:
                break;
        }
    }

    private void initEvent() {
        findViewById(R.id.id_handwork_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
        findViewById(R.id.id_quick_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AddCustomerNoActivity.class);
                intent.putExtra("enterType",0);
                startActivityForResult(intent,1);
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }
            @Override
            public void onLoadMore() { }
        });
        mAdapter.setOnDeleteListener(new NewAddressManagerAdapter.OnSelectDeleteListener() {
            @Override
            public void onItemDeleteClick(View view, int position) {
                addressId=mList.get(position).get("id");
                onDeleteAddress();
            }
        });
        mAdapter.setOnItemCheckedListener(new NewAddressManagerAdapter.OnItemCheckedListener() {
            @Override
            public void onItemCheckedClick(View view, int position) {
                addressId=mList.get(position).get("id");
                requestCodes=2;
                initData();
            }
        });
        mAdapter.setOnItemSelectListener(new NewAddressManagerAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelectClick(View view, int position) {
                String address=mList.get(position).get("address");
                String id = mList.get(position).get("id");
                String cityId=mList.get(position).get("cityId");
                String cityName=mList.get(position).get("cityName");
                String name=mList.get(position).get("name");
                String phone=mList.get(position).get("phone");
                String longitude = mList.get(position).get("longitude");
                String latitude =mList.get(position).get("latitude");
                String locAddress=mList.get(position).get("locAddress");
                String doorAddress=mList.get(position).get("doorAddress");
                String customerNo=mList.get(position).get("customerNo");
                Intent intent=new Intent(context,ModifyAddressActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("city_id",cityId);
                intent.putExtra("city_name",cityName);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("locAddress",locAddress);
                intent.putExtra("doorAddress",doorAddress);
                intent.putExtra("customerNo",customerNo);
                startActivityForResult(intent,1);
            }
        });
    }

    private void onDeleteAddress() {

        new PromptDialog.Builder(this)
                .setTitle("删除地址")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确定删除该地址?")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestCodes=1;
                        initData();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void initData() {
        String validateURL ="";
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        if (requestCodes==0){
            validateURL = UrlUtil.NEW_ADDRESS_MANAGER_URL;
            textParams.put("category","0");
        }else if (requestCodes==1){
            validateURL = UrlUtil.DELETE_ADDRESS_URL;
            textParams.put("id",addressId);
        }else if (requestCodes==2){
            validateURL = UrlUtil.SET_DEFAULT_ADDRESS_URL;
            textParams.put("id",addressId);
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                if(requestCodes==0){
                    mRecyclerView.refreshComplete();
                    onAnalysisData(data.toString());
                }else {
                   onOperationSuccess(data.toString());
                }
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                if (requestCodes==0){
                    mRecyclerView.refreshComplete();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }

    private void onOperationSuccess(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                requestCodes=0;
                setResult(1);
                initData();
            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                mList.clear();
                JSONArray jsonArray=object.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cityName = jsonObject.getString("city_name");
                    String phone= jsonObject.getString("phone");
                    String meterNo= jsonObject.getString("meterNo");
                    String gasAddress=jsonObject.optString("gas_address");
                    String customerNo= jsonObject.getString("customerNo");
                    String locAddress = jsonObject.getString("loc_address");
                    String meterType=jsonObject.optString("meterType");
                    String id=jsonObject.optString("id");
                    String cityId=jsonObject.optString("city_id");
                    String flag=jsonObject.optString("flag");
                    String address=jsonObject.optString("address");
                    String name=jsonObject.optString("name");
                    String longitude=jsonObject.optString("longitude");
                    String latitude=jsonObject.optString("latitude");
                    String doorAddress=jsonObject.optString("door_address");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("cityName",cityName);
                    map.put("phone",phone);
                    map.put("meterNo",meterNo);
                    map.put("gasAddress", gasAddress);
                    map.put("customerNo",customerNo);
                    map.put("locAddress",locAddress);
                    map.put("meterType",meterType);
                    map.put("id",id);
                    map.put("cityId",cityId);
                    map.put("flag",flag);
                    map.put("address",address);
                    map.put("name",name);
                    map.put("longitude",longitude);
                    map.put("latitude",latitude);
                    map.put("doorAddress",doorAddress);
                    mList.add(map);
                }
                mAdapter.notifyDataSetChanged();

            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
