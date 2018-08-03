package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/18  
 */
public class LpgUserListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public LpgOrderListAdapter.OnItemSelectListener listener;

    public interface OnItemSelectListener{
        /**
         * 删除
         * @param view
         * @param thisPosition
         */
        void  onSelectItemClick(View view,int thisPosition);
    }
    public void setOnItemSelectListener(LpgOrderListAdapter.OnItemSelectListener listener){
        this.listener=listener;
    }
    public LpgUserListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
        final  int thisPosition=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_lpg_user_list, null);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.id_tv_phone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.id_tv_address);
            holder.radioIsLastLogin=(RadioButton)convertView.findViewById(R.id.id_radio_isLastLogin);
            holder.deleteImg=(ImageView)convertView.findViewById(R.id.id_delete_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String isLastLogin=mList.get(position).get("isLastLogin");
        String phone=mList.get(position).get("mobile");
        String address=mList.get(position).get("addressName");
        holder.tvPhone.setText(phone);
        holder.tvAddress.setText(address);
        if (VariableUtil.deleteFlag==1){
            holder.deleteImg.setVisibility(View.VISIBLE);
            holder.radioIsLastLogin.setVisibility(View.INVISIBLE);
        }else {
            holder.radioIsLastLogin.setVisibility(View.VISIBLE);
            holder.deleteImg.setVisibility(View.GONE);
        }
        if (isLastLogin.equals(VariableUtil.VALUE_ZERO)){
            holder.radioIsLastLogin.setChecked(false);
        }else {
            holder.radioIsLastLogin.setChecked(true);
        }
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onSelectItemClick(v,thisPosition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView  tvPhone;
        TextView  tvAddress;
        RadioButton radioIsLastLogin;
        ImageView  deleteImg;
    }
}
