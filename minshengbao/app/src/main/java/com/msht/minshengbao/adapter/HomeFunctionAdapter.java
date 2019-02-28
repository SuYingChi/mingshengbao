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
 * @date 2017/4/27
 */

public class HomeFunctionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public HomeFunctionAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            convertView = mInflater.inflate(R.layout.item_function_view, null);
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
            case ConstantUtil.HOUSEHOLD_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.SANITARY_WARE:
                holder.imgFunction.setImageResource(R.drawable.home_sanitary_xh);
                break;
            case ConstantUtil.LAMP_CIRCUIT:
                holder.imgFunction.setImageResource(R.drawable.home_lanterns_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.GAS_SERVE:
                holder.imgFunction.setImageResource(R.drawable.home_gassever_xh);
                break;
            case ConstantUtil.ELECTRIC_VEHICLE_REPAIR:
                holder.imgFunction.setImageResource(R.drawable.electric_vehicle_xh);
                break;
            case ConstantUtil.INSURANCE:
                holder.imgFunction.setImageResource(R.drawable.home_insurance_xh);
                break;
            case ConstantUtil.ALL_SERVICE:
                holder.imgFunction.setImageResource(R.drawable.all_service_xh);
                break;
            case ConstantUtil.INTELLIGENT_FARM:
                holder.imgFunction.setImageResource(R.drawable.intelligent_farm_xh);
                break;
            case ConstantUtil.DRINKING_WATER:
                holder.imgFunction.setImageResource(R.drawable.water_app_xh);
                break;
            case ConstantUtil.VEGETABLE_SCXS:
                holder.imgFunction.setImageResource(R.drawable.vegetable_gentalmen_xh);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                holder.imgFunction.setImageResource(R.drawable.housekeeping_clean_xh);
                break;
            case ConstantUtil.HOME_MAINTENANCE:
                holder.imgFunction.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.LPG_NAME:
                holder.imgFunction.setImageResource(R.drawable.lpg_home_xh);
                break;
            case ConstantUtil.SHOP:
                holder.imgFunction.setImageResource(R.drawable.top_shopmall_xh);
                break;
            case ConstantUtil.GAS_PAY:
                holder.imgFunction.setImageResource(R.drawable.top_gaspay_xh);
                break;
            case ConstantUtil.GAS_METER:
                holder.imgFunction.setImageResource(R.drawable.top_gas_writetable_xh);
                break;
            case ConstantUtil.GAS_IC_CARD:
                holder.imgFunction.setImageResource(R.drawable.top_iccard_xh);
                break;
            case ConstantUtil.SERVICE_STATION:
                holder.imgFunction.setImageResource(R.drawable.service_station_xh);
                break;
                default:
                    holder.imgFunction.setImageResource(R.drawable.home_default_xh);
                    break;
        }
        return convertView;
    }
    class ViewHolder {
        ImageView imgFunction;
        TextView tvName;
    }
}
