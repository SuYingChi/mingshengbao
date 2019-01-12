package com.msht.minshengbao.functionActivity.GasService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.adapter.GetHouseAdapter;
import com.msht.minshengbao.functionActivity.fragment.SelfHelpPay;

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
 * @date 2019/1/8 
 */
public class InternetTableActivity extends BaseActivity {

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
    private String mAddress;
    private CustomDialog customDialog;
    /*****判断进入线程 ******/
    private boolean requestLine =false;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private GetHouseAdapter adapter ;
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();

    private final RequestHandler requestHandler=new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<InternetTableActivity> mWeakReference;
        public RequestHandler(InternetTableActivity activity) {
            mWeakReference=new WeakReference<InternetTableActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final InternetTableActivity activity=mWeakReference.get();
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
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (!activity.requestLine){
                                activity.jsonArray =object.optJSONArray("data");
                                activity.onCustomerAddressData();
                            }else {
                                activity.jsonObject =object.optJSONObject("data");
                               // activity.onGasExpenseData();
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
            adapter.notifyDataSetChanged();
            mLstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
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
        setCommonHeader("物联网表查询");
        customDialog=new CustomDialog(context, "正在加载");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initFindViewId();
        VariableUtil.mPos=-1;
        adapter=new GetHouseAdapter(context,houseList);
        mLstView.setAdapter(adapter);
        initHouseData();
        initEvent();
        mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCustomerNo = houseList.get(position).get("customerNo");
                mAddress=houseList.get(position).get("name");
                //选中item的position 赋给cus_pos
                VariableUtil.mPos=position;
                adapter.notifyDataSetInvalidated();
                customerNo = getCustomerNo;
                //选择地址后按钮有效点击，
                btnPayQuery.setEnabled(true);
            }
        });
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
        btnPayQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariableUtil.detailList.clear();//清除账单明细数据
                if (TextUtils.isEmpty(customerNo)) {
                    ediCustomerNo = etCustomer.getText().toString().trim();
                    customerNo= ediCustomerNo;
                }
                requestLine =true;
                initHouseData();
                VariableUtil.mPos=-1;
                adapter.notifyDataSetInvalidated();
                etCustomer.setText("");
                btnPayQuery.setEnabled(false);
                Intent intent=new Intent(context,InternetTableRechargeActivity.class);
                intent.putExtra("customerNo",customerNo);
                intent.putExtra("address",mAddress);
                startActivity(intent);
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
    private void initHouseData() {
        customDialog.show();
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        String validateURL="";
        if (!requestLine){
            textParams.put("meterType","17");
            validateURL = UrlUtil.SELECT_ADDRESS_URL;
        }else {
            validateURL = UrlUtil.SEARCH_BILL_GAS_URL;
            textParams.put("CustomerNo",customerNo);
        }
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    private void initFindViewId() {
        radioGroup =(RadioGroup)findViewById(R.id.id_radiogroup);
        radioButtonAddress =(RadioButton)findViewById(R.id.id_rb_address);
        RadioButton radioButtonCustomer =(RadioButton)findViewById(R.id.id_rb_customer);
        mLstView=(ListViewForScrollView)findViewById(R.id.id_addre_listview);
        customerLayout =(RelativeLayout)findViewById(R.id.id_customer_layout);
        etCustomer =(EditText)findViewById(R.id.id_et_customerNo);
        btnPayQuery =(Button)findViewById(R.id.id_btn_payquery);
        btnPayQuery.setEnabled(false);
    }
}
