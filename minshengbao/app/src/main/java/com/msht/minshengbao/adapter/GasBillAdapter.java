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
public class GasBillAdapter extends RecyclerView.Adapter<GasBillAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(GasBillAdapter.ItemClickCallBack clickCallBack) {
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
    public GasBillAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gas_bill,viewGroup,false);
        return new GasBillAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String amountText="¥"+mList.get(i).get("amounts")+"元";
        String balanceText="-"+mList.get(i).get("balance")+"元";
        String gasNumText="共用气"+mList.get(i).get("num")+"立方米";
        String gasFeeText=mList.get(i).get("gas_fees")+"元";
        String lateFeeText=mList.get(i).get("late_fee")+"元";
        myViewHolder.cnAmount.setText(amountText);
        myViewHolder.cnBalance.setText(balanceText);
        myViewHolder.cnDate.setText(mList.get(i).get("date"));
        myViewHolder.cnGasFee.setText(gasFeeText);
        myViewHolder.cnGasNum.setText(gasNumText);
        myViewHolder.cnLateFee.setText(lateFeeText);
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cnAmount;
        TextView cnBalance;
        TextView cnGasFee;
        TextView cnGasNum;
        TextView cnDate;
        TextView cnLateFee;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            cnAmount =(TextView) view.findViewById(R.id.id_debts);
            cnBalance =(TextView)view.findViewById(R.id.id_tv_balance);
            cnGasFee =(TextView) view.findViewById(R.id.id_gas_amount);
            cnGasNum =(TextView) view.findViewById(R.id.id_total_num);
            cnDate =(TextView) view.findViewById(R.id.id_tv_date);
            cnLateFee =(TextView) view.findViewById(R.id.id_lastfees);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
