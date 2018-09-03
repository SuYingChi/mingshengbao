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
 * @date 2018/11/6  
 */
public class TopModuleAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> functionList = new ArrayList<HashMap<String, String>>();
    public TopModuleAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            convertView = mInflater.inflate(R.layout.item_topmodule_view, null);
            holder.imgFunction =(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tvName =(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=functionList.get(position).get("name");
        String code=functionList.get(position).get("code");
        holder.tvName.setText(name);
        if (code.equals(ConstantUtil.SHOP)){
            holder.imgFunction.setImageResource(R.drawable.top_shopmall_xh);
        }else if (code.equals(ConstantUtil.GAS_PAY)){
            holder.imgFunction.setImageResource(R.drawable.top_gaspay_xh);
        }else if (code.equals(ConstantUtil.GAS_METER)){
            holder.imgFunction.setImageResource(R.drawable.top_gas_writetable_xh);
        }else if (code.equals(ConstantUtil.GAS_IC_CARD)){
            holder.imgFunction.setImageResource(R.drawable.top_iccard_xh);
        }else if (code.equals(ConstantUtil.INSURANCE)){
            holder.imgFunction.setImageResource(R.drawable.home_insurance_xh);
        }else if (code.equals(ConstantUtil.LPG_NAME)){
            holder.imgFunction.setImageResource(R.drawable.lpg_home_xh);
        }else {
            holder.imgFunction.setImageResource(R.drawable.top_shopmall_xh);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView imgFunction;
        TextView tvName;
    }
}
