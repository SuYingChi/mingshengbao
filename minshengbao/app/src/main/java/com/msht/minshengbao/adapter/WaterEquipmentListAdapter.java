package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/4/2  
 */
public class WaterEquipmentListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public WaterEquipmentListAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        this.mList=List;
        this.context=context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_water_near_equipment, null);
            holder.tvCommunityName=(TextView) convertView.findViewById(R.id.id_communityName);
            holder.tvAddress=(TextView)convertView.findViewById(R.id.id_tv_address);
            holder.tvDistance=(TextView)convertView.findViewById(R.id.id_tv_distance) ;
            holder.layoutItem=convertView.findViewById(R.id.id_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String address=mList.get(position).get("address");
        String equipment=mList.get(position).get("equipmentNo");
        String distance=mList.get(position).get("distance");
        if (equipment.equals(ConstantUtil.VALUE_ONE)) {
            distance=mList.get(position).get("distance");
            holder.tvDistance.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        }else {
            holder.tvDistance.setTextColor(ContextCompat.getColor(context,R.color.blue_end));
        }
        holder.tvCommunityName.setText(mList.get(position).get("communityName"));
        holder.tvAddress.setText(address);
        holder.tvDistance.setText(distance);
        if (VariableUtil.mPos==position){
            holder.layoutItem.setBackgroundResource(R.color.background_gray);
        }else {
            holder.layoutItem.setBackgroundResource(R.color.white);
        }
        return convertView;
    }
    class ViewHolder {
        View      layoutItem;
        TextView  tvCommunityName;
        TextView  tvAddress;
        TextView  tvDistance;
    }
}
