package com.msht.minshengbao.FunctionView.fragmeht;


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

import com.msht.minshengbao.Adapter.GethouseAdapter;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.GasService.GasExpenseQuery;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfhelpPay extends Fragment {

    private RadioGroup Radiogroup ;
    private RadioButton RBaddress,RBcustomer;
    private ListViewForScrollView mLstView;
    private RelativeLayout customerlayout;         //布局
    private EditText Etcustomer;            //客户号
    private Button btn_payquery;         //查询
    private String         userId;
    private String         password;
    private String         GetcustomerNo;
    private String         EdicustomerNo;
    private String         CustomerNo;

    private String  name;
    private String  CustomerNum;
    private String  debts,all_balance;
    private String  total_num;
    private String  discount_fees;
    private String  late_fee;

    private CustomDialog customDialog;
    private boolean requestline=false;            /*****判断进入线程 ******/
    private String validateURL;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
   // private int pos=-1;      //当前显示的行
    private GethouseAdapter adapter ;
    private final String mPageName ="燃气缴费";
    private Context mContext;
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();

    public SelfhelpPay() {
        // Required empty public constructor
    }
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (!requestline){
                                jsonArray =object.optJSONArray("data");
                                Showlistview();
                            }else {
                                jsonObject =object.optJSONObject("data");
                                initshowdata();
                            }
                        }else {
                            CustomerNo="";      //清空原来数据
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    CustomerNo="";      //清空原来数据
                    displayDialog(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void Showlistview() {
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
                GetcustomerNo = houseList.get(position).get("customerNo");
                VariableUtil.mPos=position; //选中item的position 赋给cus_pos
                adapter.notifyDataSetInvalidated();      //更新listView
                CustomerNo = GetcustomerNo;
                btn_payquery.setEnabled(true);           //选择地址后按钮有效点击，
            }
        });
    }
    private void initshowdata() {
        CustomerNo="";      //清空原来数据
        name=jsonObject.optString("name");
        CustomerNum=jsonObject.optString("customerNo");
        all_balance=jsonObject.optString("balance");
        debts=jsonObject.optString("debts");
        total_num=jsonObject.optString("total_num");
        String gas_fee=jsonObject.optString("gas_fee");
        discount_fees=jsonObject.optString("discount_fee");
        late_fee=jsonObject.optString("late_fee");
        JSONArray json=jsonObject.optJSONArray("detail_list");
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject Object = json.getJSONObject(i);
                String date = Object.getString("date");
                String num = Object.getString("num");
                String gas_fees = Object.getString("gas_fee");
                String amounts=Object.getString("amount");
                String balance=Object.getString("balance");
                String discount_fee=Object.getString("discount_fee");
                String late_fee=Object.getString("late_fee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("gas_fees", gas_fees);
                map.put("amounts",amounts);
                map.put("balance",balance);
                map.put("discount_fee", discount_fee);
                map.put("late_fee", late_fee);
                VariableUtil.detailList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent payfees=new Intent(getActivity(), GasExpenseQuery.class);
        payfees.putExtra("name", name);
        payfees.putExtra("CustomerNo", CustomerNum);
        payfees.putExtra("all_balance",all_balance);
        payfees.putExtra("debts", debts);
        payfees.putExtra("total_num", total_num);
        payfees.putExtra("discount_fees", discount_fees);
        payfees.putExtra("gas_fee",gas_fee);
        payfees.putExtra("late_fee", late_fee);
        startActivity(payfees);
    }
    private void displayDialog(String s) {
        new PromptDialog.Builder(getActivity())
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
        customDialog=new CustomDialog(getActivity(), "正在加载");
        Bundle bundle=getArguments();
        userId=bundle.getString("id");             //获取从Activity传来的值
        password=bundle.getString("password");
        Radiogroup=(RadioGroup)view.findViewById(R.id.id_radiogroup);
        RBaddress=(RadioButton)view.findViewById(R.id.id_rb_address);
        RBcustomer=(RadioButton)view.findViewById(R.id.id_rb_customer);
        mLstView=(ListViewForScrollView)view.findViewById(R.id.id_addre_listview);
        customerlayout=(RelativeLayout)view.findViewById(R.id.id_customer_layout);
        Etcustomer=(EditText)view.findViewById(R.id.id_et_customerNo);
        btn_payquery=(Button)view.findViewById(R.id.id_btn_payquery);
        btn_payquery.setEnabled(false);               //设置无效点击、背景
        VariableUtil.mPos=-1;
        adapter=new GethouseAdapter(getActivity(),houseList);
        mLstView.setAdapter(adapter);
        if (NetWorkUtil.IsNetWorkEnable(getActivity())){   //检测网络是否可否
            customDialog.show();
            initdata();
        }else {
            Nonetwork();
        }
        initEvent();
        ButtonEvent();
        return view;
    }
    private void initdata() {
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        if (!requestline){
            validateURL = UrlUtil.SelectAddress_Url;
        }else {
            validateURL = UrlUtil.Searchbill_GasUrl;
            textParams.put("CustomerNo",CustomerNo);
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
    private void Nonetwork() {
        new PromptDialog.Builder(getActivity())
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
        RBaddress.setChecked(true);     //默认选中地址
        Radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.id_rb_address:
                        customerlayout.setVisibility(View.GONE);
                        mLstView.setVisibility(View.VISIBLE);
                        EdicustomerNo="";            //编辑框数据清空清空
                        CustomerNo=GetcustomerNo;    //选择地址后获取
                        break;
                    case R.id.id_rb_customer:
                        btn_payquery.setEnabled(false);            //设置无效点击、背景
                        mLstView.setVisibility(View.GONE);
                        customerlayout.setVisibility(View.VISIBLE);
                        VariableUtil.mPos=-1;                //地址颜色变暗
                        adapter.notifyDataSetInvalidated();   //更新listView
                        GetcustomerNo="";
                        Editaction();
                        CustomerNo=EdicustomerNo;   //获取编辑框输入的客户号
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void Editaction() {
        Etcustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EdicustomerNo=Etcustomer.getText().toString().trim();
                if (TextUtils.isEmpty(EdicustomerNo)) {
                    btn_payquery.setEnabled(false);            //设置无效点击、背景

                }else {
                    btn_payquery.setEnabled(true);            //有效点击，
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void ButtonEvent() {
        btn_payquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariableUtil.detailList.clear();//清除账单明细数据
                if (TextUtils.isEmpty(CustomerNo)) {
                    EdicustomerNo = Etcustomer.getText().toString().trim();
                    CustomerNo=EdicustomerNo;   //获取编辑框输入的客户号
                }
                customDialog.show();
                requestline=true;
                initdata();
                VariableUtil.mPos=-1;
                adapter.notifyDataSetInvalidated();   //更换listView
                Etcustomer.setText("");
                btn_payquery.setEnabled(false);            //设置无效点击、背景
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
}
