package com.msht.minshengbao.functionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.SwipeAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.GasService.AddCustomerNoActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

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
 * @date 2016/6/7  
 */
public class CustomerNoManage extends BaseActivity implements View.OnClickListener {
    private SwipeAdapter adapter;
    private View     views;
    private Button   btnAddress;
    private TextView tvNoData,tvRightText;
    private String   password,userId;
    private String   houseId;
    private ListViewForScrollView mListView;
    private int          requestCode=0;
    private JSONArray    jsonArray;
    private CustomDialog customDialog;
    private static final int ADDRESS_CODE=3;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<CustomerNoManage> mWeakReference;
        public RequestHandler(CustomerNoManage activity) {
            mWeakReference = new WeakReference<CustomerNoManage>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final CustomerNoManage activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if (activity.requestCode==0){
                            activity.jsonArray =object.optJSONArray("data");
                        }
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0) {
                                if (activity.jsonArray.length() == 0) {
                                    activity.tvNoData.setVisibility(View.VISIBLE);
                                    activity.views.setVisibility(View.GONE);
                                } else {
                                    activity.onCustomerNoList();
                                    activity.views.setVisibility(View.VISIBLE);
                                    activity.tvNoData.setVisibility(View.GONE);
                                }
                            }else if (activity.requestCode==1){
                                activity.houseList.clear();
                                activity.adapter.notifyDataSetChanged();
                                activity.requestCode=0;
                                activity.onGetCustomerNoData();

                            }
                        }else {
                            activity.onDisplayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
    private void onDisplayDialog(String string) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (requestCode==2){
                            houseList.clear();  //清除原来数据
                            onGetCustomerNoData();
                        }
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onCustomerNoList() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String customerNo = jsonObject.getString("customerNo");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("customerNo", customerNo);
                houseList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                houseId = houseList.get(position).get("id");
                onDeleteData();
                return false;
            }
        });
    }
    private void onDeleteData() {
        LayoutInflater inflater=LayoutInflater.from(this);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.self_make_dialog,null);
        final Dialog dialog=new AlertDialog.Builder(CustomerNoManage.this).create();
        dialog.show();
        if (dialog.getWindow()!=null){
            dialog.getWindow().setContentView(layout);
        }
        final TextView text=(TextView)layout.findViewById(R.id.id_text1);
        final RelativeLayout btnDelete=(RelativeLayout)layout.findViewById(R.id.id_query_layout);
        text.setText("删除燃气用户号");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode=1;
                requestService();
                dialog.dismiss();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_no_manage);
        context=this;
        setCommonHeader("燃气用户号");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        VariableUtil.boolSelect =false;
        adapter = new SwipeAdapter(this,houseList);
        mListView.setAdapter(adapter);
        onGetCustomerNoData();
        initEvent();
        adapter.setOnDelectListener(new SwipeAdapter.IOnItemButtonClickListener() {
            @Override
            public void onButtonClick(View v, int position) {
                houseId=houseList.get(position).get("id");
                deleteCustomerNo();
            }
        });
    }
    private void deleteCustomerNo() {
        new PromptDialog.Builder(this)
                .setTitle("删除燃气用户号")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确定删除该燃气用户号?")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestCode=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取昵称设置返回数据
            case ADDRESS_CODE:
                if(resultCode==1) {
                    houseList.clear();  //清除原来数据
                    onGetCustomerNoData();
                }
                break;
            default:
                break;
        }
    }
    private void initFindViewId() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("编辑");
        views=findViewById(R.id.id_view);
        tvNoData =(TextView)findViewById(R.id.id_nodata);
        btnAddress =(Button)findViewById(R.id.id_btn_customer);
        mListView = (ListViewForScrollView)findViewById(R.id.id_address_listview);
    }
    private void onGetCustomerNoData() {
        customDialog.show();
        requestCode=0;
        String validateURL = UrlUtil.SELECT_ADDRESS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void requestService(){
        customDialog.show();
        String validateURL="";
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("houseId",houseId);
        if (requestCode==1){
            validateURL = UrlUtil.Address_delectUrl;
        }
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initEvent() {
        btnAddress.setOnClickListener(this);
        tvRightText.setTag(0);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        VariableUtil.boolSelect =true;
                        adapter.notifyDataSetChanged();
                        v.setTag(1);
                        tvRightText.setText("撤销");
                        break;
                    case 1:
                        VariableUtil.boolSelect =false;
                        adapter.notifyDataSetChanged();
                        v.setTag(0);
                        tvRightText.setText("编辑");
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_customer:
                Intent addAddress=new Intent(CustomerNoManage.this,AddCustomerNoActivity.class);
                addAddress.putExtra("addresscode", ADDRESS_CODE);
                startActivityForResult(addAddress, 3);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
