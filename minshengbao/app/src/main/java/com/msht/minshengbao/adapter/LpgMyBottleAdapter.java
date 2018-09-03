package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class LpgMyBottleAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> integerList = new ArrayList<HashMap<String, String>>();
    public LpgMyBottleAdapter(Context context, ArrayList<HashMap<String, String>> mList) {

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
            holder.imageBottle=(ImageView)convertView.findViewById(R.id.id_bottle_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String day=integerList.get(position).get("usedDate");
        String bottleWeight=integerList.get(position).get("bottleWeight");
        String bottleWeightText=bottleWeight+"kg瓶装气";
        String bottleId="编号："+integerList.get(position).get("bottleCode");
        holder.tvDay.setText(day);
        holder.tvBottleType.setText(bottleWeightText);
        holder.tvBottleNo.setText(bottleId);
        switch (bottleWeight){
            case ConstantUtil.VALUE_FIVE:
                holder.imageBottle.setImageResource(R.drawable.five_weight_bottle_xh);
                break;
            case ConstantUtil.VALUE_FIFTEEN:
                holder.imageBottle.setImageResource(R.drawable.fifteen_weight_bottle_xh);
                break;
            case ConstantUtil.VALUE_FIFTY:
                holder.imageBottle.setImageResource(R.drawable.fifty_weight_bottle_xh);
                break;
            default:
                holder.imageBottle.setImageResource(R.drawable.fifteen_weight_bottle_xh);
                break;

        }
        return convertView;
    }
    class ViewHolder {

         TextView tvBottleNo;
         TextView tvBottleType;
         TextView tvDay;
         ImageView imageBottle;

    }
}
