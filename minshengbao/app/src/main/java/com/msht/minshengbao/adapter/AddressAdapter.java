package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 选择地址
 * @author hong
 * @date 2018/7/6  
 */
public class AddressAdapter extends BaseAdapter{
    private ArrayList<HashMap<String, String>> addressList = new ArrayList<HashMap<String, String>>();

    private Context mContext = null;
    public AddressAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mContext = context;
        this.addressList=list;
    }
    @Override
    public int getCount() {
        if (addressList!=null){
            return addressList.size();
        }else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        if (addressList!=null){
            return addressList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int thisposition=position;
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_manage, null);
            holder.cnName =(TextView)convertView.findViewById(R.id.id_tv_name);
            holder.cnPhone =(TextView)convertView.findViewById(R.id.id_tv_phone);
            holder.cnAddress = (TextView) convertView.findViewById(R.id.id_tv_address);
            holder.imgEdit =(ImageView)convertView.findViewById(R.id.id_edit_img);
            holder.editLayout =convertView.findViewById(R.id.id_edit_layout);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.cnName.setText(addressList.get(position).get("name"));
        holder.cnAddress.setText(addressList.get(position).get("address"));
        holder.editLayout.setVisibility(View.GONE);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    class Holder{
        View editLayout;
        ImageView imgEdit;
        TextView cnAddress;
        TextView cnName;
        TextView cnPhone;
    }
}
