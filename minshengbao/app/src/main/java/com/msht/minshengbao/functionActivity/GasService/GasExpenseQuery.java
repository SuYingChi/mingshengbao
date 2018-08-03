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
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog.isShowing()&&activity.customDialog!=null){
                        activity.customDialog.dismiss();
                    }
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
                    if (activity.customDialog.isShowing()&&activity.customDialog!=null){
                        activity.customDialog.dismiss();
                    }
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
        Intent getdata=getIntent();
        customerNo=getdata.getStringExtra("CustomerNo");
        allBalance=getdata.getStringExtra("all_balance");
        name=getdata.getStringExtra("name");
        debts=getdata.getStringExtra("debts");
        totalNum=getdata.getStringExtra("total_num");
        String discountFees=getdata.getStringExtra("discount_fees");
        gasFee=getdata.getStringExtra("gas_fee");
        lateFee=getdata.getStringExtra("late_fee");
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
                    tvReal.setText("¥"+realFee);
                    tvVoucher.setText(amount+"元");
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
        View layoutData =(LinearLayout)findViewById(R.id.id_layout_data);
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
        if (TextUtils.isEmpty(debts)||debts.equals(NULL_VALUE)){
            layoutNoData.setVisibility(View.VISIBLE);
            layoutData.setVisibility(View.GONE);
            layoutBtn.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.GONE);
            layoutData.setVisibility(View.VISIBLE);
            layoutBtn.setVisibility(View.VISIBLE);
            tvBalance.setText(allBalance+"元");
            tvGasFee.setText(gasFee+"元");
            tvDebts.setText("¥"+debts+"元");
            tvReal.setText("¥"+realFee+"元");
            tvTotalNum.setText(totalNum+"立方米");
            tvLateFee.setText(lateFee+"元");
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
        String validateURL= UrlUtil.CreateOrder_Gas;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type","1");
        textParams.put("amount",realFee);
        textParams.put("customerNo",customerNo);
        textParams.put("couponId",voucherId);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
