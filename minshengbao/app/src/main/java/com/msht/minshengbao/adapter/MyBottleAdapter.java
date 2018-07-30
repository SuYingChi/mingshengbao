package com.msht.minshengbao.Adapter;

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
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class MyBottleAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> integerList = new ArrayList<HashMap<String, String>>();
    public MyBottleAdapter(Context context, ArrayList<HashMap<String, String>> mList) {

        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.integerList=mList;
    }

    @Override
    public int getCount() {
        if (integerList!=null){
            return integerList.size();
        }else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        if (integerList!=null){
            return integerList.get(position);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder =new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_my_bottle, null);
            holder.tvBottleNo=(TextView) convertView.findViewById(R.id.id_bottleNo);
            holder.tvBottleType=(TextView)convertView.findViewById(R.id.id_bottle_type);
            holder.tvDay=(TextView)convertView.findViewById(R.id.id_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String day=integerList.get(position).get("usedDate");
        String bottleWeight=integerList.get(position).get("bottleWeight")+"kg瓶装气";
        String bottleId="编号："+integerList.get(position).get("id");
        holder.tvDay.setText(day);
        holder.tvBottleType.setText(bottleWeight);
        holder.tvBottleNo.setText(bottleId);
        return convertView;
    }
    class ViewHolder {

         TextView tvBottleNo;
         TextView tvBottleType;
         TextView tvDay;

    }
}
