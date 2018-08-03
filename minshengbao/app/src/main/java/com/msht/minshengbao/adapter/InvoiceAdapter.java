package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/30.
 */

public class InvoiceAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> invoiceList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
    public InvoiceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.invoiceList=List;
    }
    @Override
    public int getCount() {
        if (invoiceList==null){
            return 0;
        }else {
            return invoiceList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (invoiceList==null){
            return null;
        }else {
            return invoiceList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_invoice_history, null);
            holder.tv_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            holder.tv_status=(TextView) convertView.findViewById(R.id.id_tv_status);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_tv_name);
            holder.tv_money=(TextView)convertView.findViewById(R.id.id_tv_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String status=invoiceList.get(position).get("status");
        if (status.equals("1")){
            holder.tv_status.setText("待寄出");
        }else {
            holder.tv_status.setText("已寄出");
        }
        holder.tv_time.setText(invoiceList.get(position).get("time"));
        holder.tv_name.setText(invoiceList.get(position).get("name"));
        holder.tv_money.setText(invoiceList.get(position).get("amount"));
        return convertView;
    }
    class ViewHolder {
        public TextView tv_time;
        public TextView  tv_status;
        public TextView  tv_name;
        public TextView  tv_money;
    }
}
