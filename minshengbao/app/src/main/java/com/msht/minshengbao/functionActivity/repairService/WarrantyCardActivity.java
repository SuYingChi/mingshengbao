package com.msht.minshengbao.functionActivity.repairService;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/17  
 */
public class WarrantyCardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrantycard);
        context=this;
        Intent data=getIntent();
        String title=data.getStringExtra("title");
        String orderNo=data.getStringExtra("orderNo");
        String guaranteeDay=data.getStringExtra("guaranteeDay");
        String masterName=data.getStringExtra("masterName");
        String serveTime=data.getStringExtra("serveTime");
        setCommonHeader("保修卡");
        ((TextView)findViewById(R.id.id_maintype)).setText(title);
        ((TextView)findViewById(R.id.id_tv_orderno)).setText(orderNo);
        ((TextView)findViewById(R.id.id_gurantee_day)).setText(guaranteeDay+"天");
        ((TextView)findViewById(R.id.id_tv_mastername)).setText(masterName);
        ((TextView)findViewById(R.id.id_repair_date)).setText(serveTime);
    }
}
