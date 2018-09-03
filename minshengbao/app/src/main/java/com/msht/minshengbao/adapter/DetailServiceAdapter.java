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
 * Created by hong on 2017/4/24.
 */

public class DetailServiceAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context mContext;
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    public DetailServiceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.mContext=context;
        this.dataList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_gassever_detail, null);
            holder.cn_name = (TextView) convertView.findViewById(R.id.id_name);
            holder.cn_description = (TextView) convertView.findViewById(R.id.id_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cn_name.setText(dataList.get(position).get("name"));
        holder.cn_description.setText(dataList.get(position).get("description"));
        return convertView;
    }
    class ViewHolder {
        public TextView cn_description;
        public TextView cn_name;
    }
}
