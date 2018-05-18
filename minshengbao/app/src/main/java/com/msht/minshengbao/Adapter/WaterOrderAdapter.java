package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.graphics.Color;
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
 * Created by hong on 2018/4/10.
 */

public class WaterOrderAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public OnItemSelectCancelListener listener;
    public OnItemSelectPhoneListener  phonelistener;
    public interface OnItemSelectCancelListener{
        void ItemSelectCancelClick(View view,int thisposition);
    }
    public interface OnItemSelectPhoneListener{
        void ItemSelectPhoneClick(View view,int thisposition);
    }
    public void setOnItemSelectPhoneListener(OnItemSelectPhoneListener listener){
        this.phonelistener=listener;
    }
    public void setOnItemSelectCancelListener(OnItemSelectCancelListener listener){
        this.listener=listener;
    }
    public WaterOrderAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.orderList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (orderList == null) {
            return 0;
        } else {
            return orderList.size();
        }
    }
    @Override
    public Object getItem(int position) {
        if (orderList == null) {
            return null;
        } else {
            return orderList.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  int thisposition=position;
       ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_water_delivery_order, null);
            holder.cn_waterVolume = (TextView) convertView.findViewById(R.id.id_water_volume);
            holder.cn_bucketVolume = (TextView) convertView.findViewById(R.id.id_bucket_volume);
            holder.cn_time = (TextView) convertView.findViewById(R.id.id_order_time);
            holder.cn_bucketFlag=(TextView)convertView.findViewById(R.id.id_bucket_flat);
            holder.cn_totalAmount=(TextView)convertView.findViewById(R.id.id_total_amount);
            holder.cn_status=(TextView)convertView.findViewById(R.id.id_tv_status);
            holder.cn_address=(TextView)convertView.findViewById(R.id.id_tv_address);
            holder.cn_orderNo=(TextView)convertView.findViewById(R.id.id_order_no);
            holder.btn_cancel=(TextView) convertView.findViewById(R.id.id_cancel_order);
            holder.Img_phone=(ImageView)convertView.findViewById(R.id.id_phone_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String bucketSpec=orderList.get(position).get("bucketSpec");
        String bucketNum=orderList.get(position).get("bucketNum");
        String waterNum=orderList.get(position).get("waterNum");
        String status=orderList.get(position).get("status");
        String bucketFlag=orderList.get(position).get("bucketFlag");
        String doorplate=orderList.get(position).get("doorplate");
        holder.cn_bucketVolume.setText(bucketSpec+"升水桶x"+bucketNum);
        holder.cn_waterVolume.setText(bucketSpec+"升直饮水x"+waterNum);
        holder.cn_totalAmount.setText(orderList.get(position).get("amount"));
        holder.cn_orderNo.setText("订单号： "+orderList.get(position).get("orderNo"));
        holder.cn_time.setText(orderList.get(position).get("applyDeliveryTime"));
        holder.cn_address.setText("地址： "+orderList.get(position).get("address")+doorplate);
        if (bucketFlag.equals("0")){
            holder.cn_bucketFlag.setVisibility(View.VISIBLE);
            holder.cn_bucketFlag.setText("(需要取桶)");
        }else {
            holder.cn_bucketFlag.setVisibility(View.GONE);
        }
        if (status.equals("0")){
            holder.cn_status.setText("待支付");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.VISIBLE);
        }else if (status.equals("1")){
            holder.cn_status.setText("支付成功");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("2")){
            holder.cn_status.setText("支付失败");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("3")){
            holder.cn_status.setText("申请退款");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("4")){
            holder.cn_status.setText("已退款");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("5")){
            holder.cn_status.setText("待接单");
            holder.btn_cancel.setVisibility(View.VISIBLE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("6")){
            holder.cn_status.setText("已接单");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.VISIBLE);
        }else if (status.equals("7")){
            holder.cn_status.setText("配送中");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.VISIBLE);
        }else if (status.equals("8")){
           // holder.cn_status.setTextColor(Color.parseColor("#ff000000"));
            holder.cn_status.setText("已完成");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }else if (status.equals("9")){
            holder.cn_status.setText("已关闭");
            holder.btn_cancel.setVisibility(View.GONE);
            holder.Img_phone.setVisibility(View.GONE);
        }
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectCancelClick(v,thisposition);
                }
            }
        });
        holder.Img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phonelistener!=null){
                    phonelistener.ItemSelectPhoneClick(v,thisposition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView  cn_waterVolume;
        TextView  cn_bucketVolume;
        TextView  cn_totalAmount;
        TextView  cn_bucketFlag;
        TextView  cn_time;
        TextView  cn_status;
        TextView  cn_orderNo;
        TextView  cn_address;
        TextView  btn_cancel;
        ImageView Img_phone;
    }
}
