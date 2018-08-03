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
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/23  
 */
public class LpgDeliveryInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    public LpgDeliveryInfoAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mList=list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return mList.get(position);
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
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_lpg_delivery_information, null);
            holder.tvDescribe = (TextView) convertView.findViewById(R.id.id_tv_describe);
            holder.tvTime = (TextView) convertView.findViewById(R.id.id_tv_time);
            holder.imageSign = (ImageView) convertView.findViewById(R.id.id_sign_img);
            holder.lineView=convertView.findViewById(R.id.id_top_line);
            holder.lineBottom=convertView.findViewById(R.id.id_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String describe=mList.get(position).get("describe");
        String createTime=mList.get(position).get("createDate");
        int lastPosition=mList.size()-1;
        holder.tvDescribe.setText(describe);
        holder.tvTime.setText(createTime);
        if (position==0){
            holder.lineView.setVisibility(View.INVISIBLE);
            holder.imageSign.setImageResource(R.drawable.radiobutton_pressed_oval);
            if (lastPosition==0){
                holder.lineBottom.setVisibility(View.INVISIBLE);
            }else {
                holder.lineBottom.setVisibility(View.VISIBLE);
            }
        }else if (position==lastPosition){
            holder.imageSign.setImageResource(R.drawable.shape_default_icon);
            holder.lineView.setVisibility(View.VISIBLE);
            holder.lineBottom.setVisibility(View.INVISIBLE);
        }else {
            holder.imageSign.setImageResource(R.drawable.shape_default_icon);
            holder.lineView.setVisibility(View.VISIBLE);
            holder.lineBottom.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class ViewHolder {
        TextView  tvDescribe;
        TextView  tvTime;
        ImageView imageSign;
        View      lineView;
        View      lineBottom;
    }
}
