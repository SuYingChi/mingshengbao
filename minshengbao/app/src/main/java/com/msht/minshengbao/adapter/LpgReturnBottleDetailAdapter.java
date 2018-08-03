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
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/26  
 */
public class LpgReturnBottleDetailAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    public LpgReturnBottleDetailAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mList=list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return mList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_lpg_return_bottle_detail, null);
            holder.tvBottleType = (TextView) convertView.findViewById(R.id.id_bottle_type);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.id_five_amount);
            holder.tvCreateDate = (TextView) convertView.findViewById(R.id.id_bottle_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String bottleType=mList.get(position).get("bottleWeight")+"kg瓶装气";
        String createDate=mList.get(position).get("createDate");
        String amount="¥"+mList.get(position).get("amount");
        holder.tvBottleType.setText(bottleType);
        holder.tvAmount.setText(amount);
        holder.tvCreateDate.setText(createDate);
        return convertView;
    }
    class ViewHolder {
        TextView  tvBottleType;
        TextView  tvAmount;
        TextView  tvCreateDate;
    }
}
