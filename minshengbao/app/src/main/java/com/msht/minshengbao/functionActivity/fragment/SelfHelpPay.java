package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.msht.minshengbao.adapter.GethouseAdapter;
import com.msht.minshengbao.functionActivity.GasService.GasExpenseQuery;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/10/20  
 */
public class SelfHelpPay extends Fragment {
    private RadioGroup radioGroup;
    private RadioButton radioButtonAddress;
    private ListViewForScrollView mLstView;
    private RelativeLayout customerLayout;
    private EditText etCustomer;
    private Button btnPayQuery;
    private String userId;
    private String password;
    private String getCustomerNo;
    private String ediCustomerNo;
    private String customerNo;
    private CustomDialog customDialog;
    /*****判断进入线程 ******/
    private boolean requestLine =false;
    private String validateURL;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private GethouseAdapter adapter ;
    private final String mPageName ="燃气缴费";
    private Context context;
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    public SelfHelpPay() {
        // Required empty public constructor
    }
    private static class RequestHandler extends Handler{
        private WeakReference<SelfHelpPay> mWeakReference;
        public RequestHandler(SelfHelpPay reference) {
            mWeakReference=new WeakReference<SelfHelpPay>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            final SelfHelpPay reference=mWeakReference.get();
            if (reference==null||reference.isDetached()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        reference.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (!reference.requestLine){
                                reference.jsonArray =object.optJSONArray("data");
                                reference.onCustomerAddressData();
                            }else {
                                reference.jsonObject =object.optJSONObject("data");
                                reference.onGasExpenseData();
                            }
                        }else {
                            //清空原来数据
                            reference.customerNo="";
                            reference.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    //清空原来数据
                    reference.customerNo="";
                    reference.displayDialog(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
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
        }catch (JSONException e){
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        mLstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCustomerNo = houseList.get(position).get("customerNo");
                //选中item的position 赋给cus_pos
                VariableUtil.mPos=position;
                adapter.notifyDataSetInvalidated();
                customerNo = getCustomerNo;
                //选择地址后按钮有效点击，
                btnPayQuery.setEnabled(true);
            }
        });
    }
    private void onGasExpenseData() {
        //清空原来数据
        customerNo="";
        String debts="";
        String name=jsonObject.optString("name");
        String customerNum=jsonObject.optString("customerNo");
        String allBalance=jsonObject.optString("balance");
        debts=jsonObject.optString("debts");
        String totalNum=jsonObject.optString("total_num");
        String gasFee=jsonObject.optString("gas_fee");
        String discountFees=jsonObject.optString("discount_fee");
        String lateFee=jsonObject.optString("late_fee");
        JSONArray json=jsonObject.optJSONArray("detail_list");
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
                map.put("late_fee", lateFee);
                VariableUtil.detailList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent payFees=new Intent(context, GasExpenseQuery.class);
        payFees.putExtra("name", name);
        payFees.putExtra("CustomerNo", customerNum);
        payFees.putExtra("all_balance",allBalance);
        payFees.putExtra("debts", debts);
        payFees.putExtra("total_num", totalNum);
        payFees.putExtra("discount_fees", discountFees);
        payFees.putExtra("gas_fee",gasFee);
        payFees.putExtra("late_fee", lateFee);
        startActivity(payFees);
    }
    private void displayDialog(String s) {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_selfhelp_pay, container, false);
        context=getActivity();
        customDialog=new CustomDialog(getActivity(), "正在加载");
        Bundle bundle=getArguments();
        userId=bundle.getString("id");
        password=bundle.getString("password");
        radioGroup =(RadioGroup)view.findViewById(R.id.id_radiogroup);
        radioButtonAddress =(RadioButton)view.findViewById(R.id.id_rb_address);
        RadioButton radioButtonCustomer =(RadioButton)view.findViewById(R.id.id_rb_customer);
        mLstView=(ListViewForScrollView)view.findViewById(R.id.id_addre_listview);
        customerLayout =(RelativeLayout)view.findViewById(R.id.id_customer_layout);
        etCustomer =(EditText)view.findViewById(R.id.id_et_customerNo);
        btnPayQuery =(Button)view.findViewById(R.id.id_btn_payquery);
        btnPayQuery.setEnabled(false);
        VariableUtil.mPos=-1;
        adapter=new GethouseAdapter(getActivity(),houseList);
        mLstView.setAdapter(adapter);
        if (NetWorkUtil.IsNetWorkEnable(getActivity())){
            customDialog.show();
            initData();
        }else {
            noNetwork();
        }
        initEvent();
        buttonEvent();
        return view;
    }
    private void initData() {
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        if (!requestLine){
            validateURL = UrlUtil.SELECT_ADDRESS_URL;
        }else {
            validateURL = UrlUtil.SEARCH_BILL_GAS_URL;
            textParams.put("CustomerNo",customerNo);
        }
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void noNetwork() {
        new PromptDialog.Builder(context)
                .setTitle("当前网络不可用")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请检查你的网络设置")
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                }).show();
    }
    private void initEvent() {
        radioButtonAddress.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.id_rb_address:
                        customerLayout.setVisibility(View.GONE);
                        mLstView.setVisibility(View.VISIBLE);
                        //编辑框数据清空清空
                        ediCustomerNo ="";
                        //选择地址后获取
                        customerNo= getCustomerNo;
                        break;
                    case R.id.id_rb_customer:
                        btnPayQuery.setEnabled(false);
                        mLstView.setVisibility(View.GONE);
                        customerLayout.setVisibility(View.VISIBLE);
                        //地址颜色变色标志
                        VariableUtil.mPos=-1;
                        adapter.notifyDataSetInvalidated();
                        getCustomerNo ="";
                        editAction();
                        //获取编辑框输入的客户号
                        customerNo= ediCustomerNo;
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void editAction() {
        etCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ediCustomerNo = etCustomer.getText().toString().trim();
                if (TextUtils.isEmpty(ediCustomerNo)) {
                    btnPayQuery.setEnabled(false);

                }else {
                    btnPayQuery.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void buttonEvent() {
        btnPayQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariableUtil.detailList.clear();//清除账单明细数据
                if (TextUtils.isEmpty(customerNo)) {
                    ediCustomerNo = etCustomer.getText().toString().trim();
                    customerNo= ediCustomerNo;
                }
                customDialog.show();
                requestLine =true;
                initData();
                VariableUtil.mPos=-1;
                adapter.notifyDataSetInvalidated();
                etCustomer.setText("");
                btnPayQuery.setEnabled(false);
            }
        });
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
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
