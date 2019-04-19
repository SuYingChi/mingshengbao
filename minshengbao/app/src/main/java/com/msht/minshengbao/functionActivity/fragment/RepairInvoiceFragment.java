package com.msht.minshengbao.functionActivity.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Bean.AdvertisingInfo;
import com.msht.minshengbao.Bean.RepairInvoiceBean;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.adapter.CustomerNoAdapter;
import com.msht.minshengbao.adapter.RepairInvoiceListAdapter;
import com.msht.minshengbao.functionActivity.invoiceModule.InvoiceOpenActivity;
import com.msht.minshengbao.functionActivity.invoiceModule.InvoiceRepairApplyActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class RepairInvoiceFragment extends Fragment {
    private XRecyclerView mRecyclerView;
    private View       layoutNoData;
    private TextView tvTotal;
    private TextView   tvHistory;
    private Button btnNext;
    private CheckBox boxAllSelect;
    private String     userId,password;
    private String     totalAmount;
    private String     invoiceId;
    private String     text="(满400包邮)";
    private double     amount=0;
    private int        count=0;
    private RepairInvoiceListAdapter mAdapter;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
   // private boolean  ignoreChange=true;
    private boolean  noChange =true;
    private ArrayList<String> idList = new ArrayList<String>();
    private ArrayList<RepairInvoiceBean> invoiceBeans = new ArrayList<RepairInvoiceBean>();
    private Context context;

    public RepairInvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_repair_invoice, container, false);
        context=getActivity();
        userId= SharedPreferencesUtil.getUserId(MyApplication.getMsbApplicationContext(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(MyApplication.getMsbApplicationContext(), SharedPreferencesUtil.Password,"");
        initView(view);
        loadData(1);
        initEvent();
        return view;
    }
    private void initEvent() {
        mAdapter.setClickCallBack(new RepairInvoiceListAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(boolean isChecked, int pos) {
                invoiceBeans.get(pos).setCheck(isChecked);
                if (isChecked){
                    String money=invoiceBeans.get(pos).getAmount();
                    double a=Double.parseDouble(money);
                    amount=amount+a;
                    NumberFormat format=new DecimalFormat("0.##");
                    totalAmount=format.format(amount);
                    count++;
                    String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                    tvTotal.setText(longText);
                }else {
                    String money=invoiceBeans.get(pos).getAmount();
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
        });
        boxAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (noChange){
                    if (isChecked){
                        amount=0;
                        for (int i=0;i<invoiceBeans.size();i++){
                            invoiceBeans.get(i).setCheck(true);
                            String money=invoiceBeans.get(i).getAmount();
                            double a=Double.parseDouble(money);
                            amount=amount+a;
                        }
                        mAdapter.notifyDataSetChanged();
                        count=invoiceBeans.size();
                        NumberFormat format=new DecimalFormat("0.##");
                        totalAmount=format.format(amount);
                        String longText="已选"+count+"个订单共"+totalAmount+"元"+text;
                        tvTotal.setText(longText);
                    }else {
                        for (int i=0;i<invoiceBeans.size();i++){
                            invoiceBeans.get(i).setCheck(false);
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<invoiceBeans.size();i++){
                    if (invoiceBeans.get(i).isCheck()){
                        idList.add(invoiceBeans.get(i).getId());
                    }
                }
                invoiceId=TypeConvertUtil.listToString(idList);
                if (!TextUtils.isEmpty(invoiceId)){
                    Intent intent=new Intent(context,InvoiceRepairApplyActivity.class);
                    intent.putExtra("idinvoice",invoiceId);
                    intent.putExtra("total_amount",totalAmount);
                    startActivityForResult(intent,1);
                }else {
                    CustomToast.showWarningDialog("请选择开发票订单！");
                }
            }
        });
    }

    private void initView(View view) {
        mRecyclerView=(XRecyclerView)view.findViewById(R.id.id_view_invoice);
        layoutNoData =view.findViewById(R.id.id_noData_view);
        tvTotal =(TextView)view.findViewById(R.id.id_tv_total);
        boxAllSelect =(CheckBox)view.findViewById(R.id.id_box_allselect);
        btnNext =(Button)view.findViewById(R.id.id_btn_next);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getMsbApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter = new RepairInvoiceListAdapter(invoiceBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        String validateURL = UrlUtil.INVOICE_GET_URL;
        HashMap<String, String> textParams = new HashMap<String, String>(4);
        String pageNum=String.valueOf(pageIndex);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("page",pageNum);
        OkHttpRequestManager.getInstance(MyApplication.getMsbApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                onAnalysisData(data.toString());
            }

            @Override
            public void responseReqFailed(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
            }
        });

    }
    private void onAnalysisData(String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            String results=object.optString("result");
            String error = object.optString("error");
            JSONArray jsonArray =object.optJSONArray("data");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (pageIndex==1){
                    invoiceBeans.clear();
                }
                onReceiveInvoiceData(jsonArray);
            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onReceiveInvoiceData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RepairInvoiceBean info = new RepairInvoiceBean();
                info.setId(jsonObject.optString("id"));
                info.setOrderNo(jsonObject.optString("no"));
                info.setMainCategory(jsonObject.optString("main_category"));
                info.setCategory(jsonObject.optString("category"));
                info.setAmount(jsonObject.optString("amount"));
                info.setTime(jsonObject.optString("time"));
                if (boxAllSelect.isChecked()){
                   info.setCheck(true);
                }else {
                    info.setCheck(false);
                }
                invoiceBeans.add(info);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        if (invoiceBeans!=null&&invoiceBeans.size()>=1){
            layoutNoData.setVisibility(View.GONE);

        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
        onCalculateData();
    }

    private void onCalculateData() {
        int preCount=0;
        double preAmount=0;
        for (int i=0;i<invoiceBeans.size();i++){
            if (invoiceBeans.get(i).isCheck()){
                String money=invoiceBeans.get(i).getAmount();
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

}
