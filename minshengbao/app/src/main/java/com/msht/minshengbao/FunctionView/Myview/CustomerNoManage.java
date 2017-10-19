package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.SwipeAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.GasService.AddCustomerNo;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.EnsureAddress;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerNoManage extends BaseActivity implements View.OnClickListener {
    private SwipeAdapter adapter;
    private View views;
    private Button btn_Addr;
    private TextView nodata,tv_rightText;
    private String password,userId;
    private String houseId,customerNum,addr;
    private ListViewForScrollView mListView;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int   requestCode=0;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static final int ADDRESS_CODE=3;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (requestCode==0){
                            jsonArray =object.optJSONArray("data");
                        }
                        if(Results.equals("success")) {
                            if (requestCode==0) {
                                if (jsonArray.length() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                    views.setVisibility(View.GONE);
                                } else {
                                    initView();
                                    views.setVisibility(View.VISIBLE);
                                    nodata.setVisibility(View.GONE);
                                }
                            }else if (requestCode==1){
                                houseList.clear();
                                requestCode=0;
                                initgetjson();

                            }
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(CustomerNoManage.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void displayDialog(String string) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (requestCode==2){
                            houseList.clear();  //清除原来数据
                            initgetjson();
                        }
                        dialog.dismiss();
                    }
                }).show();
    }
    private void initView() {
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
                showdelete();
                return false;
            }
        });
    }
    private void showdelete() {
        LayoutInflater inflater=LayoutInflater.from(this);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.self_make_dialog,null);
        final Dialog dialog=new AlertDialog.Builder(CustomerNoManage.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        final TextView text=(TextView)layout.findViewById(R.id.id_text1);
        final RelativeLayout btnDelete=(RelativeLayout)layout.findViewById(R.id.id_query_layout);
        text.setText("删除燃气用户号");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode=1;
                requestSevice();
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
       // initHeader();
        initfindViewByid();
        VariableUtil.Boolselect=false;
        adapter = new SwipeAdapter(this,houseList, new SwipeAdapter.IOnItemButtonClickListener() {
            @Override
            public void onButtonClick(View v, int position) {
                houseId=houseList.get(position).get("id");
                addr=houseList.get(position).get("name");
                delectCustomerNo();
            }
        });
        mListView.setAdapter(adapter);
        initgetjson();
        initEvent();
    }
    private void delectCustomerNo() {
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
                        requestSevice();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADDRESS_CODE://获取昵称设置返回数据
                if(resultCode==1) {
                    houseList.clear();  //清除原来数据
                    initgetjson();
                }
                break;
        }
    }
    private void initfindViewByid() {
        tv_rightText=(TextView) findViewById(R.id.id_tv_rightText);
        tv_rightText.setVisibility(View.VISIBLE);
        tv_rightText.setText("编辑");
        views=findViewById(R.id.id_view);
        nodata=(TextView)findViewById(R.id.id_nodata);
        btn_Addr=(Button)findViewById(R.id.id_btn_customer);
        mListView = (ListViewForScrollView)findViewById(R.id.id_address_listview);
    }
    private void initgetjson() {
        customDialog.show();
        requestCode=0;
        String validateURL = UrlUtil.SelectAddress_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
    private void requestSevice(){
        customDialog.show();
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("houseId",houseId);
        if (requestCode==1){
            validateURL = UrlUtil.Address_delectUrl;
        }
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
    private void initEvent() {
        btn_Addr.setOnClickListener(this);
        tv_rightText.setTag(0);
        tv_rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        VariableUtil.Boolselect=true;
                        adapter.notifyDataSetChanged();
                        v.setTag(1);
                        tv_rightText.setText("撤销");
                        break;
                    case 1:
                        VariableUtil.Boolselect=false;
                        adapter.notifyDataSetChanged();
                        v.setTag(0);
                        tv_rightText.setText("编辑");
                        break;
                }
            }
        });
    }
    private void EnsureDialog() {
        final EnsureAddress ensureAddress=new EnsureAddress(this);
        ensureAddress.setTitleText("请详细查看");
        ensureAddress.setAddressText(addr);
        ensureAddress.setCustomerText(customerNum);
        ensureAddress.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureAddress.dismiss();
            }
        });
        ensureAddress.setOnpositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureAddress.dismiss();
                requestCode=2;
                requestSevice();
            }
        });
        ensureAddress.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_customer:
                Intent addaddress=new Intent(CustomerNoManage.this,AddCustomerNo.class);
                addaddress.putExtra("addresscode", ADDRESS_CODE);
                startActivityForResult(addaddress, 3);
                break;
            default:
                break;
        }
    }
}
