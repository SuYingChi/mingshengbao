package com.msht.minshengbao.adapter;

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
public class InvoiceHistoryAdapter extends RecyclerView.Adapter<InvoiceHistoryAdapter.MyViewHolder>{

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
    public InvoiceHistoryAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  int thisPosition=position;
        holder.tvName.setText(mList.get(position).get("content"));
        holder.tvStatus.setText(mList.get(position).get("statusDes"));
        holder.tvTime.setText(mList.get(position).get("time"));
        holder.tvAmount.setText(mList.get(position).get("amount"));
        holder.tvTitle.setText(mList.get(position).get("invoiceTypeName"));
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
         TextView tvStatus;
         TextView tvName;
         TextView tvTitle;
         TextView tvAmount;
         View itemView;
        private MyViewHolder(View view){
            super(view);
            tvTime =(TextView) view.findViewById(R.id.id_tv_time);
            tvStatus =(TextView) view.findViewById(R.id.id_tv_status);
            tvName =(TextView) view.findViewById(R.id.id_tv_name);
            tvAmount =(TextView)view.findViewById(R.id.id_tv_money);
            tvTitle=(TextView)view.findViewById(R.id.id_title_type);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
