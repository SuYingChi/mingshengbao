package com.msht.minshengbao.functionActivity.publicModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.SelectAddressAdapter;
import com.msht.minshengbao.functionActivity.myActivity.AddAddressActivity;
import com.msht.minshengbao.functionActivity.myActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.myActivity.ModifyAddressActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.myActivity.NewAddressManagerActivity;

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
 * @date 2017/7/2  
 */
public class SelectAddressActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private SelectAddressAdapter mAdapter;
    private View layoutView;
    private View layoutBtnNew;
    private TextView tvRightText;
    private String userId;
    private String password;
    private String id;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SelectAddressActivity> mWeakReference;
        public RequestHandler(SelectAddressActivity activity) {
            mWeakReference=new WeakReference<SelectAddressActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SelectAddressActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.jsonArray.length() == 0) {
                                activity.layoutView.setVisibility(View.VISIBLE);
                            } else {
                                activity.onReceiveAddressData();
                                activity.layoutView.setVisibility(View.GONE);
                            }
                        }else {
                            activity.displayDialog(error);
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
        addrList.clear();
        mAdapter.notifyDataSetChanged();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String cityId=jsonObject.getString("city_id");
                String address=jsonObject.getString("address");
                String name=jsonObject.optString("name");
                String phone=jsonObject.optString("phone");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("city_id", cityId);
                map.put("address", address);
                map.put("name",name);
                map.put("phone",phone);
                map.put("longitude", longitude);
                map.put("latitude", latitude);
                addrList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (addrList.size()!=0){
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
        setContentView(R.layout.activity_select_address);
        context=this;
        setCommonHeader("选择地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initHeader();
        initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new SelectAddressAdapter(addrList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        mAdapter.setClickCallBack(new SelectAddressAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                String mAddress=addrList.get(position).get("address");
                String name=addrList.get(position).get("name");
                String phone=addrList.get(position).get("phone");
                String cityId=addrList.get(position).get("city_id");
                String longitude=addrList.get(position).get("longitude");
                String latitude=addrList.get(position).get("latitude");;
                Intent intent=new Intent();
                intent.putExtra("mAddress",mAddress);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("cityId",cityId);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                setResult(1,intent);
                finish();
            }
        });
        mAdapter.setOnItemClickListener(new SelectAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemSelectClick(int position) {
                String address=addrList.get(position).get("address");
                String id = addrList.get(position).get("id");
                String cityId=addrList.get(position).get("city_id");
                String longitude = addrList.get(position).get("longitude");
                String latitude =addrList.get(position).get("latitude");
                Intent intent=new Intent(context,ModifyAddressActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("city_id",cityId);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                startActivityForResult(intent,1);
            }
        });
        initData();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001||resultCode==0x002){
                    initData();
                }
                break;
            default:
                break;
        }
    }
    private void initEvent() {
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NewAddressManagerActivity.class);
                startActivityForResult(intent,1);
            }
        });
        layoutBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initView() {
        layoutView =findViewById(R.id.id_re_nodata);
        layoutBtnNew =findViewById(R.id.id_re_newaddress);
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_address_view);
    }
    private void initHeader() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("管理");
    }
    private void initData() {
        customDialog.show();
      //  String validateURL = UrlUtil.AddressManage_Url;
        String validateURL = UrlUtil.NEW_ADDRESS_MANAGER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
