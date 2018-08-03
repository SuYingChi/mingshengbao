package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/20.
 */

public class tableAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private int mPos;
    private ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
    public tableAdapter(Context context, ArrayList<HashMap<String, String>> List, int pos) {
        inflater=LayoutInflater.from(context);
        this.tableList=List;
        this.mContext=context;
        this.mPos=pos;
    }
    @Override
    public int getCount() {
       return tableList.size();
    }
    @Override
    public Object getItem(int position) {
        return tableList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_table, null);
            holder.address = (TextView) convertView.findViewById(R.id.id_tv_table);
            holder.radio=(RadioButton)convertView.findViewById(R.id.id_radio);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.address.setText(tableList.get(position).get("address"));
        if (mPos==position){
            holder.radio.setChecked(true);
        }else {
            holder.radio.setChecked(false);
        }
        return convertView;
    }
    class Holder{
        TextView address;
        RadioButton radio;
    }
}
