package com.msht.minshengbao.functionActivity.GasService;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.adapter.GetHouseAdapter;
import com.msht.minshengbao.adapter.ViewAdapter;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.functionActivity.MyActivity.CustomerNoManageActivity;
import com.msht.minshengbao.functionActivity.fragment.SelfHelpPay;
import com.umeng.analytics.MobclickAgent;

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
 *
 * @author hong
 * @date 2016/7/20  
 */
public class GasPayFeeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvNoDataTip;
    private String userId;
    private String password;
    private ListView mLstView;
    private String customerNo;
    private CustomDialog customDialog;
    private int requestType = 0;
    private GetHouseAdapter adapter;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler = new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<GasPayFeeActivity> mWeakReference;

        public RequestHandler(GasPayFeeActivity reference) {
            mWeakReference = new WeakReference<GasPayFeeActivity>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            final GasPayFeeActivity reference = mWeakReference.get();
            if (reference == null || reference.isFinishing()) {
                return;
            }
            if (reference.customDialog != null && reference.customDialog.isShowing()) {
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results = object.optString("result");
                        String error = object.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestType == 0) {
                                JSONArray jsonArray = object.optJSONArray("data");
                                reference.houseList.clear();
                                reference.onCustomerAddressData(jsonArray);
                            } else {
                                JSONObject jsonObject = object.optJSONObject("data");
                                reference.onGasExpenseData(jsonObject);
                            }
                        } else {
                            if (!reference.isFinishing()) {
                                reference.displayDialog(error);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void onCustomerAddressData(JSONArray jsonArray) {
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
        if (houseList != null && houseList.size() > 0) {
            tvNoDataTip.setVisibility(View.GONE);
        } else {
            tvNoDataTip.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void onGasExpenseData(JSONObject jsonObject) {
        String name = jsonObject.optString("name");
        String customerNum = jsonObject.optString("customerNo");
        String allBalance = jsonObject.optString("balance");
        String debts = jsonObject.optString("debts");
        String totalNum = jsonObject.optString("total_num");
        String gasFee = jsonObject.optString("gas_fee");
        String discountFees = jsonObject.optString("discount_fee");
        String lateFee = jsonObject.optString("late_fee");
        JSONArray json = jsonObject.optJSONArray("detail_list");
        VariableUtil.detailList.clear();
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                String date = object.getString("date");
                String num = object.getString("num");
                String gasFees = object.getString("gas_fee");
                String amounts = object.getString("amount");
                String balance = object.getString("balance");
                String discountFee = object.getString("discount_fee");
                String lateFees = object.getString("late_fee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("gas_fees", gasFees);
                map.put("amounts", amounts);
                map.put("balance", balance);
                map.put("discount_fee", discountFee);
                map.put("late_fee", lateFees);
                VariableUtil.detailList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent payFees = new Intent(context, GasExpenseQueryActivity.class);
        payFees.putExtra("name", name);
        payFees.putExtra("CustomerNo", customerNum);
        payFees.putExtra("allBalance", allBalance);
        payFees.putExtra("debts", debts);
        payFees.putExtra("totalNum", totalNum);
        payFees.putExtra("discountFees", discountFees);
        payFees.putExtra("gasFee", gasFee);
        payFees.putExtra("lateFee", lateFee);
        startActivity(payFees);
    }

    private void displayDialog(String s) {
        if (context != null) {
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
        setContentView(R.layout.activity_gas_payfee);
        context = this;
        customDialog = new CustomDialog(context, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        setCommonHeader("普表缴费");
        initView();
        adapter = new GetHouseAdapter(context, houseList);
        mLstView.setAdapter(adapter);
        initHouseData();
        mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerNo = houseList.get(position).get("customerNo");
                requestType = 1;
                initHouseData();
            }
        });
    }

    private void initHouseData() {
        customDialog.show();
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password", password);
        textParams.put("category", "1");
        String validateURL = "";
        if (requestType == 0) {
            textParams.put("meterType", "11");
            validateURL = UrlUtil.SELECT_ADDRESS_URL;
        } else {
            validateURL = UrlUtil.SEARCH_BILL_GAS_URL;
            textParams.put("CustomerNo", customerNo);
        }
        SendRequestUtil.postDataFromService(validateURL, textParams, requestHandler);
    }

    private void initView() {
        mLstView = (ListView) findViewById(R.id.id_address_dataList);
        tvNoDataTip = (TextView) findViewById(R.id.id_tv_tip);
        findViewById(R.id.id_newAdd_btn).setOnClickListener(this);
        TextView tvRecord = (TextView) findViewById(R.id.id_tv_rightText);
        tvRecord.setVisibility(View.VISIBLE);
        tvRecord.setText("缴费记录");
        tvRecord.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantUtil.VALUE1:
                if (resultCode == ConstantUtil.VALUE1) {
                    requestType = 0;
                    initHouseData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tv_rightText:
                onGasPayRecord();
                break;
            case R.id.id_newAdd_btn:
                onNewAddCustomerNo();
                break;
            default:
                break;
        }
    }

    private void onGasPayRecord() {
        Intent intent = new Intent(context, GasOrdinaryRecordActivity.class);
        startActivity(intent);
    }

    private void onNewAddCustomerNo() {
        Intent addAddress = new Intent(context, AddCustomerNoActivity.class);
        addAddress.putExtra("enterType",1);
        startActivityForResult(addAddress, 1);
    }

    private void onGasPrice() {
        String url = UrlUtil.Gasprice_Url;
        Intent price = new Intent(context, HtmlPageActivity.class);
        price.putExtra("navigate", "气价说明");
        price.putExtra("url", url);
        startActivity(price);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }
}
