package com.msht.minshengbao.adapter;

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
 * Created by hong on 2017/4/27.
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
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tv_name.setText(name);
        if (code.equals("household_clean")){
            holder.img_function.setImageResource(R.drawable.home_appliance_clean_xh);
        }else if (code.equals("household_repair")){
            holder.img_function.setImageResource(R.drawable.home_appliance_fix_xh);
        }else if (code.equals("sanitary_ware")){
            holder.img_function.setImageResource(R.drawable.home_sanitary_xh);
        }else if (code.equals("lamp_circuit")){
            holder.img_function.setImageResource(R.drawable.home_lanterns_xh);
        }else if (code.equals("other_repair")){
            holder.img_function.setImageResource(R.drawable.home_otherfix_xh);
        }else if (code.equals("gas_serve")){
            holder.img_function.setImageResource(R.drawable.home_gassever_xh);
        }else if (code.equals("insurance")){
            holder.img_function.setImageResource(R.drawable.home_insurance_xh);
        }else if (code.equals("electric_vehicle_repair")){
            holder.img_function.setImageResource(R.drawable.electric_vehicle_xh);
        }else if (code.equals("all_service")){
            holder.img_function.setImageResource(R.drawable.all_service_xh);
        }else if (code.equals("intelligent_farm")){
            holder.img_function.setImageResource(R.drawable.intelligent_farm_xh);
        }else if (code.equals("drinking_water")){
            holder.img_function.setImageResource(R.drawable.water_app_xh);
        }else if (code.equals("vegetables_scxs")){
            holder.img_function.setImageResource(R.drawable.vegetable_gentalmen_xh);
        } else {
            holder.img_function.setImageResource(R.drawable.home_default_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView  tv_name;
    }
}
