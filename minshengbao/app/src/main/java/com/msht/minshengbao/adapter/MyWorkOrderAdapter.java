package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/26.
 */

public class MyWorkOrderAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public OnItemSelectListener listener;
    public void SetOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        void ItemSelectClick(View view,int thisposition);
    }

    public  MyWorkOrderAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.orderList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final int thisposition=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_repair_order, null);
            holder.serviceIMG=(ImageView) convertView.findViewById(R.id.id_img_type);
            holder.bn_status=(Button) convertView.findViewById(R.id.id_btn_evalute);
            holder.cn_status=(TextView)convertView.findViewById(R.id.id_tv_status);
            holder.cn_order=(TextView)convertView.findViewById(R.id.id_orderNo) ;
            holder.cn_type=(TextView)convertView.findViewById(R.id.id_tv_type);
            holder.cn_title=(TextView) convertView.findViewById(R.id.id_tv_title);
            holder.create_time=(TextView)convertView.findViewById(R.id.id_create_time);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type=orderList.get(position).get("type");
        String status=orderList.get(position).get("status");
        String parent_category_name=orderList.get(position).get("parent_category_name");
        String statusDesc=orderList.get(position).get("statusDesc");
        holder.cn_type.setText(parent_category_name);
        holder.cn_status.setText(statusDesc);
        if (type.equals("1")) {
            holder.serviceIMG.setImageResource(R.drawable.home_sanitary_xh);
        }else if (type.equals("2")){
            holder.serviceIMG.setImageResource(R.drawable.home_appliance_fix_xh);
        }else if (type.equals("3")){
            holder.serviceIMG.setImageResource(R.drawable.home_lanterns_xh);
        }else if (type.equals("4")){
            holder.serviceIMG.setImageResource(R.drawable.home_otherfix_xh);
        }else if (type.equals("48")){
            holder.serviceIMG.setImageResource(R.drawable.home_appliance_clean_xh);
        }else {
            holder.serviceIMG.setImageResource(R.drawable.home_appliance_clean_xh);
        }
        if (status.equals("1")) {
            holder.bn_status.setVisibility(View.GONE);
            holder.cn_time.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("2")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("3")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("4")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("5")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("6")){
            holder.bn_status.setVisibility(View.GONE);
            holder.cn_time.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("7")){
            holder.cn_time.setVisibility(View.VISIBLE);
            holder.bn_status.setVisibility(View.VISIBLE);
            holder.create_time.setVisibility(View.GONE);
        }else if (status.equals("8")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("9")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }else if (status.equals("10")){
            holder.cn_time.setVisibility(View.GONE);
            holder.bn_status.setVisibility(View.GONE);
            holder.create_time.setVisibility(View.VISIBLE);
        }
        holder.cn_order.setText(orderList.get(position).get("orderNo"));
        holder.cn_title.setText(orderList.get(position).get("title"));
        holder.cn_time.setText(orderList.get(position).get("time"));
        holder.create_time.setText(orderList.get(position).get("time"));
        holder.bn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectClick(v,thisposition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        public ImageView serviceIMG;
        public Button    bn_status;
        public TextView  cn_status;
        public TextView  cn_order;
        public TextView  cn_type;
        public TextView  cn_title;
        public TextView  cn_time;
        public TextView  create_time;
    }
}
