package com.msht.minshengbao.FunctionView.repairService;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class Warrantycard extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrantycard);
        context=this;
        Intent data=getIntent();
        String title=data.getStringExtra("title");
        String orderNo=data.getStringExtra("orderNo");
        String guarantee_day=data.getStringExtra("guarantee_day");
        String mastername=data.getStringExtra("mastername");
        String serve_time=data.getStringExtra("serve_time");
        setCommonHeader("保修卡");
        ((TextView)findViewById(R.id.id_maintype)).setText(title);
        ((TextView)findViewById(R.id.id_tv_orderno)).setText(orderNo);
        ((TextView)findViewById(R.id.id_gurantee_day)).setText(guarantee_day+"天");
        ((TextView)findViewById(R.id.id_tv_mastername)).setText(mastername);
        ((TextView)findViewById(R.id.id_repair_date)).setText(serve_time);
    }
}
