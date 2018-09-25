package com.msht.minshengbao.functionActivity.Invoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceSendSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_send_success);
        context=this;
        mPageName="电子发票提交成功";
        setCommonHeader(mPageName);
        findViewById(R.id.id_btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.id_btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InvoiceGasHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
