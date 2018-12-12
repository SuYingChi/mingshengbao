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
            case ConstantUtil.WATER_PIPE:
                holder.imgFunction.setImageResource(R.drawable.water_pipe_xh);
                break;
            case ConstantUtil.WATER_TAP:
                holder.imgFunction.setImageResource(R.drawable.water_tap_xh);
                break;
            case ConstantUtil.SHOWER:
                holder.imgFunction.setImageResource(R.drawable.shower_xh);
                break;
            case ConstantUtil.CLOSESTOOL:
                holder.imgFunction.setImageResource(R.drawable.closestool_xh);
                break;
            case ConstantUtil.BATHROOM:
                holder.imgFunction.setImageResource(R.drawable.bathroom_cabinet_xh);
                break;
            case ConstantUtil.LAMP:
                holder.imgFunction.setImageResource(R.drawable.lamp_xh);
                break;
            case ConstantUtil.SWITCH_SOCKET:
                holder.imgFunction.setImageResource(R.drawable.switch_socket_xh);
                break;
            case ConstantUtil.CIRCUIT:
                holder.imgFunction.setImageResource(R.drawable.circuit_xh);
                break;
                default:
                    holder.imgFunction.setImageResource(R.drawable.circuit_xh);
                    break;
        }
        return convertView;
    }
    class ViewHolder {
         ImageView imgFunction;
         TextView tvName;
    }
}
