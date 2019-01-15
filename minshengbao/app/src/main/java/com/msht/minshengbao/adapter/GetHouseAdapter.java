package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/3/21
 */

public class GetHouseAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String,  String>> houseList = new ArrayList<HashMap<String,  String>>();
    public GetHouseAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            holder.addressWord = (TextView) convertView.findViewById(R.id.id_address_text);
            holder.cnCustomer =(TextView)convertView.findViewById(R.id.id_customerText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.addressWord.setText(houseList.get(position).get("name"));
        holder.cnCustomer.setText(houseList.get(position).get("customerNo"));
        holder.cnCustomer.setTextColor(Color.parseColor("#ff444444"));
        holder.addressWord.setTextColor(Color.parseColor("#ff444444"));
        return convertView;
    }
    class ViewHolder {
         TextView addressWord;
         TextView cnCustomer;
    }
}
