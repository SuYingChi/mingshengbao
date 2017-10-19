package com.msht.minshengbao.FunctionView.Myview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.AutopayAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.HtmlWeb.ReplacePayAgree;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.EnsureAddress;
import com.msht.minshengbao.ViewUI.Dialog.InputCustomerNo;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutomatePay extends BaseActivity implements View.OnClickListener {
    private AutopayAdapter adapter;
    private View views;
    private TextView nodata,tv_rightText;
    private String password,userId;
    private String Id,customerNum,addr;
    private int pos=-1;
    private ListView mListView;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int   requestCode=0;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> autoList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requestCode==0) {
                                jsonArray =object.optJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                    views.setVisibility(View.GONE);
                                } else {
                                    initView();
                                    views.setVisibility(View.VISIBLE);
                                    nodata.setVisibility(View.GONE);
                                }
                            }else if (requestCode==1){
                                JSONObject ObjectInfo = object.optJSONObject("data");
                                addr = ObjectInfo.optString("address");
                                requestCode=2;
                                showDialogs("再仔细瞧瞧哦！");
                            }else if (requestCode==2){
                                displayDialog("添加自动缴费地址完成");
                            }else if (requestCode==3){
                                autoList.remove(pos);
                                adapter.notifyDataSetChanged();
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
    private void displayDialog(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (requestCode==2){
                            autoList.clear();  //清除原来数据
                            requestCode=0;
                            requestSevice();
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
                String address= jsonObject.getString("address");
                String customerNo = jsonObject.getString("customerNo");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("customerNo", customerNo);
                map.put("address", address);
                autoList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (autoList.size()!=0){
            nodata.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else {
            nodata.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate_pay);
        context=this;
        setCommonHeader("自动缴费管理");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initfindViewByid();
        adapter=new AutopayAdapter(context,autoList, new AutopayAdapter.IOnItemButtonClickListener() {
            @Override
            public void onDelectClick(View v, int position) {
                pos=position;
                Id=autoList.get(position).get("id");
                customerNum=autoList.get(position).get("customerNo");
                addr=autoList.get(position).get("address");
                requestCode=3;
                showDialogs("确定要移除吗？");
            }
        });
        mListView.setAdapter(adapter);
        requestSevice();
    }
    private void showDialogs(String string) {
        final EnsureAddress ensureAddress=new EnsureAddress(this);
        ensureAddress.setTitleText(string);
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
                requestSevice();
            }
        });
        ensureAddress.show();

    }
    private void initfindViewByid() {
        tv_rightText=(TextView) findViewById(R.id.id_tv_rightText);
        tv_rightText.setVisibility(View.VISIBLE);
        tv_rightText.setText("添加");
        tv_rightText.setOnClickListener(this);
        views=findViewById(R.id.id_view);
        nodata=(TextView)findViewById(R.id.id_nodata);
        mListView = (ListView)findViewById(R.id.id_auto_address);
    }
    private void requestSevice() {
        customDialog.show();
        String validateURL="";
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        if (requestCode==0){
            validateURL = UrlUtil.AutomataPay_Url;
        }else if (requestCode==1){
            validateURL = UrlUtil.HouseSearch_Url;
            textParams.put("customerNo",customerNum);
        }else if (requestCode==2){
            validateURL = UrlUtil.Addautomate_AddUrl;
            textParams.put("customerNo",customerNum);
        }else if (requestCode==3){
            validateURL = UrlUtil.DelectAutopay_AddUrl;
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_tv_rightText:
                Intent intent=new Intent(context, ReplacePayAgree.class);
                startActivityForResult(intent,1);
                // showInputDialog();
                break;
            default:
                break;
        }
    }
    private void showInputDialog() {
        final InputCustomerNo input=new InputCustomerNo(context);
        final TextView tv_title=(TextView)input.getTitle();
        final EditText et_customer=(EditText)input.getEditCustomer();
        input.show();
        input.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.dismiss();
            }
        });
        input.setOnNextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerNum=et_customer.getText().toString().trim();
                requestCode=1;
                requestSevice();
                input.dismiss();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    showInputDialog();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
