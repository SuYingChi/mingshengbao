package com.msht.minshengbao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceRepairOrderAdapter extends RecyclerView.Adapter<InvoiceRepairOrderAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
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
    public InvoiceRepairOrderAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_repair_order,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  int thisPosition=position;
        String timeText=mList.get(position).get("time");
        holder.tvParentCategory.setText(mList.get(position).get("mainCategory"));
        holder.tvCategory.setText(mList.get(position).get("category"));
        holder.tvOrderId.setText(mList.get(position).get("orderId"));
        holder.tvTime.setText(timeText);
        holder.tvAmount.setText(mList.get(position).get("amount"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(thisPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvCategory;
        TextView tvParentCategory;
        TextView tvOrderId;
        TextView tvAmount;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            tvTime =(TextView) view.findViewById(R.id.id_time);
            tvCategory =(TextView) view.findViewById(R.id.id_category);
            tvParentCategory=(TextView)view.findViewById(R.id.id_parent_category) ;
            tvAmount =(TextView)view.findViewById(R.id.id_tv_money);
            tvOrderId=(TextView)view.findViewById(R.id.id_orderId);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
