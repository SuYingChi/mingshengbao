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
 *
 * @author hong
 * @date 2017/5/5
 */

public class HomeApplianceAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public HomeApplianceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext=context;
        this.functionList=List;
    }
    @Override
    public int getCount() {
        if (functionList!=null){
            return functionList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (functionList!=null){
            return functionList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_second_service, null);
            holder.imgFunction =(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tvName =(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tvName.setText(name);
        switch (code){
            case ConstantUtil.AIR_CONDITIONER_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.conditioner_clean_xh);
                break;
            case ConstantUtil.WASHING_MACHINE_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.washingmachine_clean_xh);
                break;
            case ConstantUtil.REFRIGERATOR_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.refrigerator_clean_xh);
                break;
            case ConstantUtil.HEATER_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.heater_clean_xh);
                break;
            case ConstantUtil.HOODS_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.hoods_clean);
                break;
            case ConstantUtil.GAS_STOVE_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.gas_stove_clean);
                break;
            case ConstantUtil.HOUSE_CLEANING:
                holder.imgFunction.setImageResource(R.drawable.homekeeping_clean_xh);
                break;
            case ConstantUtil.DEEP_CLEANLINESS:
                holder.imgFunction.setImageResource(R.drawable.acarid_clean_xh);
                break;
            case ConstantUtil.FIRST_CLEANLINESS:
                holder.imgFunction.setImageResource(R.drawable.sofa_clean_xh);
                break;
            case ConstantUtil.MITE_SERVICE:
                holder.imgFunction.setImageResource(R.drawable.gas_stove_clean);
                break;
            case ConstantUtil.FLOOR_WAXING:
                holder.imgFunction.setImageResource(R.drawable.gas_stove_clean);
                break;
            case ConstantUtil.MATTRESS_MITE:
                holder.imgFunction.setImageResource(R.drawable.acarid_clean_xh);
                break;
            case ConstantUtil.SOFA_CLEANLINESS:
                holder.imgFunction.setImageResource(R.drawable.sofa_clean_xh);
                break;
                default:
                    holder.imgFunction.setImageResource(R.drawable.conditioner_clean_xh);
                    break;
        }
        return convertView;
    }
    class ViewHolder {
         ImageView imgFunction;
         TextView tvName;
    }
}
