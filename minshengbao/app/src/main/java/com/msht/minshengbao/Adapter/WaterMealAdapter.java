package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.graphics.Color;
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
 * Created by hong on 2017/12/27.
 */

public class WaterMealAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<HashMap<String, String>> typeList = new ArrayList<HashMap<String, String>>();
    public WaterMealAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        inflater=LayoutInflater.from(context);
        this.mContext=context;
        this.typeList=List;
    }
    @Override
    public int getCount() {
        if (typeList!=null){
            return typeList.size();
        }else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        if (typeList!=null){
            return typeList.get(position);
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
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_water_meal_type, null);
            holder.layout_back = convertView.findViewById(R.id.id_layout_back);
            holder.tv_amount=(TextView) convertView.findViewById(R.id.id_amount);
            holder.tv_giveFee=(TextView) convertView.findViewById(R.id.id_tv_giveFee);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_amount.setText(typeList.get(position).get("amount")+"元");
        String giveFee=typeList.get(position).get("giveFee");
        if ((!giveFee.equals(""))&&(!giveFee.equals("0"))){
            holder.tv_giveFee.setVisibility(View.VISIBLE);
            holder.tv_giveFee.setText("送"+giveFee+"元");
        }else {
            holder.tv_giveFee.setText("送0元");
            holder.tv_giveFee.setVisibility(View.VISIBLE);
        }
        if (VariableUtil.MealPos==position){
            holder.layout_back.setBackgroundResource(R.drawable.shape_orange_button);
            holder.tv_amount.setTextColor(Color.parseColor("#ffffffff"));
            holder.tv_giveFee.setTextColor(Color.parseColor("#ffffffff"));
        }else {
            holder.layout_back.setBackgroundResource(R.drawable.shape_orange_border_layout);
            holder.tv_amount.setTextColor(Color.parseColor("#fff96331"));
            holder.tv_giveFee.setTextColor(Color.parseColor("#fff96331"));
        }
        return convertView;
    }
    class Holder{
        View layout_back;
        TextView tv_amount;
        TextView tv_giveFee;
    }
}
