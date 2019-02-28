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
import com.msht.minshengbao.Utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/4/26
 */

public class MyWorkOrderAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public OnItemSelectListener listener;
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        /**
         *
         * @param view
         * @param thisposition
         */
        void onItemSelectClick(View view, int thisposition);
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
        final int thisPosition=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_repair_order, null);
            holder.serviceIMG=(ImageView) convertView.findViewById(R.id.id_img_type);
            holder.bnStatus =(Button) convertView.findViewById(R.id.id_btn_evaluate);
            holder.cnStatus =(TextView)convertView.findViewById(R.id.id_tv_status);
            holder.cnOrder =(TextView)convertView.findViewById(R.id.id_orderNo) ;
            holder.cnType =(TextView)convertView.findViewById(R.id.id_tv_type);
            holder.cnTitle =(TextView) convertView.findViewById(R.id.id_tv_title);
            holder.createTime =(TextView)convertView.findViewById(R.id.id_create_time);
            holder.cnTime =(TextView) convertView.findViewById(R.id.id_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type=orderList.get(position).get("type");
        String status=orderList.get(position).get("status");
        String parentCategoryName=orderList.get(position).get("parent_category_name");
        String parentCategoryCode=orderList.get(position).get("parent_category_code");
        String statusDesc=orderList.get(position).get("statusDesc");
        holder.cnType.setText(parentCategoryName);
        holder.cnStatus.setText(statusDesc);
        switch (parentCategoryCode){
            case ConstantUtil.SANITARY_WARE:
                holder.serviceIMG.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                holder.serviceIMG.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                holder.serviceIMG.setImageResource(R.drawable.home_otherfix_xh);
               // holder.serviceIMG.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                holder.serviceIMG.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                holder.serviceIMG.setImageResource(R.drawable.housekeeping_clean_xh);
                break;
            default:
                holder.serviceIMG.setImageResource(R.drawable.home_appliance_fix_xh);
                    break;
        }
        if (status.equals("1")) {
            holder.bnStatus.setVisibility(View.GONE);
            holder.cnTime.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("2")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("3")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("4")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("5")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("6")){
            holder.bnStatus.setVisibility(View.GONE);
            holder.cnTime.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("7")){
            holder.cnTime.setVisibility(View.VISIBLE);
            holder.bnStatus.setVisibility(View.VISIBLE);
            holder.createTime.setVisibility(View.GONE);
        }else if (status.equals("8")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("9")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("10")){
            holder.cnTime.setVisibility(View.GONE);
            holder.bnStatus.setVisibility(View.GONE);
            holder.createTime.setVisibility(View.VISIBLE);
        }
        holder.cnOrder.setText(orderList.get(position).get("orderNo"));
        holder.cnTitle.setText(orderList.get(position).get("title"));
        holder.cnTime.setText(orderList.get(position).get("time"));
        holder.createTime.setText(orderList.get(position).get("time"));
        holder.bnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemSelectClick(v,thisPosition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        public ImageView serviceIMG;
        public Button   bnStatus;
        public TextView cnStatus;
        public TextView cnOrder;
        public TextView cnType;
        public TextView cnTitle;
        public TextView cnTime;
        public TextView createTime;
    }
}
