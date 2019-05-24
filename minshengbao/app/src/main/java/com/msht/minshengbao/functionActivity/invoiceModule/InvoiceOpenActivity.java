package com.msht.minshengbao.functionActivity.invoiceModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceOpenActivity extends BaseActivity {
    private XListView  mListView;
    private View       layoutNoData;
    private TextView   tvTotal;
    private TextView   tvHistory;
    private Button     btnNext;
    private CheckBox   boxAllSelect;
    private String     userId,password;
    private String     totalAmount;
    private String     invoiceId;
    private String     text="(满400包邮)";
    private double     amount=0;
    private int        count=0;
    private InvoiceAdapter mAdapter;
    private JSONArray jsonArray;
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private boolean  ignoreChange=true;
    private boolean  noChange =true;
    private ArrayList<String> idList = new ArrayList<String>();
    private ArrayList<HashMap<String, Boolean>> checkList = new ArrayList<HashMap<String, Boolean>>();
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<InvoiceOpenActivity> mWeakReference;
        public RequestHandler(InvoiceOpenActivity activity) {
            mWeakReference=new WeakReference<InvoiceOpenActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final InvoiceOpenActivity activity=mWeakReference.get();
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
                            if (activity.refreshType==0){
                                activity.mListView.stopRefresh(true);
                            }else if (activity.refreshType==1){
                                activity.mListView.stopLoadMore();
                            }
                            if(activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.invoiceList .clear();
                                    activity.checkList.clear();
                                }
                            }
                            activity.onReceiveInvoiceData();
                        }else {
                            activity.mListView.stopRefresh(false);
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mListView.stopRefresh(false);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
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
    private void onReceiveInvoiceData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id      =jsonObject.getString("id");
                String orderNo = jsonObject.getString("no");
                String mainCategory= jsonObject.getString("main_category");
                String category= jsonObject.getString("category");
                String amount  = jsonObject.getString("amount");
                String time    = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>();
                HashMap<String, Boolean> check = new HashMap<String, Boolean>();
                map.put("id", id);
                map.put("orderno", orderNo);
                map.put("main_category", mainCategory);
                map.put("category", category);
                map.put("amount",amount);
                map.put("time",time);
                if (boxAllSelect.isChecked()){
                    check.put("isCheck",true);
                }else {
                    check.put("isCheck",false);
                }
                invoiceList.add(map);
                checkList.add(check);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (invoiceList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
        onCalculateData();
    }
    private void onCalculateData() {
        int preCount=0;
        double preAmount=0;
        for (int i=0;i<checkList.size();i++){
            if (checkList.get(i).get("isCheck")){
                String money=invoiceList.get(i).get("amount");
                double a=Double.parseDouble(money);
                preAmount=preAmount+a;
                preCount++;
            }
        }
        count=preCount;
        amount=preAmount;
        NumberFormat format=new DecimalFormat("0.##");
        totalAmount=format.format(amount);
        String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
        tvTotal.setText(longText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_open);
        context=this;
        mPageName="发票订单";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
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
        tvHistory =(TextView)findViewById(R.id.id_tv_rightText);
        tvHistory.setVisibility(View.VISIBLE);
        tvHistory.setText("发票历史");
        mListView=(XListView)findViewById(R.id.id_view_invoice);
        layoutNoData =findViewById(R.id.id_nodata_view);
        tvTotal =(TextView)findViewById(R.id.id_tv_total);
        boxAllSelect =(CheckBox)findViewById(R.id.id_box_allselect);
        btnNext =(Button)findViewById(R.id.id_btn_next);
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
                totalAmount=format.format(amount);
                String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                tvTotal.setText(longText);
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
        String validateURL = UrlUtil.INVOICE_GET_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initEvent() {
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InvoiceHistoryActivity.class);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<checkList.size();i++){
                    if (checkList.get(i).get("isCheck")){
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
                invoiceId=result.toString();
                if (matchjudge(invoiceId)){
                    Intent intent=new Intent(context,InvoiceRepairApplyActivity.class);
                    intent.putExtra("idinvoice",invoiceId);
                    intent.putExtra("total_amount",totalAmount);
                    startActivityForResult(intent,1);
                }
            }
        });
        boxAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (noChange){
                    if (isChecked){
                        amount=0;
                        for (int i=0;i<invoiceList.size();i++){
                            checkList.get(i).put("isCheck",true);
                            String money=invoiceList.get(i).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount+a;
                        }
                        mAdapter.notifyDataSetChanged();
                        count=invoiceList.size();
                        NumberFormat format=new DecimalFormat("0.##");
                        totalAmount=format.format(amount);
                        String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                        tvTotal.setText(longText);
                    }else {
                        for (int i=0;i<checkList.size();i++){
                            checkList.get(i).put("isCheck",false);
                        }
                        mAdapter.notifyDataSetChanged();
                        count=0;
                        amount=0.0;
                        NumberFormat format=new DecimalFormat("0.##");
                        totalAmount=format.format(amount);
                        String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                        tvTotal.setText(longText);
                    }
                }
            }
        });
    }
    private boolean matchjudge(String idinvoice) {
        if (TextUtils.isEmpty(idinvoice)){
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
                holder.boxSelect =(CheckBox) convertView.findViewById(R.id.id_box_type);
                holder.tvTime =(TextView) convertView.findViewById(R.id.id_tv_time);
                holder.tvOrderNo =(TextView) convertView.findViewById(R.id.id_tv_oderNo);
                holder.tvName =(TextView) convertView.findViewById(R.id.id_tv_name);
                holder.tvMoney =(TextView)convertView.findViewById(R.id.id_tv_money);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvOrderNo.setText(invoiceList.get(position).get("orderno"));
            holder.tvTime.setText(invoiceList.get(position).get("time"));
            holder.boxSelect.setText(invoiceList.get(position).get("main_category"));
            holder.tvName.setText(invoiceList.get(position).get("category"));
            holder.tvMoney.setText(invoiceList.get(position).get("amount"));
            holder.boxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(position).put("isCheck",isChecked);
                    if (ignoreChange){
                        if (isChecked){
                            String money=invoiceList.get(thisposition).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount+a;
                            NumberFormat format=new DecimalFormat("0.##");
                            totalAmount=format.format(amount);
                            count++;
                            String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                            tvTotal.setText(longText);
                        }else {
                            String money=invoiceList.get(thisposition).get("amount");
                            double a=Double.parseDouble(money);
                            amount=amount-a;
                            NumberFormat format=new DecimalFormat("0.##");
                            totalAmount=format.format(amount);
                            count--;
                            String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                            tvTotal.setText(longText);
                            noChange =false;
                            boxAllSelect.setChecked(false);
                            noChange =true;
                        }
                    }
                }
            });
            ignoreChange=false;
            holder.boxSelect.setChecked(checkList.get(position).get("isCheck"));
            ignoreChange=true;
            return convertView;
        }
    }
    class ViewHolder {
        public CheckBox boxSelect;
        public TextView tvTime;
        public TextView tvOrderNo;
        public TextView tvName;
        public TextView tvMoney;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
