package com.msht.minshengbao.functionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.tableAdapter;
import com.msht.minshengbao.functionActivity.GasService.GasExpenseQueryActivity;
import com.msht.minshengbao.functionActivity.GasService.SelectCustomerNo;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectTable;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */

public class SelfWriteFrag extends Fragment implements View.OnClickListener {

    private View     mSelectTable;
    private View     mSelectAddress;
    private Button   verifySend;
    private TextView tvSelectAddress;
    private EditText etSelectTable;
    private EditText etTableNum, etLast;
    private String   customerNo;
    private String   userId;
    private String   password;
    private String   tableId;
    private String   tableAddress;
    private String   tableBh;
    private String   lastNumber;
    private String   mMeter2;
    private tableAdapter adapter;
    private int pos=-1;
    private int    requestType= 0;
    private String validateURL;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
    private Activity activity;
    private final String mPageName ="自助抄表";
    private static final int REQUEST_CODE =1;
    public SelfWriteFrag() {  /** Required empty public constructor **/}
    private final RequestHandler requestHandler =new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SelfWriteFrag> mWeakReference;
        public RequestHandler(SelfWriteFrag reference) {
            mWeakReference=new WeakReference<SelfWriteFrag>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelfWriteFrag reference=mWeakReference.get();
            if (reference==null||reference.isDetached()||reference.activity.isFinishing()){
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestType==0){
                                reference.jsonArray =object.optJSONArray("data");
                                reference.onShowDialogs();
                            }else if (reference.requestType==1){
                                reference.jsonObject =object.optJSONObject("data");
                                reference.onReceiveData();
                            }else if (reference.requestType==2){
                                reference.onShowSuccess();
                            }
                        }else {
                            if (!reference.activity.isFinishing()){
                                reference.onShowFailure(error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.activity,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onShowSuccess() {
        requestType=1;
        validateURL = UrlUtil.SEARCH_BILL_GAS_URL;
        VariableUtil.detailList.clear();//清除账单明细数据
        customDialog.show();
        requestService();
    }
    private void onShowFailure(String error) {
        new PromptDialog.Builder(activity)
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
    private void onReceiveData() {
        String name=jsonObject.optString("name");
        String customerNum =jsonObject.optString("customerNo");
        String allBalance =jsonObject.optString("balance");
        String debts=jsonObject.optString("debts");
        String totalNum =jsonObject.optString("totalNum");
        String gasFee=jsonObject.optString("gas_fee");
        String discountFees =jsonObject.optString("discount_fee");
        String lateFees =jsonObject.optString("lateFee");
        JSONArray json=jsonObject.optJSONArray("detail_list");
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                String date = object.getString("date");
                String num = object.getString("num");
                String amount = object.getString("amount");
                String balance=object.getString("balance");
                String gasFees = object.getString("gas_fee");
                String discountFee=object.getString("discount_fee");
                String lateFee=object.getString("lateFee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("amounts",amount);
                map.put("balance",balance);
                map.put("gas_fees", gasFees);
                map.put("discount_fee", discountFee);
                map.put("lateFee", lateFee);
                VariableUtil.detailList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent payfees=new Intent(getActivity(), GasExpenseQueryActivity.class);
        payfees.putExtra("name", name);
        payfees.putExtra("CustomerNo", customerNum);
        payfees.putExtra("allBalance", allBalance);
        payfees.putExtra("debts", debts);
        payfees.putExtra("totalNum", totalNum);
        payfees.putExtra("gas_fee",gasFee);
        payfees.putExtra("discountFees", discountFees);
        payfees.putExtra("lateFee", lateFees);
        startActivity(payfees);
    }
    private void onShowDialogs() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String address = jsonObject.getString("address");
                String bh = jsonObject.getString("bh");
                String lastNum = jsonObject.getString("lastNum");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("address", address);
                map.put("bh", bh);
                map.put("lastNum",lastNum);
                tableList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mSelectTable.setEnabled(true);
        etSelectTable.setText(tableList.get(0).get("address"));
        etLast.setText(tableList.get(0).get("lastNum"));
        tableBh =tableList.get(0).get("bh");
        tableId=tableList.get(0).get("id");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_self_write_frage, container, false);
        activity=getActivity();
        customDialog=new CustomDialog(activity, "正在加载...");
        userId= SharedPreferencesUtil.getUserId(activity, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(activity, SharedPreferencesUtil.Password,"");
        initView(view);
        iniEvent();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //获取昵称设置返回数据
            case REQUEST_CODE:
                if(data!=null){
                    if (resultCode==2){
                        String addressName=data.getStringExtra("addressname");
                        String customer=data.getStringExtra("customerNo");
                        tvSelectAddress.setText(addressName);
                        customerNo=customer;
                        tableList.clear();
                        tablePickers();
                    }
                }
                break;
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void tablePickers() {
        validateURL = UrlUtil.GET_TABLE_URL;
        requestType=0;
        requestService();
    }
    private void requestService() {
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestUtil.getInstance(activity.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initView(View view) {
        mSelectTable =view.findViewById(R.id.id_select_table);
        mSelectAddress =view.findViewById(R.id.id_re_select);
        verifySend =(Button)view.findViewById(R.id.id_btn_verify);
        etSelectTable =(EditText) view.findViewById(R.id.id_tv_selecttable);
        tvSelectAddress =(TextView) view.findViewById(R.id.id_select_address);
        etTableNum =(EditText)view.findViewById(R.id.id_reading_tble);
        etLast =(EditText)view.findViewById(R.id.id_last_table);
        etLast.setInputType(InputType.TYPE_NULL);
        etSelectTable.setInputType(InputType.TYPE_NULL);
        mSelectTable.setEnabled(false);
        verifySend.setEnabled(false);
    }
    private void iniEvent() {
        mSelectTable.setOnClickListener(this);
        mSelectAddress.setOnClickListener(this);
        verifySend.setOnClickListener(this);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        etSelectTable.addTextChangedListener(myTextWatcher);
        etTableNum.addTextChangedListener(myTextWatcher);
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ( TextUtils.isEmpty(etSelectTable.getText().toString())||
                    TextUtils.isEmpty(etTableNum.getText().toString())) {
                verifySend.setEnabled(false);
            } else {
                verifySend.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_select_table:
                onSelectTable();
                break;
            case R.id.id_re_select:
                onSelectAddress();
                break;
            case R.id.id_btn_verify:
                onVerifySend();//提交抄表
                break;
            default:
                break;
        }
    }
    private void onSelectTable() {
        final SelectTable selectTable=new SelectTable(activity);
        final TextView tvTitle=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tvTitle.setText("选择表具");
        adapter=new tableAdapter(getActivity(),tableList,pos);
        mListView.setAdapter(adapter);
        selectTable.show();
        selectTable.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTable.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                tableId = tableList.get(position).get("id");
                tableAddress = tableList.get(position).get("address");
                tableBh = tableList.get(position).get("bh");
                lastNumber = tableList.get(position).get("lastNum");
                etSelectTable.setText(tableAddress);
                etLast.setText(lastNumber);
                selectTable.dismiss();
            }
        });
    }
    private void onSelectAddress() {
        Intent select=new Intent(activity,SelectCustomerNo.class);
        startActivityForResult(select, REQUEST_CODE);
    }
    private void onVerifySend() {
        String etTable= etSelectTable.getText().toString().trim();
        String mLast = etLast.getText().toString().trim();
        mMeter2 = etTableNum.getText().toString().trim();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        Date curData=new Date(System.currentTimeMillis());
        String time=format.format(curData) ;
        LayoutInflater inflater=LayoutInflater.from(activity);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.item_read_table,null);
        final Dialog dialog=new AlertDialog.Builder(activity).create();
        dialog.show();
        if (dialog.getWindow()!=null){
            dialog.getWindow().setContentView(layout);
        }
        final TextView readtime = (TextView)layout.findViewById(R.id.id_read_time);
        final TextView readaddr = (TextView)layout.findViewById(R.id.id_read_address);
        final TextView lastnum = (TextView)layout.findViewById(R.id.id_last_Num);
        final TextView readnum = (TextView)layout.findViewById(R.id.id_read_Num);
        final  Button btnCancel=(Button)layout.findViewById(R.id.id_cancel);
        readtime.setText(time);
        readaddr.setText(tvSelectAddress.getText().toString());
        lastnum.setText(mLast);
        readnum.setText(mMeter2);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final  Button btnComfirm=(Button)layout.findViewById(R.id.id_comfirm);
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                onSendTableNum();
                dialog.dismiss();
            }
        });
    }
    private void onSendTableNum() {
        requestType=2;
        String requestUrl =UrlUtil.SEND_TABLE_DATA_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        textParams.put("rqbId",tableId);
        textParams.put("meter", mMeter2);
        OkHttpRequestUtil.getInstance(activity.getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
