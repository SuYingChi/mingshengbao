package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.functionActivity.Public.PayFeeWayActivity;
import com.msht.minshengbao.functionActivity.Public.SelectVoucherActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class GasExpenseQuery extends BaseActivity {
    private TextView tvBill;
    private TextView tvVoucher;
    private TextView tvReal;
    private Button   btnPayFee;
    private View  layoutReversed1;
    private View  layoutReversed2;
    private View  layoutVoucher;
    private String customerNo,allBalance;
    private String houseId,voucherId="0";
    private String name,id;
    private String debts="";
    private String totalNum;
    private String gasFee,realFee;
    private String lateFee;
    private String userId;
    private String password;
    private static final String NULL_VALUE="null";
    private CustomDialog customDialog;
    private boolean placeStatus=true;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasExpenseQuery> mWeakReference;
        public RequestHandler(GasExpenseQuery activity) {
            mWeakReference=new WeakReference<GasExpenseQuery>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GasExpenseQuery activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog.isShowing()&&activity.customDialog!=null){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            JSONObject json=object.getJSONObject("data");
                            activity.id=json.optString("id");
                            activity.onQueryResult(activity.id);
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onQueryResult(String id) {
        Intent amount=new Intent(context,PayFeeWayActivity.class);
        amount.putExtra("CustomerNo",customerNo);
        amount.putExtra("amount",realFee);
        amount.putExtra("id",id);
        startActivityForResult(amount,0x002);
    }
    private void onFailure(String s) {
        new PromptDialog.Builder(this)
                .setTitle("缴费提示")
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_expense_query);
        context=this;
        setCommonHeader("燃气费用");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        //推送统计
        PushAgent.getInstance(context).onAppStart();
        Intent getData=getIntent();
        customerNo=getData.getStringExtra("CustomerNo");
        allBalance=getData.getStringExtra("all_balance");
        name=getData.getStringExtra("name");
        debts=getData.getStringExtra("debts");
        totalNum=getData.getStringExtra("total_num");
        String discountFees=getData.getStringExtra("discount_fees");
        gasFee=getData.getStringExtra("gas_fee");
        lateFee=getData.getStringExtra("late_fee");
        realFee=debts;
        initView();
        initEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==3){
                    voucherId=data.getStringExtra("vouid");
                    String amount=data.getStringExtra("amount");
                    double b=Double.parseDouble(amount);
                    double a=Double.parseDouble(debts);
                    double fee=a-b;
                    NumberFormat format=new DecimalFormat("0.##");
                    realFee=format.format(fee);
                    String realFeeText="¥"+realFee;
                    String mountText=amount+"元";
                    tvReal.setText(realFeeText);
                    tvVoucher.setText(mountText);
                }
                break;
            case 0x002:
                placeStatus=false;
                if (resultCode==0x002){
                    setResult(0x002);
                    finish();
                }
                break;
            default:
                break;
        }
    }
    private void initView() {
        View layoutData =findViewById(R.id.id_layout_data);
        View layoutNoData =findViewById(R.id.id_nodata_layout);
        View layoutBtn =findViewById(R.id.id_layout_btn);
        layoutVoucher =findViewById(R.id.id_re_voucher);
        layoutReversed1 =findViewById(R.id.id_detail_layout);
        layoutReversed2 =findViewById(R.id.id_re_detail);
        TextView tvCustomer =(TextView)findViewById(R.id.id_customerText);
        TextView tvAddress =(TextView)findViewById(R.id.id_address);
        tvBill =(TextView)findViewById(R.id.id_tv_bill);
        TextView tvGasFee =(TextView)findViewById(R.id.id_gas_amount);
        TextView tvDebts =(TextView)findViewById(R.id.id_debts);
        TextView tvTotalNum =(TextView)findViewById(R.id.id_total_num);
        TextView tvBalance =(TextView)findViewById(R.id.id_tv_balance);
        TextView tvLateFee =(TextView)findViewById(R.id.id_lastfees);
        tvVoucher =(TextView)findViewById(R.id.id_tv_voucher);
        tvReal =(TextView)findViewById(R.id.id_real_amount);
        btnPayFee =(Button)findViewById(R.id.id_btn_payfees);
        tvAddress.setText(name);
        tvCustomer.setText(customerNo);
        String allBalanceText=allBalance+"元";
        String gasFeeText=gasFee+"元";
        String debtsText="¥"+debts+"元";
        String realFeeText="¥"+realFee+"元";
        String totalNumText=totalNum+"立方米";
        String lateFeeText=lateFee+"元";
        if (TextUtils.isEmpty(debts)||debts.equals(NULL_VALUE)){
            layoutNoData.setVisibility(View.VISIBLE);
            layoutData.setVisibility(View.GONE);
            layoutBtn.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.GONE);
            layoutData.setVisibility(View.VISIBLE);
            layoutBtn.setVisibility(View.VISIBLE);
            tvBalance.setText(allBalanceText);
            tvGasFee.setText(gasFeeText);
            tvDebts.setText(debtsText);
            tvReal.setText(realFeeText);
            tvTotalNum.setText(totalNumText);
            tvLateFee.setText(lateFeeText);
        }
    }
    private void initEvent() {
        layoutVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucher=new Intent(context,SelectVoucherActivity.class);
                voucher.putExtra("pay_amount",debts);
                voucher.putExtra("category","2");
                startActivityForResult(voucher,1);
            }
        });
        btnPayFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeStatus){
                    requestServer();
                }else {
                    onQueryResult(id);
                }

            }
        });
        tvBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent amount=new Intent(context,BillDetailActivity.class);
                amount.putExtra("CustomerNo",customerNo);
                amount.putExtra("name",name);
                startActivity(amount);
            }
        });
        layoutReversed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PreexistenceDetail.class);
                intent.putExtra("CustomerNo",customerNo);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        layoutReversed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PreexistenceDetail.class);
                intent.putExtra("CustomerNo",customerNo);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }
    private void requestServer() {
        customDialog.show();
        String validateURL= UrlUtil.CREATE_ORDER_GAS;
        HashMap<String, String> textParams = new HashMap<String, String>(6);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type","1");
        textParams.put("amount",realFee);
        textParams.put("customerNo",customerNo);
        textParams.put("couponId",voucherId);
        OkHttpRequestManager.getInstance(context).requestAsyn(validateURL,OkHttpRequestManager.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
