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
 * @date 2018/7/2  
 */
public class LpgBottleReplaceAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    public LpgBottleReplaceAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            convertView = mInflater.inflate(R.layout.layout_lpg_replace_bottle_header, null);
            holder.tvNorms = (TextView) convertView.findViewById(R.id.id_tv_norms);
            holder.tvCount = (TextView) convertView.findViewById(R.id.id_tv_count);
            holder.tvDegree = (TextView) convertView.findViewById(R.id.id_corrosive_degree);
            holder.tvDiscount=(TextView)convertView.findViewById(R.id.id_tv_discount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String corrosionType=mList.get(position).get("corrosionType");
        String count=mList.get(position).get("bottleCount");
        String norms=mList.get(position).get("bottleWeight")+"kg";
        String discount="¥"+mList.get(position).get("replacePrice");
        holder.tvDegree.setText(corrosionType);
        holder.tvNorms.setText(norms);
        holder.tvCount.setText(count);
        holder.tvDiscount.setText(discount);
        return convertView;
    }

    class ViewHolder {
        TextView  tvNorms;
        TextView  tvCount;
        TextView  tvDegree;
        TextView  tvDiscount;
    }
}
