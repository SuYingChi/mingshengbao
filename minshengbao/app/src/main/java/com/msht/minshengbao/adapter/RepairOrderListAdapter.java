package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * @date 2019/1/9 
 */
public class RepairOrderListAdapter extends RecyclerView.Adapter<RepairOrderListAdapter.MyViewHolder>{

    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public OnItemSelectListener listener;
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        /**
         *评价回调
         * @param view
         * @param position
         */
        void onItemSelectClick(View view, int position);
    }

    public  RepairOrderListAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.orderList=List;
    }
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repair_order,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {
        String status=orderList.get(position).get("status");
        String parentCategoryName=orderList.get(position).get("parent_category_name");
        String parentCategoryCode=orderList.get(position).get("parent_category_code");
        String statusDesc=orderList.get(position).get("statusDesc");
        myViewHolder.cnType.setText(parentCategoryName);
        myViewHolder.cnStatus.setText(statusDesc);
        switch (parentCategoryCode){
            case ConstantUtil.SANITARY_WARE:
                myViewHolder.serviceImage.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                myViewHolder.serviceImage.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                myViewHolder.serviceImage.setImageResource(R.drawable.home_otherfix_xh);
                // holder.serviceIMG.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                myViewHolder.serviceImage.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                myViewHolder.serviceImage.setImageResource(R.drawable.housekeeping_clean_xh);
                break;
            default:
                myViewHolder.serviceImage.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
        }
        if (status.equals("1")) {
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("2")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("3")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("4")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("5")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("6")){
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("7")){
            myViewHolder.cnTime.setVisibility(View.VISIBLE);
            myViewHolder.bnStatus.setVisibility(View.VISIBLE);
            myViewHolder.createTime.setVisibility(View.INVISIBLE);
        }else if (status.equals("8")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("9")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }else if (status.equals("10")){
            myViewHolder.cnTime.setVisibility(View.INVISIBLE);
            myViewHolder.bnStatus.setVisibility(View.GONE);
            myViewHolder.createTime.setVisibility(View.VISIBLE);
        }
        myViewHolder.cnOrder.setText(orderList.get(position).get("orderNo"));
        myViewHolder.cnTitle.setText(orderList.get(position).get("categoryDesc"));
        myViewHolder.cnTime.setText(orderList.get(position).get("time"));
        myViewHolder.createTime.setText(orderList.get(position).get("time"));
        myViewHolder.bnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemSelectClick(v,position);
                }
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (orderList!=null){
            return orderList.size();
        }else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        Button bnStatus;
        TextView cnStatus;
        TextView cnOrder;
        TextView cnType;
        TextView cnTitle;
        TextView cnTime;
        TextView createTime;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            serviceImage=(ImageView) view.findViewById(R.id.id_img_type);
            bnStatus =(Button) view.findViewById(R.id.id_btn_evaluate);
            cnStatus =(TextView)view.findViewById(R.id.id_tv_status);
            cnOrder =(TextView)view.findViewById(R.id.id_orderNo) ;
            cnType =(TextView)view.findViewById(R.id.id_tv_type);
            cnTitle =(TextView) view.findViewById(R.id.id_tv_title);
            createTime =(TextView)view.findViewById(R.id.id_create_time);
            cnTime =(TextView) view.findViewById(R.id.id_tv_time);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
