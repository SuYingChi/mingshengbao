package com.msht.minshengbao.FunctionActivity.WaterApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

public class WaterSuccess extends BaseActivity {
    private Button   btn_know;
    private TextView tv_amount;
    private TextView tv_account;
    private String   amount;
    private String   account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_success);
        mPageName="充值结果";
        context=this;
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        amount=getIntent().getStringExtra("amount");
        setCommonHeader(mPageName);
        initView();

    }
    private void initView() {
        tv_account=(TextView)findViewById(R.id.id_tv_account);
        tv_amount=(TextView)findViewById(R.id.id_tv_amount);
        tv_amount.setText(amount);
        tv_account.setText(account);
        findViewById(R.id.id_btn_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
