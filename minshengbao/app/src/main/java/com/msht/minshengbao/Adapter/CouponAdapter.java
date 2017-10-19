package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/1.
 */

public class CouponAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> haveuseList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
            holder.Layoutback=(RelativeLayout)convertView.findViewById(R.id.id_layout_back);
            holder.cn_scope=(TextView) convertView.findViewById(R.id.id_scope);
            holder.cn_amount=(TextView) convertView.findViewById(R.id.id_amount);
            holder.cn_use_limit=(TextView) convertView.findViewById(R.id.id_use_limit);
            holder.cn_start_date=(TextView) convertView.findViewById(R.id.id_start_date);
            holder.cn_end_date=(TextView) convertView.findViewById(R.id.id_end_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type=haveuseList.get(position).get("type");
        if (type.equals("1")){
            holder.Layoutback.setBackgroundResource(R.drawable.dicount_coupon_xh);
        }else {
            holder.Layoutback.setBackgroundResource(R.drawable.coupon_gray_xh);
        }
        String limit_use="买满"+haveuseList.get(position).get("use_limit")+"元可用";
        holder.cn_scope.setText(haveuseList.get(position).get("scope"));
        holder.cn_amount.setText(haveuseList.get(position).get("amount"));
        holder.cn_use_limit.setText(limit_use);
        holder.cn_start_date.setText(haveuseList.get(position).get("start_date"));
        holder.cn_end_date.setText(haveuseList.get(position).get("end_date"));
        return convertView;
    }
    class ViewHolder {
        public RelativeLayout Layoutback;
        public TextView cn_scope;
        public TextView  cn_amount;
        public TextView  cn_use_limit;
        public TextView  cn_start_date;
        public TextView  cn_end_date;
    }
}
