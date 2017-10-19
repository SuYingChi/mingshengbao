package com.msht.minshengbao.Adapter;

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
 * Created by hong on 2017/3/20.
 */

public class GetrecordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> writeList = new ArrayList<HashMap<String, String>>();
    public GetrecordAdapter(Context context, ArrayList<HashMap<String, String>> mList) {
        super();
        this.writeList=mList;
        this.mContext=context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return writeList.size();
    }

    @Override
    public Object getItem(int position) {
        return writeList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_write_table, null);
            holder.cn_meter=(TextView) convertView.findViewById(R.id.id_table_data);
            holder.cn_costomer=(TextView)convertView.findViewById(R.id.id_customerNo);
            holder.cn_address=(TextView) convertView.findViewById(R.id.id_address_text);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cn_meter.setText(writeList.get(position).get("meter")+"NM³");
        holder.cn_address.setText(writeList.get(position).get("address"));
        holder.cn_costomer.setText(writeList.get(position).get("customerNo"));
        holder.cn_time.setText(writeList.get(position).get("time"));

        return convertView;
    }
    class ViewHolder {
        public TextView cn_meter;
        public TextView cn_address;
        public TextView cn_costomer;
        public TextView cn_time;
    }

}
