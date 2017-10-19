package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/20.
 */

public class repairAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private int mPos;
    private ArrayList<HashMap<String, String>> typeList = new ArrayList<HashMap<String, String>>();
    public repairAdapter(Context context, ArrayList<HashMap<String, String>> List, int pos) {
        inflater=LayoutInflater.from(context);
        this.mContext=context;
        this.mPos=pos;
        this.typeList=List;
    }
    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeList.get(position);
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
            holder.repairtype = (TextView) convertView.findViewById(R.id.id_tv_table);
            holder.radio=(RadioButton)convertView.findViewById(R.id.id_radio);
            holder.Img=(ImageView)convertView.findViewById(R.id.id_table_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.Img.setImageResource(R.drawable.installtype);
        holder.repairtype.setText(typeList.get(position).get("name"));
        if (mPos==position){
            holder.radio.setChecked(true);
        }else {
            holder.radio.setChecked(false);
        }
        return convertView;
    }
    class Holder{
        RadioButton radio;
        ImageView Img;
        TextView repairtype;
    }
}
