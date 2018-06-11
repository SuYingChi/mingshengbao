package com.msht.minshengbao.FunctionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.Public.PayfeeWay;
import com.msht.minshengbao.FunctionActivity.Public.SelectVoucher;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class GasExpenseQuery extends BaseActivity {
    private TextView  tv_customer,tv_gasfee,tv_bill;
    private TextView  Address,Tvdebts,Tvtotal_num;
    private TextView  tv_voucher,tv_balance;
    private TextView  Tvlate_fee,tv_real;
    private Button    btn_payfees;
    private LinearLayout Layout_data;
    private View      Layout_nodata,Layout_reversed1;
    private View      Layout_reversed2;
    private RelativeLayout Layout_btn,Rvoucher;
    private String CustomerNo,all_balance;
    private String houseId,voucherId="0";
    private String name,id;
    private String debts="";
    private String total_num;
    private String discount_fees,gas_fee,real_fee;
    private String late_fee;
    private String userId;
    private String password;
    private static final String NULL_VALUE="null";
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    private boolean placeStatus=true;

   private  Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog.isShowing()&&customDialog!=null){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            JSONObject json=object.getJSONObject("data");
                            id=json.optString("id");
                            Queryresult(id);
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    if (customDialog.isShowing()&&customDialog!=null){
                    customDialog.dismiss();
                    }
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void Queryresult(String id) {
        Intent amount=new Intent(context,PayfeeWay.class);
        amount.putExtra("CustomerNo",CustomerNo);
        amount.putExtra("amount",real_fee);
        amount.putExtra("id",id);
        startActivityForResult(amount,0x002);
    }
    private void showfaiture(String s) {
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
        if (TextUtils.isEmpty(debts)||debts.equals(NULL_VALUE)){
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
                if (placeStatus){
                    requestServer();
                }else {
                    Queryresult(id);
                }

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
    private void requestServer() {
        customDialog.show();
        String validateURL= UrlUtil.CreateOrder_Gas;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type","1");
        textParams.put("amount",real_fee);
        textParams.put("customerNo",CustomerNo);
        textParams.put("couponId",voucherId);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
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
}
