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
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/23.
 */

public class SelectCityAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
    public SelectCityAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        inflater=LayoutInflater.from(context);
        this.mContext=context;
        this.cityList=List;
    }
    @Override
    public int getCount() {
        return cityList.size();
    }
    @Override
    public Object getItem(int position) {
        return cityList.get(position);
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
            holder.cn_city = (TextView) convertView.findViewById(R.id.id_tv_table);
            holder.radio=(RadioButton)convertView.findViewById(R.id.id_radio);
            holder.img=(ImageView)convertView.findViewById(R.id.id_table_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.img.setVisibility(View.GONE);
        holder.cn_city.setText(cityList.get(position).get("name"));
        if (VariableUtil.cityPos ==position){
            holder.radio.setChecked(true);
        }else {
            holder.radio.setChecked(false);
        }
        return convertView;
    }
    class Holder{
        ImageView   img;
        RadioButton radio;
        TextView cn_city;
    }
}
