package com.msht.minshengbao.FunctionView.fragmeht;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.tableAdapter;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.GasService.GasExpenseQuery;
import com.msht.minshengbao.FunctionView.GasService.SelectCustomerno;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfWriteFrage extends Fragment implements View.OnClickListener {

    private RelativeLayout mselecttable,mselectaddr;
    private Button verifysend;
    private TextView Tvselectaddr;
    private EditText Eselecttable;
    private EditText EttableNum,Etlast;
    private String customerNo;
    private String customer="";
    private String userId;
    private String password;
    private String tableId;
    private String tableaddress;
    private String tablebh;
    private String lastNumber;

    private String name;
    private String CustomerNum,all_balance;
    private String  debts;
    private String total_num;
    private String discount_fees;
    private String late_fee;

    private String Etaddress;
    private String Ettable;
    private String Etm2;
    private String Stlast;

    private tableAdapter adapter;
    private int pos=-1;
    private int    requestType= 0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private String validateURL;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
    private Context mContext;
    private final String mPageName ="自助抄表";
    private static final int REQUESTCOODE=1;
    public SelfWriteFrage() {
        // Required empty public constructor
    }
    Handler gethouseHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requestType==0){
                                jsonArray =object.optJSONArray("data");
                                showDialogs();
                            }else if (requestType==1){
                                jsonObject =object.optJSONObject("data");
                                initshowdata();
                            }
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    Handler sendmeterHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            showsuccess();
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;

            }
        }
    };

    private void showsuccess() {
        requestType=1;
        validateURL = UrlUtil.Searchbill_GasUrl;
        VariableUtil.detailList.clear();//清除账单明细数据
        requesSevice();
    }
    private void showfaiture(String error) {
        new PromptDialog.Builder(mContext)
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
    private void initshowdata() {
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
                String amount = Object.getString("amount");
                String balance=Object.getString("balance");
                String gas_fees = Object.getString("gas_fee");
                String discount_fee=Object.getString("discount_fee");
                String late_fee=Object.getString("late_fee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("amounts",amount);
                map.put("balance",balance);
                map.put("gas_fees", gas_fees);
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
        payfees.putExtra("gas_fee",gas_fee);
        payfees.putExtra("discount_fees", discount_fees);
        payfees.putExtra("late_fee", late_fee);
        startActivity(payfees);

    }
    private void showDialogs() {
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
        mselecttable.setEnabled(true);
        Eselecttable.setText(tableList.get(0).get("address"));
        Etlast.setText(tableList.get(0).get("lastNum"));
        tablebh=tableList.get(0).get("bh");
        tableId=tableList.get(0).get("id");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_self_write_frage, container, false);
        mContext=getActivity();
        customDialog=new CustomDialog(mContext, "正在加载");
        userId= SharedPreferencesUtil.getUserId(mContext, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(mContext, SharedPreferencesUtil.Password,"");
        initView(view);
        iniEvent();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUESTCOODE://获取昵称设置返回数据
                if(data!=null){
                    if (resultCode==2){
                        String addressname=data.getStringExtra("addressname");
                        customer=data.getStringExtra("customerNo");
                        Tvselectaddr.setText(addressname);
                        customerNo=customer;
                        tableList.clear();
                        tablepickers();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void tablepickers() {
        validateURL = UrlUtil.GetTable_Url;
        requestType=0;                    //获取表具信息
        requesSevice();
    }
    private void requesSevice() {
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                gethouseHandler .sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                gethouseHandler .sendMessage(msg);
            }
        });
    }

    private void initView(View view) {
        mselecttable=(RelativeLayout) view.findViewById(R.id.id_select_table);
        mselectaddr=(RelativeLayout)view.findViewById(R.id.id_re_select);
        verifysend=(Button)view.findViewById(R.id.id_btn_verify);
        Eselecttable=(EditText) view.findViewById(R.id.id_tv_selecttable);
        Tvselectaddr=(TextView) view.findViewById(R.id.id_select_address);
        EttableNum=(EditText)view.findViewById(R.id.id_reading_tble);
        Etlast=(EditText)view.findViewById(R.id.id_last_table);
        Etlast.setInputType(InputType.TYPE_NULL);
        Eselecttable.setInputType(InputType.TYPE_NULL);
        mselecttable.setEnabled(false);
        verifysend.setEnabled(false);
    }
    private void iniEvent() {
        mselecttable.setOnClickListener(this);//选择表具
        mselectaddr.setOnClickListener(this); //选择地址
        verifysend.setOnClickListener(this);//确认
        Eselecttable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( TextUtils.isEmpty(Eselecttable.getText().toString())||
                        TextUtils.isEmpty(EttableNum.getText().toString())
                        ) {
                    verifysend.setEnabled(false);
                } else {
                    verifysend.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        EttableNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(Eselecttable.getText().toString()) ||
                        TextUtils.isEmpty(EttableNum.getText().toString())) {
                    verifysend.setEnabled(false);
                } else {
                    verifysend.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_select_table:
                selecttable();
                break;
            case R.id.id_re_select:
                selectaddr();
                break;
            case R.id.id_btn_verify:
                Verifysend();//提交抄表
                break;
            default:
                break;
        }
    }
    private void selecttable() {
        final SelectTable selectTable=new SelectTable(mContext);
        final TextView tv_title=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tv_title.setText("选择表具");
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
                tableaddress = tableList.get(position).get("address");
                tablebh = tableList.get(position).get("bh");
                lastNumber = tableList.get(position).get("lastNum");
                Eselecttable.setText(tableaddress);//显示地址
                Etlast.setText(lastNumber);
                selectTable.dismiss();
            }
        });
    }
    private void selectaddr() {
        Intent selete=new Intent(getActivity(),SelectCustomerno.class);
        startActivityForResult(selete,REQUESTCOODE);
    }
    private void Verifysend() {
        Ettable=Eselecttable.getText().toString().trim();
        Stlast=Etlast.getText().toString().trim();
        Etm2=EttableNum.getText().toString().trim();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        Date curData=new Date(System.currentTimeMillis());
        String time=format.format(curData) ;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.item_read_table,null);
        final Dialog dialog=new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        final TextView readtime = (TextView)layout.findViewById(R.id.id_read_time);
        final TextView readaddr = (TextView)layout.findViewById(R.id.id_read_address);
        final TextView lastnum = (TextView)layout.findViewById(R.id.id_last_Num);
        final TextView readnum = (TextView)layout.findViewById(R.id.id_read_Num);
        final  Button btnCancel=(Button)layout.findViewById(R.id.id_cancel);
        readtime.setText(time);
        readaddr.setText(Tvselectaddr.getText().toString());
        lastnum.setText(Stlast);
        readnum.setText(Etm2);
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
                sendtableNum();
                dialog.dismiss();
            }
        });


    }
    private void sendtableNum() {
        String dataurl =UrlUtil.SendTable_dataUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        textParams.put("rqbId",tableId);
        textParams.put("meter",Etm2);
        HttpUrlconnectionUtil.executepost(dataurl,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                sendmeterHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                sendmeterHandler.sendMessage(msg);
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
