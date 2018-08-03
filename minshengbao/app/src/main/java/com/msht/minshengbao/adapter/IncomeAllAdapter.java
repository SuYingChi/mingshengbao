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
 * Created by hong on 2017/3/31.
 */
public class IncomeAllAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
    public IncomeAllAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.incomeList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (incomeList == null) {
            return 0;
        } else {
            return incomeList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (incomeList == null) {
            return null;
        } else {
            return incomeList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_income_record, null);
            holder.cn_content = (TextView) convertView.findViewById(R.id.id_type);
            holder.cn_amount = (TextView) convertView.findViewById(R.id.id_amount);
            holder.cn_time = (TextView) convertView.findViewById(R.id.id_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = incomeList.get(position).get("type");
        holder.cn_content.setText(incomeList.get(position).get("content"));
        if (type.equals("1")) {
            holder.cn_amount.setTextColor(0xfff96331);
            holder.cn_amount.setText("+"+incomeList.get(position).get("amount") + "元");
        } else if (type.equals("2")) {
            holder.cn_amount.setTextColor(0xff333333);
            holder.cn_amount.setText("-" + incomeList.get(position).get("amount") + "元");
        }else {
            holder.cn_amount.setTextColor(0xff333333);
            holder.cn_amount.setText("-" + incomeList.get(position).get("amount") + "元");
        }
        holder.cn_time.setText(incomeList.get(position).get("time"));
        return convertView;
    }
    class ViewHolder {
        public TextView  cn_content;
        public TextView  cn_amount;
        public TextView  cn_time;
    }
}
