package com.msht.minshengbao.functionActivity.myActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.AutomaticPayAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.ReplacePayAgreeActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.EnsureAddress;
import com.msht.minshengbao.custom.Dialog.InputCustomerNo;
import com.msht.minshengbao.custom.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class AutomatePayActivity extends BaseActivity implements View.OnClickListener {
    private AutomaticPayAdapter adapter;
    private View views;
    private TextView tvNoData, tvRightText;
    private String password,userId;
    private String id,customerNum, address;
    private int pos=-1;
    private ListView mListView;
    private int   requestCode=0;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> autoList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<AutomatePayActivity> mWeakReference;
        public RequestHandler(AutomatePayActivity activity) {
            mWeakReference = new WeakReference<AutomatePayActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {

            final AutomatePayActivity activity=mWeakReference.get();
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
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0) {
                                activity.jsonArray =object.optJSONArray("data");
                                if (activity.jsonArray.length() == 0) {
                                    activity.tvNoData.setVisibility(View.VISIBLE);
                                    activity.views.setVisibility(View.GONE);
                                } else {
                                    activity.onPayFee();
                                    activity.views.setVisibility(View.VISIBLE);
                                    activity.tvNoData.setVisibility(View.GONE);
                                }
                            }else if (activity.requestCode==1){
                                JSONObject objectInfo = object.optJSONObject("data");
                                activity.address = objectInfo.optString("address");
                                activity.requestCode=2;
                                activity.showDialogs("再仔细瞧瞧哦！");
                            }else if (activity.requestCode==2){
                                activity.autoList.clear();  //清除原来数据
                                activity.requestCode=0;
                                activity.requestService();
                            }else if (activity.requestCode==3){
                                if (activity.autoList!=null&&activity.autoList.size()>1){
                                    activity.autoList.remove(activity.pos);
                                    activity.adapter.notifyDataSetChanged();
                                }else {
                                    activity.requestCode=0;
                                    activity.requestService();
                                }
                            }
                        }else {
                            activity.displayDialog(error);
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
    private void displayDialog(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onPayFee() {
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
            tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else {
            tvNoData.setVisibility(View.VISIBLE);
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
        initFindViewByid();
        VariableUtil.boolSelect =false;
        adapter=new AutomaticPayAdapter(context,autoList);
        mListView.setAdapter(adapter);
        requestService();
        adapter.setOnItemDeleteButtonClickListener(new AutomaticPayAdapter.IOnItemButtonClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                pos=position;
                id =autoList.get(position).get("id");
                customerNum=autoList.get(position).get("customerNo");
                address =autoList.get(position).get("address");
                requestCode=3;
                showDialogs("确定要移除吗？");
            }
        });
    }
    private void showDialogs(String string) {
        final EnsureAddress ensureAddress=new EnsureAddress(this);
        ensureAddress.setTitleText(string);
        ensureAddress.setAddressText(address);
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
                requestService();
            }
        });
        ensureAddress.show();
    }
    private void initFindViewByid() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("编辑");
        tvRightText.setTag(0);
        findViewById(R.id.id_new_addBtn).setOnClickListener(this);
        views=findViewById(R.id.id_view);
        tvNoData =(TextView)findViewById(R.id.id_nodata);
        mListView = (ListView)findViewById(R.id.id_auto_address);
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
    private void requestService() {
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
            textParams.put("id", id);
        }
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_new_addBtn:
                Intent intent=new Intent(context, ReplacePayAgreeActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }
    private void showInputDialog() {
        final InputCustomerNo input=new InputCustomerNo(context);
        final TextView tvTitle=(TextView)input.getTitle();
        final EditText etCustomer=(EditText)input.getEditCustomer();
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
                customerNum=etCustomer.getText().toString().trim();
                requestCode=1;
                requestService();
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
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
