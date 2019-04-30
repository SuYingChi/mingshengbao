package com.msht.minshengbao.functionActivity.gasService;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/7/2  
 */
public class GasPayDetailActivity extends BaseActivity {
    private TextView tvPayAmount;
    private TextView tvOriginAmount;
    private TextView tvCustomerNo;
    private TextView tvPayTime;
    private TextView tvOverdueFine;
    private TextView tvGasNum;
    private TextView tvDiscountAmount;
    private CustomDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_pay_detail);
        context=this;
        setCommonHeader("缴费详情");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        initView();
        View layoutOverdueFine=findViewById(R.id.id_overdue_layout);
        View layoutDiscount=findViewById(R.id.id_discount_layout);
        if (data!=null){
            String customerNo=data.getStringExtra("customerNo");
            String payTime=data.getStringExtra("payTime");
            String usageAmount=data.getStringExtra("usageAmount")+"立方米";
            String overdueFine=data.getStringExtra("overdueFine");
            String totalDiscountAmt=data.getStringExtra("totalDiscountAmt");
            String amount=data.getStringExtra("amount");
            tvCustomerNo.setText(customerNo);
            tvPayTime.setText(payTime);
            tvGasNum.setText(usageAmount);
            String overdueFineText=overdueFine+"元";
            tvOverdueFine.setText(overdueFineText);
            String discountAmtText=totalDiscountAmt+"元";
            tvDiscountAmount.setText(discountAmtText);
            tvPayAmount.setText(amount);
            String originAmount="原价："+String.valueOf(TypeConvertUtil.convertToDouble(amount,0)+TypeConvertUtil.convertToDouble(totalDiscountAmt,0))+"元";
            tvOriginAmount.setText(originAmount);
            if (!TextUtils.isEmpty(overdueFine)&&!overdueFine.equals(ConstantUtil.VALUE_ZERO)){
                layoutOverdueFine.setVisibility(View.VISIBLE);
            }else {
                layoutOverdueFine.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(totalDiscountAmt)&&!totalDiscountAmt.equals(ConstantUtil.VALUE_ZERO)){
                layoutDiscount.setVisibility(View.VISIBLE);
            }else {
                layoutDiscount.setVisibility(View.GONE);
            }

        }
    }
    private void initView() {
        tvCustomerNo=(TextView)findViewById(R.id.id_customerNo);
        tvPayTime=(TextView)findViewById(R.id.id_pay_time);
        tvGasNum=(TextView)findViewById(R.id.id_user_gasNum);
        tvOriginAmount=(TextView)findViewById(R.id.id_origin_Amount);
        tvDiscountAmount=(TextView)findViewById(R.id.id_discount_amount);
        tvOverdueFine=(TextView)findViewById(R.id.id_overdue_fine);
        tvPayAmount=(TextView)findViewById(R.id.id_payAmount);
        tvOriginAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
