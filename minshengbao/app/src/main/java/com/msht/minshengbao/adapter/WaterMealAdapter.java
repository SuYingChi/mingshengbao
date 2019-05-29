package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/12/27
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
            holder.layoutBack = convertView.findViewById(R.id.id_layout_back);
            holder.tvAmount =(TextView) convertView.findViewById(R.id.id_amount);
            holder.tvGiveFee =(TextView) convertView.findViewById(R.id.id_tv_giveFee);
            holder.tvWaterVolume=(TextView)convertView.findViewById(R.id.id_water_volume);
            holder.tvOriginAmount=(TextView)convertView.findViewById(R.id.id_origin_amount);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        String amount=typeList.get(position).get("amount");
        String amountText=amount+"元";
        String giveFee=typeList.get(position).get("giveFee");
        String giveFeeText="多送"+giveFee+"元";
        String waterQuantity=typeList.get(position).get("waterQuantity")+"L";
        String originAmount=TypeConvertUtil.getStringAddToDouble(amount,giveFee)+"元";
        holder.tvAmount.setText(amountText);
        holder.tvOriginAmount.setText(originAmount);
        holder.tvWaterVolume.setText(waterQuantity);
        holder.tvOriginAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (!TextUtils.isEmpty(giveFee)&&!giveFee.equals(ConstantUtil.VALUE_ZERO)){
            holder.tvGiveFee.setVisibility(View.VISIBLE);
            holder.tvGiveFee.setText(giveFeeText);
        }else {
            holder.tvGiveFee.setText(giveFeeText);
            holder.tvGiveFee.setVisibility(View.INVISIBLE);
        }
        if (VariableUtil.MealPos==position){
            holder.layoutBack.setBackgroundResource(R.drawable.shape_orange_border_layout);
            holder.tvAmount.setTextColor(Color.parseColor("#ff1C94F3"));
            holder.tvWaterVolume.setTextColor(Color.parseColor("#ff1C94F3"));
        }else {
            holder.layoutBack.setBackgroundResource(R.drawable.shape_gray_corner_retangle_bg);
            holder.tvAmount.setTextColor(Color.parseColor("#ff383838"));
            holder.tvWaterVolume.setTextColor(Color.parseColor("#ff383838"));
        }
        return convertView;
    }
    class Holder{
        View layoutBack;
        TextView tvAmount;
        TextView tvGiveFee;
        TextView tvWaterVolume;
        TextView tvOriginAmount;
    }
}
