package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/21.
 */

public class GethouseAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();
    public GethouseAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.houseList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return houseList.size();
    }

    @Override
    public Object getItem(int position) {
        return houseList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_customerno, null);
            holder.addressimg = (ImageView) convertView.findViewById(R.id.id_addressimg);
            holder.address_word = (TextView) convertView.findViewById(R.id.id_address_text);
            holder.customerimg=(ImageView)convertView.findViewById(R.id.id_customerimg);
            holder.cn_customer=(TextView)convertView.findViewById(R.id.id_customerText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.customerimg.setImageResource(R.drawable.customer_xh);
        holder.addressimg.setImageResource(R.drawable.address_xh);
        holder.address_word.setText(houseList.get(position).get("name"));
        holder.cn_customer.setText(houseList.get(position).get("customerNo"));
        holder.cn_customer.setTextColor(Color.parseColor("#ff444444"));
        holder.address_word.setTextColor(Color.parseColor("#ff444444"));
        if(VariableUtil.mPos==position){
            holder.customerimg.setImageResource(R.drawable.customer_select);
            holder.addressimg.setImageResource(R.drawable.address_select);
            holder.cn_customer.setTextColor(Color.parseColor("#fff96331"));
            holder.address_word.setTextColor(Color.parseColor("#fff96331"));
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView addressimg;
        public ImageView customerimg;
        public TextView address_word;
        public TextView cn_customer;
    }
}
