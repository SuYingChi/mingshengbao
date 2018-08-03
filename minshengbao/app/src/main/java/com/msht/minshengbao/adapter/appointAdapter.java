package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2017/5/18.
 */

public class appointAdapter extends BaseAdapter {
    private String[] appointTime={};
    private LayoutInflater inflater;
    private Context mContext;
    private int thispos;
    public appointAdapter(Context context, String[] Time, int pos) {
        inflater=LayoutInflater.from(context);
        this.appointTime=Time;
        this.mContext=context;
        this.thispos=pos;
    }
    @Override
    public int getCount() {
        return appointTime.length;
    }

    @Override
    public Object getItem(int position) {
        return appointTime[position];
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
            holder.appointtime = (TextView) convertView.findViewById(R.id.id_tv_table);
            holder.radio=(RadioButton)convertView.findViewById(R.id.id_radio);
            holder.Img=(ImageView)convertView.findViewById(R.id.id_table_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        if (thispos==position){
            holder.radio.setChecked(true);
        }else {
            holder.radio.setChecked(false);
        }
        holder.Img.setImageResource(R.drawable.timeimg);
        holder.appointtime.setText(appointTime[position]);
        return convertView;
    }
    class Holder{
        ImageView Img;
        RadioButton radio;
        TextView appointtime;
    }
}
