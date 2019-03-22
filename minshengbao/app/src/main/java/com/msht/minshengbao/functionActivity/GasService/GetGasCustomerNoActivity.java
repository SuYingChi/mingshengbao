package com.msht.minshengbao.functionActivity.GasService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.ToastUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/3/8 
 */
public class GetGasCustomerNoActivity extends BaseActivity {
    private TextView tvCustomerNo;
    private TextView tvAddress;
    private Button   btnQuery;
    private EditText etTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gas_customer_no);
        setCommonHeader("获取用户号");
        initView();
    }
    private void initView() {
        findViewById(R.id.id_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallPhoneUtil.callPhone(context,"963666");
            }
        });
        findViewById(R.id.id_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCopyLink();
            }
        });
    }
    private void onCopyLink() {
        ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData=ClipData.newPlainText("Label","");
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtil.ToastText(context,"已复制到剪切板");
        }
    }
}
