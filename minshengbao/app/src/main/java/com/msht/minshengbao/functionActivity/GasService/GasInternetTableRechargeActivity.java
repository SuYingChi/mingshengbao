package com.msht.minshengbao.functionActivity.GasService;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/8  
 */
public class InternetTableRechargeActivity extends BaseActivity {
    private EditText etGasNum;
    private Button   btnRecharge;
    private String   mAddress;
    private String   mCustomerNo;
    private String   mLastGasNum;
    private String   mLastTime;
    private String   mGasNum;
    private String   mAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_table_recharge);
        context=this;
        setCommonHeader("物联网表充值");
        mAddress=getIntent().getStringExtra("address");
        mCustomerNo=getIntent().getStringExtra("customerNo");
        initFindViewId();
    }
    private void initFindViewId() {
        TextView tvRightText=(TextView)findViewById(R.id.id_tv_rightText);
        tvRightText.setText("充值记录");
        tvRightText.setVisibility(View.VISIBLE);
        TextView tvLastTime=(TextView)findViewById(R.id.id_last_time);
        TextView tvLastGasNum=(TextView)findViewById(R.id.id_last_gasNum);
        TextView tvCustomerNo=(TextView)findViewById(R.id.id_customerNo);
        TextView tvAddress=(TextView)findViewById(R.id.id_tv_address);
        tvCustomerNo.setText(mCustomerNo);
        tvAddress.setText(mAddress);
    }
}
