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
 * Created by hong on 2017/5/8.
 */

public class SanitaryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public SanitaryAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tv_name.setText(name);
        if (code.equals("water_pipe")){
            holder.img_function.setImageResource(R.drawable.water_pipe_xh);
        }else if (code.equals("water_tap")){
            holder.img_function.setImageResource(R.drawable.water_tap_xh);
        }else if (code.equals("shower")){
            holder.img_function.setImageResource(R.drawable.shower_xh);
        }else if (code.equals("closestool")){
            holder.img_function.setImageResource(R.drawable.closestool_xh);
        }else if (code.equals("bathroom_cabinet")){
            holder.img_function.setImageResource(R.drawable.bathroom_cabinet_xh);
        }else if (code.equals("lamp")){
            holder.img_function.setImageResource(R.drawable.lamp_xh);
        }else if (code.equals("switch_socket")){
            holder.img_function.setImageResource(R.drawable.switch_socket_xh);
        }else if (code.equals("circuit")){
            holder.img_function.setImageResource(R.drawable.circuit_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView  tv_name;
    }
}
