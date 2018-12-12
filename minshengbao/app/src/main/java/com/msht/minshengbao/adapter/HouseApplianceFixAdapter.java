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
 * @date 2017/5/8
 */

public class HouseApplianceFixAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public HouseApplianceFixAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            case ConstantUtil.AIR_CONDITIONER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.conditioner_repair_xh);
                break;
            case ConstantUtil.WASHING_MACHINE_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.washmachine_repair_xh);
                break;
            case ConstantUtil.REFRIGERATOR_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.refrigerator_repair_xh);
                break;
            case ConstantUtil.HEATER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.heater_repair_xh);
                break;
            case ConstantUtil.GAS_STOVE_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.gas_stove_repair);
                break;
            case ConstantUtil.HOODS_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.hoods_repair_xh);
                break;
            case ConstantUtil.COMPUTER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.computer_repair_xh);
                break;
            case ConstantUtil.STERILIZER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.sterilizer_repair_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.home_appliance_repair_xh);
                break;
            case ConstantUtil.SANITARY_WARE:
                holder.imgFunction.setImageResource(R.drawable.water_electric_repair_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.home_other_repair_xh);
                break;
                default:
                    break;
        }
        return convertView;
    }
    class ViewHolder {
         ImageView imgFunction;
         TextView tvName;
    }
}
