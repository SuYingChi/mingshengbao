package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.PrizesGiftsActivity;

/**
 * Demo class
 * 〈水宝充值结果
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterSuccessActivity extends BaseActivity {
    private String   amount;
    private String   account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_success);
        mPageName="充值结果";
        context=this;
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        amount=getIntent().getStringExtra("amount")+"元";
        setCommonHeader(mPageName);
        initView();
    }
    private void initView() {
        View layoutHeader=findViewById(R.id.id_re_layout);
        layoutHeader.setBackgroundResource(R.drawable.shape_change_blue_bg);
        TextView tvAccount =(TextView)findViewById(R.id.id_tv_account);
        TextView tvAmount =(TextView)findViewById(R.id.id_tv_amount);
        tvAmount.setText(amount);
        tvAccount.setText(VariableUtil.waterAccount);
        findViewById(R.id.id_advertising_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= UrlUtil.WATER_PRIZES_GIFTS+"?phone="+account;
                Intent intent=new Intent(context, PrizesGiftsActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("navigate","领取礼品");
                startActivity(intent);
            }
        });
    }
}