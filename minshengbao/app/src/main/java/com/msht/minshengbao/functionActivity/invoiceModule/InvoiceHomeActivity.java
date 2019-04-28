package com.msht.minshengbao.functionActivity.invoiceModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.adapter.uiadapter.InvoiceAllViewAdapter;
import com.msht.minshengbao.adapter.uiadapter.RedPacketViewAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/4 
 */
public class InvoiceHomeActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_home);
        context=this;
        setCommonHeader("发票申请");
        String[] fragment=new String[]{"燃气缴费发票","维修清洗发票"};
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        ViewPager viewPager=(ViewPager)findViewById(R.id.id_viewPager_fees);
        viewPager.setAdapter(new InvoiceAllViewAdapter(getSupportFragmentManager(), getApplicationContext(),fragment));
        indicator.setViewPager(viewPager,0);
        initHeader();
    }
    private void initHeader() {
        TextView tvRightText=(TextView)findViewById(R.id.id_tv_rightText);
        tvRightText.setText("历史记录");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,InvoiceAllHistoryActivity.class);
                startActivity(intent);
            }
        });

    }
}
