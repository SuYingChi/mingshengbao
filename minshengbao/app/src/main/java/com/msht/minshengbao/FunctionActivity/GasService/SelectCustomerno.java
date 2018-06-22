package com.msht.minshengbao.FunctionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.GetAddressAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectCustomerno extends BaseActivity {
    private Button btn_address;
    private ListViewForScrollView addresslist;
    private String userId,password;
    private int pos=-1;
    private JSONArray jsonArray;//数据解析
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static final int ADDRESS_CODE=1;
    private GetAddressAdapter adapter;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();

    Handler gethouseHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            initShow();
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    private void failure(String error) {
        new PromptDialog.Builder(this)
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
    private void initShow() {
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
        if (houseList.size()!=0){
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customerno);
        context=this;
        setCommonHeader("选择燃气用户号");
        userId = SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
        initfindViewByid();
        initEvent();
        adapter=new GetAddressAdapter(this, houseList,pos);
        addresslist.setAdapter(adapter);
        addresslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                String addressname=houseList.get(position).get("name");
                String customerNo=houseList.get(position).get("customerNo");
                String houseId=houseList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("addressname",addressname);
                name.putExtra("houseId",houseId);
                name.putExtra("customerNo",customerNo);
                setResult(2, name);
                finish();
            }
        });
        adapter.setRadioButtonClickListener(new GetAddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                pos=position;
                adapter.notifyDataSetChanged();
                String addressname=houseList.get(position).get("name");
                String customerNo=houseList.get(position).get("customerNo");
                String houseId=houseList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("addressname",addressname);
                name.putExtra("houseId",houseId);
                name.putExtra("customerNo",customerNo);
                setResult(2, name);
                finish();
            }
        });
        initrequest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADDRESS_CODE://获取昵称设置返回数据
                if(resultCode==1) {
                    houseList.clear();  ///清除原来数据
                    initrequest();
                }
                break;
        }
    }
    private void initfindViewByid() {
        btn_address=(Button)findViewById(R.id.id_btn_add_address);
        addresslist = (ListViewForScrollView)findViewById(R.id.id_listview);
    }

    private void initEvent() {
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addaddress=new Intent(context,AddCustomerNo.class);
                addaddress.putExtra("addresscode", ADDRESS_CODE);
                startActivityForResult(addaddress, 1);
            }
        });

    }

    private void initrequest() {
        String type="5";
        String validateURL = UrlUtil.SelectAddress_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                gethouseHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                gethouseHandler.sendMessage(msg);
            }
        });
    }
}
