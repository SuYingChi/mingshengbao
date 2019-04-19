package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.msht.minshengbao.Bean.RepairInvoiceBean;
import com.msht.minshengbao.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author hong
 */
public class RepairInvoiceListAdapter extends RecyclerView.Adapter<RepairInvoiceListAdapter.MyViewHolder>{
    private boolean ignoreChange=false;
   // private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private ArrayList<RepairInvoiceBean> invoiceBeans = new ArrayList<RepairInvoiceBean>();
    public RepairInvoiceListAdapter(ArrayList<RepairInvoiceBean> bean) {
        this.invoiceBeans=bean;

    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack{
        /**
         * item回调
         * @param isChecked
         * @param pos 下标
         */
        void onItemClick(boolean isChecked,int pos);
    }
    private ItemClickCallBack clickCallBack;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_order,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        myViewHolder.tvOrderNo.setText(invoiceBeans.get(i).getOrderNo());
        myViewHolder.tvTime.setText(invoiceBeans.get(i).getTime());
        myViewHolder.boxSelect.setText(invoiceBeans.get(i).getMainCategory());
        myViewHolder.tvName.setText(invoiceBeans.get(i).getCategory());
        myViewHolder.tvMoney.setText(invoiceBeans.get(i).getAmount());
        myViewHolder.boxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ignoreChange){
                    if (clickCallBack!=null){
                        clickCallBack.onItemClick(isChecked,i);
                    }
                }
            }
        });
        ignoreChange=false;
        myViewHolder.boxSelect.setChecked(invoiceBeans.get(i).isCheck());
        ignoreChange=true;

    }
    @Override
    public int getItemCount() {
        if (invoiceBeans !=null){
            return invoiceBeans .size();
        }else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
         CheckBox boxSelect;
         TextView tvTime;
         TextView tvOrderNo;
         TextView tvName;
         TextView tvMoney;
         View itemView;
        private MyViewHolder(View view){
            super(view);
            boxSelect =(CheckBox) view.findViewById(R.id.id_box_type);
            tvTime =(TextView) view.findViewById(R.id.id_tv_time);
            tvOrderNo =(TextView) view.findViewById(R.id.id_tv_oderNo);
            tvName =(TextView) view.findViewById(R.id.id_tv_name);
            tvMoney =(TextView)view.findViewById(R.id.id_tv_money);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
