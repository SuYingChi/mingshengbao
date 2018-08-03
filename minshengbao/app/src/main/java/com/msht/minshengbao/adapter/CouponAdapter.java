package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/1.
 */

public class CouponAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, String>> haveuseList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
    public OnItemSelectListener listener;
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        void ItemSelectClick(View view,int thisposition);
    }
    public CouponAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.haveuseList=List;
    }
    @Override
    public int getCount() {
        return haveuseList.size();
    }
    @Override
    public Object getItem(int position) {
        return haveuseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final int thisPosition=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
            holder.layoutBack =(RelativeLayout)convertView.findViewById(R.id.id_layout_back);
            holder.cnName =(TextView)convertView.findViewById(R.id.id_title_name);
            holder.cnScope =(TextView) convertView.findViewById(R.id.id_scope);
            holder.cnAmount =(TextView) convertView.findViewById(R.id.id_amount);
            holder.cnUseLimit =(TextView) convertView.findViewById(R.id.id_use_limit);
            holder.cnTime =(TextView)convertView.findViewById(R.id.id_time);
            holder.cnEndDate =(TextView) convertView.findViewById(R.id.id_end_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String type=haveuseList.get(position).get("type");
        String remainderDay=haveuseList.get(position).get("remainder_days");
        if (type.equals(VariableUtil.VALUE_ONE)){
            holder.cnAmount.setTextColor(Color.parseColor("#F5BC33"));
            holder.cnName.setTextColor(Color.parseColor("#F5BC33"));
            holder.layoutBack.setBackgroundResource(R.drawable.dicount_coupon_2xh);
        }else if (type.equals(VariableUtil.VALUE_TWO)){
            holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
            holder.cnName.setTextColor(Color.parseColor("#FF383838"));
            holder.layoutBack.setBackgroundResource(R.mipmap.coupon_haveused_2xh);
        }else if(type.equals(VariableUtil.VALUE_THREE)){
            holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
            holder.cnName.setTextColor(Color.parseColor("#FF383838"));
            holder.layoutBack.setBackgroundResource(R.mipmap.coupon_exceed_2xh);
        }
        if (!TextUtils.isEmpty(remainderDay)){
            holder.cnTime.setVisibility(View.VISIBLE);
            holder.cnTime.setText("剩"+remainderDay+"天");
        }else {
            holder.cnTime.setVisibility(View.GONE);
        }
        String limitUse="买满"+haveuseList.get(position).get("use_limit")+"元可用";
        holder.cnName.setText(haveuseList.get(position).get("name"));
        holder.cnScope.setText(haveuseList.get(position).get("scope"));
        holder.cnAmount.setText("¥"+haveuseList.get(position).get("amount"));
        holder.cnUseLimit.setText(limitUse);
        holder.cnEndDate.setText(haveuseList.get(position).get("end_date"));
        return convertView;
    }
    class ViewHolder {
        RelativeLayout layoutBack;
        TextView cnName;
        TextView cnScope;
        TextView cnAmount;
        TextView cnUseLimit;
        TextView cnTime;
        TextView cnEndDate;
    }
}
