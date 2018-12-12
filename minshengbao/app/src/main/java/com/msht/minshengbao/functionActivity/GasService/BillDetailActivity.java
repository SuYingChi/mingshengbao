package com.msht.minshengbao.functionActivity.GasService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.adapter.GasBillAdapter;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/6  
 */
public class BillDetailActivity extends BaseActivity {
    private static ArrayList<HashMap<String,  String>> List = new ArrayList<HashMap<String,  String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        context=this;
        mPageName="账单明细";
        setCommonHeader(mPageName);
        List= VariableUtil.detailList;
        PushAgent.getInstance(context).onAppStart();
        Intent data=getIntent();
        String mCustomerNo=data.getStringExtra("CustomerNo");
        String name=data.getStringExtra("name");
        TextView tvCustomerNo =(TextView)findViewById(R.id.id_customerText);
        TextView tvAddress =(TextView)findViewById(R.id.id_address);
        TextView tvPrice =(TextView)findViewById(R.id.id_tv_rightText);
        XRecyclerView mRecyclerView=(XRecyclerView )findViewById(R.id.id_bill_view);
        tvPrice.setVisibility(View.VISIBLE);
        tvPrice.setText("气价说明");
        tvCustomerNo.setText(mCustomerNo);
        tvAddress.setText(name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        GasBillAdapter mAdapter=new GasBillAdapter(List);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String navigate="气价说明";
                String url= UrlUtil.Gasprice_Url;
                Intent price=new Intent(context,HtmlPageActivity.class);
                price.putExtra("navigate",navigate);
                price.putExtra("url",url);
                startActivity(price);
            }
        });
    }
}
