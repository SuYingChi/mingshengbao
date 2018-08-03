package com.msht.minshengbao.functionActivity.GasService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class BillDetailActivity extends BaseActivity {
    private static ArrayList<HashMap<String,  String>> List = new ArrayList<HashMap<String,  String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        context=this;
        setCommonHeader("账单明细");
        List= VariableUtil.detailList;
        PushAgent.getInstance(context).onAppStart();
        Intent data=getIntent();
        String mCustomerNo=data.getStringExtra("CustomerNo");
        String name=data.getStringExtra("name");
        TextView tvCustomerNo =(TextView)findViewById(R.id.id_customerText);
        TextView tvAddress =(TextView)findViewById(R.id.id_address);
        TextView tvPrice =(TextView)findViewById(R.id.id_tv_rightText);
        tvPrice.setVisibility(View.VISIBLE);
        tvPrice.setText("气价说明");
        tvCustomerNo.setText(mCustomerNo);
        tvAddress.setText(name);
        ListView mListView=(ListView)findViewById(R.id.id_bill_view);
        BillAdapter billAdapter=new BillAdapter(context);
        mListView.setAdapter(billAdapter);
        tvPrice.setOnClickListener(new View.OnClickListener() {
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
                holder.cnAmount =(TextView) convertView.findViewById(R.id.id_debts);
                holder.cnBalance =(TextView)convertView.findViewById(R.id.id_tv_balance);
                holder.cnGasFee =(TextView) convertView.findViewById(R.id.id_gas_amount);
                holder.cnGasNum =(TextView) convertView.findViewById(R.id.id_total_num);
                holder.cnDate =(TextView) convertView.findViewById(R.id.id_tv_date);
                holder.cnLateFee =(TextView) convertView.findViewById(R.id.id_lastfees);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            String amountText="¥"+List.get(position).get("amounts")+"元";
            String balanceText="-"+List.get(position).get("balance")+"元";
            String gasNumText="共用气"+List.get(position).get("num")+"立方米";
            String gasFeeText=List.get(position).get("gas_fees")+"元";
            String lateFeeText=List.get(position).get("late_fee")+"元";
            holder.cnAmount.setText(amountText);
            holder.cnBalance.setText(balanceText);
            holder.cnDate.setText(List.get(position).get("date"));
            holder.cnGasFee.setText(gasFeeText);
            holder.cnGasNum.setText(gasNumText);
            holder.cnLateFee.setText(lateFeeText);
            return convertView;
        }
        class Holder{
            TextView cnAmount;
            TextView cnBalance;
            TextView cnGasFee;
            TextView cnGasNum;
            TextView cnDate;
            TextView cnLateFee;
        }
    }
}
