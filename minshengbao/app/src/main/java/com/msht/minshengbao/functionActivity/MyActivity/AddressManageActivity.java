package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.msht.minshengbao.adapter.AddressManageAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressManageActivity extends BaseActivity {
    private ListViewForScrollView mListView;
    private AddressManageAdapter mAdapter;
    private View   layoutNoData;
    private String userId;
    private String password;
    private String addressId;
    private JSONArray jsonArray;
    private int   requestCodes=0;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<AddressManageActivity> mWeakReference;
        public RequestHandler(AddressManageActivity activity) {
            mWeakReference = new WeakReference<AddressManageActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final AddressManageActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if (activity.requestCodes==0){
                            activity.jsonArray =object.optJSONArray("data");
                        }
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCodes==0) {
                                if (activity.jsonArray.length() == 0) {
                                    activity.layoutNoData.setVisibility(View.VISIBLE);
                                } else {
                                    activity.onGetAddressInformation();
                                    activity.layoutNoData.setVisibility(View.GONE);
                                }
                            }else if (activity.requestCodes==1||activity.requestCodes==2){
                                activity.requestCodes=0;
                                activity.setResult(1);
                                activity.addrList.clear();
                                activity.mAdapter.notifyDataSetChanged();
                                activity.initData();
                            }
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
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
    private void onGetAddressInformation() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String cityId=jsonObject.getString("city_id");
                String cityName=jsonObject.optString("city_name");
                String flag=jsonObject.optString("flag");
                String address=jsonObject.getString("address");
                String name=jsonObject.optString("name");
                String phone=jsonObject.optString("phone");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("city_id",cityId);
                map.put("city_name",cityName);
                map.put("flag",flag);
                map.put("address",address);
                map.put("name",name);
                map.put("phone",phone);
                map.put("longitude",longitude);
                map.put("latitude",latitude);
                addrList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        context=this;
        setCommonHeader("地址管理");
        requestCodes=0;
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
        mAdapter=new AddressManageAdapter(context,addrList);
        mListView.setAdapter(mAdapter);
        initData();
        initEvent();
        mAdapter.setOnItemSelectListener(new AddressManageAdapter.OnItemSelectListener() {
            @Override
            public void ItemSelectClick(View view, int thisposition) {
                String address=addrList.get(thisposition).get("address");
                String id = addrList.get(thisposition).get("id");
                String cityId=addrList.get(thisposition).get("city_id");
                String cityName=addrList.get(thisposition).get("city_name");
                String name=addrList.get(thisposition).get("name");
                String phone=addrList.get(thisposition).get("phone");
                String longitude = addrList.get(thisposition).get("longitude");
                String latitude =addrList.get(thisposition).get("latitude");
                Intent intent=new Intent(context,ModifyAddress.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("city_id",cityId);
                intent.putExtra("city_name",cityName);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                startActivityForResult(intent,1);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addressId=addrList.get(position).get("id");
                onDeleteAddress();
                return true;
            }
        });
        mAdapter.setOnDelectListener(new AddressManageAdapter.OnSelectDelectListener() {
            @Override
            public void ItemDelectClick(View view, int thisposition) {
                addressId=addrList.get(thisposition).get("id");
                onDeleteAddress();
            }
        });
        mAdapter.setOnItemCheckedListener(new AddressManageAdapter.OnItemCheckedListener() {
            @Override
            public void ItemCheckedClick(View view, int thisposition) {
                addressId=addrList.get(thisposition).get("id");
                requestCodes=2;
                initData();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001||resultCode==0x002){
                    requestCodes=0;
                    addrList.clear();
                    mAdapter.notifyDataSetChanged();
                    setResult(1);
                    initData();
                }
                break;
            default:
                break;
        }
    }
    private void initEvent() {
        findViewById(R.id.id_re_newaddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initView() {
        layoutNoData =findViewById(R.id.id_re_nodata);
        mListView=(ListViewForScrollView)findViewById(R.id.id_address_view);
    }
    private void initData() {
        customDialog.show();
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        if (requestCodes==0){
            validateURL = UrlUtil.AddressManage_Url;
        }else if (requestCodes==1){
            validateURL = UrlUtil.DelectAddress_Url;
            textParams.put("id",addressId);
        }else if (requestCodes==2){
            validateURL = UrlUtil.SetDefaultAddr_Url;
            textParams.put("id",addressId);
        }
        SendrequestUtil.postDataFromService(validateURL,textParams, requestHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
