package com.msht.minshengbao.FunctionView.GasService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionView.Public.PayfeeWay;
import com.msht.minshengbao.FunctionView.Public.SelectVoucher;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GasExpenseQuery extends BaseActivity {
    private TextView  tv_customer,tv_gasfee,tv_bill;
    private TextView  Address,Tvdebts,Tvtotal_num,tv_balance;
    private TextView tv_voucher,Tvlate_fee,tv_real;
    private Button btn_payfees;
    private LinearLayout Layout_data;
    private View  Layout_nodata,Layout_reversed1,Layout_reversed2;
    private RelativeLayout Layout_btn,Rvoucher;
    private String CustomerNo,all_balance;
    private String houseId,voucherId="0";
    private String name;
    private String debts;
    private String total_num;
    private String discount_fees,gas_fee,real_fee;
    private String late_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_expense_query);
        context=this;
        setCommonHeader("燃气费用");
        PushAgent.getInstance(context).onAppStart();   //推送统计
        Intent getdata=getIntent();
        CustomerNo=getdata.getStringExtra("CustomerNo");
        all_balance=getdata.getStringExtra("all_balance");
        name=getdata.getStringExtra("name");
        debts=getdata.getStringExtra("debts");
        total_num=getdata.getStringExtra("total_num");
        discount_fees=getdata.getStringExtra("discount_fees");
        gas_fee=getdata.getStringExtra("gas_fee");
        late_fee=getdata.getStringExtra("late_fee");
        real_fee=debts;
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
                    real_fee=format.format(fee);
                    tv_real.setText("¥"+real_fee);
                    tv_voucher.setText(amount+"元");
                }
                break;
            case 0x002:
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
        Layout_data=(LinearLayout)findViewById(R.id.id_layout_data);
        Layout_nodata=findViewById(R.id.id_nodata_layout);
        Layout_btn=(RelativeLayout)findViewById(R.id.id_layout_btn);
        Rvoucher=(RelativeLayout)findViewById(R.id.id_re_voucher);
        Layout_reversed1=findViewById(R.id.id_detail_layout);
        Layout_reversed2=findViewById(R.id.id_re_detail);
        tv_customer=(TextView)findViewById(R.id.id_customerText);
        Address=(TextView)findViewById(R.id.id_address);
        tv_bill=(TextView)findViewById(R.id.id_tv_bill);
        tv_gasfee=(TextView)findViewById(R.id.id_gas_amount);
        Tvdebts=(TextView)findViewById(R.id.id_debts);
        Tvtotal_num=(TextView)findViewById(R.id.id_total_num);
        tv_balance=(TextView)findViewById(R.id.id_tv_balance);
        Tvlate_fee=(TextView)findViewById(R.id.id_lastfees);
        tv_voucher=(TextView)findViewById(R.id.id_tv_voucher);
        tv_real=(TextView)findViewById(R.id.id_real_amount);
        btn_payfees=(Button)findViewById(R.id.id_btn_payfees);
        Address.setText(name);
        tv_customer.setText(CustomerNo);
        if (debts.equals("null")){
            Layout_nodata.setVisibility(View.VISIBLE);
            Layout_data.setVisibility(View.GONE);
            Layout_btn.setVisibility(View.GONE);
        }else {
            Layout_nodata.setVisibility(View.GONE);
            Layout_data.setVisibility(View.VISIBLE);
            Layout_btn.setVisibility(View.VISIBLE);
            tv_balance.setText(all_balance+"元");
            tv_gasfee.setText(gas_fee+"元");
            Tvdebts.setText("¥"+debts+"元");
            tv_real.setText("¥"+real_fee+"元");
            Tvtotal_num.setText(total_num+"立方米");
            Tvlate_fee.setText(late_fee+"元");
        }
    }
    private void initEvent() {
        Rvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucher=new Intent(context,SelectVoucher.class);
                voucher.putExtra("pay_amount",debts);
                voucher.putExtra("category","2");
                startActivityForResult(voucher,1);
            }
        });
        btn_payfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent amount=new Intent(context,PayfeeWay.class);
                amount.putExtra("CustomerNo",CustomerNo);
                amount.putExtra("amount",real_fee);
                amount.putExtra("voucherid",voucherId);
                startActivityForResult(amount,0x002);
            }
        });
        tv_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent amount=new Intent(context,BillDetail.class);
                amount.putExtra("CustomerNo",CustomerNo);
                amount.putExtra("name",name);
                startActivity(amount);
            }
        });
        Layout_reversed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PreexistenceDetail.class);
                intent.putExtra("CustomerNo",CustomerNo);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        Layout_reversed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PreexistenceDetail.class);
                intent.putExtra("CustomerNo",CustomerNo);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }
}
