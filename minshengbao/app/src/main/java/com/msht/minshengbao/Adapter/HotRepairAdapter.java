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
 * Created by hong on 2017/7/18.
 */

public class HotRepairAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> hotList = new ArrayList<HashMap<String, String>>();
    public HotRepairAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext=context;
        this.hotList=List;
    }
    @Override
    public int getCount() {
        if (hotList!=null){
            return hotList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (hotList!=null){
            return hotList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_hot_repair, null);
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=hotList.get(position).get("name");
        String code=hotList.get(position).get("code");
        holder.tv_name.setText(name);
        if (code.equals("air_conditioner_repair")){
            holder.img_function.setImageResource(R.drawable.home_hot_airfix_2xh);
        }else if (code.equals("washing_machine_repair")){
            holder.img_function.setImageResource(R.drawable.home_hot_washing_2xh);
        }else if (code.equals("refrigerator_repair")){
            holder.img_function.setImageResource(R.drawable.home_refrigerator_repair_xh);
        }else if (code.equals("heater_repair")){
            holder.img_function.setImageResource(R.drawable.heater_repair_xh);
        }else if (code.equals("gas_stove_clean")){
            holder.img_function.setImageResource(R.drawable.home_hot_gasstove_2xh);
        }else if (code.equals("hoods_repair")){
            holder.img_function.setImageResource(R.drawable.hoods_repair_xh);
        }else if (code.equals("computer_repair")){
            holder.img_function.setImageResource(R.drawable.computer_repair_xh);
        }else if (code.equals("sterilizer_repair")){
            holder.img_function.setImageResource(R.drawable.sterilizer_repair_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView  tv_name;
    }
}
