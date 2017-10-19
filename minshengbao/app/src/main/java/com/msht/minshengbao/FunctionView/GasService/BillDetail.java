package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionView.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class BillDetail extends BaseActivity {
    private static ArrayList<HashMap<String,  String>> List = new ArrayList<HashMap<String,  String>>();
    private ListView mListView;
    private TextView tv_customerNo,tv_address;
    private TextView  tv_price;
    private String CustomerNo;
    private String name;
    private BillAdapter  billAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        context=this;
        setCommonHeader("账单明细");
        List= VariableUtil.detailList;
        PushAgent.getInstance(context).onAppStart();   //推送统计
        Intent getdata=getIntent();
        CustomerNo=getdata.getStringExtra("CustomerNo");
        name=getdata.getStringExtra("name");
        tv_customerNo=(TextView)findViewById(R.id.id_customerText);
        tv_address=(TextView)findViewById(R.id.id_address);
        tv_price=(TextView)findViewById(R.id.id_tv_rightText);
        tv_price.setVisibility(View.VISIBLE);
        tv_price.setText("气价说明");
        tv_customerNo.setText(CustomerNo);
        tv_address.setText(name);
        mListView=(ListView)findViewById(R.id.id_bill_view);
        billAdapter=new BillAdapter(context);
        mListView.setAdapter(billAdapter);
        tv_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String navigate="气价说明";
                String url= UrlUtil.Gasprice_Url;
                Intent price=new Intent(context,HtmlPage.class);
                price.putExtra("navigate",navigate);
                price.putExtra("url",url);
                startActivity(price);
            }
        });
    }
    private class BillAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        Context context;
        public BillAdapter(Context context) {
            inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return List.size();
        }
        @Override
        public Object getItem(int position) {
            return List.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView==null){
                holder = new Holder();
                convertView = inflater.inflate(R.layout.item_gas_bill, null);
                holder.cn_amount=(TextView) convertView.findViewById(R.id.id_debts);
                holder.cn_balance=(TextView)convertView.findViewById(R.id.id_tv_balance);
                holder.cn_gasfee=(TextView) convertView.findViewById(R.id.id_gas_amount);
                holder.cn_gasnum=(TextView) convertView.findViewById(R.id.id_total_num);
                holder.cn_date=(TextView) convertView.findViewById(R.id.id_tv_date);
                holder.cn_latefee=(TextView) convertView.findViewById(R.id.id_lastfees);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.cn_amount.setText("¥"+List.get(position).get("amounts")+"元");

            holder.cn_balance.setText("-"+List.get(position).get("balance")+"元");
            holder.cn_date.setText(List.get(position).get("date"));
            holder.cn_gasfee.setText(List.get(position).get("gas_fees")+"元");
            holder.cn_gasnum.setText("共用气"+List.get(position).get("num")+"立方米");
            holder.cn_latefee.setText(List.get(position).get("late_fee")+"元");
            return convertView;
        }
        class Holder{
            TextView cn_amount;
            TextView cn_balance;
            TextView cn_gasfee;
            TextView cn_gasnum;
            TextView cn_date;
            TextView cn_latefee;
        }
    }
}
