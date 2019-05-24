package com.msht.minshengbao.functionActivity.gasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.Dialog.SelectDialog;
import com.msht.minshengbao.adapter.GetHouseAdapter;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;

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
 * @date 2019/1/8 
 */
public class GasInternetTableActivity extends BaseActivity implements View.OnClickListener {
    private TextView   tvNoDataTip;
    private ListView mLstView;
    private String userId;
    private String password;
    private String customerNo;
    private String meterNo;
    private String mAddress;
    private int    requestType=0;
    private CustomDialog customDialog;
    private JSONArray jsonArray;
    private GetHouseAdapter adapter ;
    private ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<GasInternetTableActivity> mWeakReference;
        public RequestHandler(GasInternetTableActivity activity) {
            mWeakReference=new WeakReference<GasInternetTableActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasInternetTableActivity activity=mWeakReference.get();
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
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                activity.jsonArray =object.optJSONArray("data");
                                activity.houseList.clear();
                                activity.onCustomerAddressData();
                            }else if (activity.requestType==1){
                                JSONArray array=object.optJSONArray("data");
                                activity.onTableData(array);
                            }else if (activity.requestType==2){
                                activity.customerNo="";
                                JSONObject jsonObject =object.optJSONObject("data");
                                activity.onReceiveTableData(jsonObject);
                            }else if (activity.requestType==3){
                                activity.customerNo="";
                                JSONObject jsonObject =object.optJSONObject("data");
                                activity.onReceiveGasExpenseData(jsonObject);
                            }
                        }else {
                            //清空原来数据
                            activity.customerNo="";
                            if (!activity.isFinishing()){
                                activity.displayDialog(error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    //清空原来数据
                    activity.customerNo="";
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveGasExpenseData(JSONObject jsonObject) {
        String name=jsonObject.optString("name");
        String customerNum=jsonObject.optString("customerNo");
        String allBalance=jsonObject.optString("balance");
        String debts=jsonObject.optString("debts");
        String totalNum=jsonObject.optString("total_num");
        String gasFee=jsonObject.optString("gas_fee");
        String discountFees=jsonObject.optString("discount_fee");
        String lateFee=jsonObject.optString("late_fee");
        JSONArray json=jsonObject.optJSONArray("detail_list");
        VariableUtil.detailList.clear();
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                String date = object.getString("date");
                String num = object.getString("num");
                String gasFees = object.getString("gas_fee");
                String amounts=object.getString("amount");
                String balance=object.getString("balance");
                String discountFee=object.getString("discount_fee");
                String lateFees=object.getString("late_fee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("gas_fees", gasFees);
                map.put("amounts",amounts);
                map.put("balance",balance);
                map.put("discount_fee", discountFee);
                map.put("late_fee", lateFees);
                VariableUtil.detailList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent payFees=new Intent(context, GasExpenseQueryActivity.class);
        payFees.putExtra("name", name);
        payFees.putExtra("CustomerNo", customerNum);
        payFees.putExtra("allBalance",allBalance);
        payFees.putExtra("debts", debts);
        payFees.putExtra("totalNum", totalNum);
        payFees.putExtra("discountFees", discountFees);
        payFees.putExtra("gasFee",gasFee);
        payFees.putExtra("lateFee", lateFee);
        startActivity(payFees);
    }
    private void onReceiveTableData(JSONObject jsonObject) {
        String address=jsonObject.optString("address");
        String name=jsonObject.optString("name");
        String lastRechargeAmount=jsonObject.optString("lastRechargeAmount");
        String customerNo=jsonObject.optString("customerNo");
        String lastRechargeTime=jsonObject.optString("lastRechargeTime");
        Intent intent=new Intent(context,GasInternetTableRechargeActivity.class);
        intent.putExtra("address",address);
        intent.putExtra("customerNo",customerNo);
        intent.putExtra("lastRechargeAmount",lastRechargeAmount);
        intent.putExtra("lastRechargeTime",lastRechargeTime);
        intent.putExtra("meterNo",meterNo);
        startActivity(intent);
    }
    private void onTableData(JSONArray array) {
        tableList.clear();
        if (array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.optJSONObject(i);
                String id = jsonObject.optString("id");
                String bh = jsonObject.optString("bh");
                String address = jsonObject.optString("address")+"(表号:"+bh+")";
                String moduleType = "2";
                String lastNum = jsonObject.optString("lastNum");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("moduleType", moduleType);
                map.put("name", address);
                map.put("bh", bh);
                map.put("lastNum", lastNum);
                tableList.add(map);
            }
            JSONObject json = array.optJSONObject(0);
            int  tableType = json.optInt("lx");
            switch (tableType){
                case 11:
                    onOrdinaryTableExpense();
                    break;
                case 12:
                    onIcCardTable();
                    break;
                case 17:
                    onInternetTable();
                    break;
                default:
                    break;
            }
        }
    }
    private void onInternetTable() {
        if (context!=null&&tableList!=null){
            new SelectDialog(context,tableList,-1).builder()
                    .setTitleText("请选择表具")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .setOnSheetItemClickListener(new SelectDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            meterNo = tableList.get(position).get("bh");
                            onSearchRecord(meterNo);
                        }
                    })
                    .show();
        }
    }
    private void onCustomerAddressData() {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (houseList!=null&&houseList.size()>0){
            tvNoDataTip.setVisibility(View.GONE);
        }else {
            tvNoDataTip.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
    private void displayDialog(String s) {
        if (context!=null){
            new PromptDialog.Builder(context)
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_table);
        context=this;
        setCommonHeader("物联网表");
        customDialog=new CustomDialog(context, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initFindViewId();
        VariableUtil.mPos=-1;
        adapter=new GetHouseAdapter(context,houseList);
        mLstView.setAdapter(adapter);
        initHouseData();
        mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerNo = houseList.get(position).get("customerNo");
                mAddress=houseList.get(position).get("name");
                onQueryTableType();
                //选择地址后按钮有效点击，
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE1:
                if (resultCode==ConstantUtil.VALUE1){
                    requestType=0;
                    initHouseData();
                }
                break;
            default:
                break;
        }
    }
    private void onQueryTableType() {
        requestType=1;
        customDialog.show();
        String validateURL=UrlUtil.GET_TABLE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onSearchRecord(String meterNo) {
        requestType=2;
        customDialog.show();
        String validateURL=UrlUtil.INTERNET_TABLE_LAST_DATA;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("meterNo",meterNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onOrdinaryTableExpense() {
        requestType=3;
        customDialog.show();
        String validateURL=UrlUtil.SEARCH_BILL_GAS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initHouseData() {
        requestType=0;
        customDialog.show();
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        textParams.put("meterType","17");
        String validateURL= UrlUtil.SELECT_ADDRESS_URL;;
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void initFindViewId() {
        findViewById(R.id.id_recharge_record).setOnClickListener(this);
        findViewById(R.id.id_price_explain).setOnClickListener(this);
        tvNoDataTip=(TextView)findViewById(R.id.id_tv_tip);
        mLstView=(ListView)findViewById(R.id.id_address_dataList);
        findViewById(R.id.id_newAdd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAddress=new Intent(context,AddCustomerNoActivity.class);
                addAddress.putExtra("enterType",2);
                startActivityForResult(addAddress, 1);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.id_recharge_record:
                onRechargeRecord();
                break;
            case R.id.id_price_explain:
                onGasPrice();
                break;
            default:
                break;
        }
    }

    private void onRechargeRecord() {
        Intent name=new Intent(context, GasInternetRecodeListActivity.class);
        startActivity(name);
    }

    private void onGasPrice() {
        String url= UrlUtil.Gasprice_Url;
        Intent price=new Intent(context,HtmlPageActivity.class);
        price.putExtra("navigate","气价说明");
        price.putExtra("url",url);
        startActivity(price);
    }

    private void onIcCardTable() {
        Intent card=new Intent(context,GasIcCardActivity.class);
        startActivity(card);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
