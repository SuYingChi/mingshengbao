package com.msht.minshengbao.FunctionView.Invoice;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvoiceOpen extends BaseActivity {
    private XListView  mListView;
    private View       Rnodata;
    private TextView   tv_total;
    private TextView   tv_history;
    private Button     btn_next;
    private CheckBox   box_allselect;
    private String     userId,password;
    private String     total_amount;
    private String     idinvoice;
    private String     text="(满400包邮)";
    private double     amount=0;
    private int        count=0;
    private InvoiceAdapter mAdapter;
    private final int SUCCESS   = 1;
    private final int FAILURE   = 0;
    private JSONArray jsonArray;   //数据解析
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private boolean  ignoreChange=true;
    private boolean  nochange=true;
    private ArrayList<String> idList = new ArrayList<String>();
    private ArrayList<HashMap<String, Boolean>> checkList = new ArrayList<HashMap<String, Boolean>>();
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
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
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    invoiceList .clear();
                                    checkList.clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
    private void faifure(String error) {
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
                String id      =jsonObject.getString("id");
                String orderno = jsonObject.getString("no");
                String main_category= jsonObject.getString("main_category");
                String category= jsonObject.getString("category");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>();
                HashMap<String, Boolean> check = new HashMap<String, Boolean>();
                map.put("id", id);
                map.put("orderno", orderno);
                map.put("main_category", main_category);
                map.put("category", category);
                map.put("amount",amount);
                map.put("time",time);
                check.put("ischeck",false);
                invoiceList.add(map);
                checkList.add(check);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (invoiceList.size()==0){
            Rnodata.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            Rnodata.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_open);
        customDialog=new CustomDialog(this, "正在加载");
        context=this;
        setCommonHeader("发票订单");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        intView();
        mListView.setPullLoadEnable(true);
        mAdapter = new InvoiceAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
        initEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    finish();
                }
                break;
            default:
                break;
        }
    }
    private void intView() {
        tv_history=(TextView)findViewById(R.id.id_tv_rightText);
        tv_history.setVisibility(View.VISIBLE);
        tv_history.setText("发票历史");
        mListView=(XListView)findViewById(R.id.id_view_invoice);
        Rnodata=findViewById(R.id.id_nodata_view);
        tv_total=(TextView)findViewById(R.id.id_tv_total);
        box_allselect=(CheckBox)findViewById(R.id.id_box_allselect);
        btn_next=(Button)findViewById(R.id.id_btn_next);
    }
    private void initData() {
        customDialog.show();
        loadData(1);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                loadData(1);
                count=0;
                amount=0.00;
                NumberFormat format=new DecimalFormat("0.##");
                total_amount=format.format(amount);
                tv_total.setText("已选"+count+"个订单共"+total_amount+"元"+text);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                loadData(pageIndex + 1);
            }
        });
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.Invoice_getUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
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
    private void initEvent() {
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InvoiceHistory.class);
                startActivity(intent);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<checkList.size();i++){
                    if (checkList.get(i).get("ischeck")){
                        idList.add(invoiceList.get(i).get("id"));
                    }
                }
                StringBuilder result=new StringBuilder();
                boolean flag=false;
                for (String string : idList) {
                    if (flag) {
                        result.append(",");
                    }else {
                        flag=true;
                    }
                    result.append(string);
                }
                idinvoice=result.toString();
                if (matchjudge(idinvoice)){
                    Intent intent=new Intent(context,ApplyInvoice.class);
                    intent.putExtra("idinvoice",idinvoice);
                    intent.putExtra("total_amount",total_amount);
                    startActivityForResult(intent,1);
                }
            }
        });
        box_allselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (nochange){
                    if (isChecked){
                        amount=0;
                        for (int i=0;i<invoiceList.size();i++){
                            checkList.get(i).put("ischeck",true);
                            String money=invoiceList.get(i).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount+a;
                        }
                        mAdapter.notifyDataSetChanged();
                        count=invoiceList.size();
                        NumberFormat format=new DecimalFormat("0.##");
                        total_amount=format.format(amount);
                        tv_total.setText("已选"+count+"个订单共"+total_amount+"元"+text);
                    }else {
                        for (int i=0;i<checkList.size();i++){
                            checkList.get(i).put("ischeck",false);
                        }
                        mAdapter.notifyDataSetChanged();
                        count=0;
                        amount=0.0;
                        NumberFormat format=new DecimalFormat("0.##");
                        total_amount=format.format(amount);
                        tv_total.setText("已选"+count+"个订单共"+total_amount+"元"+text);
                    }
                }
            }
        });
    }
    private boolean matchjudge(String idinvoice) {
        if (idinvoice.equals("")){
            new PromptDialog.Builder(this)
                    .setTitle(R.string.my_dialog_title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("请选择开发票订单！")
                    .setButton1("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }else {
            return true;
        }
    }
    private class InvoiceAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        public InvoiceAdapter(Context context) {
            super();
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            if (invoiceList==null){
                return 0;
            }else {
                return invoiceList.size();
            }
        }
        @Override
        public Object getItem(int position) {
            if (invoiceList==null){
                return null;
            }else {
                return invoiceList.get(position);
            }
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int thisposition=position;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_invoice_order, null);
                holder.box_select=(CheckBox) convertView.findViewById(R.id.id_box_type);
                holder.tv_time=(TextView) convertView.findViewById(R.id.id_tv_time);
                holder.tv_orderNo=(TextView) convertView.findViewById(R.id.id_tv_oderNo);
                holder.tv_name=(TextView) convertView.findViewById(R.id.id_tv_name);
                holder.tv_money=(TextView)convertView.findViewById(R.id.id_tv_money);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_orderNo.setText(invoiceList.get(position).get("orderno"));
            holder.tv_time.setText(invoiceList.get(position).get("time"));
            holder.box_select.setText(invoiceList.get(position).get("main_category"));
            holder.tv_name.setText(invoiceList.get(position).get("category"));
            holder.tv_money.setText(invoiceList.get(position).get("amount"));
            holder.box_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(position).put("ischeck",isChecked);
                    if (ignoreChange){
                        if (isChecked){
                            String money=invoiceList.get(thisposition).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount+a;
                            NumberFormat format=new DecimalFormat("0.##");
                            total_amount=format.format(amount);
                            count++;
                            tv_total.setText("已选"+count+"个订单共"+total_amount+"元"+text);
                        }else {
                            String money=invoiceList.get(thisposition).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount-a;
                            NumberFormat format=new DecimalFormat("0.##");
                            total_amount=format.format(amount);
                            count--;
                            tv_total.setText("已选"+count+"个订单共"+total_amount+"元"+text);
                            nochange=false;
                            box_allselect.setChecked(false);
                            nochange=true;
                        }
                    }
                }
            });
            ignoreChange=false;
            holder.box_select.setChecked(checkList.get(position).get("ischeck"));
            ignoreChange=true;
            return convertView;
        }
    }
    class ViewHolder {
        public CheckBox  box_select;
        public TextView  tv_time;
        public TextView  tv_orderNo;
        public TextView  tv_name;
        public TextView  tv_money;
    }
}
