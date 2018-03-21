package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.AddrManageAdapter;
import com.msht.minshengbao.Adapter.MyWorkOrderAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressManage extends BaseActivity {
    private ListViewForScrollView mListView;
    private AddrManageAdapter mAdapter;
    private View   Rview;
    private String userId;
    private String password;
    private String Id;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private JSONArray jsonArray;
    private int   requestCodes=0;
    private CustomDialog customDialog;
    private final String mPageName ="地址管理";
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (requestCodes==0){
                            Log.d("地址",msg.obj.toString());
                            jsonArray =object.optJSONArray("data");
                        }
                        if(Results.equals("success")) {
                            if (requestCodes==0) {
                                if (jsonArray.length() == 0) {
                                    Rview.setVisibility(View.VISIBLE);
                                } else {
                                    initShow();
                                    Rview.setVisibility(View.GONE);
                                }
                            }else if (requestCodes==1||requestCodes==2){
                                requestCodes=0;
                                setResult(1);
                                addrList.clear();
                                mAdapter.notifyDataSetChanged();
                                initData();
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
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
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
                        dialog.dismiss();
                    }
                }).show();
    }
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String city_id=jsonObject.getString("city_id");
                String cityName=jsonObject.optString("city_name");
                String flag=jsonObject.optString("flag");
                String address=jsonObject.getString("address");
                String name=jsonObject.optString("name");
                String phone=jsonObject.optString("phone");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("city_id",city_id);
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
        mAdapter=new AddrManageAdapter(context,addrList);
        mListView.setAdapter(mAdapter);
        initData();
        initEvent();
        mAdapter.setOnItemSelectListener(new AddrManageAdapter.OnItemSelectListener() {
            @Override
            public void ItemSelectClick(View view, int thisposition) {
                String address=addrList.get(thisposition).get("address");
                String id = addrList.get(thisposition).get("id");
                String city_id=addrList.get(thisposition).get("city_id");
                String city_name=addrList.get(thisposition).get("city_name");
                String name=addrList.get(thisposition).get("name");
                String phone=addrList.get(thisposition).get("phone");
                String longitude = addrList.get(thisposition).get("longitude");
                String latitude =addrList.get(thisposition).get("latitude");
                Toast.makeText(context, latitude+","+longitude, Toast.LENGTH_SHORT)
                        .show();
                Intent intent=new Intent(context,ModifyAddress.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("city_id",city_id);
                intent.putExtra("city_name",city_name);
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
                Id=addrList.get(position).get("id");
                delectAddres();
                return true;
            }
        });
        mAdapter.setOnDelectListener(new AddrManageAdapter.OnSelectDelectListener() {
            @Override
            public void ItemDelectClick(View view, int thisposition) {
                Id=addrList.get(thisposition).get("id");
                delectAddres();
            }
        });
        mAdapter.setOnItemCheckedListener(new AddrManageAdapter.OnItemCheckedListener() {
            @Override
            public void ItemCheckedClick(View view, int thisposition) {
                Id=addrList.get(thisposition).get("id");
                requestCodes=2;
                initData();
            }
        });
    }
    private void delectAddres() {
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
                Intent intent=new Intent(context,AddAddress.class);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initView() {
        Rview=findViewById(R.id.id_re_nodata);
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
            textParams.put("id",Id);
        }else if (requestCodes==2){
            validateURL = UrlUtil.SetDefaultAddr_Url;
            textParams.put("id",Id);
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
}
